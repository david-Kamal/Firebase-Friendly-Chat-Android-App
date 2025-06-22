package com.david.calvados.friendlychat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Button registerBtn;
    private EditText userEmail, userPassword;
    private TextView AlreadyHaveAccountLink;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;

    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBtn = (Button) findViewById(R.id.register_btn);
        userEmail = (EditText) findViewById(R.id.register_email);
        userPassword = (EditText) findViewById(R.id.register_password);
        AlreadyHaveAccountLink = (TextView) findViewById(R.id.already_have_account_link);

        // Authentication
        mAuth = FirebaseAuth.getInstance();
        Log.i("main", mAuth.getLanguageCode() + "");
        //      Database reference
        RootRef = FirebaseDatabase.getInstance().getReference();


        loadingBar = new ProgressDialog(this);

        AlreadyHaveAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            SendUserToLoginActivity();
            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateNewAccount();

            }
        });
    }

    private void CreateNewAccount() {

        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter your E-mail...", Toast.LENGTH_SHORT).show();
        }if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter your Password...", Toast.LENGTH_SHORT).show();
        }
        else
        {

            loadingBar.setTitle("Creating new account");
            loadingBar.setMessage("Please wait while we are creating a new account for you..");
            // When it shows on screen and the user click on the screen then the loading bar won't disappear
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            loadingBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                String currentUserID = mAuth.getCurrentUser().getUid();
                                RootRef.child("Users").child(currentUserID).setValue("");

                                SendUserToMainActivity();
                                Toast.makeText(RegisterActivity.this, "Account Created Successfully!!", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }else
                            {
                                String message = task.getException().toString();

                                Toast.makeText(RegisterActivity.this, "Error :- "+ task.getException(), Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });



        }



    }

    public void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(loginIntent);
    }


    private void SendUserToMainActivity()
    {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }



}
