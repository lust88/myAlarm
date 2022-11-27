package com.lstojak.myalarm.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import com.lstojak.myalarm.util.DbHelper


class SmsBroadcastReceiver : BroadcastReceiver() {

    companion object {
        val ALARM = "ALARM";
    }

    private var dbHelper : DbHelper? = null

    override fun onReceive(context: Context, intent: Intent) {
        dbHelper = DbHelper.getInstance(context)
        val alarmPhoneNumber : String? = dbHelper!!.readPhoneNumber().value

        if (alarmPhoneNumber.isNullOrBlank()) {
            Toast.makeText(context,"Please fill the settings",Toast.LENGTH_LONG).show()
            return
        }

        val bundle: Bundle? = intent.extras

        try {
            if (bundle != null) {
                val pdusObj = bundle.get("pdus") as Array<Any>
                for (i in pdusObj.indices) {
                    val currentMessage: SmsMessage = SmsMessage.createFromPdu(pdusObj[i] as ByteArray)
                    val phoneNumber: String = currentMessage.displayOriginatingAddress
                    val message: String = currentMessage.displayMessageBody
                    Log.i("SmsReceiver", "senderNum: $phoneNumber; message: $message")

                    if (phoneNumber.contains(alarmPhoneNumber!!) && message.contains(ALARM)) {
                        Thread {
                            playAlarmSound(context)
                        }.start()

                        Toast.makeText(context,"Alarm: $message",Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Message from $phoneNumber : body $message", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("SmsReceiver", "Exception smsReceiver: $e")
        }
    }

    private fun playAlarmSound(context: Context) {
        val music: MediaPlayer = MediaPlayer.create(context, com.lstojak.myalarm.R.raw.alarm)
        music.start();

        while (music.isPlaying) {}
        music.start();
    }
}