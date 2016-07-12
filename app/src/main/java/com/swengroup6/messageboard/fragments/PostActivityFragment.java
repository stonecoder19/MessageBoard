package com.swengroup6.messageboard.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.swengroup6.messageboard.R;
import com.swengroup6.messageboard.adapters.PostListAdapter;
import com.swengroup6.messageboard.helper.PostFiller;
import com.swengroup6.messageboard.models.Post;
import com.swengroup6.messageboard.sync.PostSync;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PostActivityFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView postList;
    private PostListAdapter postListAdapter;
    private List<Post> posts;
    private ProgressBar progressBar;
    int discussionid;
    private TextView txt_notpresent;
    private ImageView img_notpresent;
    private SwipeRefreshLayout swipeRefreshLayout;
    ConnectivityManager cm;
    View view;
    public PostActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        cm = (ConnectivityManager)this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_post, container, false);

        progressBar = (ProgressBar)view.findViewById(R.id.progressPost);
        progressBar.setIndeterminate(true);

        setUpSwipeRefresh();

        txt_notpresent = (TextView)view.findViewById(R.id.txt_notpresent); //place holder text
        img_notpresent = (ImageView)view.findViewById(R.id.img_notpresnt); //place holder image

        discussionid = getActivity().getIntent().getExtras().getInt("discussionid"); //get id of discussion from previous activity

        //PostFiller postFiller = new PostFiller();
        //posts = postFiller.findPostsByDiscussion(id);
       // posts = postFiller.getAllPosts();

        posts = Post.find(Post.class,"discussionid = ?",discussionid+""); //retrieve posts with that discussion id

        if(posts.size()>0){
            progressBar.setVisibility(View.GONE); //hide the progress bar
        }

        if(posts.size() == 0){
            txt_notpresent.setVisibility(View.VISIBLE); //show the placeholder text
            img_notpresent.setVisibility(View.VISIBLE); //show the place holder image
        }


        setUpRecyclerView();
        new PostSyncTask(this.getActivity(),discussionid).execute(""); //refreshes the posts



        return view;
    }

    private void setUpSwipeRefresh(){
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swiperefresh); //reference swipe refresh layout
        swipeRefreshLayout.setOnRefreshListener(this); //set the listner for the swipe refresh to the current fragment
        swipeRefreshLayout.setColorSchemeResources(R.color.blue,R.color.green,R.color.orange); //sets the colors for the swipe refresh
    }

    private void setUpRecyclerView(){
        postList = (RecyclerView)view.findViewById(R.id.postList); //reference post list
        postList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity()); //reponsible the layout of the recycler view
        postList.setLayoutManager(llm); //sets layout manager for recycler view
        postListAdapter = new PostListAdapter(this.getActivity(),posts); //responsible for linking the models to the views
        postList.setAdapter(postListAdapter); //sets the adapter of the list
    }


    private class PostSyncTask extends AsyncTask<String,Integer,Boolean> {

        Context ctxt;
        int discussionid;

        public  PostSyncTask(Context ctxt,int discussionid){
            this.ctxt = ctxt;
            this.discussionid = discussionid;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(ctxt,"Loading posts .. ",Toast.LENGTH_SHORT).show();

            txt_notpresent.setVisibility(View.GONE); //hides the place holder text
            img_notpresent.setVisibility(View.GONE); //hides the place holder image

            if(swipeRefreshLayout.isRefreshing()){ //checks if swipe refresh is active
                progressBar.setVisibility(View.GONE); //hide the progress bar
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return new PostSync().syncPosts(discussionid); //syncs pots with that on server
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressBar.setVisibility(View.GONE); //hide the progress bar
            swipeRefreshLayout.setRefreshing(false); //disable swipe refresh

            if(result){
                Toast.makeText(ctxt,"Sucessful",Toast.LENGTH_SHORT).show();
                updatePostList();
            }else{
                if(!isConnected()){ //if not conected to the network
                    Toast.makeText(ctxt,"No Internet",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ctxt,"Loading failed",Toast.LENGTH_SHORT).show();
                }

                if(posts.size() == 0){ //checks if there are no posts
                    txt_notpresent.setVisibility(View.VISIBLE); //shows placeholder text
                    img_notpresent.setVisibility(View.VISIBLE); //shows placeholder image
                }
            }

        }
    }

    public void updatePostList(){
        posts = Post.find(Post.class,"discussionid = ?",discussionid+"");
        postListAdapter.updatePostList(posts);
        if(posts.size()>0){
            txt_notpresent.setVisibility(View.GONE); //hides the place holder text
            img_notpresent.setVisibility(View.GONE); //hides the place holder image
        }
    }

    @Override
    public void onRefresh() {
        new PostSyncTask(this.getActivity(),discussionid).execute(""); //refreshes posts
    }

    private boolean isConnected(){

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

}
