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
    private List<UserData> allDrivers = new ArrayList<>();

    private RatingCustomList aa1;
    private RatingCustomList2 aa2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        lv = (ListView)findViewById(R.id.driverList);
        lv2 = (ListView)findViewById(R.id.participantsList);

        aa1 = new RatingCustomList(this, allDrivers);
        aa2 = new RatingCustomList2(this, allParticipants);
        lv.setAdapter(aa1);
        lv2.setAdapter(aa2);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserData ud = allDrivers.get(position);
                CustomDialogRatingClass cdrc = new CustomDialogRatingClass(RatingActivity.this,ud.user,ud.uid,allDrivers,position,aa1);
                cdrc.getWindow().setBackgroundDrawableResource(R.drawable.background_rating);
                cdrc.show();
            }
        });
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserData ud = allParticipants.get(position);
                CustomDialogRatingClass cdrc = new CustomDialogRatingClass(RatingActivity.this,ud.user,ud.uid,allParticipants,position,aa2);
                cdrc.getWindow().setBackgroundDrawableResource(R.drawable.background_rating);
                cdrc.show();
            }
        });

        FillListViews();
    }

    private void FillListViews()
    {
        findRiders();
        findParticipants();
    }

    private void findRiders() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query q = FirebaseFirestore.getInstance().collection("rides").whereArrayContains("participants", uid);
        q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult())
                    {
                        //if((Long)doc.get("leaveTime") < currentTime)
                        if(true)
                        {
                            final Route r = doc.toObject(Route.class);
                            FirebaseFirestore.getInstance().collection("users").document(r.getUid())
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot doc = task.getResult();
                                        User u = doc.toObject(User.class);
                                        UserData ud = new UserData(r, u, r.getUid());
                                        allDrivers.add(ud);
                                        aa1.notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    private void findParticipants() {
        Query q = FirebaseFirestore.getInstance().collection("rides").whereEqualTo("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
        q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot doc : task.getResult())
                    {
                        final Route r = doc.toObject(Route.class);
                        List<String> rParticipants = (List) r.getParticipants();

                        try {
                            for(final String uid : rParticipants)
                            {
                                FirebaseFirestore.getInstance().collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful())
                                        {
                                            DocumentSnapshot doc = task.getResult();
                                            User u = doc.toObject(User.class);
                                            UserData ud = new UserData(r, u, uid);
                                            allParticipants.add(ud);
                                            aa2.notifyDataSetChanged();
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
}
