package com.swengroup6.messageboard.sync;

import com.swengroup6.messageboard.models.Post;
import com.swengroup6.messageboard.restcalls.RestPost;

import java.util.List;

/**
 * Created by Matthew on 11/19/2015.
 */
public class PostSync  {


    public PostSync(){

    }

    public boolean syncPosts(int discussionid){
        RestPost restPost = new RestPost();

        List<Post> postList;

        try{
            postList = restPost.getPosts(discussionid);
        }catch(Exception e){
            return false;
        }

        if(postList.size() == 0){
            return false;
        }

        if(postList == null){
            return false;
        }

        List<Post> saved_posts;

        for(int i=0;i<postList.size();i++){
            Post post = postList.get(i);

            saved_posts = Post.find(Post.class,"postid = ?",post.getPostid()+"");

            if(saved_posts.size()>0){
                post.setId(saved_posts.get(0).getId());
            }

            post.save();
        }

        return true;
    }


    public boolean syncAddPosts(String json){

        RestPost restPost = new RestPost();

        Post post;

        try{
            post = restPost.postPost(json);
        }catch(Exception e){
            return false;
        }

        if(post == null){
            return false;
        }

        List<Post>saved_posts;

        saved_posts = Post.find(Post.class,"postid = ?",post.getPostid()+"");

        if(saved_posts.size()>0){
            post.setId(saved_posts.get(0).getId());
        }

        post.save();

        return true;
    }
}
