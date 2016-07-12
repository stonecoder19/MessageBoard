package com.swengroup6.messageboard.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.swengroup6.messageboard.R;
import com.swengroup6.messageboard.activities.DiscussionActivity;
import com.swengroup6.messageboard.models.Forum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew on 11/6/2015.
 */
public class ForumListAdapter extends RecyclerView.Adapter<ForumListAdapter.ForumViewHolder> {



    public static class ForumViewHolder extends RecyclerView.ViewHolder{

        TextView forum_name;
        TextView forum_desc;
        ImageView forum_img;

        public ForumViewHolder(View itemView) {
            super(itemView);
            forum_name = (TextView)itemView.findViewById(R.id.forum_name);
            forum_desc = (TextView)itemView.findViewById(R.id.forum_desc);
            forum_img = (ImageView)itemView.findViewById(R.id.forum_img);
        }
    }

    List<Forum>forumList;
    Context ctxt;
    String filter = "";

    public ForumListAdapter(List<Forum> forumList,Context ctxt){
        this.forumList = new ArrayList<>(forumList);
        this.ctxt = ctxt;

    }


    @Override
    public ForumViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_forum_item, parent, false);

        final ForumViewHolder forumViewHolder = new ForumViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = forumViewHolder.getAdapterPosition();
                Intent intent = new Intent(parent.getContext(), DiscussionActivity.class);
                intent.putExtra("forumid",forumList.get(position).getForumid());
                intent.putExtra("forumname",forumList.get(position).getName());
                parent.getContext().startActivity(intent);
            }
        });

        return forumViewHolder;

    }

    @Override
    public void onBindViewHolder(ForumViewHolder holder, int position) {



        Forum forum = forumList.get(position);



        String forumname = forum.getName();

        int startPos = forumname.toLowerCase().indexOf(filter.toLowerCase());
        int endPos = startPos+filter.length();

        if(startPos!=-1){
            Spannable spannable  = new SpannableString(forumname);
            ColorStateList blueColor = new ColorStateList(new int[][] { new int[] {}}, new int[] { Color.BLUE });
            TextAppearanceSpan higlhightSpan = new TextAppearanceSpan(null,Typeface.BOLD,-1,blueColor,null);

            spannable.setSpan(higlhightSpan,startPos,endPos,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.forum_name.setText(spannable);

        }else{
            if (forumname != null) {
                holder.forum_name.setText(forumname);
            }
        }



        String forumdesc = forum.getDescription();
        if (forumdesc != null) {
            holder.forum_desc.setText(forumdesc);
        }

        char firstLetter = forumname.toUpperCase().charAt(0);
        //String strColor = firstLetter == 'P' ? "#2196F3" : "#E91E63";
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color2 = generator.getColor(forumname);
        TextDrawable drawable2 = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .toUpperCase()
                .endConfig()
                .buildRound(firstLetter + "", color2);

        holder.forum_img.setImageDrawable(drawable2);

    }

    @Override
    public int getItemCount() {
        return forumList.size();
    }

    public void updateForumList(List<Forum>newForumList){
        this.forumList = new ArrayList<>(newForumList);
        notifyDataSetChanged();
    }

    public void animateTo(List<Forum> models,String filter) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
        this.filter = filter;
    }

    private void applyAndAnimateRemovals(List<Forum> newModels) {
        for (int i = forumList.size() - 1; i >= 0; i--) {
            final Forum model = forumList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Forum> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Forum model = newModels.get(i);
            if (!forumList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Forum> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Forum model = newModels.get(toPosition);
            final int fromPosition = forumList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Forum removeItem(int position) {
        final Forum model = forumList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Forum model) {
        forumList.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Forum model = forumList.remove(fromPosition);
        forumList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }




}
