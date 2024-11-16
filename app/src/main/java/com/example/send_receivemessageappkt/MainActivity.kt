package com.example.send_receivemessageappkt

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        if the permission is not granted, then request for the permission and the define in the arrayOf Manifest
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
//            requesting permissions of the Manifest file
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECEIVE_SMS,Manifest.permission.SEND_SMS),111)
        }else{
//            calling receiveMsg() method
            receiveMsg()
        }


    }

//    if the permission is granted, then show the result via requestCode == 111
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

//    if the requestCode ==111 and grantResults is Permission Granted
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//            then we calling receive() method
            receiveMsg()
    }

//    creating receive() method to read and receive the message
    private fun receiveMsg() {
//        getting BroadcastReceiver in broadcastReceiver variable
        var boardCastReceiver = object : BroadcastReceiver(){

//            implementing BroadcastReceiver abstract method called onReceive() method
            override fun onReceive(context: Context?, intent: Intent?) {

//                if the Android SKD Build Version is Equal and Greater than KITKAT, then run the application, otherwise Application is not for this version
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){

//                    for loop for reading every sms. Getting message from default Message manager app
                    for (sms in Telephony.Sms.Intents.getMessagesFromIntent(intent)){
//                        displaying message body of receiver message
                        Toast.makeText(applicationContext,sms.displayMessageBody,Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

//    Registering BroadcastReceiver for SMS_RECEIVED
        registerReceiver(boardCastReceiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }
}