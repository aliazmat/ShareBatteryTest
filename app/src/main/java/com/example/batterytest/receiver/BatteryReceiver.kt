package com.example.batterytest.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.util.Log
import com.example.batterytest.util.Utils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class BatteryReceiver() : BroadcastReceiver() {

    override fun onReceive(context: Context?, _intent: Intent?) {
       Log.e("TAG","Battery Receiver")
        val batteryPct: Float? = _intent?.let { intent ->
            val level: Int = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            Log.e("TAG","Battery Temperature::"+intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,-1))
            Log.e("TAG","Battery Plugged::"+intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0))
            Log.e("TAG","Battery Voltage::"+intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,-1))
            Log.e("TAG","Battery Technology::"+intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY))
            val scale: Int = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            level * 100 / scale.toFloat()
        }

        Log.e("TAG","Battery Percentage::${batteryPct}")

    }


}