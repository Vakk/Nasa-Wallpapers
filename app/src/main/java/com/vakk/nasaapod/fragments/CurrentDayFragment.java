package com.vakk.nasaapod.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vakk.nasaapod.FullscreenActivity;
import com.vakk.nasaapod.helpers.Image;
import com.vakk.nasaapod.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import api.ResponseListener;
import api.retrofit.nasa.NasaQuery;

/**
 * Created by vakk on 3/16/16.
 */
public class CurrentDayFragment extends Fragment {
    // main view
    View root;
    public CurrentDayFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.show_current_day, container, false);
        this.root=root;
        setCurrentDayPicture();
        return root;
    }

    private void setCurrentDayPicture() {
        NasaQuery.getInstance().getImageByDay(new ResponseListener() {
            @Override
            public void done(Object obj) {
                Image image = (Image) obj;
                try {
                    setDescriptions(image);
                    showImage(image);
                } catch (NullPointerException e) {
                    Toast.makeText(root.getContext(),
                            getResources().getString(R.string.server_null_pointer),
                            Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void fail(Object obj) {
                Toast.makeText(
                        root.getContext(),
                        root.getResources().getString(R.string.current_day_server_response_error),
                        Toast.LENGTH_SHORT).show();
            }
        }, null);
    }

        void setDescriptions(Image image){
            TextView name = (TextView) root.findViewById(R.id.name);
            TextView desription = (TextView) root.findViewById(R.id.description);
            name.setText(image.getName());
            if (image.getDescription().length() > 100) {
                StringBuilder cutDescription = new StringBuilder(image.getDescription().substring(0, 100));
                cutDescription.append("...");
                desription.setText(cutDescription.toString());
            } else desription.setText(image.getDescription());
    }

    void showImage(final Image image){
        ImageView imageView = (ImageView)root.findViewById(R.id.nasa_image);
        Glide
                .with(root.getContext())
                .load(image.getUrl())
                .centerCrop()
                //.placeholder(R.drawable.loading_spinner)
                .crossFade()
                .into(imageView);
        final List<Image>list;
        list = new ArrayList<>();
        list.add(image);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(root.getContext(),FullscreenActivity.class);
                intent.putExtra("images", (Serializable) list);
                startActivity(intent);
            }
        });
    }
}
