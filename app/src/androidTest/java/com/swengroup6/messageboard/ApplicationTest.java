package com.swengroup6.messageboard;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.test.suitebuilder.annotation.SmallTest;


import com.swengroup6.messageboard.helper.Connect;
import com.swengroup6.messageboard.models.Discussion;
import com.swengroup6.messageboard.models.Forum;
import com.swengroup6.messageboard.models.Post;
import com.swengroup6.messageboard.restcalls.RestDiscussion;
import com.swengroup6.messageboard.restcalls.RestForum;
import com.swengroup6.messageboard.restcalls.RestPost;

import junit.framework.TestResult;

import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    String testForum = "{'name':'TestForum','description':'This is just a test'}";
    String testDiscussion = "{'forum':1,'title':'TestDiscussion'}";
    String testPost = "{'discussion':2,'author':'Test Name','title':'Test Title','message:','Test message'}";
    public ApplicationTest() {
        super(Application.class);


    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /*@SmallTest
    public void testJsonAddForum(){
        RestForum restForum = new RestForum();
        Forum forum = null;
        try{
            forum = restForum.postForum(testForum);
        }catch(Exception e){

        }
        assertNotNull(forum);
    }

    @SmallTest
    public void testJsonAddDiscussion(){
        RestDiscussion restDiscussion = new RestDiscussion();
        Discussion discussion = null;
        try{
            discussion = restDiscussion.postDiscussion(testDiscussion);
        }catch(Exception e){

        }

        assertNotNull(discussion);
    }

    @SmallTest
    public void testJsonAddPost(){
        RestPost restPost = new RestPost();
        Post post = null;
        try{
            post = restPost.postPost(testPost);
        }catch(Exception e){

        }

        assertNotNull(post);
    }*/

    @SmallTest
    public void testgetForums(){
        List<Forum> forums = null;
        RestForum restForum = new RestForum();

        try{
            forums = restForum.getForums();
        }catch(Exception e){

        }
        assertNotNull(forums);
    }

    @SmallTest
    public void testgetDiscussions(){
        List<Discussion> discussions = null;
        RestDiscussion restDiscussion = new RestDiscussion();

        try{
            discussions = restDiscussion.getDiscussions(2);
        }catch(Exception e){

        }
        assertNotNull(discussions);
    }

    @SmallTest
    public void testgetPosts(){
        List<Post> posts = null;
        RestPost restPost = new RestPost();

        try{
            posts = restPost.getPosts(2);
        }catch(Exception e){

        }
        assertNotNull(posts);
    }

    @SmallTest
    public void testConnectivity(){
        assertTrue(Connect.isConnected(getContext()));
    }

    @SmallTest
    public void testInternetConnectivity(){
        boolean internet = false;

        try{
            internet = Connect.haveInternetConnectivity();
        }catch(Exception e){
            internet = false;
        }
        assertTrue(internet);
    }






    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}