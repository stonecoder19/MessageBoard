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
import com.swengroup6.messageboard.fragments.DiscussionActivityFragment;
import com.swengroup6.messageboard.fragments.ForumFragment;
import com.swengroup6.messageboard.sync.DiscussionSync;

import org.json.JSONObject;

public class DiscussionActivity extends AppCompatActivity {

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_discussion);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String name = getIntent().getExtras().getString("forumname");
        final int forumid = getIntent().getExtras().getInt("forumid");

        getSupportActionBar().setTitle(name);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();


                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(view.getContext());
                View promptsView = li.inflate(R.layout.dialog_add_discussion, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        view.getContext());

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText topicInput = (EditText) promptsView
                        .findViewById(R.id.editTextDiscussionName);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                               /* new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        //result.setText(userInput.getText());
                                        String name = topicInput.getText().toString();


                                        if(!(name.equals(""))){
                                            sendJsonDiscussion(name, forumid);
                                        }else{
                                            Toast.makeText(ctxt,)
                                        }

                                    }
                                }*/null)
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
                                String name = topicInput.getText().toString(); //gets topic of discussion


                                if(!(name.equals(""))){
                                    sendJsonDiscussion(name, forumid); //creates discussion and sends it to server
                                    alertDialog.dismiss();
                                }else{
                                    Toast.makeText(view.getContext(),"Discussion must have a topic",Toast.LENGTH_SHORT).show();
                                }

                                //Dismiss once everything is OK.

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

    private void sendJsonDiscussion(String name,int forumid){
        try {
            JSONObject json = new JSONObject();
            json.put("topic", name);
            json.put("forum",new Integer(forumid));

            new PostDiscussionTask(this,json.toString()).execute("");

        }catch(Exception e){

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

    private class PostDiscussionTask extends AsyncTask<String,Integer,Boolean> {

        Context ctxt;
        String json;

        public PostDiscussionTask(Context ctxt,String json){
            this.ctxt = ctxt;
            this.json = json;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ctxt,"Adding Discussion","Please wait..");
            progressDialog.setIndeterminate(true);
            progressDialog.setCanceledOnTouchOutside(true);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return new DiscussionSync().syncPostDiscussion(json);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();

            if(result){
                Toast.makeText(ctxt, "Successful", Toast.LENGTH_SHORT).show();
                DiscussionActivityFragment fragment = (DiscussionActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                fragment.updateDiscussionList();

            }else{
                Toast.makeText(ctxt,"Failed",Toast.LENGTH_SHORT).show();
            }

        }





    }






}
