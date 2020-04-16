package com.example.mobproj2020new;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.firestore.CollectionReference;

import java.util.Calendar;
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
    private String struri = "DEF_URI";
    public static final int REQKEY = 1212;
    private StorageReference storageRef;

    private Context profileContext;
    private String profileUid;

    public void setUserCreationInfo(String fname, String lname, String phone)
    {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        AppUser.init();
        if(uid.equals("")) {
            return;
        }
        mUsersDocRef = FirebaseFirestore.getInstance().collection("users").document(uid);

        User user = new User(fname,lname,phone);
        mUsersDocRef.set(user); // add on success, on failure event listeners for checking for errors if needed
    }

    //Update profile data on EditProfileActivity
    public void updateUserInfo(final Context context){
        mUsersDocRef = FirebaseFirestore.getInstance().collection("users").document(AppUser.getUid());

        User user = AppUser.createStaticUser();
        Log.d("######DatabaseHandler check####", "fname " + user.getFname() + "\nlname "
                + user.getLname()  + "\nphone " + user.getPhone() + "\nemail " + user.getEmail()
                + "\nbio " + user.getBio() + "\nimgUri " + user.getImgUri() + "\nuid " + user.getUid());
        mUsersDocRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        }); // add on success, on failure event listeners for checking for errors if needed
    }

    // Saves route to database, informing of success, failure
    public void createRide(final Route route, final Context context)
    {
        DocumentReference mRoutesDocRef = mRoutesColRef.document();
        mRoutesDocRef.set(route).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Set notification alarm for ride, day before ride or hour after posting if ride is less than a day away
                    long leaveTime = route.getLeaveTime();
                    long time = leaveTime - Calendar.getInstance().getTimeInMillis() > Constant.DayInMillis
                            ? leaveTime - Constant.DayInMillis
                            : leaveTime - (Constant.HourInMillis * 3)
                            ;
                    String timeString = CalendarHelper.getFullTimeString(leaveTime);
                    SleepReceiver.setAlarm(context, time, "Created ride Reminder", (route.getStartAddress() + " - " + route.getEndAddress() + " leaving at " + timeString));
                    // Return to main activity...
                    // Show Toast text / pop up text or whatever in main instead...
                    Toast.makeText(context, "Ride Created", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(context, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
                else {
                    Toast.makeText(context, "FAILED", Toast.LENGTH_SHORT).show();
                    Log.d("FAILURETEXT", task.getException().toString());
                }
            }
        });
    }

    public void checkProfileCreated(final Context context)
    {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(uid.equals("")) {
            return;
        }
        mUsersDocRef = FirebaseFirestore.getInstance().collection("users").document(uid);
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
                            FirebaseHelper.loggedIn = true;
                            MainActivity.setImageSettings(context);
                            /*
                            Intent intent = new Intent(varContext, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            varContext.startActivity(intent);*/
                        }
                        else
                        {
                            FirebaseHelper.loggedIn = false;
                            Intent intent = new Intent(varContext, EditProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            varContext.startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    public void setProfileCreated(final boolean value)
    {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(uid.equals("")) {
            return;
        }
        mUsersDocRef = FirebaseFirestore.getInstance().collection("users").document(uid);
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
        FirebaseStorage fbs;
        fbs = FirebaseStorage.getInstance();
        storageRef = fbs.getReference();
        StorageReference ppRef = storageRef.child("profpics/"+AppUser.getUid());

        ppRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        AppUser.setImgUri();
                    }
                });
        //UploadTask upTask = ppRef.putFile(imageUri);


        // Add listeners for fail/complete/success?
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


    public void GoToProfile(final Context context, final String uid)
    {
        profileContext = context;
        profileUid = uid;

        FirebaseFirestore myFirestoreRef = FirebaseFirestore.getInstance();
        DocumentReference myDocRef = myFirestoreRef.collection("users").document(profileUid);
        final User mUser = new User();

        myDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        final User user = doc.toObject(User.class);

                        getProfilepick(user);
                       /* Intent intent = new Intent(context, ProfileActivity.class);
                        intent.putExtra("JOKUKEY", user);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);*/
                    }
                }
            }
        });
    }

    //GET users (profile) image From FirebaseStorage
    private void getProfilepick(final User user){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("profpics/" + profileUid);
        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                Log.d("######DatabaseHandler ImgUri####", String.valueOf(task.getResult()));
                struri = String.valueOf(task.getResult());

                //set Users image Uri and Uid
                user.setImgUid(struri);
                user.setUid(profileUid);
                gotoProfileActivity(user);
            }
        });
    }

    public void GetOfferedRides(final ArrayAdapter<Route> aa, final List<Route> routeData)
    {
        Query q = mRoutesColRef.whereEqualTo("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot doc : task.getResult())
                    {
                        Route r = doc.toObject(Route.class);

                        routeData.add(r);

                        aa.notifyDataSetChanged();


                    }
                }
            }
        });
    }

    public void GetBookedRides(final ArrayAdapter<Route> aa, final List<Route> routeData)
    {
        Query q = mRoutesColRef.whereArrayContains("participants", FirebaseAuth.getInstance().getCurrentUser().getUid());
        q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot doc : task.getResult())
                    {
                        Route r = doc.toObject(Route.class);

                        routeData.add(r);

                        aa.notifyDataSetChanged();


                    }
                }
            }
        });
    }

    private void gotoProfileActivity(final User user){
        Intent intent = new Intent(profileContext, ProfileActivity.class);
        intent.putExtra("JOKUKEY", user);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        profileContext.startActivity(intent);
    }

    public void BookTrip(final String rideId, final String userId, final Context context)
    {
        final Context mContext = context;

        if(rideId.equals("") || userId.equals("")) // Big failure
            return;

        final DocumentReference routeDoc = FirebaseFirestore.getInstance().collection("rides").document(rideId);
        routeDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();
                    if((long) doc.get("freeSlots") >= 1) {
                        // Removing free slot and adding user as participant
                        Route route = doc.toObject(Route.class);
                        route.removeFreeSlot();
                        try {
                            route.addToParticipants(userId);
                        } catch (Exception e) {
                            route.initParticipants();
                            route.addToParticipants(userId);
                        }
                        routeDoc.set(route);
                        final long leaveTime = route.getLeaveTime();
                        final String startAdd = route.getStartAddress();
                        final String endAdd = route.getEndAddress();
                        // Adding ride to user's booked trips...
                        mUsersDocRef = FirebaseFirestore.getInstance().collection("users").document(userId);
                        mUsersDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful())
                                {
                                    DocumentSnapshot uDoc = task.getResult();
                                    User user = uDoc.toObject(User.class);
                                    try {
                                        user.addToBookedRides(rideId);
                                    } catch(Exception e) {
                                        user.initBookedRides();
                                        user.addToBookedRides(rideId);
                                    }
                                    mUsersDocRef.set(user);

                                    // Creating timed notification for ride
                                    long time = leaveTime - Calendar.getInstance().getTimeInMillis() > Constant.DayInMillis
                                            ? leaveTime - Constant.DayInMillis
                                            : leaveTime - (Constant.HourInMillis * 3)
                                    ;
                                    String timeString = CalendarHelper.getFullTimeString(leaveTime);
                                    SleepReceiver.setAlarm(context, time, "Booked ride Reminder", (startAdd) + " - " + endAdd + " leaving at " + timeString);
                                  
                                  Intent i = new Intent(mContext, MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    mContext.startActivity(i);

                                }
                            }
                        });
                    }
                }
            }
        });
    }

    //-----------------Remove button for booked trips-----------//
    /*public void RemoveTrip(final String rideId, final String userId)
    {
        final DocumentReference routeDoc = FirebaseFirestore.getInstance().collection("rides").document(rideId);
        routeDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task <DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();
                    if((long)) doc.get("freeSlots") >= 1) {
                    Route route = doc.toObject(Route.class);
                    route.
                }
                }
            }
        }
    }*/
}
