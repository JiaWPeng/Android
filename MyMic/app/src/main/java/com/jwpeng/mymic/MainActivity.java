package com.jwpeng.mymic;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.jwpeng.mymic.Uilt.AudioRecordUtils;
import com.jwpeng.mymic.Uilt.PopWindowFactory;
import com.jwpeng.mymic.Uilt.TimeUilts;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    static final int  VOICE_REQUEST_CODE = 66;
    private static final int REQUES_CODE = 1024 + 1;
    private static final String TAG = "MIC";
    private Button bfBut,lyBut;
    private ImageView mImageView;
    private TextView mTextView,cancelTextView;
    private AudioRecordUtils mAudioRecoderUtils;
    private Context context;
    private PopWindowFactory mPop;
    private RelativeLayout rl;
    private TextView txt;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        context = this;
        initView();
        initEnvent();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initEnvent() {
        bfBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer player = new MediaPlayer();
                try {
                    player.setDataSource(txt.getText().toString());
                    player.prepare();
                    player.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        mAudioRecoderUtils = new AudioRecordUtils();

        /**
         * 录音回调
         */
        mAudioRecoderUtils.setOnAudioStatusUpdateListener(new AudioRecordUtils.OnAudioStatusUpdateListener() {
            @Override
            public void onUpdate(double db, long time) {
                mImageView.getDrawable().setLevel((int) (3000 + 6000 *db / 100));
                mTextView.setText(TimeUilts.long2String(time));
            }

            @Override
            public void onStop(String filePath) {
                Toast.makeText(MainActivity.this, "录音保存在：" + filePath, Toast.LENGTH_SHORT).show();
                txt.setText(filePath);
                mTextView.setText(TimeUilts.long2String(0));

            }
        });
        requestPermissions();
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= 30){
            if (Environment.isExternalStorageManager()){
                StartListener();
            }else{
                ActivityCompat.requestPermissions(
                        (Activity) context,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO},
                        VOICE_REQUEST_CODE);
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:"+getApplication().getPackageName()));
                if (intent.resolveActivity(getPackageManager()) != null)
                startActivityForResult(intent,REQUES_CODE);

            }
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if ((ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    &&(ContextCompat.checkSelfPermission(context,Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)){
                StartListener();
            }else {
                ActivityCompat.requestPermissions(
                        (Activity) context,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO},
                        VOICE_REQUEST_CODE);
            }
        }else{
            StartListener();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == VOICE_REQUEST_CODE){
            if ((grantResults[0] == PackageManager.PERMISSION_GRANTED)
            && (grantResults[1] == PackageManager.PERMISSION_GRANTED)){
                StartListener();
            }else {
                Toast.makeText(context, "已拒绝权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void StartListener() {
        lyBut.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float startY = 0;
                float endY = 0;
                float moveY = 0;
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        TouchDown(event);
                        break;
                    case MotionEvent.ACTION_UP:
                        TouchUP(event, startY);
                        break;
                    case MotionEvent.ACTION_MOVE :
                        TouchMove(event, startY);
                        break;
                }
                return false;
            }
        });
    }

    private void TouchMove(MotionEvent event, float startY) {
        float moveY;
        moveY = event.getY();

        Log.d(TAG, "onTouch: moveY"+moveY+"startY"+ startY);

        int cancenl = (int) Math.abs(moveY - startY);

        Log.d(TAG, "onTouch: MOVE"+ cancenl);

        if (cancenl > 200){
            cancelTextView.setText("松开完成取消");
        }
    }

    private void TouchUP(MotionEvent event, float startY) {
        float endY;
        endY = event.getY();

        Log.d(TAG, "onTouch: endY"+ endY+"startY"+ startY);

        int end = (int) Math.abs(startY -endY);

        Log.d(TAG, "onTouchend: "+end);

        if (end<200) {

            mAudioRecoderUtils.stopRecord();

            mPop.dismiss();

            lyBut.setText("按住说话");

        }else{

            mAudioRecoderUtils.cancelRecord();

            mPop.dismiss();

            lyBut.setText("按住说话");

            Toast.makeText(context, "已取消！", Toast.LENGTH_SHORT).show();

            Log.d(TAG, "onTouch: cancel"+mAudioRecoderUtils.cancelRecord());
        }
    }

    private void TouchDown(MotionEvent event) {
        float startY;
        startY = event.getY();

        Log.d(TAG, "onTouch: startY"+startY);

        cancelTextView.setText("上滑取消");

        mPop.showAtLocation(rl, Gravity.CENTER,0,0);

        lyBut.setText("松开保存");

        mAudioRecoderUtils.startRecord();
    }


    private void initView() {
        rl = findViewById(R.id.rl);
        bfBut = findViewById(R.id.bf_but);
        lyBut = findViewById(R.id.record_but);
        txt = findViewById(R.id.src_text);
        final View view = View.inflate(this,R.layout.layout_microphone,null);
        mPop = new PopWindowFactory(this,view);
        mImageView = view.findViewById(R.id.mic_image);
        mTextView = view.findViewById(R.id.mic_text);
        cancelTextView = view.findViewById(R.id.cancel_text);
    }
}