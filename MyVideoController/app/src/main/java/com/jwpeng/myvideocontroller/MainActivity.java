package com.jwpeng.myvideocontroller;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        initView();
        initEnvent();
    }

    private void initEnvent() {
        String videouri = Utils.videoUri;
        Uri uri = Uri.parse ( videouri );
        // 设置视频控制器
        videoView.setMediaController ( new MediaController ( this ) );
        videoView.setOnCompletionListener ( new MyPlayerOnCompletionListener() );
        videoView.setVideoURI ( uri );
        videoView.start ();
    }

    private void initView() {
        videoView = findViewById ( R.id.video );
    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText ( MainActivity.this, "播放完成", Toast.LENGTH_SHORT ).show ();
        }
    }
}