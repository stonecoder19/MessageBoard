package com.swengroup6.messageboard.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.swengroup6.messageboard.R;
import com.swengroup6.messageboard.fragments.ForumFragment;
import com.swengroup6.messageboard.helper.Constants;
import com.swengroup6.messageboard.models.Forum;
import com.swengroup6.messageboard.models.Post;
import com.swengroup6.messageboard.sync.ForumSync;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    List<Forum> forums= new ArrayList<>();
    private int isAdmin = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        isAdmin  = sharedPreferences.getInt(Constants.PREFS_LOGIN,0); //check if user is admin

        if(isAdmin!=0){
            getSupportActionBar().setTitle("Message Board-Admin");
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        if(isAdmin == 0){
            fab.setVisibility(View.GONE); //disable adding forums for non admin
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //  .setAction("Action", null).show();
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(view.getContext());
                View promptsView = li.inflate(R.layout.dialog_addforum, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        view.getContext());

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText forumnameInput = (EditText) promptsView
                        .findViewById(R.id.editTextForumName);

                final EditText forumdescInput = (EditText) promptsView
                        .findViewById(R.id.editTextForumDesc);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                /*new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // get user input and set it to result
                                        // edit text
                                        //result.setText(userInput.getText());
                                        String name = forumnameInput.getText().toString();
                                        String desc = forumdescInput.getText().toString();

                                        if (!(name.equals("") || desc.equals(""))) {
                                            sendJsonForum(name, desc);
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
                                String name = forumnameInput.getText().toString();
                                String desc = forumdescInput.getText().toString();

                                if (!(name.equals("") || desc.equals(""))) {
                                    sendJsonForum(name, desc);
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

    private void sendJsonForum(String name,String desc){
        try {
            JSONObject json = new JSONObject();
            json.put("name", name);
            json.put("description",desc);

            new PostForumTask(this,json.toString()).execute("");



        }catch(Exception e){

        }

    }

    private class PostForumTask extends AsyncTask<String,Integer,Boolean> {

        Context ctxt;
        String json;


        public PostForumTask(Context ctxt,String json){
            this.ctxt = ctxt;
            this.json = json;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ctxt, "Posting to Forum",
                    "Please wait..", true);
            progressDialog.setIndeterminate(true);
            progressDialog.setCanceledOnTouchOutside(true);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return new ForumSync().syncPostForum(json);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();

            if(result){
                Toast.makeText(ctxt, "Post successful", Toast.LENGTH_SHORT).show();
                ForumFragment fragment = (ForumFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                fragment.updateForumList();

            }else{
                Toast.makeText(ctxt,"Post failed",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.login:
                Intent intent = new Intent(this,LoginActivity.class);
                this.startActivity(intent);
                //Toast.makeText(this,"You clicked me",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                //Toast.makeText(this,"Log out",Toast.LENGTH_SHORT).show();
                Logout();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void Logout(){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(Constants.PREFS_LOGIN);
        editor.apply();
        Toast.makeText(this,"Logging out..",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,MainActivity.class);
        this.startActivity(intent);

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(isAdmin == 0) {
            inflater.inflate(R.menu.menu_main, menu);
        }else{
            inflater.inflate(R.menu.menu_admin, menu);
        }
        return true;
    }*/
}
