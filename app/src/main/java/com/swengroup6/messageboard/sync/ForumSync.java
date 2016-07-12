package com.swengroup6.messageboard.sync;

import com.swengroup6.messageboard.models.Forum;
import com.swengroup6.messageboard.restcalls.RestForum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 11/19/2015.
 */
public class ForumSync {


    public ForumSync(){

    }

    /**
     * gets forums from server and save them to sqlite database on phone
     * @return
     */
    public boolean synForums(){

        RestForum restForum = new RestForum();

        List<Forum> forums = new ArrayList<>();

        try {
          forums = restForum.getForums(); //performs request to server to get froums
        }catch(Exception e){
            return false;
        }

        if(forums == null){ //checks if there are no fourms
            return false;
        }

        if(forums.size()==0){
            return false;
        }

        List<Forum>saved_forums;

        for(int i=0;i<forums.size();i++){

            Forum forum = forums.get(i);

            saved_forums = Forum.find(Forum.class,"forumid = ?",forum.getForumid()+""); //gets a list of forums that match the current forum

            if(saved_forums.size()>0){
                forum.setId(saved_forums.get(0).getId()); //to avoid duplication of forums
            }

            forum.save(); //saves forum to database
        }
        return true;
    }

    /**
     * posts forum to server and saves single forum to database
     * @param json
     * @return
     */
    public boolean syncPostForum(String json){
        RestForum restForum = new RestForum();

        Forum forum;

        try {
            forum = restForum.postForum(json); //gets response of post request to posts route
        }catch(Exception e){
            return false;
        }

        if(forum == null){ //checks if server sent back anything
            return false;
        }

        List<Forum> saved_forums;

        saved_forums = Forum.find(Forum.class,"forumid = ?",forum.getForumid()+""); //gets forum that matches the current forum

        if(saved_forums.size()>0){
            forum.setId(saved_forums.get(0).getId()); //to avoid duplication of forum
        }

        forum.save(); //save forum to database

        return true;

    }


}
