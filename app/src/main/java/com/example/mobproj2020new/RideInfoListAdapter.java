package com.example.mobproj2020new;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

public class RideInfoListAdapter extends ArrayAdapter<Route> {

    Context context;

    public RideInfoListAdapter(Context context, List<Route> routeList)
    {
        super(context, 0, routeList);
        this.context = context;
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
        TextView address2 = convertView.findViewById(R.id.ListItemText4);
        String startAdd = r.getStartAddress();
        String outPutStartAdd = startAdd.substring(0, 1).toUpperCase() + startAdd.substring(1);
        String endAdd = r.getEndAddress();
        String outPutEndAdd = endAdd.substring(0, 1).toUpperCase() + endAdd.substring(1);
        address.setText(outPutStartAdd);
        address2.setText(outPutEndAdd);
        leaveTime1.setText(CalendarHelper.getDateTimeString(r.getLeaveTime()));
        leaveTime2.setText(CalendarHelper.getHHMMString(r.getLeaveTime()));

        return convertView;
    }

    //--------places string to getcoder to find lat and lon, returns city------//
    private String locate(String address){
        try {
            Geocoder geocoder = new Geocoder(context);
            List<Address> addressPoint = geocoder.getFromLocationName(address, 1);
            Address newAddress = addressPoint.get(0);

            float lat = (float) newAddress.getLatitude();
            float lon = (float) newAddress.getLongitude();

            String result = myGeoLocate(lat, lon);
            return result;
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return "FAILED";
    }

    //-------returns city for locate function-------//
    private String myGeoLocate(float first, float second){
        Geocoder geocoder = new Geocoder(context);
        String city = null;
        try{
            List<Address> addresses = geocoder.getFromLocation(first, second, 1);
            city = addresses.get(0).getLocality();
            return city;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "FAILED";
    }
}
