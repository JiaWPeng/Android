
package com.jwpeng.mycamera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jwpeng.mycamera.Uilts.TimeUilt;
import com.jwpeng.mycamera.Uilts.ImageUilts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageUilts imageUilts;
    private Button button,xcBut;
    private String TAG = "MyCamera";
    private ImageView imageView,iv;
    private TextView textView;
    public Uri photoUri,xcuri;

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
        xcBut = findViewById(R.id.xc_but);
        iv = findViewById(R.id.imageView);
        textView = findViewById(R.id.iv_text);
    }

    private void initEvent() {
        requestPermissions();
    }

    private static final int MY_PERSSIONS_CAMERA = 100;
    private static final int REQUES_CODE = 1024 + 1;
    private void requestPermissions() {
        // ??????11 and ??????6?????? ??????????????????
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
        xcBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private static final int REQUEST_CODE_GALLERY = 3;
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        startActivityForResult(intent,REQUEST_CODE_GALLERY);
    }

    private static final int REQUEST_CODE_CAMERA = 2;
    private void openCAMERA() {
        imageUilts = new ImageUilts();
        // ?????????
        String fileName = TimeUilt.getName()+".jpg";
        // ??????????????????
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        // ??????????????????
        File file = new File(imageUilts.FolderPath + fileName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // ??????7.0????????????????????????
            photoUri = FileProvider.getUriForFile(this, "iamge.fileprovider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }else{
            photoUri = Uri.fromFile(file);
        }
        Log.d(TAG, "openCAMERA: "+photoUri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent,REQUEST_CODE_CAMERA);
    }


    private static final int REQUEST_CODE_CJ = 5;
    private void StartGallery(Uri uri) {
        imageUilts = new ImageUilts();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // ??????????????????????????????
        intent.putExtra("crop", "true");
        // ?????????????????????????????????????????????????????????????????????
        if (Build.MANUFACTURER.contains("HUAWEI")) {
            //??????????????????????????????????????????????????? value=1?????????value=0.5???????????????????????????
            intent.putExtra("aspectX", 0.5);
            intent.putExtra("aspectY", 0.5);
            Log.d(TAG, "StartGallery: HUAWEI");
        }else{
            //?????????????????????????????????
            intent.putExtra("aspectX", 0.5);
            intent.putExtra("aspectY", 0.5);
            Log.d(TAG, "StartGallery: OTHER");
        }

        // ????????????????????????????????????????????????????????????????????????????????????
        //intent.putExtra("circleCrop", true);

        // ????????????????????????
        intent.putExtra("scale", true);

        // ??????????????????
        File cropFile = new File(imageUilts.FolderPath + "test.jpg");
        Log.d(TAG, "StartGallery: File"+cropFile);

        xcuri = Uri.fromFile(cropFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, xcuri);
        Log.d(TAG, "StartGallery: xcuri "+xcuri);

        // ???????????????????????????
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // return-data=true????????????????????????????????????????????????????????????????????????onActivityResult????????????
        intent.putExtra("return-data", false);
        startActivityForResult(intent, REQUEST_CODE_CJ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_CODE_CAMERA) {
            Uri uri = null;
            uri = photoUri;
            iv.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
            imageView.setImageURI(uri);
            Log.d(TAG, "onActivityResult: "+uri);
        }


        if (data != null){
            switch (requestCode){
                case REQUEST_CODE_GALLERY:
                    if (data.getData() != null){
                        xcuri = data.getData();
                        StartGallery(xcuri);
                    }
                    break;
                case REQUEST_CODE_CJ:
                        Bitmap bitmap = null;
                        try {
                            // ?????????
                            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(xcuri));
                            iv.setVisibility(View.INVISIBLE);
                            textView.setVisibility(View.INVISIBLE);
                            imageView.setImageBitmap(bitmap); // ?????????????????????????????????
                            Log.d(TAG, "onActivityResult: bitmap "+bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERSSIONS_CAMERA){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                  && grantResults[1] == PackageManager.PERMISSION_GRANTED ){
                ButOnCilck();
            }else{
                Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
            }
        }
    }

}