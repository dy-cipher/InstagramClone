package com.example.instagramclone.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel(analyze = Comment.class)
@ParseClassName("Comment")
public class Comment extends ParseObject {

    public Comment(){}

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_USER = "user";

    public static List<String> fromJsonArray(JSONArray jsonArray) {
        List<String> commentList = new ArrayList<String>();

        try {
            for (int i = 0; i < jsonArray.length(); i++){
                commentList.add(jsonArray.getJSONObject(i).getString("objectId"));
            }
        }catch (NullPointerException | JSONException e){
            e.printStackTrace();
        }

        return commentList;
    }

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }
    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }


    public ParseUser getUser(){
        return  getParseUser(KEY_USER);
    }
    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

}
