package com.example.batterytest.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.util.Log
import android.widget.Toast
import com.example.batterytest.util.Utils

class PowerReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context?, _intent: Intent?) {

        Log.e("TAG","Power Receiver")
        Toast.makeText(context,"Boot Completed Battery Test",Toast.LENGTH_LONG).show()
        _intent?.let { intent ->
            val chargingStatus = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0)
            if(chargingStatus != 0){
                context?.let { Utils.scheduleTimer(it) }
            }else{
                context?.let { Utils.cancelTimer() }
            }
        }
    }


}