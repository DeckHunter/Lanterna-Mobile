package com.example.deckdeveloper.atividade_lanternaapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Switch
import android.widget.Toast
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var cameraM : CameraManager
    private lateinit var Btn_Ligar : ImageButton
    private lateinit var Btn_Piscar : Switch

    private lateinit var sensorManager: SensorManager
    private var Luz: Sensor? = null

    var Ligada = false
    var Piscando = false
    var Luz_Anbiente = 300

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Btn_Ligar = findViewById(R.id.power)
        cameraM = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        Btn_Ligar.setOnClickListener { flashLightOnRoOff(it) }
        Btn_Piscar = findViewById(R.id.Piscar)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Luz = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun flashLightOnRoOff(v: View?) {

        if(Btn_Piscar.isChecked){
            Piscando = true
            while(Piscando == true){
                if(Btn_Piscar.isChecked){
                    if (!Ligada){
                        val cameraListId = cameraM.cameraIdList[0]
                        cameraM.setTorchMode(cameraListId,true)
                        Ligada = true
                    }
                    if(Ligada){
                        val cameraListId = cameraM.cameraIdList[0]
                        cameraM.setTorchMode(cameraListId,false)
                        Ligada = false
                    }
                }else{
                    Piscando = false;
                }

            }
        }

        if (!Ligada){
            val cameraListId = cameraM.cameraIdList[0]
            cameraM.setTorchMode(cameraListId,true)
            Ligada = true
            Btn_Ligar.setImageResource(R.drawable.ic_brightness_on)

        }
        if(Ligada){
            val cameraListId = cameraM.cameraIdList[0]
            cameraM.setTorchMode(cameraListId,false)
            Ligada = false
            Btn_Ligar.setImageResource(R.drawable.ic_brightness_off)

        }

        if(Luz_Anbiente < 200 && !Ligada && !Piscando){
            val cameraListId = cameraM.cameraIdList[0]
            cameraM.setTorchMode(cameraListId,true)
            Ligada = true
            Btn_Ligar.setImageResource(R.drawable.ic_brightness_on)
        }
    }
    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, Luz, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {

        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent) {
        val Nivel_De_Luz = event.values[0]
        Luz_Anbiente = Nivel_De_Luz.toInt()

    }

}