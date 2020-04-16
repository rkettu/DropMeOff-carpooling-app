package com.example.mobproj2020new;

import java.io.Serializable;

public class UserData implements Serializable {

    Route route;
    User user;
    String uid;

    UserData(Route route, User user, String uid)
    {
        this.route = route;
        this.user = user;
        this.uid = uid;
    }

}

