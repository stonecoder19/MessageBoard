package com.swengroup6.messageboard.sync;

import com.swengroup6.messageboard.models.Discussion;
import com.swengroup6.messageboard.restcalls.RestDiscussion;

import java.util.List;

/**
 * Created by Matthew on 11/19/2015.
 */
public class DiscussionSync {

    public DiscussionSync(){

    }

    /**
     * takes id of forum and performs GET requst to get forums from server and saves it to sqlite database on phone
     * @param forumid
     * @return
     */
    public boolean syncDiscussion(int forumid){

        RestDiscussion restDiscussion = new RestDiscussion();

        List<Discussion> discussionList;

        try {

            discussionList = restDiscussion.getDiscussions(forumid); //gets discussions from forums
        }catch(Exception e){
            return false;
        }

        if(discussionList == null){
            return false;
        }


        if(discussionList.size()==0){ //checks if there are no discussions
            return false;
        }



        List<Discussion> saved_discussions;

        for(int i=0;i<discussionList.size();i++){
            Discussion discussion = discussionList.get(i);

            saved_discussions = Discussion.find(Discussion.class,"discussionid = ?",discussion.getDiscussionid()+""); //checks if there any discussions matching the current discussion

            if(saved_discussions.size()>0){
                discussion.setId(saved_discussions.get(0).getId()); //to avoid duplication
            }

            discussion.save(); //saves discussion to database
        }

        return true;
    }

    public boolean syncPostDiscussion(String json){

        RestDiscussion restDiscussion = new RestDiscussion();

        Discussion discussion;

        try{
            discussion = restDiscussion.postDiscussion(json); //sends discussion to server as json
        }catch(Exception e){
            return false;
        }

        if(discussion == null){ //if server failed to reply
            return false;
        }

        List<Discussion> saved_discuss;

        saved_discuss = Discussion.find(Discussion.class,"discussionid = ?",discussion.getDiscussionid()+""); //list of discussions that matches the current discussion

        if(saved_discuss.size()>0){
            discussion.setId(saved_discuss.get(0).getId()); //to avoid duplication
        }

        discussion.save(); //store discussion to database

        return true;
    }
}
