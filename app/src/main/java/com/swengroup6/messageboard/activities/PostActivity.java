package com.swengroup6.messageboard.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.swengroup6.messageboard.R;
import com.swengroup6.messageboard.fragments.ForumFragment;
import com.swengroup6.messageboard.fragments.PostActivityFragment;
import com.swengroup6.messageboard.sync.PostSync;

import org.json.JSONObject;

public class PostActivity extends AppCompatActivity {


    int discussionid;
    String discussion_topic;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        discussionid = bundle.getInt("discussionid");
        discussion_topic = bundle.getString("discuss_topic");

        getSupportActionBar().setTitle(discussion_topic);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater li = LayoutInflater.from(view.getContext());
                View promptsView = li.inflate(R.layout.dialog_add_post, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        view.getContext());

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText editTextUser = (EditText)promptsView.findViewById(R.id.editTextPostUser);
                //final EditText editTextTitle = (EditText)promptsView.findViewById(R.id.editTextPostTitle);
                final EditText editTextMessage = (EditText)promptsView.findViewById(R.id.editTextPostMessage);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                null)
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                final AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialog) {

                        Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                // TODO Do something
                                String postuser = editTextUser.getText().toString().trim();  //gets name of user
                                String postmessage = editTextMessage.getText().toString().trim();  //gets content of post

                                if(!(postuser.equals("") ||  postmessage.equals(""))){
                                    sendJsonPost(postuser,postmessage); //creates post and sends it to server
                                    alertDialog.dismiss();
                                }else{
                                    Toast.makeText(view.getContext(),"All fields must be filled",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                });

                // show it
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();

            }

        });

    }


    private void sendJsonPost(String postuser,String postmessage){
        try {
            JSONObject json = new JSONObject();
            json.put("discussion",new Integer(discussionid));
            json.put("author", postuser);
            json.put("message",postmessage);

            new PostForumTask(this,json.toString()).execute("");



        }catch(Exception e){

        }

    }

    private class PostForumTask extends AsyncTask<String,Integer,Boolean>
    {
        Context ctxt;
        String json;

        public PostForumTask(Context ctxt,String json){
            this.ctxt = ctxt;
            this.json = json;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ctxt,"Adding Post","Please wait");
            progressDialog.setIndeterminate(true);
            progressDialog.setCanceledOnTouchOutside(true);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return new PostSync().syncAddPosts(json);
        }

        @Override
        protected void onPostExecute(Boolean result) {

            progressDialog.dismiss();

            if(result){
                Toast.makeText(ctxt,"Successful",Toast.LENGTH_SHORT).show();
                PostActivityFragment fragment = (PostActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                fragment.updatePostList();
            }else{
                Toast.makeText(ctxt,"Failed",Toast.LENGTH_SHORT).show();
            }


        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
