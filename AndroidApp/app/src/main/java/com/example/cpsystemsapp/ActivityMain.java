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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.ArrayList;

public class ActivityMain extends AppCompatActivity {
    String URLRegister = "http://localhost/CPSystems/podatkovnaBaza/signup.php";
    //String URLRegister = "http://192.168.1.10/CPSystems/podatkovnaBaza/signup.php";
    String URLLogin = "http://localhost/CPSystems/podatkovnaBaza/login.php";
    //String URLLogin = "http://192.168.1.10/CPSystems/podatkovnaBaza/login.php";

    Button btn_register, btn_login;
    EditText et_name, et_mail, et_username, et_password, et_repassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_name = findViewById(R.id.et_name);
        et_mail = findViewById(R.id.et_mail);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_repassword = findViewById(R.id.et_repassword);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login = (Button) findViewById(R.id.btn_login);
    }

    class AuthenticationPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentList = new ArrayList<>();

        public AuthenticationPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }

}