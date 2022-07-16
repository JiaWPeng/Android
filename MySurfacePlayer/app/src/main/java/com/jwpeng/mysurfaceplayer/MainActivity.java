package com.jwpeng.mysurfaceplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    Button startBut,stopBut,pauseBut;
    MediaPlayer mediaPlayer;
    private int postion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        initView();
        initEnvent();


    }

    private void initEnvent() {
        mediaPlayer = new MediaPlayer ();
        surfaceView.getHolder ().setKeepScreenOn ( true );
        surfaceView.getHolder ().addCallback ( new SurfaceListener());

        startBut.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying ()) {
                    mediaPlayer.pause ();
                    Log.i ( "TAG", "onClick: startBut.pause" );
                    postion = mediaPlayer.getCurrentPosition ();
                } else {
                    if (postion > 0){
                    mediaPlayer.start ();
                        Log.i ( "TAG", "onClick: startBut.start" );
                    }else {
                        try {
                            play (  );
                        } catch ( IOException e ) {
                            e.printStackTrace ();
                        }

                    }

                }
            }
        } );

        stopBut.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
                postion = 0;
                Log.i ( "TAG", "onClick: stopBut" );
            }
        } );

        pauseBut.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    Log.i ( "TAG", "onClick: pauseBut.pause" );
                } else {
                    mediaPlayer.start();
                    Log.i ( "TAG", "onClick: pauseBut.start" );
                }
            }
        } );
    }

    private void initView() {
        surfaceView = findViewById ( R.id.surface );
        startBut = findViewById ( R.id.start_but );
        stopBut = findViewById ( R.id.stop_but );
        pauseBut = findViewById ( R.id.pause_but );
    }


    @Override
    protected void onPause() {
        super.onPause ();
        if (mediaPlayer.isPlaying ()){
            // 保存当前视频位置
            postion = mediaPlayer.getCurrentPosition ();
            mediaPlayer.stop ();
            Log.i ( "TAG", "onPause: stop" );
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy ();
        // 停止播放
        if (mediaPlayer.isPlaying ()){
            mediaPlayer.stop ();
            Log.i ( "TAG", "onDestroy: kiler" );
        }
        // 释放资源
        mediaPlayer.release ();
    }

    private class SurfaceListener implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(@NonNull SurfaceHolder holder) {
            if (postion > 0){
                // 开始播放
                try {
                    play();
                    Log.i ( "TAG", "surfaceCreated() called" );
                } catch ( IOException e ) {
                    e.printStackTrace ();
                }
                // 从指定位置开始播放
                mediaPlayer.seekTo ( postion );
                Log.i ( "TAG", "surfaceCreated: seeto" );

            }
        }

        @Override
        public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

        }
    }

    private void play() throws IOException {
        String uri = Uilt.videoUri;
//            mediaPlayer.reset ();
        mediaPlayer.seekTo ( postion );
            Log.i ( "TAG", "play() called" );
            // 设置播放的视频
            mediaPlayer.setDataSource ( uri );
            // 把画面输出到SurfaceView
            mediaPlayer.setDisplay ( surfaceView.getHolder () );
            mediaPlayer.prepare ();
            mediaPlayer.start ();




    }
}