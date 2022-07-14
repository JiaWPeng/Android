package com.jwpeng.mywifi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button but_on,but_off,but_visible;
    ListView listView;
    Switch aSwitch;
    private WifiManager wifiManager;
    List<ScanResult> list;
    String[] wifissid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        initView();
        initEvent();
    }

    private void initEvent() {
        if (ContextCompat.checkSelfPermission ( this, Manifest.permission_group.LOCATION )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions ( this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.CHANGE_WIFI_STATE,
            },1 );
        }
        wifiManager = (WifiManager) getApplicationContext().getSystemService ( Context.WIFI_SERVICE );
        if (wifiManager.isWifiEnabled ()){
            aSwitch.setChecked ( true );
        }else {
            aSwitch.setChecked ( false );
        }
        but_on.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                WifiOn ();
            }
        } );
        but_off.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                WifiOff ();
            }
        } );
        but_visible.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                // 开始扫描
                wifiManager.startScan ();
                list = wifiManager.getScanResults ();
                wifissid = new String[list.size ()];
                for (int i=0;i<list.size ();i++) {
                    ScanResult scanResult = list.get ( i );
                    wifissid [i] = String.valueOf ( scanResult.SSID );
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>  ( MainActivity.this,
                        android.R.layout.simple_list_item_1,
                        wifissid );
                listView.setAdapter ( adapter );
            }
        } );
        aSwitch.setOnCheckedChangeListener ( new CompoundButton.OnCheckedChangeListener () {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    WifiOn ();

                }else {
                    WifiOff ();
                }
            }
        } );
    }

    private void WifiOff() {
        if ( wifiManager.isWifiEnabled () ){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                Intent pen = new Intent ( Settings.Panel.ACTION_INTERNET_CONNECTIVITY );
                startActivityForResult ( pen,0 );
            }else{
                wifiManager.setWifiEnabled ( false );
                Toast.makeText ( MainActivity.this, "WIFI已关闭", Toast.LENGTH_SHORT ).show ();
            }
        }else{
            Toast.makeText ( MainActivity.this, "WIFI已经关闭", Toast.LENGTH_SHORT ).show ();
        }
    }

    private void WifiOn() {
        if (! wifiManager.isWifiEnabled()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                Intent pen = new Intent ( Settings.Panel.ACTION_INTERNET_CONNECTIVITY );
                startActivityForResult ( pen,0 );
            }else{
                wifiManager.setWifiEnabled ( true );
                Toast.makeText ( MainActivity.this, "WIFI已打开", Toast.LENGTH_SHORT ).show ();
            }
        }else{
            Toast.makeText ( MainActivity.this, "WIFI已经打开", Toast.LENGTH_SHORT ).show ();
        }
    }


    private void initView() {
        but_on = findViewById ( R.id.but_on );
        but_off = findViewById ( R.id.but_off );
        but_visible = findViewById ( R.id.but_visible );
        listView = findViewById ( R.id.list_wifi );
        aSwitch = findViewById ( R.id.switch_wifi );
    }
}