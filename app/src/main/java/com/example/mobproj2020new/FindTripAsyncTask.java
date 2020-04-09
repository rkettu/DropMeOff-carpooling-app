package com.example.mobproj2020new;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;

interface ReporterInterface{
    void getTripData(String uid, String startAddress, String endAddress, String duration, String rideId,
                     String price, String leaveTime, long freeSlots, List<String> wayPoints, List<String> participants);
}

public class FindTripAsyncTask extends AsyncTask<Float, Integer, String> {

    private CollectionReference mRoutesColRef = FirebaseFirestore.getInstance().collection("rides");
    private ReporterInterface reporterInterface;
    private final static String TAG = "FindTripASyncTask";
    private String uid, startAddress, endAddress, duration, price, leaveTime;
    private long freeSlots;
    private String rideId;
    private List<String> wayPoints;
    private List<HashMap<String, String>> points;
    private List<String> participants;
    private String userName;
    private String picUri = "DEF_URI";
    private Context context;
    private ProgressDialog pd;

    public FindTripAsyncTask(ReporterInterface callbackInterface, Context context){
        this.context = context;
        this.reporterInterface = callbackInterface;
    }

    @Override
    protected String doInBackground(Float... floats) {
        float startlat = floats[0];
        float startlon = floats[1];
        float stoplat = floats[2];
        float stoplon = floats[3];
        float t1 = floats[4];
        float t2 = floats[5];

        try {
            return GetMatchingRoutes(t1, t2, startlat, startlon, stoplat, stoplon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String GetMatchingRoutes(float time1, float time2, final float startLat, final float startLng, final float endLat, final float endLng) {
        Query query = mRoutesColRef.whereGreaterThanOrEqualTo("leaveTime", time1).whereLessThanOrEqualTo("leaveTime", time2);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        if((long) doc.get("freeSlots") >= 1){
                            try{
                                long pickupDistance = 10;
                                points = (List) doc.get("points");
                                if(isRouteInRange(pickupDistance, startLat, startLng, endLat, endLng, points)) {

                                    Log.d(TAG, "onComplete: doc.getId()" + doc.getId());
                                    uid = (String) doc.get("uid");
                                    rideId = doc.getId();
                                    startAddress = (String) doc.get("startAddress");
                                    endAddress = (String) doc.get("endAddress");
                                    duration = (String) doc.get("duration");
                                    freeSlots = (long) doc.get("freeSlots");
                                    leaveTime = doc.get("leaveTime").toString();
                                    participants = (List) doc.get("participants");
                                    price = doc.get("price").toString();
                                    wayPoints = (List) doc.get("waypointAddresses");

                                    reporterInterface.getTripData(uid, startAddress, endAddress, duration, rideId, price,
                                            leaveTime, freeSlots, participants, wayPoints);
                                }
                                else{
                                    Log.d(TAG, "onComplete: Failed isRouteInRange");
                                }
                            }
                            catch (Exception e){
                                Log.d(TAG, "onComplete: catch" + e.toString());
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        return "hihiihi";
    }

    private double distanceBetweenCoordinates(double lat1, double lng1, double lat2, double lng2)
    {
        // Haversine Algorithm
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lng2-lng1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return c * 6371;
    }

    private boolean isRouteInRange(float pickupDist, double lat1, double lng1, double lat2, double lng2, List<HashMap<String,String>> points)
    {
        Log.d("HEREWEARE", "again");
        double minDist1 = 10000000;
        double minDist2 = 10000000;
        int index1 = -1;
        int index2 = -1;
        for(int i = 0; i < points.size(); i++)
        {
            // Comparing user start coordinates one at a time with route coordinates
            double routePointLat = Double.parseDouble(points.get(i).get("lat"));
            double routePointLng = Double.parseDouble(points.get(i).get("lng"));
            double dist1 = distanceBetweenCoordinates(lat1,lng1,routePointLat,routePointLng);
            double dist2 = distanceBetweenCoordinates(lat2,lng2,routePointLat,routePointLng);
            if(dist1<minDist1)
            {
                // Minimum distance between start coordinate and some route coordinate
                minDist1 = dist1;
                index1 = i;
            }
            if(dist2<minDist2)
            {
                // Between end coord and some route coordinate
                minDist2 = dist2;
                index2 = i;
            }
        }
        Log.d("HEIHEIHEI", (minDist1 + " " + minDist2 + " " + pickupDist + " " + index1 + " " +index2));
        // If start coordinate is matched before end coordinate route is going the right way...
        if(index1 < index2)
        {
            // If start and end coordinates are within pickup distance from the route points
            if(minDist1 <= pickupDist && minDist2 <= pickupDist)
            {
                return true;
            }
        }
        return false;
    }

    private void setUserName(final String uid) {
        Log.d(TAG, "setUserName: täsänäi" + uid);
        FirebaseFirestore myFirebaseStoreRef = FirebaseFirestore.getInstance();
        DocumentReference myDocRef = myFirebaseStoreRef.collection("users").document(uid);
        myDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc = task.getResult();
                if (doc.exists()) {
                    userName = (String) doc.get("fname");
                }
            }
        });
    }

    private void setProfilePicture(final String uid){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("profpics/" + uid);
        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                try{
                    if(task.getResult() != null){
                        Log.d(TAG, "onComplete: setProfPic: " + uid);
                        picUri = String.valueOf(task.getResult());
                    }
                }
                catch (Exception e){
                    Log.d(TAG, "Failed to get profile picture");
                    e.printStackTrace();
                }
            }
        });
    }
}