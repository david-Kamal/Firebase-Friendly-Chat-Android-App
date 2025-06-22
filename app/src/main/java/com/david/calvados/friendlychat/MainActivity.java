package com.david.calvados.friendlychat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ViewPager mViewpager;
    private TabLayout mTablayout;
    private TabsAccessAdapter mTabsAccessAdapter;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Friendly App");
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        mAuth = FirebaseAuth.getInstance();

        Log.i("main", mAuth.getCurrentUser() + "");

        currentUser = mAuth.getCurrentUser();
        RootRef = FirebaseDatabase.getInstance().getReference();

        mViewpager = (ViewPager) findViewById(R.id.main_tabs_pager);
        mTabsAccessAdapter = new TabsAccessAdapter(getSupportFragmentManager());
        mViewpager.setAdapter(mTabsAccessAdapter);


        mTablayout = (TabLayout) findViewById(R.id.main_tabs);
        mTablayout.setupWithViewPager(mViewpager);

    }

    // Check wether the user is logged in previously

    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser == null)
        {
            SendUserToLoginActivity();
        }
        // watch video number 14
        else
        {
            VerifyUserExistence();
        }

    }

    private void VerifyUserExistence()
    {
        String currentUserId = mAuth.getCurrentUser().getUid();

        // get user name from the database server
        RootRef.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ((dataSnapshot.child("name").exists()))
                {
                    Toast.makeText(MainActivity.this, "Welcome...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SendUserToSettingsActivity();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    // For the menu logout

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.options_menu,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);

         if (item.getItemId() == R.id.main_logout)
         {
            mAuth.signOut();
            SendUserToLoginActivity();
         }
        if (item.getItemId() == R.id.find_friends_group_option)
        {
            SendUserToFindFriendsActivity();
        }

         if (item.getItemId() == R.id.main_create_group_option)
        {
            RequestNewGroup();
        }
        // main_settings_option
        if (item.getItemId() == R.id.main_settings_option)
        {
            SendUserToSettingsActivity();
        }

         return true;
    }

    private void RequestNewGroup()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
        builder.setTitle("Enter Group Name : ");
        final EditText groupNameField = new EditText(MainActivity.this);
        groupNameField.setHint("e.g Friends");
        builder.setView(groupNameField);

        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName = groupNameField.getText().toString();
                if(TextUtils.isEmpty(groupName))
                {
                    Toast.makeText(MainActivity.this, "Please write Group Name", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    CreateNewGroup(groupName);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void CreateNewGroup(final String groupName) {

        RootRef.child("Groups").child(groupName).setValue("")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, groupName +" group is created successfully!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }




    private void SendUserToLoginActivity()
    {
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }

    private void SendUserToSettingsActivity()
    {
        Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        startActivity(intent);
        finish();
    }

    private void SendUserToFindFriendsActivity()
    {
        Intent intent = new Intent(MainActivity.this,FindFriendsActivity.class);
        startActivity(intent);

    }
}
