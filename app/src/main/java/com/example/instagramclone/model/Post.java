package com.example.instagramclone.model;


import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel(analyze = Post.class)
@ParseClassName("Post")
public class Post extends ParseObject {

    public Post() {}

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_KEY = "createdAt";
    public static final String KEY_COMMENT_LIST = "comment_list";
    public static final String KEY_NUMBER_LIKE= "number_like";
    public static final String KEY_LIST_LIKE = "list_like";


    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage(){
        return  getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image){
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser(){
        return  getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public JSONArray getCommentList(){return getJSONArray(KEY_COMMENT_LIST);}
    public void setCommentList(Comment comment){add(KEY_COMMENT_LIST, comment);}

    public  int getNumberLike() {return getInt(KEY_NUMBER_LIKE);}
    public void setNumberLike(int num){put(KEY_NUMBER_LIKE, num);}

    public JSONArray getListLike(){return getJSONArray(KEY_LIST_LIKE);}
    public void setListLike(ParseUser user){add(KEY_LIST_LIKE, user);}
    public void removeLike(ArrayList<String> listUser){
        remove(KEY_LIST_LIKE);
        put(KEY_LIST_LIKE, listUser);
    }


    public static ArrayList<String> fromJsonArray(JSONArray jsonArray) {
        ArrayList<String> userObjectId = new ArrayList<String>();

        try {
            for (int i = 0; i < jsonArray.length(); i++){
                userObjectId.add(jsonArray.getJSONObject(i).getString("objectId"));
            }
        }catch (NullPointerException | JSONException e){
            e.printStackTrace();
        }

        return userObjectId;
    }
}
