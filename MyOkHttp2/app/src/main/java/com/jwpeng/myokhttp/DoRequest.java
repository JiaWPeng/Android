package com.jwpeng.myokhttp;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DoRequest {
    protected String returnMsg;
    private int getpost;


    public String getReturnMsg(){
        return this.returnMsg;
    }

    public void doInBackground(String username,String password,int GetPost) throws IOException {
        if (GetPost == 0){
            getpost = 0;
            String path = "https://jwpeng.xyz/loginRequest.php?username=" + username + "&password=" + password;
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(path)
                    .get()
                    .build();
            extracted(client, request,getpost);
        }else if (GetPost == 1){
            getpost = 1;
            String path = "https://jwpeng.xyz/loginRequest.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("username", username)
                    .add("password",password)
                    .build();
            Request request = new Request.Builder()
                    .url(path)
                    .post(requestBody)
                    .build();
            extracted(client,request,getpost);
        }else {
            String path = "https://jwpeng.xyz/androiduser.php";
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("username", username)
                    .add("password",password)
                    .build();
            Request request = new Request.Builder()
                    .url(path)
                    .post(requestBody)
                    .build();
            extracted(client,request,getpost);
        }
    }

    private void extracted(OkHttpClient client, Request request,int getpost) throws IOException {
        Call call = client.newCall(request);
        Response response = call.execute();

        // 解析响应结果
        Headers headers = response.headers();
        for (int i=0;i < headers.size(); i++){
            if (getpost == 0){
                Log.i("GET返回头",headers.name(i) + ":" + headers.value(i));
            }else if (getpost == 1){
                Log.i("POSt返回头",headers.name(i) + ":" + headers.value(i));
            }else {
                Log.i("Register返回头",headers.name(i) + ":" + headers.value(i));
            }

        }
        this.returnMsg = response.body().string();
        Log.i ( "msg",returnMsg );
    }
}
