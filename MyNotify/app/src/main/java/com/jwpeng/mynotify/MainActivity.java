package com.jwpeng.mynotify;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jwpeng.mynotify.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_ID = 1;
    private Button button;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initView();
        initEnvent();
    }

    private void initEnvent() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotification();
            }
        });
    }

    private void addNotification() {
        String notify = editText.getText().toString();

        //
        NotificationManager manger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 设置通知的各属性
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentInfo("内容消息")
                .setContentText(notify)
                .setContentTitle("Notify Demo")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setSubText("SubText")
                .setTicker("editText.getText()")
                .setSmallIcon(R.drawable.ic_not)
                .setWhen(System.currentTimeMillis());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("001","Demo",NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.setShowBadge(true);

            manger.createNotificationChannel(channel);
            builder.setChannelId("001");
        }

        // 点击通知跳转
//        Intent intent = new Intent(this, LoginActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
//        builder.setContentIntent(pendingIntent);

        /**
         * 点击通知后打开多个Activity
         */
        Intent intent1 = new Intent(this,MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intent intent2 = new Intent(this,LoginActivity.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intent[] intents = new Intent[2];
        intents[0] = intent1;
        intents[1] = intent2;
        PendingIntent pendingIntent1 = PendingIntent.getActivities(this,
                0,
                intents,
                PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent1);

        Notification n = builder.build();
        manger.notify(NOTIFICATION_ID,n);

    }

    private void initView() {
        button = findViewById(R.id.notify_but);
        editText = findViewById(R.id.notify_text);
    }
}