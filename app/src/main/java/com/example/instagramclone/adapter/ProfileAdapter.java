package com.example.instagramclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.instagramclone.R;
import com.example.instagramclone.model.Post;

import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends ArrayAdapter<Post> {

    Context context;
    public ProfileAdapter(@NonNull Context context, ArrayList<Post> posts) {
        super(context, 0, posts);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.item_profile, parent, false);
        }

        Post post = getItem(position);
        ImageView image = listitemView.findViewById(R.id.ivImage);

        Glide.with(getContext()).load(post.getImage().getUrl()).into(image);
        return listitemView;
    }
}




