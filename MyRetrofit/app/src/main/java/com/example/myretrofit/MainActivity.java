package com.example.myretrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<MainData> dataArrayList = new ArrayList<>();
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler_view);
    }

    private void initEvent() {
        // 初始化适配器
        adapter = new MainAdapter(MainActivity.this,dataArrayList);
        // 设置布局管理器
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        // 设置适配器
        recyclerView.setAdapter(adapter);
        // 创建get数据方法
        getData();
    }

    private void getData() {
        // 初始化进度条对话框
        ProgressDialog dialog = new ProgressDialog(MainActivity.this);
        // 设置信息对话框
        dialog.setMessage("请稍等...");
        // 设置不可取消
        dialog.setCancelable(false);
        // 显示对话框
        dialog.show();
        // 初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://picsum.photos/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        // 创建Interface
        MainInterface mainInterface = retrofit.create(MainInterface.class);
        // 调用Interface
        Call<String> stringCall = mainInterface.STRING_CALL();

        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // 判断条件
                if (response.isSuccessful() && response.body() != null){
                    // 当响应成功切不为空时
                    dialog.dismiss();
                    try {
                        // 初始化Json数组
                        JSONArray jsonArray = new JSONArray(response.body());
                        parseArray(jsonArray);
                        // 解析Json数组
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void parseArray(JSONArray jsonArray) {
        // 清除数组列表
        dataArrayList.clear();
        // 用于循环
        for (int i=0; i<jsonArray.length();i++){
            try {
                // 初始化Json对象
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // 初始化主数据、
                MainData mainData = new MainData();
                // 设置图像
                mainData.setImage(jsonObject.getString("download_url"));
                // 设置name
                mainData.setName(jsonObject.getString("author"));
                // 在数组列表添加数据
                dataArrayList.add(mainData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // 创建适配器
            adapter = new MainAdapter(MainActivity.this,dataArrayList);
            // 设置适配器
            recyclerView.setAdapter(adapter);
        }


    }
}