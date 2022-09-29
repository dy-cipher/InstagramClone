package com.example.instagramclone.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.instagramclone.R;
import com.example.instagramclone.activities.CommentActivity;
import com.example.instagramclone.activities.DetailActivity;
import com.example.instagramclone.fragment.ProfileFragment;
import com.example.instagramclone.model.Post;
import com.example.instagramclone.model.User;
import com.example.instagramclone.utils.TimeFormatter;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {


    Context context;
    List<Post> posts;
    View view;
    public ArrayList<String> users;
    public int numberLike;
    public static final String TAG = "PostAdapter";

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

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }


    public  class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivImage, ivComment, ivImageProfile, ivHeart;
        TextView tvDescription,  tvUserName, tvCreatedAt, tvNumberLike;
        RelativeLayout rlContainer;
        LinearLayout ProfileContainer;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivHeart = itemView.findViewById(R.id.ivHeart);
            ivImageProfile = itemView.findViewById(R.id.ivImageProfile);
            tvNumberLike = itemView.findViewById(R.id.tvNumberLike);
            ivComment = itemView.findViewById(R.id.ivComment);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
            rlContainer = itemView.findViewById(R.id.rlContainer);
            ProfileContainer = itemView.findViewById(R.id.ProfileContainer);
        }


        public void bind(Post post){
            users = Post.fromJsonArray(post.getListLike());
            tvDescription.setText(post.getDescription());
            tvUserName.setText(post.getUser().getUsername());
            tvCreatedAt.setText(TimeFormatter.getTimeDifference(post.getCreatedAt().toString()));
            tvNumberLike.setText(String.valueOf(numberLike) + " like(s)");
            Glide.with(context).load(post.getUser().getParseFile(User.KEY_PROFILE).getUrl()).into(ivImageProfile);

            ParseUser currentUser = ParseUser.getCurrentUser();

            if (users.contains(currentUser.getObjectId())) {
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.red_heart);
                ivHeart.setImageDrawable(drawable);
            }

            rlContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("data", Parcels.wrap(post));
                    context.startActivity(i);

                }
            });
            ParseFile image = post.getImage();

            if (image != null){
                Glide.with(context)
                        .load(image.getUrl())
                        .centerCrop()
                        .transform(new RoundedCorners(20))
                        .into(ivImage);
            }


            ivComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent (context, CommentActivity.class);
                    intent.putExtra("post", Parcels.wrap(post));
                    context.startActivity(intent);
                }
            });


            ProfileContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();

                    // set parameters
                    ProfileFragment profileFragment = ProfileFragment.newInstance("Some Title");
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("post", Parcels.wrap(post));
                    profileFragment.setArguments(bundle);

                    fragmentManager.beginTransaction().replace(R.id.flContainer, profileFragment).commit();
                }
            });


            ivHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    numberLike = post.getNumberLike();
                    int index;

                    if (!users.contains(currentUser.getObjectId())){
                        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.red_heart);
                        ivHeart.setImageDrawable(drawable);

                        numberLike++;
                        index = -1;
                    }else {
                        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.heart);
                        ivHeart.setImageDrawable(drawable);

                        numberLike--;
                        index = users.indexOf(currentUser.getObjectId());
                    }

                    tvNumberLike.setText(String.valueOf(numberLike) + " like(s)");
                    saveLike(post, numberLike, index, currentUser);
                }
            });
        }

        private void saveLike(Post post, int numberLike, int index, ParseUser currentUser) {
            post.setNumberLike(numberLike);

            if (index == -1){
                post.setListLike(currentUser);
                users.add(currentUser.getObjectId());
            }else {
                users.remove(index);
                post.removeLike(users);
            }

            post.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e != null){
                        Log.e(TAG,"issue saving like", e);
                        return;
                    }

                    Log.i(TAG, users.toString());
                }
            });
        }


    }
}
