package com.jwpeng.mysensor;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    ListView listView;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        initView();
        initEvent();

    }

    private void initEvent() {
        textView.setVisibility ( View.GONE );

        sensorManager = (SensorManager) getSystemService ( SENSOR_SERVICE );
        ArrayList arrayList = new ArrayList ();
        List list =  sensorManager.getSensorList ( Sensor.TYPE_ALL );
        for (int i=0; i < list.size (); i++){
            /**
             * setText()  把以前的内容替换了，
             * append()  在以前的内容后面添加。
             */
            textView.setVisibility ( View.VISIBLE );
            Sensor sensorlist = (Sensor) list.get ( i );
            arrayList.add ( sensorlist.getName ()+ "\n"  + sensorlist.getVendor () + "\n" + sensorlist.getVersion () );
            textView.append ( "\n" + sensorlist.getName () + "\n" + sensorlist.getVendor () + "\n" + sensorlist.getVersion ());
        }
        final ArrayAdapter adapter = new ArrayAdapter ( this, android.R.layout.simple_list_item_1,arrayList );
        listView.setAdapter ( adapter );
    }

    private void initView() {
        textView = findViewById ( R.id.sensor_text );
        listView = findViewById ( R.id.sensor_lv );

    }
}