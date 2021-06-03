package com.example.cpsystemsapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

public class FragmentCamera extends Fragment {
    public static final String UPLOAD_URL = "http://192.168.1.10/CPSystems/podatkovnaBaza/upload.php";
    public static final String UPLOAD_KEY = "image";
    private View view;
    private Bitmap bitmap;
    private static final int PERMISSION_CODE = 9000;
    private static final int IMAGE_CAPTURE_CODE = 9001;
    private static final int IMAGE_SELECT_CODE = 9002;
    private static final int IMAGE_SEND_CODE = 9003;

    ImageView imageView;
    ImageButton buttonSend, buttonCapture, buttonSearchPic;
    Uri imageUri, filePath;
    Double longitude, latitude;
    String imagePath = "image_path";
    String imageName = "image_name";
    boolean check = true;

    ProgressDialog progressDialog;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        buttonSend = (ImageButton) view.findViewById(R.id.buttonSend);
        buttonCapture = (ImageButton) view.findViewById(R.id.buttonCapture);
        buttonSearchPic = (ImageButton) view.findViewById(R.id.buttonSearchPic);
        //twLong = (TextView) view.findViewById(R.id.twLong);
        //twLat = (TextView) view.findViewById(R.id.twLat);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        buttonCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Image information
                Long timeStamp = System.currentTimeMillis() / 1000;
                String title = "newImage_" + timeStamp.toString();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.Images.Media.TITLE, title);
                contentValues.put(MediaStore.Images.Media.DESCRIPTION, "From the camera");
                imageUri = getActivity().getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                Log.i("cameraIntent: ", "" + imageUri);

                //Camera intent
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
            }
        });

        buttonSearchPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gallery intent

                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, IMAGE_SELECT_CODE);
            }
        });

        /*buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Send button listener
                getCurrentLocation();
                uploadImage();

                Intent sendIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                sendIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(sendIntent, "Select Image From Gallery"), IMAGE_SEND_CODE);
                Long timeStamp = System.currentTimeMillis() / 1000;
                imageName = "newImage_" + timeStamp.toString();

            }
        });*/

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Display image from CAMERA
        if (requestCode == IMAGE_CAPTURE_CODE) {
            if (resultCode == ActivityMenu.RESULT_OK) {
                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                imageView.setImageBitmap(bitmap);
            }
        }
        //display image from GALLERY
        if (requestCode == IMAGE_SELECT_CODE) {
            if (resultCode == ActivityMenu.RESULT_OK) {
                Uri galleryUri = data.getData();
                //get image from gallery into imageView
                try {
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), galleryUri);
                    imageView.setImageBitmap(bmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //send button
    }

    private void getCurrentLocation() {
        //Initialize locationManager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        //check for provider
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            //when location service enabled
            //get last location
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //Initialize location
                    Location location = task.getResult();
                    //check for location
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

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

                                latitude = location1.getLatitude();
                                longitude = location1.getLongitude();

                                Log.i("Latitude: ", String.valueOf(latitude));
                                Log.i("Longitude: ", String.valueOf(longitude));
                            }
                        };
                        //request location updates
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_SELECT_CODE);
    }

    private void uploadImage() {
        class UploadImage extends AsyncTask<Bitmap, Void, String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Uploading Image", "Please wait...", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "Image uploaded!", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String, String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);

                String result = rh.sendPostRequest(UPLOAD_URL, data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}
