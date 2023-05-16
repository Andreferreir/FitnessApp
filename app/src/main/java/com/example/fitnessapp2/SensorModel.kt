package com.example.fitnessapp2

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fitnessapp2.Database.Measurement
import java.util.*

class SensorModel : SensorEventListener{
    private var _newStepsMeasurement = MutableLiveData<Measurement>().apply {
        value = null
    }
    public val newStepsMeasurement : LiveData<Measurement> =  _newStepsMeasurement

    public val sensorValue : MutableLiveData<Float> =  MutableLiveData<Float>().apply {
        value = 0.0f
    }

    public val incrementValue : MutableLiveData<Float> =  MutableLiveData<Float>().apply {
        value = 0.0f
    }

    public val totalValue : MutableLiveData<Float> =  MutableLiveData<Float>().apply {
        value = 0.0f
    }
    public val heartRate : MutableLiveData<Float> =  MutableLiveData<Float>().apply {
        value = 0.0f
    }

    private var previousSteps = 0f
    private var previousDate : Date? = null
    private var running = false
    private var statusMessage = ""

    companion object {
        private var sensorManager: SensorManager? = null
        private var stepSensor : Sensor? = null
        private var HRSensor : Sensor? = null
        fun initializeSensorManager(context: Context) {
            if(sensorManager == null) {
                sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
                Log.e("sensorManager","initializeSensorManager()")
            }
        }
    }

    init {
        stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        HRSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (HRSensor == null) {
            statusMessage = "No heart rate sensor detected on this device"
            Log.e("HR","could not connect")
        } else {
            sensorManager?.registerListener(this, HRSensor, SensorManager.SENSOR_DELAY_UI)
            Log.e("HR","registerListener")
        }

        if (stepSensor == null) {
            statusMessage = "No sensor detected on this device"
            Log.e("steps","could not connect")
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
            Log.e("steps","registerListener")
        }
    }
    fun setRunningState(state: Boolean) {
        this.running = state
    }
    override fun onSensorChanged(event: SensorEvent?) {
        if (running)  {
            if (event!!.sensor.type === Sensor.TYPE_ACCELEROMETER) {
                heartRate.value =  event!!.values[0]
            }
            else{
                val valueRead = event!!.values[0]

                Log.e("steps","new measurement " + valueRead)

                sensorValue.value = valueRead

                if (previousSteps == 0.0f) {
                    previousSteps = valueRead
                    previousDate = Date()
                } else {
                    val differente =   valueRead - previousSteps
                    if(differente > 0 ) {
                        incrementValue.value = differente
                        totalValue.value = totalValue.value?.plus(differente)
                        val measurement =
                            Measurement(value = differente, startDate = previousDate, endDate = Date())
                        _newStepsMeasurement.value = measurement
                        //keep previous date for the next time
                        previousDate = Date()
                    }
                }
            }

        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}