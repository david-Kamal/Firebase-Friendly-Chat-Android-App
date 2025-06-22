package com.david.calvados.friendlychat;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment {

    private View groupFragmentView;
    private ListView list_view;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_groups = new ArrayList<>();

    private DatabaseReference GroupRef;


    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        groupFragmentView =  inflater.inflate(R.layout.fragment_groups, container, false);

        GroupRef = FirebaseDatabase.getInstance().getReference().child("Groups");


        InitializeFields();

        RetrieveAndDisplayGroups();

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            // position of the item, and the id og the item
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // store the name of the current group name
                String currentGroupName = adapterView.getItemAtPosition(position).toString();

                // get contects because it is a fragment
                Intent groupChatIntent = new Intent(getContext(),GroupChatActivity.class);
                // take the name with you to the next activity
                groupChatIntent.putExtra("groupName", currentGroupName);
                startActivity(groupChatIntent);


            }
        });


        return groupFragmentView;
    }

    private void RetrieveAndDisplayGroups()
    {

        GroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                //we don't want the group name to be repeated
                Set<String> set = new HashSet<>();

                // we can read every child of the groups (group1,group2)
                Iterator iterator = dataSnapshot.getChildren().iterator();

                // read every child line by line
                while(iterator.hasNext())
                {
                    // prevent duplication of names
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }
                list_of_groups.clear();
                list_of_groups.addAll(set);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }

    private void InitializeFields()
    {
        list_view = (ListView) groupFragmentView.findViewById(R.id.list_view);
        // storing groups on dataabse
        arrayAdapter = new ArrayAdapter<String>(getContext() , android.R.layout.simple_list_item_1, list_of_groups);
        list_view.setAdapter(arrayAdapter);
    }

}
