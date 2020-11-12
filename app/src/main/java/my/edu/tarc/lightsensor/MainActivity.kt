package my.edu.tarc.lightsensor

import android.annotation.SuppressLint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

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