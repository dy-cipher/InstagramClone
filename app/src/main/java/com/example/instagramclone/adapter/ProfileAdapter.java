package com.example.instagramclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.instagramclone.R;
import com.example.instagramclone.model.Post;
import com.parse.ParseFile;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    Context context;
    List<Post> posts;
    View view;


    // Pass in the context and list of posts
    public ProfileAdapter (Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }


    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_profile, parent, false);
        return new ProfileAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public  class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);

        }


        public void bind(Post post) {

            ParseFile image = post.getImage();

            if (image != null) {
                Glide.with(context)
                        .load(image.getUrl())
                        .centerCrop()
                        .transform(new RoundedCorners(20))
                        .into(ivImage);
            }
        }

    }
}




