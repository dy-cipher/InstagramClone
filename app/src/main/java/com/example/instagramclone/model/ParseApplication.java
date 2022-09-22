package com.example.instagramclone.model;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;


public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comment.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("h3wkLFPsWMOJ9XHdhGiHWffFM3qvC3fZ2Nbdqxac")
                .clientKey("LAfKpeYehh1lcglQhFm2g6ZZLyFyIqmh4TVAhBIq")
                .server("https://parseapi.back4app.com")
                .build()
        );

    }
}
