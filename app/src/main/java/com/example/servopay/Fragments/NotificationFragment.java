package com.example.servopay.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.servopay.Adapter.NotificationAdapter;
import com.example.servopay.Model.Notification;
import com.example.servopay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationList=new ArrayList<>();
        notificationAdapter = new NotificationAdapter(getContext(),notificationList);
        recyclerView.setAdapter(notificationAdapter);

        readNotifications();



        return view;
    }

    private void readNotifications() {

        FirebaseDatabase.getInstance().getReference().child("notifications").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    notificationList.add(snapshot.getValue(Notification.class));
                }

                Collections.reverse(notificationList);
                notificationAdapter.notifyDataSetChanged();


            }


            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}