package com.example.servopay.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.servopay.Model.Post;
import com.example.servopay.Model.User;
import com.example.servopay.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private CircleImageView imageProfile;
    private ImageView options,myPictures,savedPictures;
    private TextView posts,followers,following,fullname,bio,username;
    private FirebaseUser fUser;
    private Button editProfile;
    String profileID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        String data = getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).getString("profileId","none");

        if(data.equals("none")){
            profileID=fUser.getUid();
        }
        else {profileID = data;}

        imageProfile = view.findViewById(R.id.image_profile);
        options = view.findViewById(R.id.options);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        posts = view.findViewById(R.id.posts);
        fullname = view.findViewById(R.id.fullname);
        bio = view.findViewById(R.id.bio);
        username = view.findViewById(R.id.username);
        myPictures = view.findViewById(R.id.my_pictures);
        savedPictures = view.findViewById(R.id.saved_pictures);
        editProfile = view.findViewById(R.id.edit_profile);

        profileID = fUser.getUid();



        userInfo();
        getFolloweraAndFollowingCount();
        getPostCount();

        if(profileID.equals(fUser.getUid())){
            editProfile.setText("Edit Profile");
        }
        else {checkFollowingStatus();}

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btnText = editProfile.getText().toString();

                if (btnText.equals("Edit Profile")){
                    //GOTO edit activity

                }
                else {
                    if(btnText.equals("follow")){
                        FirebaseDatabase.getInstance().getReference("Follow").child(fUser.getUid()).child("following").child(profileID).setValue(true);

                        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileID).child("followers").child(fUser.getUid()).setValue(true);
                    }
                    else {
                        FirebaseDatabase.getInstance().getReference("Follow").child(fUser.getUid()).child("following").child(profileID).removeValue();

                        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileID).child("followers").child(fUser.getUid()).removeValue();


                    }

                }
            }
        });

        return view;
    }

    private void checkFollowingStatus() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(fUser.getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.child(profileID).exists()){
                    editProfile.setText("following");
                }
                else{
                    editProfile.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void getPostCount() {
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                int counter = 0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Post post = snapshot.getValue(Post.class);

                    if(post.getPublisher().equals(profileID)) counter++;
                }
                posts.setText(String.valueOf(counter));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    private void getFolloweraAndFollowingCount() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("follow").child(profileID);

        ref.child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                followers.setText(""+ dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        ref.child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                following.setText("" + snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });




    }

    private void userInfo() {

        FirebaseDatabase.getInstance().getReference().child("Users").child(profileID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Picasso.get().load(user.getImageurl()).into(imageProfile);
                username.setText(user.getUsername());
                fullname.setText(user.getName());
                bio.setText(user.getBio());


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}