package com.example.mobproj2020new;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;

// Maybe make everything static?
public class DatabaseHandler {
    private DocumentReference mUsersDocRef;
    private CollectionReference mRoutesColRef = FirebaseFirestore.getInstance().collection("rides");
    private String uid;
    private String FNAMEKEY = "fname";
    private String LNAMEKEY = "lname";
    private String PHONEKEY = "phone";
    private String mPhone = "empty";
    public static final int REQKEY = 1212;
    private StorageReference storageRef;
    private FirebaseStorage fbs;


    public void init(FirebaseUser user)
    {
        uid = user.getUid();
        fbs = FirebaseStorage.getInstance();
        storageRef = fbs.getReference();
        mUsersDocRef = FirebaseFirestore.getInstance().collection("users").document(uid);
    }

    public void setUserCreationInfo(String fname, String lname, String phone)
    {
        User user = new User(fname,lname,phone);
        mUsersDocRef.set(user); // add on success, on failure event listeners for checking for errors if needed
    }

    // Saves route to database, informing of success, failure
    public void createRide(Route route, final Context context)
    {
        DocumentReference mRoutesDocRef = mRoutesColRef.document();
        mRoutesDocRef.set(route).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Return to main activity...
                    // Show Toast text / pop up text or whatever in main instead...
                    Toast.makeText(context, "Ride Created", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "FAILED", Toast.LENGTH_SHORT).show();
                    Log.d("FAILURETEXT", task.getException().toString());
                }
            }
        });
    }

    public void getPhone()
    {
        mUsersDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        // Creating User object based on document and getting phone string
                        String phone = doc.toObject(User.class).getPhone();   // Currently only printing this value
                        Log.d("HALOOOO", ("phone number from db: " + phone));
                    }
                }
            }
        });
    }

    public void checkProfileCreated(Context context)
    {
        final Context varContext = context;
        mUsersDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        // Creating User object based on document and getting bool
                        boolean created = doc.toObject(User.class).getProfileCreated();
                        if(created)
                        {
                            Intent intent = new Intent(varContext, ChoosePickUpOrTransportationActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            varContext.startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(varContext, EditProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            varContext.startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    public void setProfileCreated(final boolean value)
    {
        mUsersDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    User user = doc.toObject(User.class);
                    user.setProfCreated(value);
                    mUsersDocRef.set(user);
                }
            }
        });
    }

    public void putImageToStorage(Uri imageUri)
    {
        StorageReference ppRef = storageRef.child("profpics/"+uid);
        UploadTask upTask = ppRef.putFile(imageUri);
        // Add listeners for fail/complete/success?
    }

    // TODO: do this in another thread ???
    public void getMatchingRoutes(final float pickupDistance, final float startLat, final float startLng, final float endLat, final float endLng)
    {
        // Checking all rides with free passenger slots
        Query query = mRoutesColRef.whereGreaterThanOrEqualTo("freeSlots", 1); // TODO also add checking for ride distance...
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        try {
                            List<HashMap<String, String>> points = (List) doc.get("points");
                            if (isRouteInRange(pickupDistance, startLat, startLng, endLat, endLng, points)) {
                                Route route = doc.toObject(Route.class);
                                Log.d("HALOOOOOOOO", "Found route matching criteria: " + doc.getId());
                            }
                            //float routeStartLat = Float.parseFloat(points.get(0).get("lat"));
                            //float routeStartLng = Float.parseFloat(points.get(0).get("lng"));
                            //float routeEndLat = Float.parseFloat(points.get(points.size()-1).get("lat"));
                            //float routeEndLng = Float.parseFloat(points.get(points.size()-1).get("lng"));
                            // Checking if distance between start points and end points is less than pickup distance
                            //if(distanceBetweenCoordinates(startLat, startLng, routeStartLat, routeStartLng) <= pickupDistance
                            //&& distanceBetweenCoordinates(endLat, endLng, routeEndLat, routeEndLng) <= pickupDistance)
                            //{
                            // Start and end are both within radius!
                            // Update listview... or something
                            //}
                        } catch(Exception e) {
                            Log.d("EXCEPTIONALERT", e.toString());
                        }
                    }
                }
                else
                {
                    Log.d("VITTUJENPERKELE", "Error getting documents: ", task.getException());
                }
            }
        });
    }


    // Move to some Math class?
    // Returns distance in km
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

    public void GoToProfile(final Context context, String uid)
    {
        FirebaseFirestore myFirestoreRef = FirebaseFirestore.getInstance();
        DocumentReference myDocRef = myFirestoreRef.collection("users").document(uid);;

        myDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                        if(doc.exists()) {
                            User user = doc.toObject(User.class);
                            Intent intent = new Intent(context, ProfileActivity.class);
                            intent.putExtra("JOKUKEY", user);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                    }
                }
            }
        });
    }
    /*
        mUsersDocRef = FirebaseFirestore.getInstance().collection("users").document(uid);
        mUsersDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        // Creating User object based on document and getting phone string
                        String phone = doc.toObject(User.class).getPhone();   // Currently only printing this value
                        Log.d("HALOOOO", ("phone number from db: " + phone));
                    }
                }
            }
        });
    }*/
}
