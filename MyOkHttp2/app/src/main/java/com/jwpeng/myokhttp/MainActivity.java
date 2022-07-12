package com.jwpeng.myokhttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    EditText usernamef, passwordf;
    TextView status, role, method;
    Button Register, loginGET, loginPOST;
    private int getpost;
    private Handler handler = new Handler ( Looper.getMainLooper () );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        initView ();
        initEvent ();
    }

    public void showMsg(String msg) {
        String text = msg;
        handler.post ( new Runnable () {
            @Override
            public void run() {
                role.setText ( msg );
                switch (text)  {
                    case "error" : status.setText ( "登录失败" );
                    case "123" : status.setText ( "登录成功" );
                    // default:status.setText ( "登录成功" );
                }
            }
        } );

    }


//    public void loginGET(View view){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                String username = usernamef.getText().toString();
//                String password = passwordf.getText().toString();
//                Message message = Message.obtain();
//                try {
//                    getpost = 0;
//                    extracted(username,password,getpost);
//                    method.setText("GET方法");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }


//    public void loginPOST(View view){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                String username = usernamef.getText().toString();
//                String password = passwordf.getText().toString();
//                Message message = Message.obtain();
//                try {
//                    getpost = 1;
//                    extracted(username, password,getpost);
//                    method.setText("POST方法");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Looper.loop();
//            }
//        }).start();
//    }

    private void extracted(String username, String password, int getpost) throws IOException {
        DoRequest dr = new DoRequest ();
        dr.doInBackground ( username, password, getpost );
        String res = dr.getReturnMsg ();
        showMsg ( res );
    }

    private void initEvent() {
        loginPOST.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                new Thread ( new Runnable () {
                    @Override
                    public void run() {
                        String username = usernamef.getText ().toString ();
                        String password = passwordf.getText ().toString ();
                        Message message = Message.obtain ();
                        try {
                            getpost = 1;
                            extracted ( username, password, getpost );
                            handler.post ( new Runnable () {
                                @Override
                                public void run() {
                                    method.setText ( "POST方法" );
                                }
                            } );
                        } catch ( IOException e ) {
                            e.printStackTrace ();
                        }
                    }
                } ).start ();
                Looper.loop();
            }
        } );

        loginGET.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                new Thread ( new Runnable () {
                    @Override
                    public void run() {
                        Looper.prepare ();
                        String username = usernamef.getText ().toString ();
                        String password = passwordf.getText ().toString ();
                        Message message = Message.obtain ();
                        try {
                            getpost = 0;
                            extracted ( username, password, getpost );
                            handler.post ( new Runnable () {
                                @Override
                                public void run() {
                                    method.setText ( "GET方法" );
                                }
                            } );
                        } catch ( IOException e ) {
                            e.printStackTrace ();
                        }
                    }
                } ).start ();
                Looper.loop ();
            }
        } );
        Register.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                new Thread ( new Runnable () {
                    @Override
                    public void run() {
                        Looper.prepare ();
                        String username = usernamef.getText ().toString ();
                        String password = passwordf.getText ().toString ();
                        Message message = Message.obtain ();
                        try {
                            getpost = 2;
                            extracted ( username, password, getpost );
                            handler.post ( new Runnable () {
                                @Override
                                public void run() {
                                    method.setText ( "注册成功，请登录" );
                                }
                            } );
                        } catch ( IOException e ) {
                            e.printStackTrace ();
                        }
                    }
                } ).start ();
                Looper.loop ();
            }
        } );
    }

    private void initView() {
        usernamef = findViewById ( R.id.user_edit );
        passwordf = findViewById ( R.id.pwd_edit );
        role = findViewById ( R.id.loginuser9 );
        method = findViewById ( R.id.loginEvent6 );
        status = findViewById ( R.id.loginif7 );
        Register = findViewById ( R.id.Register );
        loginGET = findViewById ( R.id.loginGET );
        loginPOST = findViewById ( R.id.loginPOST );
    }
}