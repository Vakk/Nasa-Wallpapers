package com.vakk.nasaapod.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vakk.nasaapod.R;

import java.util.List;

/**
 * Created by vakk on 3/16/16.
 */
public class MenuAdapter extends ArrayAdapter<String> {
    String[] items;
    LayoutInflater inflater;
    /**
     * Constructor
     *
     * @param context  The current context.
     */
    public MenuAdapter(Context context,String [] items) {
        super(context, R.layout.menu_item,items);
        this.items=items;
        inflater= LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView=inflater.inflate(R.layout.menu_item,parent,false);
        }
        TextView item = (TextView)convertView.findViewById(R.id.item);
        try {
            item.setText(getItem(position));
            // TODO: 3/21/16 replace with string from resources
        } catch (ArrayIndexOutOfBoundsException e){item.setText("Wrong index");};
        return convertView;
    }

    @Override
    public  String getItem(int position) throws ArrayIndexOutOfBoundsException{
        return items[position];
    }
}
