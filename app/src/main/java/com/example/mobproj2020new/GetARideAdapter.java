package com.example.mobproj2020new;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class GetARideAdapter extends BaseAdapter implements TaskLoadedCallback {

    private ArrayList<User> userList;
    private ArrayList<GetARideUtility> tripList;
    private String userStartPoint;
    private String userEndPoint;

    LayoutInflater inflater;
    Context mContext;

    public GetARideAdapter() {

    }

    public GetARideAdapter(String userStartPoint, String userEndPoint){
        this.userStartPoint = userStartPoint;
        this.userEndPoint = userEndPoint;
    }

    public String getUserStartPoint() {
        return userStartPoint;
    }

    public void setUserStartPoint(String userStartPoint) {
        this.userStartPoint = userStartPoint;
    }

    public String getUserEndPoint() {
        return userEndPoint;
    }

    public void setUserEndPoint(String userEndPoint) {
        this.userEndPoint = userEndPoint;
    }

    public GetARideAdapter(Context context, ArrayList<GetARideUtility> arrayList) {
        //inflater = LayoutInflater.from(mContext);
        this.tripList = new ArrayList<>();
        mContext = context;
    }

    @Override
    public int getCount() {
        return GetARideUtility.arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return GetARideUtility.arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onTaskDone(Object... values) {

    }

    public static class ViewHolder {
        TextView startPoint;
        TextView endPoint;
        TextView tripUser;
        TextView freeSlots;
        TextView duration;
        TextView tripTime;
        TextView price;
        TextView date;
    }

    private String locate(String address){
        try {
            Geocoder geocoder = new Geocoder(mContext);
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

    private String myGeoLocate(float first, float second){
        Geocoder geocoder = new Geocoder(mContext);
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

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        //--Set view holders--//
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.from(parent.getContext()).inflate(R.layout.adapter_get_a_ride, parent, false);
            holder.endPoint = view.findViewById(R.id.tv1);
            holder.startPoint = view.findViewById(R.id.tv2);
            //holder.duration = view.findViewById(R.id.tv3);
            //holder.freeSlots = view.findViewById(R.id.tv5);
            holder.tripUser = view.findViewById(R.id.tv6);
            holder.price = view.findViewById(R.id.tv7);
            holder.date = view.findViewById(R.id.tv8);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        //--Setting text to holders--//

        GetRoute getRoute = new GetRoute(new ReporterInterface() {
            @Override
            public void dataParsed(String output) {
                Log.d("TAG", "dataParsed: " + GetARideUtility.arrayList.get(position).getPrice());
                Log.d("TAG", "dataParsed: " + Float.parseFloat(output));
                float finalPrice = GetARideUtility.arrayList.get(position).getPrice() * Float.parseFloat(output);
                holder.price.setText(String.format("%.2f", finalPrice ) + "€");
            }
        });
        getRoute.execute(getUserStartPoint(), getUserEndPoint());

        holder.tripUser.setText(User.arrayList.get(position).getFname());
        holder.startPoint.setText(locate(GetARideUtility.arrayList.get(position).getStartAddress()));
        holder.endPoint.setText(locate(GetARideUtility.arrayList.get(position).getEndAddress()));
        //holder.duration.setText(GetARideUtility.arrayList.get(position).getDuration());
        //holder.freeSlots.setText(String.valueOf(GetARideUtility.arrayList.get(position).getFreeSlots()));

        //------------ Time -----------------//
        long millis = GetARideUtility.arrayList.get(position).getLeaveTime();
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(millis);
        String timeString = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR) + "\n" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        holder.date.setText(timeString);

        //--onclick listener and passing data to next activity--//
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: " + GetARideUtility.arrayList.get(position).getUid());
                Intent profileIntent = new Intent(mContext, GetARideProfileActivity.class);
                profileIntent.putExtra("user", User.arrayList.get(position).getFname());
                profileIntent.putExtra("userPic", User.arrayList.get(position).getImgUri());
                profileIntent.putExtra("uid", GetARideUtility.arrayList.get(position).getUid());
                profileIntent.putExtra("start", GetARideUtility.arrayList.get(position).getStartAddress());
                profileIntent.putExtra("destination", GetARideUtility.arrayList.get(position).getEndAddress());
                profileIntent.putExtra("duration", GetARideUtility.arrayList.get(position).getDuration());
                profileIntent.putExtra("seats", String.valueOf(GetARideUtility.arrayList.get(position).getFreeSlots()));
                profileIntent.putExtra("price", holder.price.getText().toString());
                profileIntent.putExtra("date", String.valueOf(GetARideUtility.arrayList.get(position).getLeaveTime()));
                profileIntent.putExtra("rideId", GetARideUtility.arrayList.get(position).getRideId());
                profileIntent.putStringArrayListExtra("waypoints", (ArrayList<String>) GetARideUtility.arrayList.get(position).getWaypointAddresses());
                mContext.startActivity(profileIntent);
            }
        });
        return view;
    }

    public interface ReporterInterface {
        void dataParsed(String output);
    }

    private class GetRoute extends AsyncTask<String, Integer, String> {

        ReporterInterface reporterInterface;

        public GetRoute(ReporterInterface callbackInterface) {
            this.reporterInterface = callbackInterface;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (reporterInterface != null) {
                reporterInterface.dataParsed(result);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String startAddress = strings[0];
            String endAddress = strings[1];

            if (geoLocate(startAddress, endAddress) != null) {
                String routeUrl = geoLocate(startAddress, endAddress);
                if(loadFromWeb(routeUrl) != null){
                    String jsonData = loadFromWeb(routeUrl);
                    if(dataParser(jsonData) != null){
                        return dataParser(jsonData);
                    }
                    else{
                        Log.d("TAG", "dataParser failed");
                    }
                }
                else{
                    Log.d("TAG", "loadFromWeb failed");
                }
            }
            Log.d("TAG", "geoLocate failed");
            return "FAILED";
        }

        private String geoLocate(String startPoint, String destination) {
            Geocoder geocoder = new Geocoder(mContext);
            String result;
            try {
                List<Address> listStartPoint = geocoder.getFromLocationName(startPoint, 1);
                Address addStart = listStartPoint.get(0);
                List<Address> listEndPoint = geocoder.getFromLocationName(destination, 1);
                Address addDestination = listEndPoint.get(0);

                double startLat, startLon, endLat, endLon;
                startLat = addStart.getLatitude();
                startLon = addStart.getLongitude();
                endLat = addDestination.getLatitude();
                endLon = addDestination.getLongitude();

                result = getRoute(startLat, startLon, endLat, endLon);
                return result;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "FAILED";
        }

        private String loadFromWeb(String urlstr) {
            try {
                URL url = new URL(urlstr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                String htmlText = PriceFetchUtility.fromStream(inputStream);
                return htmlText;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        private String getRoute(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
            String origin = "origin=" + startLatitude + "," + startLongitude;
            String dest = "destination=" + endLatitude + "," + endLongitude;
            String mode = "mode=driving";
            String params = origin + "&" + dest + "&" + mode;

            String url = "https://maps.googleapis.com/maps/api/directions/json?" + params + "&key=" + mContext.getString(R.string.api_maps_key);
            return url;
        }

        private String dataParser(String data) {
            JSONObject jDistance;
            JSONArray jRoutes;
            JSONArray jLegs;
            double totalDistance = 0;
            try {
                JSONObject jsonObject = new JSONObject(data);
                jRoutes = jsonObject.getJSONArray("routes");
                for (int i = 0; i < jRoutes.length(); i++) {
                    jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                    for (int j = 0; j < jLegs.length(); j++) {
                        jDistance = ((JSONObject) jLegs.get(j)).getJSONObject("distance");
                        totalDistance = totalDistance + Double.parseDouble(jDistance.getString("value"));
                        Log.d("TAG", "dataParser: " + jDistance);
                        double finalDistance = totalDistance / 1000.0;
                        return Constant.DISTANCE = String.valueOf(finalDistance);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "failed to parse";
        }
    }
}


/*    public void filter(String[] charText){
        String charText1 = charText[0].toLowerCase(Locale.getDefault());
        String charText2 = charText[1].toLowerCase(Locale.getDefault());
        list.clear();
        if(charText1.length() == 0 && charText2.length() == 0){
            list.addAll(tripList);
        } else{
            for(GetARideUtility utility : tripList){
                if(utility.getStartPoint().toLowerCase(Locale.getDefault()).contains(charText1) && utility.getEndPoint().toLowerCase(Locale.getDefault()).contains(charText2)){
                    list.add(utility);
                }
            }
        }
        notifyDataSetChanged();
    }*/

