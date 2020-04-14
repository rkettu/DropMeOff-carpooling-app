package com.example.mobproj2020new;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RideInfoListAdapter extends ArrayAdapter<Route> {


    public RideInfoListAdapter(Context context, List<Route> routeList)
    {
        super(context, 0, routeList);
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent)
    {
        Route r = getItem(pos);
        View listItem = convertView;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_route_list_item, parent, false);
        }

        TextView address = (TextView) convertView.findViewById(R.id.ListItemText1);
        TextView leaveTime1 = (TextView) convertView.findViewById(R.id.ListItemText2);
        TextView leaveTime2 = (TextView) convertView.findViewById(R.id.ListItemText3);
        address.setText(r.getStartAddress() + " - " + r.getEndAddress());
        leaveTime1.setText(CalendarHelper.getDateTimeString(r.getLeaveTime()));
        leaveTime2.setText(CalendarHelper.getHHMMString(r.getLeaveTime()));

        return convertView;
    }
}
