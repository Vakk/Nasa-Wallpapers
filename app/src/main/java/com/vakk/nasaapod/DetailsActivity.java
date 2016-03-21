package com.vakk.nasaapod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.vakk.nasaapod.helpers.Image;

/**
 * Created by vakk on 3/20/16.
 */
public class DetailsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        Intent intent = getIntent();
        Image image = (Image)intent.getSerializableExtra("image");
        TextView name = (TextView)findViewById(R.id.name);
        TextView description = (TextView)findViewById(R.id.description);
        TextView url = (TextView)findViewById(R.id.url);
        TextView photographer = (TextView)findViewById(R.id.photographer);
        TextView date = (TextView)findViewById(R.id.date);
        name.setText(image.getName());
        description.setText(image.getDescription());
        url.setText(image.getUrl());
        date.setText(image.getDate());
        photographer.setText(image.getPhotographer());
    }
}
