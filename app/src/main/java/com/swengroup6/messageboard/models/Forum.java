package com.swengroup6.messageboard.models;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;

/**
 * Created by Matthew on 11/6/2015.
 */
public class Forum extends SugarRecord<Forum> {

    @SerializedName("id")
    int forumid; //database id of forum model

    @SerializedName("name")
    String name;  // name of the forum

    @SerializedName("description")
    String description; //description of the forum

    public Forum(){

    }


    public Forum(int id,String name,String description){
        this.forumid = id;
        this.name = name;
        this.description = description;
    }

    /**
     * returns id if the forum
     * @return formuid
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
     * returns the name of the forum
     * @return name
     */

    public String getName() {
        return name;
    }

    /**
     * sets the name of the forum
     * @param name
     */

    public void setName(String name) {
        this.name = name;
    }

    /**
     * returns the description of the forum
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets the description of the forum
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
