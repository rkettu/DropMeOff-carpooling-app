package com.example.mobproj2020new;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.INotificationSideChannel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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


public class GetARideAdapter extends BaseAdapter {

    private ArrayList<GetARideUtility> tripList = new ArrayList<>();
    private ArrayList<String> myList = new ArrayList<>();
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
        this.tripList = arrayList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return tripList.size();
    }

    @Override
    public Object getItem(int position) {
        return tripList.get(position).getStartAddress();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        TextView startPoint;
        TextView endPoint;
        TextView tripUser;
        TextView hidden;
        TextView price;
        TextView date;
    }

    //--------places string to getcoder to find lat and lon, returns city------//
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

    //-------returns city for locate function-------//
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
        //-------------Set view holders-----------//

        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.from(parent.getContext()).inflate(R.layout.adapter_get_a_ride, parent, false);
            holder.endPoint = view.findViewById(R.id.tv1);
            holder.startPoint = view.findViewById(R.id.tv2);
            holder.hidden = view.findViewById(R.id.tv3);
            holder.tripUser = view.findViewById(R.id.tv6);
            holder.price = view.findViewById(R.id.tv7);
            holder.date = view.findViewById(R.id.tv8);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        //--------------------Setting text to holders-------------------//

        try{
            //--------set price for given trip-------//
            GetRoute getRoute = new GetRoute(new ReporterInterface() {
                @Override
                public void dataParsed(String output) {
                    float finalPrice = tripList.get(position).getPrice() * Float.parseFloat(output);
                    holder.price.setText(String.format("%.2f", finalPrice ) + "€");
                }
            });
            getRoute.execute(getUserStartPoint(), getUserEndPoint());

            //----------Set ride providers first name----------//
            GetUserName getUserName = new GetUserName(new newReporterInterface() {
                @Override
                public void getName(String result) {
                    holder.tripUser.setText(result);
                    myList.add(result);
                    if(myList.size() == tripList.size()){
                        GetRideActivity.shutPd();
                    }
                }
            });

            getUserName.execute(tripList.get(position).getUid());

            //-------hidden param for ride providers picUri--------//
            GetUserPicture getUserPicture = new GetUserPicture(new newestReporterInterface() {
                @Override
                public void getUserPic(String result) {
                    holder.hidden.setText(result);
                }
            });
            getUserPicture.execute(tripList.get(position).getUid());

            //-------------Set startAddress and destination----------//
            holder.startPoint.setText(tripList.get(position).getStartCity());
            holder.endPoint.setText(tripList.get(position).getEndCity());

            //----------------Set time----------//
            long millis = tripList.get(position).getLeaveTime();
            String timeString = CalendarHelper.getFullTimeString(millis);
            holder.date.setText(timeString);

        }catch (Exception e){
            e.printStackTrace();
            Log.d("getView", "getView: " + e.toString());
        }

        //--onclick listener and passing data to next activity--//
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(mContext, GetARideProfileActivity.class);
                profileIntent.putExtra("user", holder.tripUser.getText().toString());
                profileIntent.putExtra("userPic", holder.hidden.getText().toString());
                profileIntent.putExtra("uid", tripList.get(position).getUid());
                profileIntent.putExtra("start", tripList.get(position).getStartAddress());
                profileIntent.putExtra("destination", tripList.get(position).getEndAddress());
                profileIntent.putExtra("duration", tripList.get(position).getDuration());
                profileIntent.putExtra("seats", String.valueOf(tripList.get(position).getFreeSlots()));
                profileIntent.putExtra("price", holder.price.getText().toString());
                profileIntent.putExtra("date", String.valueOf(tripList.get(position).getLeaveTime()));
                profileIntent.putExtra("rideId", tripList.get(position).getRideId());
                profileIntent.putStringArrayListExtra("waypoints", (ArrayList<String>) tripList.get(position).getWaypointAddresses());
                mContext.startActivity(profileIntent);
            }
        });

        return view;
    }


    //------------interfaces and ASyncTask for route, username and picture--------------//

    public interface ReporterInterface {
        void dataParsed(String output);
    }

    public interface newReporterInterface {
        void getName(String result);
    }

    public interface newestReporterInterface {
        void getUserPic(String result);
    }

    private class GetUserPicture extends AsyncTask<String, Integer, String>{

        newestReporterInterface newestReporterInterface;

        public GetUserPicture(newestReporterInterface newestReporterInterface){
            this.newestReporterInterface = newestReporterInterface;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }

        @Override
        protected String doInBackground(String... strings) {
            final String userPic = strings[0];
            if(userPic != null){
                StorageReference myStorageRef = FirebaseStorage.getInstance()
                        .getReference("profpics/" + userPic);
                myStorageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        try{
                            if(task.getResult() != null){
                                String userPicUri = String.valueOf(task.getResult());
                                newestReporterInterface.getUserPic(userPicUri);
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
            return null;
        }
    }

    private class GetUserName extends AsyncTask<String, Integer, String> {

        newReporterInterface newReporterInterface;

        public GetUserName(newReporterInterface newReporterInterface) {
            this.newReporterInterface = newReporterInterface;
        }

        @Override
        protected String doInBackground(String... strings) {
            final String user = strings[0];
            if (user != null) {
                FirebaseFirestore myFireStoreRef = FirebaseFirestore.getInstance();
                DocumentReference myDocRef = myFireStoreRef.collection("users").document(user);
                myDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            if (doc.exists()) {
                                String userName = (String) doc.get("fname");
                                newReporterInterface.getName(userName);
                            }
                        }
                    }
                });
            }
            return null;
        }
    }

    private class GetRoute extends AsyncTask<String, Integer, String> {

        ReporterInterface reporterInterface;

        public GetRoute(ReporterInterface callbackInterface) {
            this.reporterInterface = callbackInterface;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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

