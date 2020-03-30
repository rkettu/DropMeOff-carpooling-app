package com.example.mobproj2020new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.graphics.Color;

import android.util.Log;
import android.widget.EditText;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LogInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseHandler db;

    private final String TAG = "HALOOOOO";

    EditText userEdit;
    EditText passEdit;

    String userNameStr = "";
    String passwordStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = new DatabaseHandler();

        userEdit = findViewById(R.id.usernameEdit);
        passEdit = findViewById(R.id.passwordEdit);

        FirebaseAuth.AuthStateListener als = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mAuth.getCurrentUser() != null)
                {
                    CheckProfileCreated();
                }
                else
                {
                    // User not logged in anymore...
                    // Maybe return to MainActivity here?
                }
            }
        };
        mAuth.addAuthStateListener(als);
    }

    private void CheckProfileCreated()
    {
        //Log.d("HALOOOOOOOOOOOOOOOOO", "Taalla ollaan");
        //db.init(mAuth.getCurrentUser());
        db.checkProfileCreated(getApplicationContext());
    }


    //Login button press
    public void login(View V){
        String email = userEdit.getText().toString();
        String password = passEdit.getText().toString();

        if(email.equals(""))
        {
            noEntry(userEdit);
        }
        else if(password.equals(""))
        {
            noEntry(passEdit);
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                onBackPressed();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LogInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    //SignUp button press
    public void signUp(View v){
        Intent signUpIntent = new Intent(this, SignUp.class);
        startActivity(signUpIntent);
    }

    //execute if username or password is empty
    public void noEntry(EditText et){
        et.setText("");
        et.setHintTextColor(Color.parseColor("#B75252"));
        if (et.getId() == R.id.usernameEdit) userEdit.setHint("Username*");
        else passEdit.setHint("Password*");
    }

    //Exit app with pressing back putton on your phone
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        return;
    }

}
