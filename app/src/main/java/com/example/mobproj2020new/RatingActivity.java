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
import java.util.List;

public class RatingActivity extends AppCompatActivity {

    private List<String> bookedRides;
    private List<String> startAddress;
    private List<String> endAddresses;
    private List<String> userUid;
    private List<String> userName;
    private List<String> testi;
    public static int index = 0;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        startAddress = new ArrayList<>();
        endAddresses = new ArrayList<>();
        userUid = new ArrayList<>();
        userName = new ArrayList<>();
        testi = new ArrayList<>();

        getUserUid();
    }

    private void getUserUid() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        findUserRides(uid);
        findParticipants(uid);
        Log.d("TESTIII","getUserUid() user id haettu = " + uid);
    }

    private void findUserRides(String uid) {
        FirebaseFirestore myFirestoreRef = FirebaseFirestore.getInstance();
        DocumentReference myDocRef = myFirestoreRef.collection("users").document(uid);

        myDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        try {
                            bookedRides = (List) doc.get("bookedRides");
                            findRidesDetails();
                        }catch (Exception e){
                            findViewById(R.id.driverEmptyTxt).setVisibility(View.VISIBLE);
                            bookedRides = new ArrayList<>();
                        }
                    }
                }
            }
        });
    }

    private void findRidesDetails() {
        Log.d("TESTIII","RidesSize:" + bookedRides.size());
        index = 0;

        for(int i = 0; i < bookedRides.size(); i++)
        {
            Log.d("TESTIII","findRides:" + bookedRides.get(i));

            FirebaseFirestore myFirestoreRef = FirebaseFirestore.getInstance();
            DocumentReference myDocRef = myFirestoreRef.collection("rides").document(bookedRides.get(i));

            myDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();

                        if(doc.exists())
                        {
                            try {
                                userUid.add(doc.getString("uid"));
                                startAddress.add(doc.getString("startAddress"));
                                endAddresses.add(doc.getString("endAddress"));
                                Log.d("TESTIII", doc.getString("startAddress"));
                                index++;
                                if(index == bookedRides.size()){
                                    index = 0;
                                    getUserName();
                                    //setDataToList();
                                }
                            }catch (Exception e){
                                startAddress = new ArrayList<>();
                                Log.d("TESTIII","vittu");
                            }
                        }
                    }
                }
            });
        }
    }

    private void getUserName() {
        for(int i = 0; i < userUid.size(); i++)
        {
            FirebaseFirestore myFireRef = FirebaseFirestore.getInstance();
            DocumentReference myDRef = myFireRef.collection("users").document(userUid.get(i));

            myDRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot doc = task.getResult();
                        if(doc.exists()){
                            userName.add(doc.getString("fname"));
                            index++;
                            if(index == userUid.size()){
                                setDataToList();
                            }
                        }
                    }
                }
            });
        }
    }

    private void findParticipants(String uid) {

        FirebaseFirestore myFire = FirebaseFirestore.getInstance();
        Query query = FirebaseFirestore.getInstance().collection("rides").whereArrayContains("uid", uid);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc : task.getResult()){
                    if(task.isSuccessful()){
                        testi.add(doc.toString());
                        Log.d("TESTIIIII", "QUERYYYYYYYYYYYYYY: " + testi.get(0));
                    }else
                    {
                        Log.d("TESTIIIII", "QUERYYYYYYYYYYYYYYei PELAAA!");
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
