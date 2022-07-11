package com.jwpeng.mybluetooth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Button b1, b2, b3, b4;
    ListView listItem;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    @SuppressLint("MissingPermission")
    public void on(View v) {
        if (BA != null) {
            boolean startbluetooth = BA.isEnabled();
            if (startbluetooth) {
                    Toast.makeText(this, "蓝牙已经打开", Toast.LENGTH_SHORT).show();
            } else{
                BA.enable();
                Toast.makeText(getApplicationContext(), "蓝牙已打开", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.i("Bluetooth:::", "不支持蓝牙！");
            finish();
        }
    }

    public void off(View v) {
        notifBluetooth();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            BA.disable();
            Toast.makeText(getApplicationContext(), "蓝牙已关闭", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void visible(View v) {
        notifBluetooth();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
            Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivityForResult(getVisible, 0);
            return;
        }
    }

    public void list(View v) {
        notifBluetooth();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            pairedDevice = BA.getBondedDevices();
            ArrayList list = new ArrayList();
            for (BluetoothDevice bt : pairedDevice) list.add(bt.getName());
            Toast.makeText(getApplicationContext(), "显示已配对的设备", Toast.LENGTH_SHORT).show();
            final ArrayAdapter adapter = new ArrayAdapter(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,list);
            listItem.setAdapter(adapter);
            return;
        }
    }

    public void notifBluetooth() {
        if (BA == null) {
            Log.i("Bluetooth:::", "不支持蓝牙！");
            finish();
        }
    }

    private void initView() {
        b1 = findViewById(R.id.but);
        b2 = findViewById(R.id.but2);
        b3 = findViewById(R.id.but3);
        b4 = findViewById(R.id.but4);
        listItem = (ListView) findViewById(R.id.list_item);
        BA = BluetoothAdapter.getDefaultAdapter();
    }
}