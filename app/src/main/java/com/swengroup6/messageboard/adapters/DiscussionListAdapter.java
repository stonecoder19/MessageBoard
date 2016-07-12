package com.swengroup6.messageboard.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.swengroup6.messageboard.activities.PostActivity;
import com.swengroup6.messageboard.helper.DateTimeParser;
import com.swengroup6.messageboard.models.Discussion;
import com.swengroup6.messageboard.models.Forum;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Matthew on 11/6/2015.
 */
public class DiscussionListAdapter extends RecyclerView.Adapter <DiscussionListAdapter.DiscussionViewHolder> {


    public static class DiscussionViewHolder extends RecyclerView.ViewHolder{

        TextView discussion_topic;
        TextView startname;
        TextView lastpostname;
        TextView lastposttime;
        ImageView discussion_img;

        public DiscussionViewHolder(View itemView) {
            super(itemView);

            discussion_topic = (TextView)itemView.findViewById(R.id.discussion_topicname);
            startname = (TextView)itemView.findViewById(R.id.startname);
            lastpostname = (TextView)itemView.findViewById(R.id.lastpostname);
            lastposttime = (TextView)itemView.findViewById(R.id.lastpostime);
            discussion_img = (ImageView)itemView.findViewById(R.id.discussionImg);
        }
    }

    private List<Discussion> discussionList;
    private Context ctxt;
    String filter = "";

    public DiscussionListAdapter(List<Discussion>discussionList,Context ctxt){
        this.discussionList = new ArrayList<>(discussionList);
        this.ctxt = ctxt;

    }

    @Override
    public DiscussionViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_discussion_item,parent,false);

        final DiscussionViewHolder discussionViewHolder = new DiscussionViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = discussionViewHolder.getAdapterPosition();
                Intent intent = new Intent(parent.getContext(), PostActivity.class);
                intent.putExtra("discussionid",discussionList.get(position).getDiscussionid());
                intent.putExtra("discuss_topic",discussionList.get(position).getDiscussion_topic());
                parent.getContext().startActivity(intent);
            }
        });

        return discussionViewHolder;
    }

    @Override
    public void onBindViewHolder(DiscussionViewHolder holder, int position) {

        Discussion discussion = discussionList.get(position);

        String discussionTopic = discussion.getDiscussion_topic();

        if(discussionTopic!=null){
            holder.discussion_topic.setText(discussionTopic);
        }

        String startname = discussion.getFirstpostname();

        if(startname!=null){
            holder.startname.setText(startname);
        }

        String lastpostname = discussion.getLastpostname();

        if(lastpostname!=null){
            holder.lastpostname.setText(lastpostname);
        }



        String lastposttime = discussion.getLastposttime();
        /*try{
            SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
            String lDate=sf.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lastposttime));
        }catch(Exception e){

        }*/



        if(lastposttime!=null){
            holder.lastposttime.setText(DateTimeParser.parseDateTime(lastposttime));
        }

        /*char firstLetter = discussionTopic.toUpperCase().charAt(0);
        //String strColor = firstLetter == 'P' ? "#2196F3" : "#E91E63";
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color2 = generator.getColor(discussionTopic);
        TextDrawable drawable2 = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .toUpperCase()
                .endConfig()
                .buildRound(firstLetter + "", color2);

        holder.discussion_img.setImageDrawable(drawable2);*/


    }

    @Override
    public int getItemCount() {
        return discussionList.size();
    }

    public void updateDiscussions(List<Discussion> newDiscussions){
        this.discussionList = new ArrayList<>(newDiscussions);
        notifyDataSetChanged();
    }

    public void animateTo(List<Discussion> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Discussion> newModels) {
        for (int i = discussionList.size() - 1; i >= 0; i--) {
            final Discussion model = discussionList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Discussion> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Discussion model = newModels.get(i);
            if (!discussionList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Discussion> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Discussion model = newModels.get(toPosition);
            final int fromPosition = discussionList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Discussion removeItem(int position) {
        final Discussion model = discussionList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Discussion model) {
        discussionList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Discussion model = discussionList.remove(fromPosition);
        discussionList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}
