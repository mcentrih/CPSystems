package com.example.cpsystemsapp;

import androidx.appcompat.app.AppCompatActivity;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

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

public class ActivityRegister extends AppCompatActivity {
    String URLRegister = "http://192.168.1.11/CPSystems/podatkovnaBaza/signup.php";

    TextView alreadyRegistered;
    Button btn_register;
    EditText et_name, et_mail, et_username, et_password, et_repassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_name = findViewById(R.id.et_name);
        et_mail = findViewById(R.id.et_mail);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_repassword = findViewById(R.id.et_repassword);
        btn_register = (Button) findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, password, name, mail, repassword;
                username = String.valueOf(et_username.getText());
                password = String.valueOf(et_password.getText());
                name = String.valueOf(et_name.getText());
                mail = String.valueOf(et_mail.getText());
                repassword = String.valueOf(et_repassword.getText());

                if (password.equals(repassword)) {
                    if (!username.equals("") && !password.equals("") && !name.equals("") && !mail.equals("") && !repassword.equals("")) {
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //DATABASE FIELDS
                                String[] field = new String[4];
                                field[0] = "fullname";
                                field[1] = "username";
                                field[2] = "password";
                                field[3] = "email";
                                //DATA ARRAY
                                String[] data = new String[4];
                                data[0] = name;
                                data[1] = username;
                                data[2] = password;
                                data[3] = mail;
                                PutData putData = new PutData(URLRegister, "POST", field, data);  //POST to database
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        String result = putData.getResult();
                                        Log.i("PutData", result);
                                        if (result.equals("Sign Up Success")) {
                                            Toast.makeText(ActivityRegister.this, "Registration successfull!", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
                                            startActivity(i);
                                            finish();
                                        } else {
                                            Toast.makeText(ActivityRegister.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        Toast.makeText(ActivityRegister.this, "All fields required, check if passwords match!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(ActivityRegister.this, "Re-enter password!", Toast.LENGTH_SHORT).show();
                    Log.i("ERROR PASS", "Re-enter password!");
                }
            }
        });
    }

    public void onClickAlreadyRegistered(View view) {
        Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
        startActivity(i);
    }
}