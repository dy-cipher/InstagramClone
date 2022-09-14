package com.example.instagramclone.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.instagramclone.LoginActivity;
import com.example.instagramclone.R;
import com.example.instagramclone.adapter.ProfileAdapter;
import com.example.instagramclone.model.Post;
import com.example.instagramclone.model.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends DialogFragment {


    Button btnLogOut;
    RecyclerView rvPosts;
    ImageView ivImage,ivEdit;
    TextView tvUsername;


    public static final String TAG = "ProfileFragment";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 69;
    public static final ParseUser CURRENT_USER =  ParseUser.getCurrentUser();

    private File photoFile;
    public String photoFileName = "photo.jpg";

    ProfileAdapter profileAdapter;
    List<Post> posts;
    ProgressBar pbLoading;


    String profile_url = ParseUser.getCurrentUser().getParseFile(User.KEY_PROFILE).getUrl();


    public ProfileFragment(){}


    public static ProfileFragment newInstance(String title) {
        ProfileFragment frag = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        ivEdit = view.findViewById(R.id.ivEdit);
        ivImage = view.findViewById(R.id.ivImage);
        tvUsername = view.findViewById(R.id.tvUserName);
        pbLoading = view.findViewById(R.id.pbLoading);

        posts = new ArrayList<>();
        profileAdapter = new ProfileAdapter(getContext(), posts);
        rvPosts.setAdapter(profileAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(layoutManager);
        queryPost();


        Glide.with(getContext())
                .load(profile_url)
                .centerCrop()
                .transform(new RoundedCorners(30))
                .into(ivImage);

        tvUsername.setText(ParseUser.getCurrentUser().getUsername());
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
            }
        });
        btnLogOut = (Button) view.findViewById(R.id.btnLogOut);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if(resultCode== Activity.RESULT_OK){

                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                updateProfile();
                pbLoading.setVisibility(ProgressBar.INVISIBLE);


            } else { // Result was a failure
                Toast.makeText(getContext(), "Error taking picture", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider1", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }


    }
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);


    }


    public void queryPost() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.whereEqualTo(Post.KEY_USER, CURRENT_USER);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts_profile, ParseException e) {
                if(e != null){
                    Log.e(TAG,"issue findind post", e);
                    return;
                }
                for (Post post: posts_profile){
                    Log.i(TAG, "Post: "+ post.getDescription() + " user: " +post.getUser().getUsername());

                }
                posts.addAll(posts_profile);
                profileAdapter.notifyDataSetChanged();
            }
        });

    }

    public void updateProfile(){
        pbLoading.setVisibility(ProgressBar.VISIBLE);

        User currentUser = (User) CURRENT_USER;
        // Set custom properties
        currentUser.setProfile(new ParseFile(photoFile));

        currentUser.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                // If successful add file to user and signUpInBackground
                if(null != e)
                    Log.e(TAG, "Error saving");


            }
        });
    }

}
