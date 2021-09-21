package com.example.letsgro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * This is FullScreenActivity class to display an image in full screen.
 * Being called in intent function to display full screen image.
 *
 * Example:  Intent i = new Intent(getActivity().getApplicationContext(),FullScreenActivity.class);
 * @author boshenglee
 * @version 1.0
 */

public class FullScreenActivity extends AppCompatActivity {

    ImageView fullScreenImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        fullScreenImageView = (ImageView)findViewById(R.id.fullscreenimageview);

        getSupportActionBar().hide();
        getSupportActionBar().setTitle("Full Image");

        Intent i = getIntent();

        int position= i.getExtras().getInt("id");
        Bundle args = i.getBundleExtra("imageList");
        List<String> imageList = (ArrayList<String>) args.getSerializable("ARRAYLIST");
        fullScreenImageView.setImageURI(Uri.parse(imageList.get(position)));


    }
}