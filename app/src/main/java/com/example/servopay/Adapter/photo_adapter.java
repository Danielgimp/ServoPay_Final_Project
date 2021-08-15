package com.example.servopay.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servopay.Model.Post;
import com.example.servopay.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class photo_adapter extends RecyclerView.Adapter<photo_adapter.ViewHolder>{

    private Context mContext;
    private List<Post> mPostsl;


    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.photo_item,parent,false);

        return new photo_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull photo_adapter.ViewHolder holder, int position) {

        Post post = mPostsl.get(position);
        Picasso.get().load(post.getImageurl()).placeholder(R.mipmap.ic_launcher).into(holder.postImage);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView postImage;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            postImage = itemView.findViewById(R.id.post_image);
        }
    }
}
