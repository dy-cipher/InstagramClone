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
import com.example.instagramclone.model.Comment;
import com.example.instagramclone.model.User;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    Context context;
    List<Comment> commentList;
    View view;

    public CommentAdapter(Context context, List<Comment> comments){
        this.commentList = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.bind(comment, holder);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivProfile;
        TextView tvUsername, tvDescription;
        private Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivProfile = itemView.findViewById(R.id.ivProfile);
        }

        public void bind(Comment comment, ViewHolder holder) {
            tvUsername.setText(comment.getUser().getUsername());
            tvDescription.setText(comment.getDescription());
            Glide.with(holder.itemView.getContext())
                    .load(comment.getUser().getParseFile(User.KEY_PROFILE)
                            .getUrl())
                    .centerCrop()
                    .transform(new RoundedCorners(30))
                    .into(ivProfile);
        }
    }

}
