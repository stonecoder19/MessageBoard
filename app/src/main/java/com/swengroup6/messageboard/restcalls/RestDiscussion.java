package com.swengroup6.messageboard.restcalls;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.swengroup6.messageboard.helper.Constants;
import com.swengroup6.messageboard.helper.GsonExclude;
import com.swengroup6.messageboard.models.Discussion;
import com.swengroup6.messageboard.models.Forum;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 11/19/2015.
 */
public class RestDiscussion {

    String hosturl = Constants.HOSTURL; //host url for message board app
    String route = "/discussions/"; //route for discussions

    public RestDiscussion(){

    }

    /**
     * takes a forumid an returns a list of discussions in that forum using a GET request to server
     * @param forumid
     * @return discussions
     * @throws Exception
     */
    public List<Discussion>getDiscussions(int forumid) throws Exception{

        List<Discussion> discussions = new ArrayList<>();

        URL url = new URL(hosturl+route+forumid+""+"/"); //creates a new url to connect to host server

        HttpURLConnection conn = (HttpURLConnection)url.openConnection(); //opens a connection to the server

        conn.setRequestMethod("GET"); //sets the request method as a GET request

        Reader reader = new InputStreamReader(conn.getInputStream()); //creates a new input stream to recieve data from server

        GsonExclude exlude = new GsonExclude(); //used for quicker parsing of json

        Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(exlude)
                        .addSerializationExclusionStrategy(exlude).create();


        //converts json to list of java objects
        discussions = gson.fromJson(reader,new TypeToken<List<Discussion>>(){}.getType());

        return discussions;

    }

    /**
     * takes a json strinf of post object and sends it to the server and returns a Discussion object response from the server
     * @param json
     * @return reply_discussion
     * @throws Exception
     */

    public Discussion postDiscussion(String json) throws Exception{


        Discussion reply_discussion;
        URL url = new URL(hosturl+route); //creates a new url to connect to host server

        HttpURLConnection conn = (HttpURLConnection)url.openConnection(); //opens a connection to the server


        conn.setRequestMethod("POST");//sets the request method as a POST request

        conn.setDoOutput(true); //send request body
        conn.setDoInput(true); //expect input
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json"); //tells server  that app is sending data in json format

        GsonExclude exlude = new GsonExclude();

        Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(exlude)
                .addSerializationExclusionStrategy(exlude).create();


        OutputStreamWriter wr= new OutputStreamWriter(conn.getOutputStream()); //creates a new output stream to write data to server
        wr.write(json); //sends the request body as json
        wr.flush(); //flushes the output stream
        wr.close();  //close the output stream

        Reader reader = new InputStreamReader(conn.getInputStream()); //creates a new input stream from data sent back from server


        //converts json response to Discussion object
        reply_discussion = gson.fromJson(reader,Discussion.class);

        reader.close(); //closes the input stream


        return reply_discussion;




    }
}
