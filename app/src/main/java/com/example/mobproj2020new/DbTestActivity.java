package com.example.mobproj2020new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DbTestActivity extends AppCompatActivity {

    private final String TAG = "HEYYYY";

    private FirebaseAuth mAuth;
    private Button loginBtn;
    private Button logoutBtn;
    private Button createUserBtn;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_test);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailField);
        passwordEditText = findViewById(R.id.passwordField);
        loginBtn = findViewById(R.id.loginBtn);
        createUserBtn = findViewById(R.id.passwordBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogInBtnFunc();
            }
        });
        createUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUserBtnFunc();
            }
        });

        FirebaseAuth.AuthStateListener als = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(mAuth.getCurrentUser() != null)
                {
                    // Go to logged in activity whenever auth state changes to logged in
                    LogInSuccess();
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

    private void LogOutBtnFunc()
    {
        mAuth.signOut();
    }

    private void LogInBtnFunc()
    {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if(email.equals("") || password.equals(""))
        {
            Toast.makeText(DbTestActivity.this, "Email or password empty",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(DbTestActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void CreateUserBtnFunc()
    {
        // Go to create user activity
        Intent intent = new Intent(DbTestActivity.this, CreateUserActivity.class);
        startActivity(intent);
    }

    private void LogInSuccess()
    {
        // Logging in to main service
        Intent intent = new Intent(DbTestActivity.this, LoggedInActivity.class);
        startActivity(intent);
    }
}
