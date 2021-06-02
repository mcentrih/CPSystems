package com.example.cpsystemsapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FragmentCamera extends Fragment {
    private View view;
    private static final int PERMISSION_CODE = 9000;
    private static final int IMAGE_CAPTURE_CODE = 9001;
    private static final int IMAGE_SELECT_CODE = 9002;
    ImageView imageView;
    ImageButton buttonSend, buttonCapture, buttonSearchPic;
    Uri imageUri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        buttonSend = (ImageButton) view.findViewById(R.id.buttonSend);
        buttonCapture = (ImageButton) view.findViewById(R.id.buttonCapture);
        buttonSearchPic = (ImageButton) view.findViewById(R.id.buttonSearchPic);

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
    }

}
