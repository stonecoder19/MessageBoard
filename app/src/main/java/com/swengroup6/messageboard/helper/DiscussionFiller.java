package com.swengroup6.messageboard.helper;

import com.swengroup6.messageboard.models.Discussion;
import com.swengroup6.messageboard.models.Forum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 11/6/2015.
 */
public class DiscussionFiller {

    List<Discussion> discussionList = new ArrayList<Discussion>();

    public DiscussionFiller(){

        initDiscussionList();
    }

    private void initDiscussionList(){

        Discussion discussion1 = new Discussion(1,1,"Lorem ipsum","Jake Randall","Mary Gillian","Sep 17 19:58");

        discussionList.add(discussion1);

        Discussion discussion2 = new Discussion(2,1,"New Classes To Be Built","UWI Mona","John Gill","Sep 18 20:38");

        discussionList.add(discussion2);

        Discussion discussion3 = new Discussion(3,1,"Graduation Postponed!!","UWI Mona","Jane Wright","Oct 18 17:45");

        discussionList.add(discussion3);

        Discussion discussion4 = new Discussion(4,2,"Lorem ipsum","Jake Randall","Mary Gillian","Sep 17 19:58");

        discussionList.add(discussion4);

        Discussion discussion5 = new Discussion(5,2,"New Classes To Be Built","UWI Mona","John Gill","Sep 18 20:38");

        discussionList.add(discussion5);

        Discussion discussion6 = new Discussion(6,2,"Graduation Postponed!!","UWI Mona","Jane Wright","Oct 18 17:45");

        discussionList.add(discussion6);

        Discussion discussion7 = new Discussion(7,3,"Lorem ipsum","Jake Randall","Mary Gillian","Sep 17 19:58");

        discussionList.add(discussion7);

        Discussion discussion8 = new Discussion(8,3,"New Classes To Be Built","UWI Mona","John Gill","Sep 18 20:38");

        discussionList.add(discussion8);

        Discussion discussion9 = new Discussion(9,3,"Graduation Postponed!!","UWI Mona","Jane Wright","Oct 18 17:45");

        discussionList.add(discussion9);

        Discussion discussion10 = new Discussion(10,4,"Lorem ipsum","Jake Randall","Mary Gillian","Sep 17 19:58");

        discussionList.add(discussion10);

        Discussion discussion11 = new Discussion(11,4,"New Classes To Be Built","UWI Mona","John Gill","Sep 18 20:38");

        discussionList.add(discussion11);

        Discussion discussion12 = new Discussion(12,4,"Graduation Postponed!!","UWI Mona","Jane Wright","Oct 18 17:45");

        discussionList.add(discussion12);

    }


    public List<Discussion> getAllDiscussions() {
        return discussionList;
    }

    public List<Discussion> getDiscussionList(int forumid){
        List<Discussion> discussions = new ArrayList<>();

        for(int i=0;i<discussionList.size();i++){
            if(discussionList.get(i).getForumid() == forumid){
                discussions.add(discussionList.get(i));
            }
        }
        return discussions;
    }



}
