package com.example.mobproj2020new;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RatingCustomList2 extends ArrayAdapter<UserData> {

    private Context context;

    public RatingCustomList2(Context context, List<UserData> userDataList){
        super(context, 0, userDataList);
        //super(context, R.layout.rating_list_item, startAddress);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
       // LayoutInflater inflater = context.getLayoutInflater();
       // View rowView = inflater.inflate(R.layout.rating_list_item, null, true);

       // TextView txtStartAddress = (TextView)rowView.findViewById(R.id.startAddress);
       // TextView txtUserName = (TextView)rowView.findViewById(R.id.userName);

        //txtUserName.setText(userNames.get(position));
        //txtStartAddress.setText(startAddress.get(position) + " - " + endAddress.get(position));

        UserData ud = getItem(position);
        View listItem = convertView;
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.rating_list_item, parent, false);
        }
        TextView txtStartAddress = (TextView)convertView.findViewById(R.id.startAddress);
        TextView txtUserName = (TextView)convertView.findViewById(R.id.userName);
        CircleImageView userImage = convertView.findViewById(R.id.circleView);
        Picasso.with(context).load(ud.user.getImgUri()).into(userImage);

        txtUserName.setText(ud.user.getFname());
        txtStartAddress.setText(ud.route.getStartCity()+ " - " + ud.route.getEndCity());

        // get TextViews etc of list item and setText to them with values from UserData object

        return convertView;

        //return rowView;
    }
}
