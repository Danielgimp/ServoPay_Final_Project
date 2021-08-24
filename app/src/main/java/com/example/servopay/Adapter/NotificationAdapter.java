package com.example.servopay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servopay.Fragments.PostDetailFragment;
import com.example.servopay.Fragments.ProfileFragment;
import com.example.servopay.Model.Notification;
import com.example.servopay.Model.Post;
import com.example.servopay.Model.User;
import com.example.servopay.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    private Context mContext;
    private List<Notification> mNotifications;

    public NotificationAdapter(Context mContext, List<Notification> mNotifications) {
        this.mContext = mContext;
        this.mNotifications = mNotifications;
    }



    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item,parent,false);

        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NotificationAdapter.ViewHolder holder, int position) {

        Notification notification = mNotifications.get(position);

        getUser(holder.imageProfile,holder.username, notification.getUserid());

        if(notification.isPost()){
            holder.postImage.setVisibility(View.VISIBLE);
            getPostImage(holder.postImage,notification.getPostid());
        }
        else{
            holder.postImage.setVisibility(View.GONE);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notification.isPost()){
                    mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit().putString("postid",notification.getPostid()).apply();

                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PostDetailFragment()).commit();
                }
                else {
                    mContext.getSharedPreferences("PROFILE",Context.MODE_PRIVATE).edit().putString("postId",notification.getUserid()).apply();

                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();

                }
            }
        });

    }

    private void getPostImage(ImageView imageView, String postId) {

        FirebaseDatabase.getInstance().getReference().child("Posts").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                Picasso.get().load(post.getImageurl()).placeholder(R.mipmap.ic_launcher).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    private void getUser(ImageView imageView, TextView textView, String userId) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.getImageurl().equals("default")){
                    imageView.setImageResource(R.mipmap.ic_launcher);

                }
                else{
                    Picasso.get().load(user.getImageurl()).into(imageView);
                    textView.setText(user.getUsername());
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageProfile,postImage;
        public TextView username,comment;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageProfile=itemView.findViewById(R.id.image_profile);
            postImage=itemView.findViewById(R.id.post_image);
            username= itemView.findViewById(R.id.username);
            comment=itemView.findViewById(R.id.comment);


        }
    }
}
