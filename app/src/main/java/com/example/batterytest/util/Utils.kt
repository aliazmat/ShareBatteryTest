package com.example.batterytest.util

import android.content.Context
import android.content.Context.HARDWARE_PROPERTIES_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.os.*
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.batterytest.receiver.BatteryReceiver
import com.example.batterytest.receiver.PowerReceiver
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Process
import java.util.*


object Utils {

    val timer:Timer= Timer(true)
    fun shutDownDevice(context: Context){
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager?
        pm?.reboot(null)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getCPUTemperature(context: Context): FloatArray {
        val hwpm = context.getSystemService(HARDWARE_PROPERTIES_SERVICE) as HardwarePropertiesManager
        return hwpm.getDeviceTemperatures(
            HardwarePropertiesManager.DEVICE_TEMPERATURE_CPU,
            HardwarePropertiesManager.TEMPERATURE_CURRENT
        )

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getGPUTemperature(context: Context): FloatArray {
        val hwpm = context.getSystemService(HARDWARE_PROPERTIES_SERVICE) as HardwarePropertiesManager
        return hwpm.getDeviceTemperatures(
            HardwarePropertiesManager.DEVICE_TEMPERATURE_GPU,
            HardwarePropertiesManager.TEMPERATURE_CURRENT
        )

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getBatteryTemperature(context: Context): FloatArray {
        val hwpm = context.getSystemService(HARDWARE_PROPERTIES_SERVICE) as HardwarePropertiesManager
        return  hwpm.getDeviceTemperatures(
            HardwarePropertiesManager.DEVICE_TEMPERATURE_BATTERY,
            HardwarePropertiesManager.TEMPERATURE_CURRENT
        )

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getCPUUsage(context: Context): Array<out CpuUsageInfo> {
        val hwpm = context.getSystemService(HARDWARE_PROPERTIES_SERVICE) as HardwarePropertiesManager
        hwpm.getDeviceTemperatures(
            HardwarePropertiesManager.DEVICE_TEMPERATURE_BATTERY,
            HardwarePropertiesManager.TEMPERATURE_CURRENT
        )
        return hwpm.cpuUsages
    }

    fun cpuTemperature(): Float {
        val process: Process
        return try {
            process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp")
            process.waitFor()
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            val line: String = reader.readLine()
            if (line != null) {
                val temp = line.toFloat()
                temp / 1000.0f
            } else {
                51.0f
            }
        } catch (e: Exception) {
            e.printStackTrace()
            0.0f
        }
    }

    fun scheduleTimer(context: Context){
        val timerTaskObj: TimerTask = object : TimerTask() {
            override fun run() {
                val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
                    context.registerReceiver(null, ifilter)
      }
                Log.e("TAG","Timer Battery Temperature::"+batteryStatus?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,-1))
                Log.e("TAG","Timer Battery Plugged::"+batteryStatus?.getIntExtra(BatteryManager.EXTRA_PLUGGED,0))
                Log.e("TAG","Timer Battery Voltage::"+batteryStatus?.getIntExtra(BatteryManager.EXTRA_VOLTAGE,-1))
                Log.e("TAG","Timer Battery Technology::"+batteryStatus?.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY))
            }
        }
        timer?.schedule(timerTaskObj,1000,30*1000)
    }

    fun cancelTimer(){
        timer?.cancel()
    }


    fun printThermal() {
        var temp: String?
        var type: String?
        for (i in 0..28) {
            temp = thermalTemp(i)
            if (!temp!!.contains("0.0")) {
                type = thermalType(i)
                if (type != null) {
                    println("ThermalValues $type : $temp\n")
                }
            }
        }
    }

    fun thermalTemp(i: Int): String? {
        val process: Process
        val reader: BufferedReader
        val line: String?
        var t: String? = null
        var temp = 0f
        try {
            process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone$i/temp")
            process.waitFor()
            reader = BufferedReader(InputStreamReader(process.inputStream))
            line = reader.readLine()
            if (line != null) {
                temp = line.toFloat()
            }
            reader.close()
            process.destroy()
            if (temp.toInt() != 0) {
                if (temp.toInt() > 10000) {
                    temp = temp / 1000
                } else if (temp.toInt() > 1000) {
                    temp = temp / 100
                } else if (temp.toInt() > 100) {
                    temp = temp / 10
                }
            } else t = "0.0"
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return t
    }

    fun thermalType(i: Int): String? {
        val process: Process
        val reader: BufferedReader
        val line: String?
        var type: String? = null
        try {
            process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone$i/type")
            process.waitFor()
            reader = BufferedReader(InputStreamReader(process.inputStream))
            line = reader.readLine()
            if (line != null) {
                type = line
            }
            reader.close()
            process.destroy()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return type
    }


}