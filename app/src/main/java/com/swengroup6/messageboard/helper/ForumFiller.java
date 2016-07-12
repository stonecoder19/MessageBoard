package com.swengroup6.messageboard.helper;

import com.swengroup6.messageboard.models.Forum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 11/6/2015.
 */
public class ForumFiller {


    List<Forum>forumList = new ArrayList<Forum>();
    public ForumFiller(){

        initFroumList();
    }

    private void initFroumList(){
        Forum forum1 = new Forum(1,"News Forum","This forum deals with important news announcements");

        forumList.add(forum1);

        Forum forum2 = new Forum(2,"Social Events","Thi forum deals with social events and parties");

        forumList.add(forum2);

        Forum forum3 = new Forum(3,"Advetisement","This forum is used for anyone who wants to advertise something");

        forumList.add(forum3);

        Forum forum4 = new Forum(4,"Hall Announcements","This forum is used for halls to get the word out to their hall members");

        forumList.add(forum4);
    }


    public List<Forum> getForumList() {
        return forumList;
    }


    public Forum getForumById(int forumid){

        for(int i=0;i<forumList.size();i++){
            if(forumList.get(i).getForumid() == forumid){
                return forumList.get(i);
            }
        }

        return null;
    }
}
