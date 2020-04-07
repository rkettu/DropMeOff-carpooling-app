package com.example.mobproj2020new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class RatingActivity extends AppCompatActivity {

    private List<String> bookedRides;
    private List<String> startAddress;
    private List<String> endAddresses;
    public static int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        startAddress = new ArrayList<>();
        endAddresses = new ArrayList<>();

        getUserUid();
    }

    private void getUserUid() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        findUserRides(uid);
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
                                startAddress.add(doc.getString("startAddress"));
                                endAddresses.add(doc.getString("endAddress"));
                                Log.d("TESTIII", doc.getString("startAddress"));
                                index++;
                                if(index == bookedRides.size()){
                                    setDataToList();
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

    private void setDataToList() {
        Log.d("TESTIII44", "setDatassa!");
        if(startAddress != null){
            for(int i = 0; i < startAddress.size(); i++)
            {
                Log.d("TESTIII44", startAddress.get(i));
            }
        }
        else {
            Log.d("TESTIII44", "empty ");
        }

    }
}
