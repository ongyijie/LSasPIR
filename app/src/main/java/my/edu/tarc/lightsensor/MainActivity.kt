package my.edu.tarc.lightsensor

import android.annotation.SuppressLint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.FirebaseDatabase


@SuppressLint("NewApi")
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class MainActivity : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var lightSensor: Sensor? = null
    private lateinit var textViewReading: TextView
    private lateinit var textViewPower: TextView
    private lateinit var textViewMaxRange: TextView
    private lateinit var textViewMaxDelay: TextView
    private lateinit var textViewMinDelay: TextView


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
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
        var relay2: String = "0"    //Aircond
        var lcdscr: String = "0"    //Projector
        var lcdtxt: String = ""

        //if-else statement to trigger action (switch on lights and air conditioner)
        if (reading < 100) {
            relay1 = "1"
            relay2 = "1"
            lcdscr = "1"
            lcdtxt = "****Welcome!****"
        } else {
            relay1 = "0"
            relay2 = "0"
            lcdscr = "0"
            lcdtxt = ""
        }

        val database = FirebaseDatabase.getInstance()

        //val options = FirebaseOptions.Builder()
                //.setApiKey("AIzaSyCSc4UTyiRiPXl0s6B8ykO89H5ZoRhHhL0")
                //.setApplicationId("solenoid-lock-f65e8")
                //.setDatabaseUrl("https://console.firebase.google.com/u/0/project/solenoid-lock-f65e8/database/solenoid-lock-f65e8/data")
                //.build()

        //To common resources firebase
        val data1 = database.getReference("PI_03_CONTROL/relay1")
        data1.setValue(relay1)
        val data2 = database.getReference("PI_03_CONTROL/relay2")
        data2.setValue(relay2)
        val data3 = database.getReference("PI_03_CONTROL/lcdscr")
        data3.setValue(lcdscr)
        val data4 = database.getReference("PI_03_CONTROL/lcdtxt")
        data4.setValue(lcdtxt)

        //To our own firebase
        val lightSensor= database.getReference("LightSensor")
        lightSensor.setValue(event.values[0].toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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