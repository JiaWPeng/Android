package com.jwpeng.mycamera.Uilts;

import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class ImageUilts {
    public String FolderPath;
    private String TAG = "ImageUilts";

    public ImageUilts(){
        this(Environment.getExternalStorageDirectory()+"/MyCamera/");
    }
    public ImageUilts(String filePath){
        File path = new File(filePath);
        if (!path.exists()){
            path.mkdir();
            Log.d(TAG, "ImageUilts: 创建成功");
        }else{
            //拍一次删一次，避免堆积
            File[] files = path.listFiles();
            if (files != null){
                for (int i = 0; i<files.length;i++ ){
                    files[i].delete();
                    Log.d(TAG, "ImageUilts: "+files[i]+"已删除");
                }
            }
            path.mkdir();
            Log.d(TAG, "ImageUilts: 删除成功");
        }
        this.FolderPath = filePath;
    }

    // 根据文件路径获取byte流
    public static byte[] getPhotoByUrl(String photoUrl) {
        File file = new File(photoUrl);
        if (!file.exists()) {
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] temp = new byte[1024];
            int size = 0;
            while ((size = fis.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
            fis.close();
            return out.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
