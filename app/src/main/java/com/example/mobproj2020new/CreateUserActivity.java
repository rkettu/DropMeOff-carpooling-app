package com.example.mobproj2020new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class CreateUserActivity extends AppCompatActivity {
    private final String TAG = "CREATEUSERACTIVITY";

    private EditText fnameField;
    private EditText lnameField;
    private EditText emailField;
    private EditText phoneField;
    private EditText pass1Field;
    private EditText pass2Field;

    private FirebaseAuth mAuth;
    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        mAuth = FirebaseAuth.getInstance();

        fnameField = (EditText) findViewById(R.id.fnameField);
        lnameField = (EditText) findViewById(R.id.lnameField);
        emailField = (EditText) findViewById(R.id.userEmailField);
        phoneField = (EditText) findViewById(R.id.phoneField);
        pass1Field = (EditText) findViewById(R.id.passwordField1);
        pass2Field = (EditText) findViewById(R.id.passwordField2);

        pass2Field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Check if password fields match
                if(pass1Field.getText().toString().equals(pass2Field.getText().toString())) {
                    // Passwords match!
                }
                else {
                    // Passwords don't match
                    // Update some textview in realtime that says passwords don't match
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final Button createUserBtn = (Button) findViewById(R.id.createUserBtn);
        createUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateUserBtnFunc();
            }
        });

        dbHandler = new DatabaseHandler();
    }

    private void CreateUserBtnFunc()
    {
        final String email = emailField.getText().toString();
        final String pass1 = pass1Field.getText().toString();
        final String pass2 = pass2Field.getText().toString();
        final String phone = phoneField.getText().toString();
        final String fname = fnameField.getText().toString();
        final String lname = lnameField.getText().toString();

        if(email.equals("") || pass1.equals("") || phone.equals("") || fname.equals("") || lname.equals(""))
        {
            Toast.makeText(CreateUserActivity.this, "Some fields empty",
                    Toast.LENGTH_SHORT).show();
        }
        else if(!pass1.equals(pass2)) {
            Toast.makeText(CreateUserActivity.this, "Passwords don't match",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.createUserWithEmailAndPassword(email, pass1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(fname).build();
                        user.updateProfile(profileUpdates);
                        // Init dbHandler object for this class only
                        dbHandler.setUserCreationInfo(fname, lname, phone);
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(CreateUserActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        //  Sign in failed...
                    }
                }
            });
        }
    }
}
