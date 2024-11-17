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
import android.telephony.SmsManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.send_receivemessageappkt.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

//    initiating ViewBinding
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        inflating layout of the .xml via ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
//        getting root of the .xml file via ViewBinding
        setContentView(binding.root)
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

//            event listener on btSendID button
            binding.btSendID.setOnClickListener {
//              getting default message app of the device
                var sms = SmsManager.getDefault()
//                sending text message to enter number of edNum EditText. By passing some required argument
                sms.sendTextMessage(binding.edNum.text.toString(),"ME",binding.edMessage.text.toString(),null,null)
            }
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
//                        Toast.makeText(applicationContext,sms.displayMessageBody,Toast.LENGTH_LONG).show()

//                        setting originationAddress edNum means Phone Number
                        binding.edNum.setText(sms.originatingAddress)
//                        setting displayMessageBody to edMessage means Phone Number
                        binding.edMessage.setText(sms.displayMessageBody)

                    }
                }
            }
        }

//    Registering BroadcastReceiver for SMS_RECEIVED
        registerReceiver(boardCastReceiver, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }
}