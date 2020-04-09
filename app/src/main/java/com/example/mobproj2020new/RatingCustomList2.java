package com.example.mobproj2020new;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

public class RatingCustomList2 extends ArrayAdapter<String> {

    private final Activity context;
    private final List<String> startAddress;
    private final List<String> endAddress;
    private final List<String> userNames;

    public RatingCustomList2(Activity context, List<String> startAddress, List<String> userNames, List<String> endAddress){
        super(context, R.layout.rating_list_item, startAddress);
        this.context = context;
        this.startAddress = startAddress;
        this.userNames = userNames;
        this.endAddress = endAddress;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.rating_list_item, null, true);

        TextView txtStartAddress = (TextView)rowView.findViewById(R.id.startAddress);
        TextView txtUserName = (TextView)rowView.findViewById(R.id.userName);

        txtUserName.setText(userNames.get(position));
        txtStartAddress.setText(startAddress.get(position) + " - " + endAddress.get(position));


        return rowView;
    }
}
