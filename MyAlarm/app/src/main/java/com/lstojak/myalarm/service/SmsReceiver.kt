package com.lstojak.myalarm.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import com.lstojak.myalarm.util.DbHelper


class SmsReceiver : BroadcastReceiver() {

    companion object {
        val SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED"
    }

    private var dbHelper : DbHelper? = null


    override fun onReceive(context: Context?, intent: Intent?) {
//        if(context == null || intent == null || intent.action == null){
//            return
//        }
////        if (intent.action != (Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
////            return
////        }
//        dbHelper = DbHelper.getInstance(context)
//        val phoneNumber : String? = dbHelper!!.readPhoneNumber().value
//
//        val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
//
//        for (message in smsMessages) {
//            if (message.displayOriginatingAddress.equals(phoneNumber)) {
//                if(message.messageBody.contains("ALARM")) {
//                    val music: MediaPlayer = MediaPlayer.create(context, com.lstojak.myalarm.R.raw.alarm)
//                    music.start();
//
//                    Toast.makeText(context,
//                        "ALARM",
//                        Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(context,
//                        "Message from ${message.displayOriginatingAddress} : body ${message.messageBody}",
//                        Toast.LENGTH_SHORT).show()
//                }
//            } else {
//                Toast.makeText(context,
//                    "Different number",
//                    Toast.LENGTH_SHORT).show()
//            }
//        }

        val bundle: Bundle? = intent!!.extras

        try {
            if (bundle != null) {
                val pdusObj = bundle.get("pdus") as Array<Any>
                for (i in pdusObj.indices) {
                    val currentMessage: SmsMessage = SmsMessage.createFromPdu(pdusObj[i] as ByteArray)
                    val phoneNumber: String = currentMessage.getDisplayOriginatingAddress()
                    val message: String = currentMessage.getDisplayMessageBody()
                    Log.i("SmsReceiver", "senderNum: $phoneNumber; message: $message")


                    // Show Alert
                    val duration = Toast.LENGTH_LONG
                    val toast = Toast.makeText(
                        context,
                        "senderNum: $phoneNumber, message: $message", duration
                    )
                    toast.show()
                } // end for loop
            } // bundle is null
        } catch (e: Exception) {
            Log.e("SmsReceiver", "Exception smsReceiver$e")
        }

        TODO("Not yet implemented")
    }

}