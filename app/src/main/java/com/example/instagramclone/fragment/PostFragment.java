package com.example.instagramclone.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.instagramclone.MainActivity;
import com.example.instagramclone.R;
import com.example.instagramclone.model.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class PostFragment extends Fragment {

    EditText etDescription;
    ImageView ivPicture;
    Button btnSubmit, btnPicture;
    ProgressBar pbLoading;
    RelativeLayout rlContainer;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 25;
    private File photoFile;
    public String photoFileName = "photo.jpg";
    final String TAG = "MainActivity";

    public PostFragment(){}

    public static PostFragment newInstance(String title) {
        PostFragment postFragment = new PostFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        postFragment.setArguments(args);
        return postFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivPicture = (ImageView) view.findViewById(R.id.ivPicture);
        etDescription = (EditText) view.findViewById(R.id.etDescription);
        btnPicture = (Button) view.findViewById(R.id.btnPicture);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);
        rlContainer = (RelativeLayout) view.findViewById(R.id.rlContainer);



        // Camera button click
        btnPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Launching camera", Toast.LENGTH_SHORT).show();
                launchCamera();
                ivPicture.setVisibility(View.VISIBLE);
            }
        });

        // Submit button click
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Description = etDescription.getText().toString();
                if(Description.isEmpty()){
                    Toast.makeText(getContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(photoFile == null || ivPicture.getDrawable() == null){
                    Toast.makeText(getContext(), "There is no image", Toast.LENGTH_SHORT).show();
                    return;

                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(Description, currentUser);
            }
        });



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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        rlContainer.setVisibility(View.VISIBLE);
        btnPicture.setVisibility(View.INVISIBLE);
        if(requestCode ==CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if(resultCode== Activity.RESULT_OK){

                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivPicture.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Error taking picture", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
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

    private void savePost(String description, ParseUser currentUser) {
        // Show progress bar

        Post post = new Post();
        pbLoading.setVisibility(View.VISIBLE);
        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Post was saved succesfully");
                etDescription.setText("");
                ivPicture.setImageResource(0);
                // Hide progress bar
                pbLoading.setVisibility(View.INVISIBLE);
            }
        });
    }



}
