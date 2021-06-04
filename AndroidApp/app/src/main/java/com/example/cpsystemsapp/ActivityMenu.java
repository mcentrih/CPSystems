package com.example.cpsystemsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class ActivityMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    private static final int IMAGE_SELECT_CODE = 1002;

    private DrawerLayout drawer;
    ImageView imageView;
    ImageButton buttonSend, buttonCapture, buttonSearchPic;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        imageView = findViewById(R.id.imageView);
        buttonSend = findViewById(R.id.buttonSend);
        buttonCapture = findViewById(R.id.buttonCapture);
        buttonSearchPic = findViewById(R.id.buttonSearchPic);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentCamera()).commit();
            navigationView.setCheckedItem(R.id.nav_camera);
        }

        //for SKD older than 23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED &&
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                requestPermissions(permission, PERMISSION_CODE);
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //selecting and displaying fragment
        switch (item.getItemId()) {
            case R.id.nav_camera:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentCamera()).commit();
                break;
            case R.id.nav_road:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentRoad()).commit();
                break;
            case R.id.nav_car:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentCar()).commit();
                break;
            case R.id.nav_send:
                Intent sendIntent = new Intent(getBaseContext(), ActivitySend.class);
                startActivity(sendIntent);
                break;
            case R.id.nav_settings:
                Toast.makeText(this, "Settings!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_communication:
                Toast.makeText(this, "Communication!", Toast.LENGTH_SHORT).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*
    public void onClickCapture(View view) {
        //for SKD older than 23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE);
            } else {
                //permissions already granted
                openCamera();
            }
        } else {
            //SDK >= 23
            openCamera();
        }
    }
     */

    /*
    private void openCamera() {
        //Image information
        Long timeStamp = System.currentTimeMillis() / 1000;
        String title = "newImage_" + timeStamp.toString();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, title);
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "From the camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        //Camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Toast.makeText(this, imageUri.toString(), Toast.LENGTH_SHORT).show();
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //cameraIntent.putExtra("send_data", imageUri);
        Log.i("cameraIntent: ", "" + imageUri);
        Log.i("imageTitle: ", "" + title);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE); //-> 'startActivityForResult(android.content.Intent, int)' is deprecated

    }
    */

    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //all permissions granted
                    openCamera();
                } else {
                    Toast.makeText(this, "Permissions denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    */

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //after taking picture from camera
        if (requestCode == IMAGE_CAPTURE_CODE && resultCode == RESULT_OK) {
            //Show taken picture in out imageViewCamera
            Uri imageUri = (data).getData();
            String test = data.getStringExtra(MediaStore.EXTRA_OUTPUT);
            Log.i("imageURI: ", "" + test);
            imageView.setImageURI(imageUri); //->Attempt to invoke virtual method 'void android.widget.ImageView.setImageURI(android.net.Uri)' on a null object reference

            //PRESTAVLJENO V FRAGMENT!
        }
    }
    */

}