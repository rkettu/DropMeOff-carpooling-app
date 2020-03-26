package com.example.mobproj2020new;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GetARideAdapter extends BaseAdapter {

    private ArrayList<GetARideUtility> tripList = null;
    private List<GetARideUtility> list;
    LayoutInflater inflater;
    Context mContext;

    public GetARideAdapter(Context context, List<GetARideUtility> list){
        mContext = context;
        this.list = list;
        inflater = LayoutInflater.from(mContext);
        this.tripList = new ArrayList<GetARideUtility>();
        this.tripList.addAll(list);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder{
        TextView startPoint;
        TextView endPoint;
        TextView tripEstTime;
        TextView tripDate;
        TextView tripUser;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.adapter_get_a_ride,null);
            holder.startPoint = view.findViewById(R.id.tv2);
            holder.endPoint = view.findViewById(R.id.tv1);
            holder.tripUser = view.findViewById(R.id.tv5);
            holder.tripEstTime = view.findViewById(R.id.tv3);
            holder.tripDate = view.findViewById(R.id.tv4);
            view.setTag(holder);
        }   else{
            holder = (ViewHolder) view.getTag();
        }

        holder.startPoint.setText(list.get(position).getStartPoint());
        holder.endPoint.setText(list.get(position).getEndPoint());
        holder.tripEstTime.setText(list.get(position).getTripStartTime());
        holder.tripDate.setText(list.get(position).getTripDate());
        holder.tripUser.setText(list.get(position).getTripUser());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: " + tripList.get(position).getTripUser());
                Intent profileIntent = new Intent(mContext, ProfileActivity.class);
                profileIntent.putExtra("user", tripList.get(position).getTripUser());
                mContext.startActivity(profileIntent);
            }
        });
        return view;
    }

    public void filter(String[] charText){
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
    }
}

