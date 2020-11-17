package my.edu.tarc.ls_as_pir

import android.annotation.SuppressLint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


@SuppressLint("NewApi")
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MainActivity : AppCompatActivity(), SensorEventListener {

    //Declaring variables
    private var sensorManager: SensorManager? = null
    private var lightSensor: Sensor? = null
    private lateinit var textViewReading: TextView
    private lateinit var textViewPower: TextView
    private lateinit var textViewMaxRange: TextView
    private lateinit var textViewMaxDelay: TextView
    private lateinit var textViewMinDelay: TextView


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    //Function that runs whenever sensor detect changes
    override fun onSensorChanged(event: SensorEvent?) {

        //Link UI to program
        textViewReading = findViewById(R.id.textViewReading)
        textViewPower = findViewById(R.id.textViewPower)
        textViewMaxRange = findViewById(R.id.textViewMaxRange)
        textViewMaxDelay = findViewById(R.id.textViewMaxDelay)
        textViewMinDelay = findViewById(R.id.textViewMinDelay)

        if(event!!.sensor.type == Sensor.TYPE_LIGHT){
            textViewReading.text = event.values[0].toString()
            textViewPower.text = lightSensor!!.power.toString()
            textViewMaxRange.text = lightSensor!!.maximumRange.toString()
            textViewMaxDelay.text = lightSensor!!.maxDelay.toString()
            textViewMinDelay.text = lightSensor!!.minDelay.toString()
        }

        //Declaring variables
        val reading: Float = event.values[0]
        var relay1: String = "0"    //Lights
        var relay2: String = "0"    //Air conditioner

        //if-else statement to trigger actions (switch on lights and air conditioner)
        if (reading < 100) { //switch on
            relay1 = "1"
            relay2 = "1"
        } else {             //switch off
            relay1 = "0"
            relay2 = "0"
        }

        val handler = Handler()
        handler.postDelayed({   //Delay the real-time update to prevent overloading

            //Defining database
            val database = FirebaseDatabase.getInstance("https://bait2123-202010-03.firebaseio.com")
            //val database = FirebaseDatabase.getInstance("https://solenoid-lock-f65e8.firebaseio.com")

            //Write to common resources firebase
            val data1 = database.getReference("PI_03_CONTROL/relay1")
            data1.setValue(relay1)
            val data2 = database.getReference("PI_03_CONTROL/relay2")
            data2.setValue(relay2)

            //val lightSensor= database.getReference("LightSensor")
            //lightSensor.setValue(event.values[0].toString())

        }, 1000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Calling Android light sensor
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager?
        lightSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }
}