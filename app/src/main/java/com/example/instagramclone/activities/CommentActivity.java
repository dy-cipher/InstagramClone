package com.example.instagramclone.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instagramclone.R;
import com.example.instagramclone.model.Comment;
import com.example.instagramclone.model.Post;
import com.example.instagramclone.model.User;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

public class CommentActivity extends AppCompatActivity {
    Button btnComment;
    ImageView ivImageProfile;
    EditText etComment;
    TextView tvUserName;
    Context context;
    Post post;
    public static final String TAG = "CommentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        initWidgets();

        post = Parcels.unwrap(getIntent().getParcelableExtra("post"));
        ParseUser currentUser = ParseUser.getCurrentUser();
        tvUserName.setText(currentUser.getUsername());
        Glide.with(CommentActivity.this).load(currentUser.getParseFile(User.KEY_PROFILE).getUrl()).into(ivImageProfile);

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // button to send comment
                String description = etComment.getText().toString();
                if (description.isEmpty()){
                    Toast.makeText(context, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                ParseUser currentUser = ParseUser.getCurrentUser();
                saveComment(description, currentUser);
            }
        });


    }

    public void initWidgets(){
        btnComment = findViewById(R.id.btnComment);
        ivImageProfile = findViewById(R.id.ivImageProfile);
        etComment = findViewById(R.id.etComment);
        tvUserName = findViewById(R.id.tvUserName);
    }

    public void saveComment(String description, ParseUser user){
        Comment comment = new Comment();

        comment.setUser(user);
        comment.setDescription(description);
        post.setCommentList(comment);

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(context, "Error while saving", Toast.LENGTH_SHORT).show();
                    return;
                }

                etComment.setText("");
            }
        });
    }
}