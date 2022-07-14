package com.jwpeng.mynfc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        initView();
        initEvent();
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent ( intent );
//        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())
//                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
//            processIntent(intent);
//            Log.i ( "NFC", "onNewIntent() called with: intent = [" + intent + "]" );
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume ();

        if(NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent ().getAction())|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent ().getAction())
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(getIntent ().getAction()))
        {
            processIntent(getIntent());
            Log.i ( "NFC", "ifResume called" );
        }
        Log.i ( "NFC", "Resume called" );
    }



    // 将字符序列转换为 16 进制字符串
    private String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder ("0x");
        if (src == null || src.length <= 0){
            return null;
        }
        char[] buffer = new char[2];
        for (int i=0; i< src.length;i++){
            buffer[0] = Character.forDigit ( (src[i] >>> 4)&0x4F,16 );
            buffer[1] = Character.forDigit ( src[i]&0x4F,16 );
            stringBuilder.append ( buffer );
        }
        return stringBuilder.toString ();
    }



    private void processIntent(Intent intent) {

        //取出封装在 intent 中的 TAG
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter. EXTRA_TAG);
        for(String tech : tagFromIntent. getTechList()) {
            System.out.println(tech);
        }
        boolean auth = false;
        //读取 TAG
        MifareClassic mfc = MifareClassic. get(tagFromIntent);
        try {
            String metaInfo = "";
            mfc.connect();
            int type = mfc.getType();//获取 TAG 的类型
            int sectorCount = mfc. getSectorCount();//获取 TAG 中包含的扇区数
            String typeS = "";
            switch(type) {
                case MifareClassic. TYPE_CLASSIC:
                    typeS = "TYPE_CLASSIC";
                    break;
                case MifareClassic. TYPE_PLUS:
                    typeS = "TYPE_PLUS";
                    break;
                case MifareClassic. TYPE_PRO:
                    typeS = "TYPE_PRO";
                    break;
                case MifareClassic. TYPE_UNKNOWN:
                    typeS = "TYPE_UNKNOWN";
                    break;
            }
            metaInfo += "卡片类型：" +typeS +"\n 共" +sectorCount +"个扇区\n 共"
                    + mfc. getBlockCount() +"个块\n 存储空间: " +mfc. getSize() +"B\n";
            for(int j = 0; j < sectorCount; j ++) {
                // 用KeyA对一个扇区进行身份验证
                auth =mfc. authenticateSectorWithKeyA(j, MifareClassic. KEY_DEFAULT);
                int bCount;
                int bIndex;
                if(auth) {
                    metaInfo += "Sector " +j +":验证成功\n";
                    //读取扇区中的块
                    bCount = mfc. getBlockCountInSector(j);
                    bIndex = mfc. sectorToBlock(j);
                    for(int i = 0; i < bCount; i ++) {
                        byte[] data = mfc. readBlock(bIndex);
                        metaInfo += "Block " +bIndex +" : "
                                + bytesToHexString(data) +"\n";
                        bIndex ++;
                    }
                } else {
                    metaInfo += "Sector " +j +":验证失败\n";
                }
            }
            textView. setText(metaInfo);
        } catch(Exception e) {
            e. printStackTrace();
        }
    }


    private void initEvent() {
        // 获得NFC控制器
        nfcAdapter = NfcAdapter.getDefaultAdapter ( this );

        if (nfcAdapter == null){
            textView.setText ( "设备不支持NFC！" );
            textView.setTextColor ( Color.RED );
            return;
        }
        if (!nfcAdapter.isEnabled ()){
            textView.setText ( "请在系统中开启NFC！" );
            return;
        }

    }

    private void initView() {
        textView = findViewById ( R.id.nfc_text );
    }
}