package com.swengroup6.messageboard.helper;

import com.swengroup6.messageboard.models.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Matthew on 11/6/2015.
 */
public class PostFiller {

    private List<Post> postList = new ArrayList<>();

    public PostFiller(){
        initPostList();
    }

    private void initPostList(){
        String topic = getRandomString(10);
        String lastnames[] = {"Harrsion","James","Stone","Clarke","Robertson","Phillips","Jackson","Kent"};
        String firstnames[] = {"Mary","Jane","Jack","Romario","Phil","Stacy","Delora"};
        Random random = new Random();
        int wordsSize = 30;

        for(int i=0;i<36;i++) {
            String name = firstnames[random.nextInt(firstnames.length)] + " " + lastnames[random.nextInt(lastnames.length)];
            Post post = new Post(i + 1, (i / 3) + 1, topic, name, random.nextInt(30) + 1 + " Sep " + random.nextInt(24) + ":" + (random.nextInt(49) + 10));
            String message = "";
            if(i%3!=0){
                post.setTitle("Re:"+topic);
            }
            for (int j = 0; j < wordsSize; j++){
                message+=" "+getRandomString(random.nextInt(8)+3);
            }
            post.setMessage(message);
            postList.add(post);
        }
    }


    public List<Post> getAllPosts(){
        return postList;
    }

    public List<Post> findPostsByDiscussion(int discussionid){
        List<Post> discussPostList = new ArrayList<>();

        for(int i = 0;i<postList.size();i++){
            if(postList.get(i).getDiscussionid() == discussionid){
                discussPostList.add(postList.get(i));
            }
        }
        return discussPostList;
    }


    private static final String ALLOWED_CHARACTERS ="qwertyuiopasdfghjklzxcvbnm";

    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }


}
