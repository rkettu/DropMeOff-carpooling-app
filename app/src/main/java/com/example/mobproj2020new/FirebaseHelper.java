package com.example.mobproj2020new;

import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseHelper {
    private static FirebaseHelper instance = null;

    public static boolean loggedIn = false;

    public static FirebaseHelper getInstance() {
        if (instance == null)
            instance = new FirebaseHelper();

        return instance;
    }

    public static void GoToLogin(Context context)
    {
        Intent intent = new Intent(context, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
