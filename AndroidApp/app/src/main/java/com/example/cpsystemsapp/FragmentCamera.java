package com.example.cpsystemsapp;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.fragment.app.Fragment;

public class FragmentCamera extends Fragment {
    private View view;
    private static final int IMAGE_CAPTURE_CODE = 9001;
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
        return view;

    }

    public void onClickSend (View view) {
        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
    }

}
