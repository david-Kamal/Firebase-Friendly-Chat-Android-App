package com.david.calvados.friendlychat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    private Button updateAccountSettings;
    private EditText userName;


    private String   currentUserID;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        updateAccountSettings = (Button) findViewById(R.id.update_settings_button);
        userName = (EditText) findViewById(R.id.set_user_name);

        // getting the id from the dataabse
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();



        updateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpdateSettings();
            }
        });


        RetrieveUserInfo();


    }

    // update the anme of the current user to the edit text
    private void RetrieveUserInfo()
    {
        RootRef.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // if user id exists on the database
                if((dataSnapshot.exists()) && (dataSnapshot.hasChild("name")))
                {
                    String retrieveUserName = dataSnapshot.child("name").getValue().toString();

                    userName.setText(retrieveUserName);
                }
                else
                {

                    Toast.makeText(SettingsActivity.this, "Please update your user name...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void UpdateSettings()
    {
        String setUserName = userName.getText().toString();

        if (TextUtils.isEmpty(setUserName))
        {
            Toast.makeText(this, "Please write your user name...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String, Object> profileMap = new HashMap<>();
            profileMap.put("uid", currentUserID);
            profileMap.put("name", setUserName);

            RootRef.child("Users").child(currentUserID).setValue(profileMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                SendUserToMainActivity();
                                Toast.makeText(SettingsActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(SettingsActivity.this, "Error :- "+ message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


    }

    private void SendUserToMainActivity()
    {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
