package com.example.mobproj2020new;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class LoggedInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        final Button signOutBtn = (Button) findViewById(R.id.signOutBtn);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                ReturnToLogin();
            }
        });

        DatabaseHandler db = new DatabaseHandler();
        db.init(mUser);

        TextView userInfo = (TextView)findViewById(R.id.userInfoTextView);
        userInfo.setText(mUser.getDisplayName()+"\n"+mUser.getEmail()+"\n");
    }

    private void ReturnToLogin()
    {
        // Currently returns only to previous...
        this.finish();
    }
}
