package com.example.mobproj2020new;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import static java.security.AccessController.getContext;

public class GetARideAdapter extends ArrayAdapter<GetARideUtility> {

    ArrayList<GetARideUtility> tripList = new ArrayList<>();

    public GetARideAdapter(Context context, int textViewResourceId, ArrayList<GetARideUtility> objects){
        super(context, textViewResourceId, objects);
        tripList = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View listItem = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listItem = inflater.inflate(R.layout.adapter_get_a_ride, null);

        TextView endpoint = listItem.findViewById(R.id.tv1);
        endpoint.setText(tripList.get(position).getEndPoint());

        TextView startpoint = listItem.findViewById(R.id.tv2);
        startpoint.setText(tripList.get(position).getStartPoint());

        TextView triptime = listItem.findViewById(R.id.tv3);
        triptime.setText(tripList.get(position).getTripTime());

        TextView triplength = listItem.findViewById(R.id.tv4);
        triplength.setText(tripList.get(position).getTripLength());

        TextView user = listItem.findViewById(R.id.tv5);
        user.setText(tripList.get(position).getUser());

        return listItem;
    }
}
