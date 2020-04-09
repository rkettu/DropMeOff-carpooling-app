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

    //kuljettajien arviot
    private List<String> bookedRides;
    private List<String> startAddress;
    private List<String> endAddresses;
    private List<String> userUid;
    private List<String> userName;

    //kyytiläisten arviot
    private List<String> participants;
    private List<String> partiUserName;
    private List<String> partiStartAddress;
    private List<String> partiEndAddress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        startAddress = new ArrayList<>();
        endAddresses = new ArrayList<>();
        userUid = new ArrayList<>();
        userName = new ArrayList<>();

        participants = new ArrayList<>();
        partiUserName = new ArrayList<>();
        partiStartAddress = new ArrayList<>();
        partiEndAddress = new ArrayList<>();

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
        index = 0;
        final List<HashMap<String,String>> list = new ArrayList<>();

        Query query = FirebaseFirestore.getInstance().collection("rides").whereEqualTo("uid", uid);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Log.d("TESTIII", "PARTIT: " + task.getResult().size());
                    for(DocumentSnapshot doc : task.getResult())
                    {
                        Log.d("TESTIII", "PARTIT: " + task.getResult().size());
                        index++;
                        if((Long)doc.get("leaveTime") > currentTime)
                        {
                            try {
                                String address = doc.get("startAddress") + " " + doc.get("endAddress");
                                List<String> participants = (List) doc.get("participants");
                                for(int i = 0; i < participants.size(); i++)
                                {
                                    HashMap<String,String> hm = new HashMap<String, String>();
                                    hm.put(participants.get(i), address);
                                    list.add(hm);
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


    private void setDataToList() {

        RatingCustomList listAdapter = new RatingCustomList(RatingActivity.this,  startAddress, userName, endAddresses);
        lv = (ListView)findViewById(R.id.driverList);
        lv.setAdapter(listAdapter);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CustomDialogRatingClass cdrc = new CustomDialogRatingClass(RatingActivity.this, userUid.get(position), userName.get(position));
                cdrc.show();
            }
        });


    }
}
