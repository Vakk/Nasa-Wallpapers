package com.vakk.nasaapod.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.vakk.nasaapod.FullscreenActivity;
import com.vakk.nasaapod.R;
import com.vakk.nasaapod.adapters.ImageAdapter;
import com.vakk.nasaapod.helpers.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import api.ResponseListener;
import api.retrofit.nasa.NasaQuery;

/**
 * Created by vakk on 3/17/16.
 */
public class ShowByRangeFragment extends Fragment {
    View root;
    List<Image>list;
    ListView listView;
    ImageAdapter adapter;
    public ShowByRangeFragment (){};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.show_image_list_fragment,null);
        Button send = (Button)root.findViewById(R.id.accept);
        listView =(ListView)root.findViewById(R.id.images);
        // receive images in current range
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = new ArrayList<Image>();
                EditText low_date = (EditText) root.findViewById(R.id.low_date);
                EditText high_date = (EditText) root.findViewById(R.id.high_date);
                String date1 = low_date.getText().toString();
                String date2 = high_date.getText().toString();
                getImageByRange(date1,date2);
            }
        });
        // discover which item is selected
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(root.getContext(),FullscreenActivity.class);
                intent.putExtra("images",(Serializable)list);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
        return root;
    }
    boolean checkDate (String date1,String date2){
        return true;
    }
    void getImageByRange(String date1,String date2){
        if (checkDate(date1,date2)){
            NasaQuery.getInstance().getImageByRange(new ResponseListener() {
                @Override
                public void done(Object obj) {
                    list.add((Image)obj);
                    adapter.notifyList();
                }

                @Override
                public void fail(Object obj) {

                }
            },date1,date2);
            adapter = new ImageAdapter(root.getContext(),list);
            listView.setAdapter(adapter);
        }
    }
}
