package com.swengroup6.messageboard.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.swengroup6.messageboard.R;
import com.swengroup6.messageboard.helper.DateTimeParser;
import com.swengroup6.messageboard.models.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 11/6/2015.
 */
public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostViewHolder>{


    public static class PostViewHolder extends RecyclerView.ViewHolder{

        TextView post_author;
        TextView post_title;
        TextView post_date;
        TextView post_message;
        ImageView post_image;
        public PostViewHolder(View itemView) {
            super(itemView);

            post_author = (TextView)itemView.findViewById(R.id.postUser);
            post_date = (TextView) itemView.findViewById(R.id.postDate);
            post_message = (TextView)itemView.findViewById(R.id.postMessage);
            post_image = (ImageView)itemView.findViewById(R.id.postImage);
            post_title = (TextView)itemView.findViewById(R.id.postTitle);
        }
    }

    Context ctxt;
    List<Post>posts;
    public PostListAdapter(Context ctxt,List<Post> posts){
        this.ctxt = ctxt;
        this.posts = posts;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post_item,parent,false);
        PostViewHolder postViewHolder = new PostViewHolder(view);
        return postViewHolder;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {

        Post post = posts.get(position);

        String author = post.getAuthor();

        if(author!=null){
            holder.post_author.setText(author);
        }

        String posttitle = post.getTitle();

        if(posttitle!=null){
            holder.post_title.setText(posttitle);
        }

        String postmessage = post.getMessage();

        if(postmessage!=null){
            holder.post_message.setText(postmessage);
        }

        String postdate = post.getDate();

        if(postdate!=null){
            holder.post_date.setText(DateTimeParser.parseDateTime(postdate));
        }

        char firstLetter = author.toUpperCase().charAt(0);
        //String strColor = firstLetter == 'P' ? "#2196F3" : "#E91E63";
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color2 = generator.getColor(author);
        TextDrawable drawable2 = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .toUpperCase()
                .endConfig()
                .buildRound(firstLetter + "", color2);

        holder.post_image.setImageDrawable(drawable2);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void updatePostList(List<Post> newPosts){
        this.posts = new ArrayList<>(newPosts);
        notifyDataSetChanged();
    }
}
