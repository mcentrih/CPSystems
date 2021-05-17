package com.example.cpsystemsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityMain extends AppCompatActivity {
    EditText et_usernameLogin, et_passwordLogin;
    String usernameLogin, passwordLogin;
    String URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = findViewById(R.id.viewPager);

        AuthenticationPagerAdapter pagerAdapter = new AuthenticationPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new LoginFragment());
        pagerAdapter.addFragment(new RegisterFragment());
        viewPager.setAdapter(pagerAdapter);

        et_usernameLogin = findViewById(R.id.et_usernameLogin);
        et_passwordLogin = findViewById(R.id.et_passwordLogin);
        usernameLogin = "";
        passwordLogin = "";
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
    public void onClickOpentrack(View view) {
        Intent i = new Intent(getBaseContext(), ActivityTrack.class);
        startActivity(i);
    }

    public void onClickLogin (View view) {
        usernameLogin = et_usernameLogin.getText().toString().trim();
        passwordLogin = et_passwordLogin.getText().toString().trim();
        if(!usernameLogin.equals("") && !passwordLogin.equals("")) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("Login sucessfull!")) {
                        Intent i = new Intent(ActivityMain.this, ActivityTrack.class);
                        startActivity(i);
                        finish();
                    } else if (response.equals("Login failed!")) {
                        Toast.makeText(ActivityMain.this, "Check USERNAME and PASSWORD!", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ActivityMain.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("usernameLogin", usernameLogin);
                    data.put("passwordLogin", passwordLogin);
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
        else {
            Toast.makeText(this, "Fill in required information!", Toast.LENGTH_SHORT).show();
        }
    }

}