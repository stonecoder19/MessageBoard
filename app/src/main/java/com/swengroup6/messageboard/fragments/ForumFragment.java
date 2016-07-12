package com.swengroup6.messageboard.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.swengroup6.messageboard.R;
import com.swengroup6.messageboard.adapters.ForumListAdapter;
import com.swengroup6.messageboard.helper.Constants;
import com.swengroup6.messageboard.helper.ForumFiller;
import com.swengroup6.messageboard.models.Forum;
import com.swengroup6.messageboard.restcalls.RestForum;
import com.swengroup6.messageboard.sync.ForumSync;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForumFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,SearchView.OnQueryTextListener {

    private RecyclerView forumList;
    private ForumListAdapter forumListAdapter;
    private List<Forum> forums;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;
    private MenuItem searchitem;
    private View view;
    int isAdmin;


    public ForumFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forum, container, false); //references layout view for fragment


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFS,Context.MODE_PRIVATE);

        isAdmin = sharedPreferences.getInt(Constants.PREFS_LOGIN,0);

        progressBar = (ProgressBar)view.findViewById(R.id.progressForum); //reference progress bar
        progressBar.setIndeterminate(true); //"loading amount" is not measured.

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this); //sets the current fragment as the on refresh listener
        swipeRefreshLayout.setColorSchemeResources(R.color.blue,R.color.orange,R.color.green); //sets colors of swipe refresh

        // ForumFiller forumFiller = new ForumFiller();
       // forumListAdapter = new ForumListAdapter(forumFiller.getForumList(),this.getActivity());

        forums = Forum.listAll(Forum.class); //gets all the forums from database

        if(forums.size()>0){ //if there are forums in the list
            progressBar.setVisibility(View.GONE); //hide progress bar
        }

        setUpRecyclerView(); //sets up recycler list view

        new ForumAsyncTask(this.getActivity()).execute(""); //refreshes the list content



        return view;
    }

    private void setUpRecyclerView(){
        forumList = (RecyclerView)view.findViewById(R.id.forumList); //references recycler view for forum list
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity()); //used to layout the list
        forumList.setLayoutManager(llm); //sets the layout manager
        forumList.setHasFixedSize(true);
        forumListAdapter = new ForumListAdapter(forums,this.getActivity());
        forumList.setAdapter(forumListAdapter); //sets the adapter for list (responsible for matching the models with the views)
    }

    private class ForumAsyncTask extends AsyncTask<String,Integer,Boolean>{

        Context ctxt;

        public ForumAsyncTask(Context ctxt){
            this.ctxt = ctxt;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(ctxt,"Loading Forums",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return new ForumSync().synForums(); //syncs content on server with phone
        }

        @Override
        protected void onPostExecute(Boolean result) {

            swipeRefreshLayout.setRefreshing(false); //disable swipe refresh

            progressBar.setVisibility(View.GONE);  //hide progress bar
            if(result){  //if the sync succeded
                updateForumList(); //updates the forum recycler view
                Toast.makeText(ctxt,"Sucsessful",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ctxt,"Loading failed",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if(isAdmin == 0){
            inflater.inflate(R.menu.menu_main, menu);
        }else{
            inflater.inflate(R.menu.menu_admin, menu);
        }

        Context context = getActivity();
        SearchManager searchManager  = (SearchManager)context.getSystemService(Context.SEARCH_SERVICE);

        searchitem = menu.findItem(R.id.search); //refrence search icon

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

    public void updateForumList(){
        forums = Forum.listAll(Forum.class); //retrieves all forums from database
        forumListAdapter.updateForumList(forums); //updates forum recycler view
    }

    @Override
    public void onRefresh() {

        new ForumAsyncTask(this.getActivity()).execute(""); //refreshes content
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<Forum> filteredModelList = filter(forums, query);
        forumListAdapter.animateTo(filteredModelList,query);
        forumList.scrollToPosition(0);
        return true;
    }

    /**
     * takes a query and returns a list of forums that match the query
     * @param models
     * @param query
     * @return
     */
    private List<Forum> filter(List<Forum> models, String query) {
        query = query.toLowerCase();
        final List<Forum> filteredModelList = new ArrayList<>();
        for (Forum model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
