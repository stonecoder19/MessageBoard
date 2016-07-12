package com.swengroup6.messageboard.restcalls;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.swengroup6.messageboard.helper.Constants;
import com.swengroup6.messageboard.helper.GsonExclude;
import com.swengroup6.messageboard.models.Discussion;
import com.swengroup6.messageboard.models.Post;

import java.io.InputStream;
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
public class RestPost {

    String host = Constants.HOSTURL; //host url for message board app
    String route = "/posts/"; //route for discussions

    public RestPost(){


    }

    /**
     * takes id of discussion and gets all the posts related that discussion using a GET request to the server
     * @param discusionid
     * @return list of posts
     * @throws Exception
     */
    public List<Post> getPosts(int discusionid) throws Exception{

        List<Post>posts = new ArrayList<>();

        URL url = new URL(host+route+discusionid+"/"); //creates url to connect to server

        HttpURLConnection conn = (HttpURLConnection)url.openConnection(); //opens connection to server

        conn.setRequestMethod("GET"); //sets the request method to a GET request

        Reader reader = new InputStreamReader(conn.getInputStream()); //creates a new input stream

        GsonExclude exclude = new GsonExclude(); //speed up parsing process of json

        Gson gson = new GsonBuilder().addSerializationExclusionStrategy(exclude)
                        .addDeserializationExclusionStrategy(exclude).create();

        //converts json to Post object
        posts = gson.fromJson(reader,new TypeToken<List<Post>>(){}.getType());


        return posts;
    }

    /**
     * takes json of post to be sent to the server and uses POST request to send it and returns response as Post object
     * @param json
     * @return reply_post
     * @throws Exception
     */
    public Post postPost(String json) throws Exception{


        Post reply_post;
        URL url = new URL(host+route); //creates url to connect to server

        HttpURLConnection conn = (HttpURLConnection)url.openConnection(); //opens connection to server


        conn.setRequestMethod("POST");  //sets the request method to a POST request

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");

        GsonExclude exlude = new GsonExclude(); //speed up parsing process of json

        Gson gson = new GsonBuilder().addDeserializationExclusionStrategy(exlude)
                .addSerializationExclusionStrategy(exlude).create();


        OutputStreamWriter wr= new OutputStreamWriter(conn.getOutputStream()); //creates output stream
        wr.write(json); //writes json to output strean
        wr.flush();
        wr.close(); //closes output stream

        Reader reader = new InputStreamReader(conn.getInputStream()); //creates input stream to accept data from server




        //converts json to Post object
        reply_post = gson.fromJson(reader,Post.class);

        reader.close(); //close the input stream


        return reply_post;




    }
}
