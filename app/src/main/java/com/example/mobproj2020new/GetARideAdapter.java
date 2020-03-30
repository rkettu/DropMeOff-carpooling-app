package com.example.mobproj2020new;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class GetARideAdapter extends BaseAdapter {

    private List<User> userList;
    private ArrayList<GetARideUtility> tripList;
    private List<GetARideUtility> list;
    LayoutInflater inflater;
    Context mContext;
    private String newName = "";
    private String imageUri = "";
    GetARideUtility utility;

    public GetARideAdapter(){

    }

    public GetARideAdapter(Context context, ArrayList<GetARideUtility> arrayList){
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
        return tripList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder{
        TextView startPoint;
        TextView endPoint;
        TextView tripUser;
        TextView freeSlots;
        TextView duration;
        TextView tripTime;
        TextView price;
        TextView date;
    }

    public String geoLocate(float lat, float lon){
        Geocoder geocoder = new Geocoder(mContext);
        String address = "";
        String kaupunki = "";
        try{
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
             address = addresses.get(0).getAddressLine(0);
             kaupunki = addresses.get(0).getLocality();

        }catch (Exception e){
            e.printStackTrace();
        }
        return kaupunki;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        DatabaseHandler dbh = new DatabaseHandler();
        dbh.getRideProfile(mContext, GetARideUtility.arrayList.get(position).getUid());

        final ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = inflater.from(parent.getContext()).inflate(R.layout.adapter_get_a_ride, parent, false);
            holder.endPoint = view.findViewById(R.id.tv1);
            holder.startPoint = view.findViewById(R.id.tv2);
            holder.duration = view.findViewById(R.id.tv3);
            holder.tripTime = view.findViewById(R.id.tv4);
            holder.freeSlots = view.findViewById(R.id.tv5);
            holder.tripUser = view.findViewById(R.id.tv6);
            holder.price = view.findViewById(R.id.tv7);
            holder.date = view.findViewById(R.id.tv8);
            view.setTag(holder);
        }   else{
            holder = (ViewHolder) view.getTag();
        }

        holder.tripUser.setText(newName);
        holder.startPoint.setText(GetARideUtility.arrayList.get(position).getStartAddress());
        holder.endPoint.setText(GetARideUtility.arrayList.get(position).getEndAddress());
        holder.duration.setText(GetARideUtility.arrayList.get(position).getDuration());
        holder.freeSlots.setText(String.valueOf(GetARideUtility.arrayList.get(position).getFreeSlots()));
        holder.price.setText(String.valueOf(GetARideUtility.arrayList.get(position).getPrice()));
        holder.date.setText(String.valueOf(GetARideUtility.arrayList.get(position).getLeaveTime()));

        notifyDataSetChanged();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: " + GetARideUtility.arrayList.get(position).getUid());
                Intent profileIntent = new Intent(mContext, GetARideProfileActivity.class);
                profileIntent.putExtra("user", newName);
                profileIntent.putExtra("start", GetARideUtility.arrayList.get(position).getStartAddress());
                profileIntent.putExtra("destination", GetARideUtility.arrayList.get(position).getEndAddress());
                profileIntent.putExtra("duration", GetARideUtility.arrayList.get(position).getDuration());
                profileIntent.putExtra("seats", String.valueOf(GetARideUtility.arrayList.get(position).getFreeSlots()));
                profileIntent.putExtra("price", String.valueOf(GetARideUtility.arrayList.get(position).getPrice()));
                profileIntent.putExtra("date", String.valueOf(GetARideUtility.arrayList.get(position).getLeaveTime()));
                mContext.startActivity(profileIntent);
            }
        });
        return view;
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
}

