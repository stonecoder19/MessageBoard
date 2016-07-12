package com.swengroup6.messageboard.models;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Created by Matthew on 11/6/2015.
 */
public class Post extends SugarRecord<Post> {

    @SerializedName("id")
    int postid; //id of the post

    @SerializedName("discussion")
    int discussionid; //id of the discussion

    @SerializedName("title")
    String title; //title of the discussion

    @SerializedName("author")
    String author; //author of the discussion

    @SerializedName("message")
    String message; //post content

    @SerializedName("created")
    String date; //time post was created


    public Post(){

    }

    /**
     * initializes the instance variables
     * @param id
     * @param discussionid
     * @param title
     * @param author
     * @param date
     */
    public Post(int id,int discussionid,String title,String author,String date){
        this.postid = id;
        this.title = title;
        this.author = author;
        this.date = date;
        this.discussionid = discussionid;

    }


    /**
     * returns the id of the post
     * @return postid
     */
    public int getPostid() {
        return postid;
    }

    /**
     * sets the id of the post
     * @param postid
     */
    public void setPostid(int postid) {
        this.postid = postid;
    }

    /**
     * returns the title of the post
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * sets the title of the post
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * returns the author of the post
     * @return author
     */

    public String getAuthor() {
        return author;
    }

    /**
     * sets the author of the post
     * @param author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * returns the date that post was created
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * sets the date that post was created
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * returns the content of the post
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * sets the content of the post
     * @param message
     */

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * returns id of discussion linked to post
     * @return discussionid
     */

    public int getDiscussionid() {
        return discussionid;
    }

    /**
     * sets the id of discussion
     * @param discussionid
     */
    public void setDiscussionid(int discussionid) {
        this.discussionid = discussionid;
    }
}
