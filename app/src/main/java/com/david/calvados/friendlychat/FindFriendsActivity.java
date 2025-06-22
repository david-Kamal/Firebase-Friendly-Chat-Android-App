package com.david.calvados.friendlychat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FindFriendsActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    private RecyclerView FindFriendsRecyclerList;
    private DatabaseReference UsersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        FindFriendsRecyclerList = (RecyclerView) findViewById(R.id.find_friends_recycler_list);
        FindFriendsRecyclerList.setLayoutManager(new LinearLayoutManager(this));

        mToolBar = (Toolbar) findViewById(R.id.find_friends_toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Find Friends");


    }

    @Override
    protected void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Contacts> options =
                new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(UsersRef, Contacts.class).build();


        FirebaseRecyclerAdapter<Contacts, FindFriendsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Contacts, FindFriendsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FindFriendsViewHolder holder, int position, @NonNull Contacts model)
                    {
                        // get the name of the user from the database
                        holder.userName.setText(model.getName());

                    }

                    @Override
                    public FindFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        // connect layout
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_display_layout, viewGroup, false);
                        FindFriendsViewHolder viewHolder = new FindFriendsViewHolder(view);
                        return viewHolder;
                    }
                };

        FindFriendsRecyclerList.setAdapter(adapter);

        adapter.startListening();

    }

    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder
    {

        TextView userName;

        public FindFriendsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.user_profile_name);
        }
    }



}
