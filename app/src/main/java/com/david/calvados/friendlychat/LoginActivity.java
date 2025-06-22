package com.david.calvados.friendlychat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private Button loginBtn, phoneLoginBtn;
    private EditText userEmail, userPassword;
    private TextView NeedNewAccountLink, ForgetPasswordLink;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button) findViewById(R.id.login_btn);
        phoneLoginBtn = (Button) findViewById(R.id.phone_login_btn);
        userEmail = (EditText) findViewById(R.id.login_email);
        userPassword = (EditText) findViewById(R.id.login_password);
        NeedNewAccountLink = (TextView) findViewById(R.id.need_new_account_link);
        ForgetPasswordLink = (TextView) findViewById(R.id.forget_password_link);
        loadingBar = new ProgressDialog(this);


//      Authentication
        mAuth = FirebaseAuth.getInstance();







        NeedNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToRegisterActivity();
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToLogin();
            }
        });



    }

    private void AllowUserToLogin() {

        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter your E-mail...", Toast.LENGTH_SHORT).show();
        }if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter your Password...", Toast.LENGTH_SHORT).show();
        }else
        {

            loadingBar.setTitle("Logging in");
            loadingBar.setMessage("Please wait ");
            // When it shows on screen and the user click on the screen then the loading bar won't disappear
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();

            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())

                            {
                                SendUserToMainActivity();
                                Toast.makeText(LoginActivity.this, "Logged in Successfully!!", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }else
                            {
                                Toast.makeText(LoginActivity.this, "Error :- "+ task.getException(), Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }

                        }
                    });
        }



    }


    private void SendUserToMainActivity()
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void SendUserToRegisterActivity()
    {
        Intent signUpIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(signUpIntent);
    }
}
