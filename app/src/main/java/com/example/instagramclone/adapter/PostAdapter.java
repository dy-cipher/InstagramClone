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
import com.example.instagramclone.R;
import com.example.instagramclone.model.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {


    Context context;
    List<Post> posts;
    View view;


    // Pass in the context and list of posts
    public PostAdapter (Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }


    public  class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivImage;
        TextView tvDescription;
        TextView tvUserName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvUserName = itemView.findViewById(R.id.tvUserName);
        }


        public void bind(Post post){
            tvDescription.setText(post.getDescription());
            tvUserName.setText(post.getUser().getUsername());

            if (post.getImage() != null){
                Glide.with(context)
                        .load(post.getImage().getUrl())
                        .circleCrop()
                        .into(ivImage);
            }
        }

    }
}
