package com.example.mobproj2020new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class RatingActivity extends AppCompatActivity {

    public static int index = 0;
    private ListView lv, lv2;
    final Long currentTime = Calendar.getInstance().getTimeInMillis();

    private List<UserData> allParticipants = new ArrayList<>();

    //kuljettajien arviot
    private List<String> bookedRides;
    private List<String> startAddress;
    private List<String> endAddresses;
    private List<String> userUid;
    private List<String> userName;

    //kyytiläisten arviot
    private List<String> rParticipants;
    private List<String> partiUserName;
    private List<String> partiStartAddress;
    private List<String> partiEndAddress;

    private RatingCustomList2 aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        startAddress = new ArrayList<>();
        endAddresses = new ArrayList<>();
        userUid = new ArrayList<>();
        userName = new ArrayList<>();

        rParticipants = new ArrayList<>();
        partiUserName = new ArrayList<>();
        partiStartAddress = new ArrayList<>();
        partiEndAddress = new ArrayList<>();



        lv2 = (ListView)findViewById(R.id.participantsList);

        aa = new RatingCustomList2(this, allParticipants);
        lv2.setAdapter(aa);
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Intent i = new Intent(JokuActivity.this, JokuMuuActivity.class);
                UserData ud = allParticipants.get(position);
                //i.putExtra(ud);
                //startActivity(i);
                Log.d("TESTIIIIII", "TESTIIIIIIIIIIIIIIIIII " + allParticipants.get(position).user.getFname());
                CustomDialogRatingClass cdrc = new CustomDialogRatingClass(RatingActivity.this, ud.user.getUid(), ud.user.getFname(), ud.user);
                cdrc.show();
            }
        });

        getUserUid();
    }

    private void getUserUid() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        findUserRides(uid);
        findParticipants(uid);
        Log.d("TESTIII", "Get current UID: " + uid);
    }

    private void findUserRides(final String uid) {

        Query query = FirebaseFirestore.getInstance().collection("rides").whereArrayContains("participants", uid);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult())
                    {
                        index++;
                        if((Long)doc.get("leaveTime") < currentTime)
                        {
                            try {
                                userUid.add(doc.getString("uid"));
                                startAddress.add(doc.getString("startAddress"));
                                endAddresses.add(doc.getString("endAddress"));
                                userName.add(doc.getString("username"));
                                Log.d("TESTIII", "UID:T " + userName);

                                if(task.getResult().size() <= index){
                                    setDataToList();
                                }

                            }catch (Exception e){
                                startAddress = new ArrayList<>();
                                Log.d("TESTIII","vittu");
                            }
                        }
                    }

                }
            }
        });
    }

    private void findRidesDetails() {
    }


    private void findParticipants(String uid) {
        Query q = FirebaseFirestore.getInstance().collection("rides").whereEqualTo("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot doc : task.getResult())
                    {
                        final Route r = doc.toObject(Route.class);

                        rParticipants = (List) r.getParticipants();

                        try {
                            for(String uid : rParticipants)
                            {
                                FirebaseFirestore.getInstance().collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful())
                                        {
                                            DocumentSnapshot doc = task.getResult();
                                            User u = doc.toObject(User.class);
                                            UserData ud = new UserData(r.getUid(), r.getStartAddress(), r.getEndAddress(), u);
                                            allParticipants.add(ud);
                                            aa.notifyDataSetChanged();
                                            Log.d("TESTIIII", "fasdfasdfasdfa " + ud.user.getFname());
                                        }
                                    }
                                });
                            }
                        }catch (Exception e){

                        }

                    }
                }
            }
        });

    }



    private void setDataToList() {

        RatingCustomList listAdapter = new RatingCustomList(RatingActivity.this,  startAddress, userName, endAddresses);
        lv = (ListView)findViewById(R.id.driverList);
        lv.setAdapter(listAdapter);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CustomDialogRatingClass cdrc = new CustomDialogRatingClass(RatingActivity.this, userUid.get(position), userName.get(position) );
                cdrc.show();
            }
        });


    }
}
