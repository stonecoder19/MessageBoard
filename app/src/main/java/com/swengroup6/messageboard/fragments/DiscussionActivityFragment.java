package com.swengroup6.messageboard.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.swengroup6.messageboard.R;
import com.swengroup6.messageboard.adapters.DiscussionListAdapter;
import com.swengroup6.messageboard.helper.DiscussionFiller;
import com.swengroup6.messageboard.models.Discussion;
import com.swengroup6.messageboard.models.Forum;
import com.swengroup6.messageboard.sync.DiscussionSync;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class DiscussionActivityFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,SearchView.OnQueryTextListener{

    private RecyclerView discussionList;
    private DiscussionListAdapter discussionListAdapter;
    private List<Discussion> discussions = new ArrayList<>();
    private TextView txt_notpresent;
    private ImageView img_notpresent;
    int forumid;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private SearchView searchView;
    private MenuItem searchitem;
    ConnectivityManager cm;
    private View view;

    public DiscussionActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        cm =   (ConnectivityManager)this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_discussion, container, false);



        setUpSwipeRefresh();

        progressBar = (ProgressBar)view.findViewById(R.id.progressDiscussion);
        progressBar.setIndeterminate(true);

        txt_notpresent = (TextView)view.findViewById(R.id.txt_notpresent); //place holder text when the list is empty
        img_notpresent = (ImageView)view.findViewById(R.id.img_notpresnt); //place holder image when the list is empty

        forumid = getActivity().getIntent().getExtras().getInt("forumid"); //get the id of forum previous activity


       // DiscussionFiller discussionFiller = new DiscussionFiller();
       // discussions = discussionFiller.getDiscussionList(forumid);


        discussions = Discussion.find(Discussion.class,"forumid = ?",forumid+""); //retrieve all discussions matching the forumid

        if(discussions.size()>0){ //if the list is not empty
            progressBar.setVisibility(View.GONE); //hide the progress bar
        }

        if(discussions.size() == 0){ //if the list is empty
            txt_notpresent.setVisibility(View.VISIBLE); //show place holder text
            img_notpresent.setVisibility(View.VISIBLE); //show place holder image
        }

        setUpRecyclerView(); //sets up recycler view for discussion list


        new DiscussionAsync(forumid,this.getActivity()).execute(""); //refreshes discussions from server



        return view;
    }

    private void setUpSwipeRefresh(){
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swiperefresh); //references swipe refresh layout
        swipeRefreshLayout.setOnRefreshListener(this); //sets the refresh listener to the current fragment
        swipeRefreshLayout.setColorSchemeResources(R.color.blue,R.color.green,R.color.orange); //sets the swipe refresh colors
    }

    private void setUpRecyclerView(){

        discussionList = (RecyclerView)view.findViewById(R.id.discussionList); //references discussion
        discussionList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity()); //responsible for the layout of the recycler view
        discussionList.setLayoutManager(llm); //sets the layout of the recycler view
        discussionListAdapter = new DiscussionListAdapter(discussions,this.getActivity()); //adapter responsible for linking models to views
        discussionList.setAdapter(discussionListAdapter); //sets the adapter for the list
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);

        Context context = getActivity();
        SearchManager searchManager  = (SearchManager)context.getSystemService(Context.SEARCH_SERVICE);

        searchitem = menu.findItem(R.id.search); //reference search icon

        if(searchitem == null){
            Toast.makeText(context,"Search is null",Toast.LENGTH_SHORT).show();
        }
        searchView = (SearchView) MenuItemCompat.getActionView(searchitem); //set up search view


        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search name"); //sets the hint text

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.setQuery("", false); //clears text from search view
                return true;
            }
        });
    }

    private class DiscussionAsync extends AsyncTask<String,Integer,Boolean> {

        int forumid;
        Context ctxt;

        public DiscussionAsync(int forumid,Context ctxt){
            this.forumid = forumid;
            this.ctxt = ctxt;
        }
        @Override
        protected void onPreExecute() {
            Toast.makeText(ctxt,"Loading..",Toast.LENGTH_SHORT).show();
            txt_notpresent.setVisibility(View.GONE); //hides the place holder text
            img_notpresent.setVisibility(View.GONE); //hides place holder image

            if(swipeRefreshLayout.isRefreshing()){ //if swipe refresh is currently active
                progressBar.setVisibility(View.GONE); //hide the progress bar
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {

            if(!isConnected()) { //if not connected to network
                return false;
            }else{
                return new DiscussionSync().syncDiscussion(forumid); //sync discussions on phone with server
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {


            swipeRefreshLayout.setRefreshing(false); //disable swipe refresh
            progressBar.setVisibility(View.GONE); //hide the progress bar
            if(result){
                updateDiscussionList(); //udpdates the discussion list
                Toast.makeText(ctxt,"Loading succssesful",Toast.LENGTH_SHORT).show();

            }else{
                if(!isConnected()){ //if not connected to network
                    Toast.makeText(ctxt,"No Internet",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ctxt,"Loading failed",Toast.LENGTH_SHORT).show();
                }

                if(discussions.size() == 0){ //if the list is empty
                    txt_notpresent.setVisibility(View.VISIBLE); //show the place holder text
                    img_notpresent.setVisibility(View.VISIBLE); //show the place holder image
                }
            }
        }
    }

    public void updateDiscussionList(){
        discussions = Discussion.find(Discussion.class,"forumid = ?",forumid+""); //retrieve discussion match the forum id
        discussionListAdapter.updateDiscussions(discussions); //update the discussion list
        if(discussions.size()>0) { //if the list is empty
            txt_notpresent.setVisibility(View.GONE); //hide the place holder text
            img_notpresent.setVisibility(View.GONE); //hide the place holder image
        }

    }

    private boolean isConnected(){

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    @Override
    public void onRefresh() {

        Toast.makeText(this.getActivity(),"Refreshing..",Toast.LENGTH_SHORT).show();
        new DiscussionAsync(forumid,this.getActivity()).execute(""); //refrsh discussion list
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {

        final List<Discussion> filteredModelList = filter(discussions, query);
        discussionListAdapter.animateTo(filteredModelList); //show remove animation
        discussionList.scrollToPosition(0);
        return true;
    }

    /**
     * takes a query and returns a list of discussions matching that query
     * @param models
     * @param query
     * @return filteredModelList
     */
    private List<Discussion> filter(List<Discussion> models, String query) {
        query = query.toLowerCase();
        final List<Discussion> filteredModelList = new ArrayList<>();
        for (Discussion model : models) {
            final String text = model.getDiscussion_topic().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }


}
