package com.example.cpsystemsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ActivityRoad extends AppCompatActivity implements SensorEventListener{
    public static final String UPLOAD_ROAD_URL = "http://192.168.1.11/CPSystems/podatkovnaBaza/uploadRoad.php";
    public static final String UPLOAD_KEY_IMAGE = "image";
    public static final String UPLOAD_KEY_LATITUDE = "latitude";
    public static final String UPLOAD_KEY_LONGITUDE = "longitude";
    public static final String UPLOAD_KEY_DESCRIPTION = "opis";
    public static final String UPLOAD_KEY_X = "xAxis";
    public static final String UPLOAD_KEY_Y = "yAxis";
    public static final String UPLOAD_KEY_Z = "zAxis";
    private static final int IMAGE_SELECT_CODE = 5000;
    FusedLocationProviderClient fusedLocationProviderClient;
    Uri filePath;
    private Bitmap bitmap;
    TextView twXAxis, twYAxis, twZAxis;
    EditText etDescription;
    ImageButton buttonBackRoad, buttonSendRoad, buttonImageRoad;
    String latitude, longitude, description;
    float mLastX, mLastY, mLastZ;
    private boolean initialized;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private final float NOISE = (float) 2.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road);

        TextView twXAxis = findViewById(R.id.twXAxis);
        TextView twYAxis = findViewById(R.id.twYAxis);
        TextView twZAxis = findViewById(R.id.twZAxis);
        ImageButton buttonBackRoad = findViewById(R.id.buttonBackRoad);
        ImageButton buttonSendRoad = findViewById(R.id.buttonSendRoad);
        ImageButton buttonImageRoad = findViewById(R.id.buttonImageRoad);
        EditText etDescription = findViewById(R.id.etDescription);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        initialized = false;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener((SensorEventListener) this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);


        buttonBackRoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonSendRoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description = etDescription.getText().toString();
                uploadRoadData();
            }
        });

        buttonImageRoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
                showFileChooser();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //display image from GALLERY
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_SELECT_CODE && resultCode == ActivityMenu.RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), filePath);
                //imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //send button
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent event) {
        TextView twXAxis = findViewById(R.id.twXAxis);
        TextView twYAxis = findViewById(R.id.twYAxis);
        TextView twZAxis = findViewById(R.id.twZAxis);

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (!initialized) {
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            twXAxis.setText("0.0");
            twYAxis.setText("0.0");
            twZAxis.setText("0.0");
            initialized = true;
        } else {
            float deltaX = Math.abs(mLastX - x);
            float deltaY = Math.abs(mLastY - y);
            float deltaZ = Math.abs(mLastZ - z);
            if (deltaX < NOISE) deltaX = (float) 0.0;
            if (deltaY < NOISE) deltaY = (float) 0.0;
            if (deltaZ < NOISE) deltaZ = (float) 0.0;
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            twXAxis.setText(Float.toString(deltaX));
            twYAxis.setText(Float.toString(deltaY));
            twZAxis.setText(Float.toString(deltaZ));

            if(deltaY > 5.0) {
                Toast.makeText(this, "BUMP", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //not used
    }

    private void getCurrentLocation() {
        //Initialize locationManager
        LocationManager locationManager = (LocationManager) ActivityRoad.this.getSystemService(Context.LOCATION_SERVICE);

        //check for provider
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //when location service enabled
            //get last location
            if (ActivityCompat.checkSelfPermission(ActivityRoad.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(ActivityRoad.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //Initialize location
                    Location location = task.getResult();
                    //check for location
                    if (location != null) {
                        latitude = String.valueOf(location.getLatitude());
                        longitude = String.valueOf(location.getLongitude());

                        Log.i("Latitude: ", String.valueOf(latitude));
                        Log.i("Longitude: ", String.valueOf(longitude));
                    } else {
                        //if location NULL
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        //location callback
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                //Initialize location
                                Location location1 = locationResult.getLastLocation();

                                latitude = String.valueOf(location1.getLatitude());
                                longitude = String.valueOf(location1.getLongitude());

                                Log.i("Latitude: ", String.valueOf(latitude));
                                Log.i("Longitude: ", String.valueOf(longitude));
                            }
                        };
                        //request location updates
                        if (ActivityCompat.checkSelfPermission(ActivityRoad.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(ActivityRoad.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });
        } else {
            //location service not enabled
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void uploadRoadData() {
        class UploadRoadData extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            RequestHandler requestHandler = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ActivityRoad.this, "Uploading event", "Please wait...", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Event uploaded!", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {


                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String, String> data = new HashMap<>();
                data.put(UPLOAD_KEY_IMAGE, uploadImage);
                data.put(UPLOAD_KEY_LATITUDE, latitude);
                data.put(UPLOAD_KEY_LONGITUDE, longitude);
                data.put(UPLOAD_KEY_DESCRIPTION, description);

                /*
                Log.i("BCKG-lat: ", latitude);
                Log.i("BCKG-lng: ", longitude);
                 */

                String result = requestHandler.sendPostRequest(UPLOAD_ROAD_URL, data);

                return result;
            }
        }

        UploadRoadData ui = new UploadRoadData();
        ui.execute(bitmap);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_SELECT_CODE);
    }

}