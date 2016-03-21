package com.vakk.nasaapod.fragments;

/**
 * Created by vakk on 3/17/16.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vakk.nasaapod.helpers.Image;
import com.vakk.nasaapod.R;

/**
 * Full screen image viewer
 */
public class PageFragment extends Fragment {

    public static final String IMAGE = "image";
    // root view, base view
    View root;
    // current image
    Image image;
    // show name and description or not
    boolean showMore = false;
    // image description
    TextView description;
    // image name
    TextView name;
    public static PageFragment newInstance(Image image) {
        PageFragment pageFragment = new PageFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(IMAGE, image);
        pageFragment.setArguments(arguments);
        return pageFragment;
    }
    // initialize activity, ge and set image object
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        image = (Image)getArguments().getSerializable(IMAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fullscreen, null);
        name = (TextView)root.findViewById(R.id.name);
        description =(TextView)root.findViewById(R.id.description);
        try {
            showImage();
            setTexts();
        } catch (NullPointerException e){
            Toast.makeText(root.getContext(),"some error with object",Toast.LENGTH_SHORT).show();}
        return root;
    }

    /**
     * Show image into  ImageView
     */
    void showImage(){
        ImageView imageView = (ImageView)root.findViewById(R.id.nasa_image);
        // show from website with library
        Glide.with(root.getContext())
                .load(image.getUrl())
                . fitCenter()
                        //.placeholder(R.drawable.loading_spinner)
                .crossFade()
                .into(imageView);
        // show or hide picture description and name
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showMore) {
                    description.setVisibility(View.INVISIBLE);
                    name.setVisibility(View.INVISIBLE);
                    showMore = false;
                } else {
                    description.setVisibility(View.VISIBLE);
                    name.setVisibility(View.VISIBLE);
                    showMore = true;
                }
            }
        });
    }

    /**
     * set text description and name
     */
    void setTexts(){
        // set text invisible for comfort
        name.setVisibility(View.INVISIBLE);
        description.setVisibility(View.INVISIBLE);
        name.setText(image.getName());
        description.setText(image.getDescription());
        if (image.getDescription().length() > 100) {
            // if description contain more than 100 symbols - show first 100 symbols
            StringBuilder cutDescription = new StringBuilder(image.getDescription().substring(0, 100));
            cutDescription.append("...");
            description.setText(cutDescription.toString());
        }
        else description.setText(image.getDescription());}
}