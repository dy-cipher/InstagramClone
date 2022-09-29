package com.example.instagramclone.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.instagramclone.R;
import com.example.instagramclone.adapter.PostAdapter;
import com.example.instagramclone.listeners.EndlessRecyclerViewScrollListener;
import com.example.instagramclone.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends DialogFragment {

    private static final String TAG = "HomeFragment" ;
    RecyclerView rvPosts;
    PostAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    List<Post> posts;



    public HomeFragment(){}


    public static HomeFragment newInstance(String title) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        homeFragment.setArguments(args);
        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = view.findViewById(R.id.rvPosts);
        swipeRefreshLayout = view.findViewById(R.id.srlContainer);


        posts = new ArrayList<>();
        adapter = new PostAdapter(getContext(), posts);

        rvPosts.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(layoutManager);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                adapter.addAll(posts);
                queryPost();

                swipeRefreshLayout.setRefreshing(false);
            }
        });



        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryPost();
                int curSize = adapter.getItemCount();

                view.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyItemRangeInserted(curSize, posts.size() - 1);
                    }
                });
            }
        };
        rvPosts.addOnScrollListener(scrollListener);
//        queryPost();

    }

    protected void queryPost() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_KEY);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts_home, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for(Post post: posts_home){
                    Log.i(TAG, "Post" + post.getDescription() + ",  username: " + post.getUser().getUsername());

                }
                posts.addAll(posts_home);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
