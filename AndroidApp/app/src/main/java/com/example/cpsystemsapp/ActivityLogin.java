package com.example.cpsystemsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class ActivityLogin extends AppCompatActivity {
    String URLLogin = "http://192.168.1.11/CPSystems/podatkovnaBaza/login.php";

    TextView notRegistered;
    Button btn_login;
    EditText et_username, et_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        notRegistered = findViewById(R.id.notRegistered);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, password;
                username = String.valueOf(et_username.getText());
                password = String.valueOf(et_password.getText());


                if (!username.equals("") && !password.equals("")) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //DATABASE FIELDS
                            String[] field = new String[2];
                            field[0] = "username";
                            field[1] = "password";
                            //DATA ARRAY
                            String[] data = new String[2];
                            data[0] = username;
                            data[1] = password;
                            PutData putData = new PutData(URLLogin, "POST", field, data);  //POST to database
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    Log.i("PutData", result);
                                    if (result.equals("Login Success")) {
                                        Toast.makeText(ActivityLogin.this, "Login successfull!", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(getApplicationContext(), ActivityMenu.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Toast.makeText(ActivityLogin.this, "Login failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(ActivityLogin.this, "All fields required, check if passwords match!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onClickNotRegistered (View view) {
        Intent i = new Intent(getApplicationContext(), ActivityRegister.class);
        startActivity(i);
    }

    public void onClickOpentrack(View view) {
        Intent i = new Intent(getBaseContext(), ActivityMenu.class);
        startActivity(i);
    }
}