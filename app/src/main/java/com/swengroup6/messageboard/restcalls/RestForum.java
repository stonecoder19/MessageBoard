package com.swengroup6.messageboard.restcalls;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.swengroup6.messageboard.helper.Constants;
import com.swengroup6.messageboard.helper.GsonExclude;
import com.swengroup6.messageboard.models.Forum;
import com.swengroup6.messageboard.models.Post;

import java.io.IOException;
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
public class RestForum {

    String host = Constants.HOSTURL; //host url for message board app
    String route = "/forums/";  //route to get forums

    public RestForum(){

    }

    /**
     * performs a GET request on the server and returns a list of forums
     * @return list of fourms
     * @throws Exception
     */
    public List<Forum>  getForums() throws Exception{
        List<Forum>forums = new ArrayList<>();
        URL url = new URL(host+route); //creates a url to connect to server
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //opens connection to server

        conn.setRequestMethod("GET"); //sets the request method as GET

        Reader reader = new InputStreamReader(conn.getInputStream()); //creates a new input stream to accept data from server
        GsonExclude exclude = new GsonExclude(); //used to speed up parsing of json

        Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(exclude)
                .addSerializationExclusionStrategy(exclude).create();

        //converts json response to list of forum objects
        forums = gson.fromJson(reader, new TypeToken<List<Forum>>(){}.getType());

        reader.close(); //closes the input stream

        return forums;


    }

    /**
     * takes a json and sends it as a request body to the server susing POST equest and returns reply as a Forum object
     * @param json
     * @return reply_forum
     * @throws Exception
     */
    public Forum postForum(String json) throws Exception{


        Forum reply_forum; //forum that server sends back
        URL url = new URL(host+route); //creaes a new url to connect server

        HttpURLConnection conn = (HttpURLConnection)url.openConnection(); //opens connection to server


        conn.setRequestMethod("POST"); //sets request method as post

        conn.setDoOutput(true); //tells server to expect a request body
        conn.setDoInput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json"); //tells server to accept json

        GsonExclude exlude = new GsonExclude(); //used to speed up json parsing

        Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(exlude)
                .addSerializationExclusionStrategy(exlude).create();


        OutputStreamWriter wr= new OutputStreamWriter(conn.getOutputStream()); //creates output stream to send data to server
        wr.write(json); //writes json to the output stream
        wr.flush();
        wr.close(); //close output stream

        Reader reader = new InputStreamReader(conn.getInputStream()); //creates input stream  to accept data from server




        //converts json to Forum object
        reply_forum = gson.fromJson(reader,Forum.class);

        reader.close();


        return reply_forum;




    }




}
