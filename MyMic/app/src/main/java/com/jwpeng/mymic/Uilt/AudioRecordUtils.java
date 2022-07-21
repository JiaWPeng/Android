package com.jwpeng.mymic.Uilt;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class AudioRecordUtils {
    private String filepath = null;
    private String FolderPath = null;
    private long startTime;
    private long endTime;
    private MediaRecorder mediaRecorder;
    private final String TAG = "fan";
    private int BASE =1;
    // 取样时间
    private int SPACE = 100;
    public static final int MAX_LENGTH = 1000*60*10;

    private final Handler mHandler = new Handler();

    private OnAudioStatusUpdateListener audioStatusUpdateListener;

    public AudioRecordUtils() {
        this(Environment.getExternalStorageDirectory()+"/record/");
    }

    public AudioRecordUtils(String filePath){
        File path = new File(filePath);
        if (!path.exists()){
            path.mkdirs();
        }
        this.FolderPath = filePath;
    }

    public void startRecord(){
        // 开始录音
        if (mediaRecorder == null){
            mediaRecorder = new MediaRecorder();
        }

        try {
            // 设置麦克风
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

            // 设置音频编码格式
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

            //设置文件输出格式: THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP
            //                (3gp格式，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            filepath = FolderPath + TimeUilts.getCurrentTime() + ".mp3";

            // MediaRecorder准备
            mediaRecorder.setOutputFile(filepath);
            mediaRecorder.setMaxDuration(MAX_LENGTH);
            mediaRecorder.prepare();
            mediaRecorder.start();
            startTime = System.currentTimeMillis();
            updateMicStatus();
            Log.e("fan", "startRecord: startTime" +  startTime);
        }catch (IllegalStateException e){
            Log.i(TAG, "startRecord: (File mRecAudioFile)failed!"+ e.getMessage());
        }catch (IOException e){
            Log.i(TAG, "startRecord: (File mRecAudioFile)failed!"+ e.getMessage());
        }
    }

    /**
     * 停止录音
     */
    public long stopRecord(){
        if (mediaRecorder == null){
            return 0L;
        }
        endTime = System.currentTimeMillis();
        try {
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            audioStatusUpdateListener.onStop(filepath);
            filepath = null;
        } catch (RuntimeException e) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            File file = new File(filepath);
            if (file.exists())
                file.delete();
            filepath ="";
        }
        return endTime - startTime;
    }

    /**
     * 取消录音
     */
    public boolean cancelRecord(){
            try {
                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;
            } catch (RuntimeException e) {

            }
            File file = new File(filepath);
            if (file.exists())
                file.delete();
            filepath = "";

        return true;
    }

    /**
     * 创建新线程，来录音
     */
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        @Override
        public void run() {
            updateMicStatus();
        }
    };

    public void setOnAudioStatusUpdateListener(OnAudioStatusUpdateListener audioStatusUpdateListener){
        this.audioStatusUpdateListener = audioStatusUpdateListener;
    }

    /**
     * 更新麦克风状态
     * 录音中
     */
    private void updateMicStatus() {
        if (mediaRecorder != null){
            double ratio = (double) mediaRecorder.getMaxAmplitude() / BASE;
            double db = 0;
            if (ratio >1){
                db = 20*Math.log10(ratio);
                if (null != audioStatusUpdateListener){
                    audioStatusUpdateListener.onUpdate(db,System.currentTimeMillis()-startTime);
                }
            }
            mHandler.postDelayed(mUpdateMicStatusTimer,SPACE);
        }
    }


    public interface OnAudioStatusUpdateListener{
        /**
         * 录音中
         * @param db 当前分贝
         * @param time 录音时间
         */
        public void onUpdate(double db,long time);

        /**
         * 停止录音
         * @param filePath 保存路径
         */
        public void onStop(String filePath);
    }
}
