package com.jwpeng.mygps


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var CC = arrayOf("2431652649@qq.com")

    @SuppressLint("IntentReset")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sendbutton.setOnClickListener { 
            val intent = Intent(Intent.ACTION_SEND)
            intent.setData(Uri.parse("mailto"))
            intent.setType("text/plain")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(send_email.text.toString()))
            intent.putExtra(Intent.EXTRA_TEXT,text1_email.text.toString())
            intent.putExtra(Intent.EXTRA_SUBJECT,title_email.text.toString())
            intent.putExtra(Intent.EXTRA_CC,CC)
            intent.putExtra(Intent.EXTRA_BCC,CC)
            startActivity(Intent.createChooser(intent,"发送Email...."))

        }
    }

}