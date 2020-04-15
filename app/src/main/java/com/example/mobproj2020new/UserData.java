package com.example.mobproj2020new;

import java.io.Serializable;

public class UserData implements Serializable {

    String routeId;
    String routeStartAddress;
    String routeEndAddress;
    User user;

    UserData(String routeId, String routeStartAddress, String routeEndAddress, User user)
    {
        this.routeId = routeId;
        this.routeEndAddress = routeEndAddress;
        this.routeStartAddress = routeStartAddress;
        this.user = user;
    }

}

