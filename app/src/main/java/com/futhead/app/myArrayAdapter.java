package com.futhead.app;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class myArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private static ArrayList<String> numstats;
    private static ArrayList<String> namestats;

    public myArrayAdapter(Context context, ArrayList<String> namestats, ArrayList<String> numstats) {
        super(context, R.layout.simple_list_item_1, numstats);
        this.context = context;
        this.numstats = numstats;
        this.namestats = namestats;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.simple_list_item_1, parent, false);
        TextView nameView = (TextView) rowView.findViewById(R.id.stat_name);
        TextView numView = (TextView) rowView.findViewById(R.id.stat_number);

        if(Integer.parseInt(numstats.get(position)) >= 90){
            numView.setTextColor(Color.GREEN);
        }else if(Integer.parseInt(numstats.get(position))>=80&&Integer.parseInt(numstats.get(position))<90){
            //numView.setTextColor(Color.);
        }else if(Integer.parseInt(numstats.get(position))>=70&&Integer.parseInt(numstats.get(position))<80){
            numView.setTextColor(Color.YELLOW);
        }else if(Integer.parseInt(numstats.get(position))>=60&&Integer.parseInt(numstats.get(position))<70){
            //numView.setTextColor(Color.);
        }
        else if(Integer.parseInt(numstats.get(position))<60){
            numView.setTextColor(Color.RED);
        }

        nameView.setText(namestats.get(position));
        numView.setText(numstats.get(position));

        return rowView;
    }
}