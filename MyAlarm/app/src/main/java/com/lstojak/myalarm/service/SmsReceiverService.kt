package com.lstojak.myalarm.service

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log


class SmsReceiverService : Service() {

    private val TAG = this.javaClass.simpleName

    private var mSMSreceiver: SmsBroadcastReceiver? = null
    private var mIntentFilter: IntentFilter? = null

//    override fun onCreate() {
//        super.onCreate()
//        Log.i(TAG, "SmsReceiverService started")
//        //SMS event receiver
//
////        val intent = Intent("android.provider.Telephony.SMS_RECEIVED")
////        val infos = packageManager.queryBroadcastReceivers(intent, 0)
////        for (info in infos) {
////            Log.i(TAG, "Receiver name:" + info.activityInfo.name + "; priority=" + info.priority)
////        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//
//        // Unregister the SMS receiver
////        unregisterReceiver(mSMSreceiver)
//    }

    override fun onBind(arg0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        mSMSreceiver = SmsBroadcastReceiver()
        mIntentFilter = IntentFilter()
        mIntentFilter!!.addAction("android.provider.Telephony.SMS_RECEIVED")
        mIntentFilter!!.setPriority(999)
        registerReceiver(mSMSreceiver, mIntentFilter)

        return START_STICKY;
    }

}