package com.example.batterytest

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.HardwarePropertiesManager
import android.os.HardwarePropertiesManager.DEVICE_TEMPERATURE_CPU
import android.os.HardwarePropertiesManager.TEMPERATURE_CURRENT
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.batterytest.databinding.ActivityMainBinding
import com.example.batterytest.receiver.BatteryReceiver
import com.example.batterytest.util.Utils


class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
      //  Log.e("TAG","CPU Temp::"+Utils.cpuTemperature())
        Utils.scheduleTimer(this)
        Utils.printThermal()
//        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
//            this.registerReceiver(BatteryReceiver(), ifilter)
//        }

//        val hwpm = getSystemService(HARDWARE_PROPERTIES_SERVICE) as HardwarePropertiesManager
//       val cpuTemp= hwpm.getDeviceTemperatures(DEVICE_TEMPERATURE_CPU,TEMPERATURE_CURRENT)
//        cpuTemp.forEach {  it->
//            Log.e("TAG","Value is::"+it)
//        }
//        val cpuUsage = hwpm.cpuUsages

    }
}