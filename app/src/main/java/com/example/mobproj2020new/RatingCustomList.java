package com.example.mobproj2020new;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RatingCustomList extends ArrayAdapter<UserData> {

    private final Activity context;

    public RatingCustomList(Activity context, List<UserData> udList){
        super(context, R.layout.rating_list_item, udList);
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){

        UserData ud = getItem(position);

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.rating_list_item, null, true);

        TextView txtStartAddress = (TextView)rowView.findViewById(R.id.startAddress);
        TextView txtUserName = (TextView)rowView.findViewById(R.id.userName);
        CircleImageView userImage = rowView.findViewById(R.id.circleView);
        Picasso.with(context).load(ud.user.getImgUri()).into(userImage);

        txtUserName.setText(ud.user.getFname());
        txtStartAddress.setText(ud.route.getStartCity() + " - " + ud.route.getEndCity());

        return rowView;
    }
}
