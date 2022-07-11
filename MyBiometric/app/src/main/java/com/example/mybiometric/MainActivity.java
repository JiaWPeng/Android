package com.example.mybiometric;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
    }


    private void initEvent() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("验证信息")
                        .setDescription("要用户验证才能继续！")
                        .setNegativeButtonText("取消")
                        .build();
                getPrimpt().authenticate(promptInfo);
            }
        });
    }

    private void initView() {
        button = findViewById(R.id.but);
    }

    private BiometricPrompt getPrimpt(){
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt.AuthenticationCallback callback =new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                notifyUser(errString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                notifyUser("识别成功！");
                Intent intent = new Intent(MainActivity.this,SecretActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                notifyUser("识别失败！");
            }
        };
        BiometricPrompt biometricPrompt = new BiometricPrompt(this,executor,callback);
        return biometricPrompt;

    }
    private void notifyUser(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}