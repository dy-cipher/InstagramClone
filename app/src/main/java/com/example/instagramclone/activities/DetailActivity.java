package com.example.instagramclone.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.instagramclone.R;
import com.example.instagramclone.adapter.CommentAdapter;
import com.example.instagramclone.model.Comment;
import com.example.instagramclone.model.Post;
import com.example.instagramclone.model.User;
import com.example.instagramclone.utils.TimeFormatter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    TextView tvUsername,tvCreatedAt,tvDescription, tvSave, tvComment, tvShare, tvLike;
    ImageView ivImage, ivImageProfile, ivHeart;
    RelativeLayout rlContainer;
    RecyclerView rvComments;
    CommentAdapter adapter;
    List<Comment> comments;
    List<String> idComments;
    public List<String> users;

    Context context;
    public static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Post post = Parcels.unwrap(getIntent().getParcelableExtra("data"));
        idComments  = Comment.fromJsonArray(post.getCommentList());
        try {
            users = Post.fromJsonArray(post.getListLike());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        tvUsername = findViewById(R.id.tvUsername);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvDescription = findViewById(R.id.tvDescription);
        ivImage = findViewById(R.id.ivImage);
        ivImageProfile = findViewById(R.id.ivImageProfile);
        ivHeart = findViewById(R.id.ivHeart);
        tvSave = findViewById(R.id.tvSave);
        tvComment = findViewById(R.id.tvComment);
        tvShare = findViewById(R.id.tvShare);
        tvLike = findViewById(R.id.tvLike);
        rlContainer = findViewById(R.id.rlContainer);
        rvComments = findViewById(R.id.rvComments);

        comments = new ArrayList<>();
        adapter = new CommentAdapter(context, comments);
        rvComments.setLayoutManager(new LinearLayoutManager(context));
        rvComments.setAdapter(adapter);

        if (users.contains(ParseUser.getCurrentUser().getObjectId())) {
            Drawable drawable = ContextCompat.getDrawable(DetailActivity.this, R.drawable.red_heart);
            ivHeart.setImageDrawable(drawable);
        }

        tvDescription.setText(post.getDescription());
        tvUsername.setText(post.getUser().getUsername());
        tvCreatedAt.setText(TimeFormatter.getTimeStamp(post.getCreatedAt().toString()));
        tvLike.setText(String.valueOf(post.getNumberLike()) + " like(s)");


        String picture_url = post.getImage().getUrl();
        String profile_url = post.getUser().getParseFile(User.KEY_PROFILE).getUrl();

        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
            }
        });

        Glide.with(DetailActivity.this)
                .load(profile_url)
                .transform(new RoundedCorners(30))
                .centerCrop()
                .into(ivImageProfile);

        Glide.with(DetailActivity.this)
                .load(picture_url)
                .transform(new RoundedCorners(30))
                .centerCrop()
                .into(ivImage);

        queryPost();
    }


    protected void queryPost() {
        ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.include(Comment.KEY_USER);
        query.whereContainedIn("objectId", idComments);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> commentList, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting Comments", e);
                    Toast.makeText(context, "Issue with getting Posts", Toast.LENGTH_SHORT).show();
                    return;
                }

                comments.addAll(commentList);
                adapter.notifyDataSetChanged();

            }
        });
    }
}