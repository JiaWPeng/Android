
package com.jwpeng.mycamera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.jwpeng.mycamera.Uilts.TimeUilt;
import com.jwpeng.mycamera.Uilts.ImageUilts;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERSSIONS_CAMERA = 100;
    private static final int REQUES_CODE = 1024 + 1;
    private ImageUilts imageUilts;
    private Button button;
    private String TAG = "MyCamera";
    private ImageView imageView;
    private Bitmap photo;
    public Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
    }

    private void initView() {
        button = findViewById(R.id.but);
        imageView = findViewById(R.id.image);
    }

    private void initEvent() {
        requestPermissions();
    }

    private void requestPermissions() {
        // 安卓11 and 安卓6以上 细分权限获取
        if (Build.VERSION.SDK_INT >=30) {
            if (Environment.isExternalStorageManager()){
                ButOnCilck();
            }else {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                },MY_PERSSIONS_CAMERA);
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:"+getApplication().getPackageName()));
                if (intent.resolveActivity(getPackageManager()) != null)
                    startActivityForResult(intent,REQUES_CODE);
            }
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                ButOnCilck();
            }else{
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                },MY_PERSSIONS_CAMERA);
            }
        }else{
            ButOnCilck();
        }
    }

    private void ButOnCilck() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCAMERA();

            }
        });
    }

    private int REQUEST_CODE = 2;
    private void openCAMERA() {
        imageUilts = new ImageUilts();
        // 文件名
        String fileName = TimeUilt.getName()+".jpg";
        // 创建相机意图
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        // 创建文件路径
        File file = new File(imageUilts.FolderPath + fileName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // 安卓7.0以上使用文件共享
            photoUri = FileProvider.getUriForFile(this, "iamge.fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }else{
            photoUri = Uri.fromFile(file);
        }
        Log.d(TAG, "openCAMERA: "+photoUri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent,REQUEST_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_CODE) {
            Uri uri = null;
//            if (data != null && data.getData() != null){
//                uri = data.getData();
//                Log.d(TAG, "onActivityResult: "+data.getData());
//            }
//            if (uri != null){
//                if (photoUri != null){
//                    uri = photoUri;
//                    Log.d(TAG, "onActivityResult: "+uri);
//                }
//            }
            uri = photoUri;
            imageView.setImageURI(uri);
            Log.d(TAG, "onActivityResult: "+uri);
        }

//        // 获得的是缩略图
//        if (requestCode == 2 && resultCode == RESULT_OK && data != null){
//            Bundle bundle = data.getExtras();
//            photo = (Bitmap) bundle.get("data");
//            imageView.setImageBitmap(photo);
//
//        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERSSIONS_CAMERA){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                  && grantResults[1] == PackageManager.PERMISSION_GRANTED ){
                ButOnCilck();
            }else{
                Toast.makeText(this, "未获得权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

}