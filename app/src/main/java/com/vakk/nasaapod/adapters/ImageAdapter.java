package com.vakk.nasaapod.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vakk.nasaapod.helpers.Image;
import com.vakk.nasaapod.R;

import java.util.List;

/**
 * Created by vakk on 3/16/16.
 */
public class ImageAdapter extends ArrayAdapter<Image> {
    List<Image>images;
    LayoutInflater inflater;
    View view;
    /**
     * Constructor
     *
     * @param context  The current context.
     */
    public ImageAdapter(Context context,List<Image>list) {
        super(context,R.layout.image_adapter,list);
        this.images=list;
        inflater=LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView=inflater.inflate(R.layout.image_adapter,parent,false);
        }
        view=convertView;
        drawImage(images.get(position));
        TextView name = (TextView)convertView.findViewById(R.id.name);
        name.setText(images.get(position).getName());
        return convertView;
    }
    void drawImage(Image image){

        ImageView imageView =  (ImageView)view.findViewById(R.id.image);
        Glide.with(getContext())
                .load(image.getUrl())
                . fitCenter()
                        //.placeholder(R.drawable.loading_spinner)
                .crossFade()
                .into(imageView);
    }
    @Override
    public int getPosition(Image item) {
        return super.getPosition(item);
    }

    public void setList(List<Image> list){
        this.images=list;
    }
    public void notifyList(){
        notifyDataSetChanged();
    }
}
