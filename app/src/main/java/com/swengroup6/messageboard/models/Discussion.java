package com.swengroup6.messageboard.models;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Created by Matthew on 11/6/2015.
 */
public class Discussion extends SugarRecord<Discussion> {

    @SerializedName("id")
    int discussionid; //id of the discussion

    @SerializedName("forum")
    int forumid; //id of the forum related to discussion

    @SerializedName("topic")
    String discussion_topic;  //topic of the discussion

    @SerializedName("firstpostauthor")
    String firstpostname;  //name of the person who wrote the first post of the dscussion

    @SerializedName("lastpostauthor")
    String lastpostname; //name of person who rote the last post of the discussion

    @SerializedName("lastposttime")
    String lastposttime; //time when last post was created

    @SerializedName("postnumber")
    int postnumber;  //number of posts in discussion

    public Discussion(){

    }

    /**
     * initalzes instance variables
     * @param discussionid
     * @param forumid
     * @param discussion_topic
     * @param firstpostname
     * @param lastpostname
     * @param lastposttime
     */
    public Discussion(int discussionid,int forumid,String discussion_topic,String firstpostname,String lastpostname,String lastposttime){
        this.discussionid = discussionid;
        this.forumid = forumid;
        this.discussion_topic = discussion_topic;
        this.firstpostname = firstpostname;
        this.lastpostname = lastpostname;
        this.lastposttime = lastposttime;
    }

    /**
     * gets the id of the forum
     * @return forumid
     */
    public int getForumid() {
        return forumid;
    }

    /**
     * sets the id of the forum
     * @param forumid
     */

    public void setForumid(int forumid) {
        this.forumid = forumid;
    }

    /**
     * returns the id of the discussion
     * @return discussionid
     */

    public int getDiscussionid() {
        return discussionid;
    }

    /**
     * sets the id of the discussion
     * @param discussionid
     */

    public void setDiscussionid(int discussionid) {
        this.discussionid = discussionid;
    }

    /**
     * returns the topic of the discussion
     * @return discussion_topic
     */

    public String getDiscussion_topic() {
        return discussion_topic;
    }

    /**
     * sets the topic of the discussion
     * @param discussion_topic
     */

    public void setDiscussion_topic(String discussion_topic) {
        this.discussion_topic = discussion_topic;
    }

    /**
     * gets the name of person who made first post
     * @return firstpostname
     */

    public String getFirstpostname() {
        return firstpostname;
    }

    /**
     * sets the name of person who first posted
     * @param firstpostname
     */

    public void setFirstpostname(String firstpostname) {
        this.firstpostname = firstpostname;
    }

    /**
     * gets the name of the person who last posted
     * @return lastpostname
     */

    public String getLastpostname() {
        return lastpostname;
    }

    /**
     * sets the name of person who last posted
     * @param lastpostname
     */

    public void setLastpostname(String lastpostname) {
        this.lastpostname = lastpostname;
    }

    /**
     * gets the time of the last post
     * @return lastposttime
     */

    public String getLastposttime() {
        return lastposttime;
    }

    /**
     * sets the time of the last post
     * @param lastposttime
     */

    public void setLastposttime(String lastposttime) {
        this.lastposttime = lastposttime;
    }

    /**
     * gets the post number
     * @return postnumber
     */

    public int getPostnumber() {
        return postnumber;
    }
}
