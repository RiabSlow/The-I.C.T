package com.sakkar.theice.mainfeed;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sakkar.theice.R;

import java.util.ArrayList;

import static android.support.v7.recyclerview.R.styleable.RecyclerView;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<Posts> list;
    private LayoutInflater inflater;
    Posts post;

    public MyAdapter(ArrayList<Posts> list,LayoutInflater inflater) {
        this.list = list;
        this.inflater=inflater;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(inflater.inflate(R.layout.posts_design_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        post=list.get(position);
        holder.setContent(post.getPost());
        holder.setDate(post.getDate());
        //holder.setComments(""+post.getC());
        holder.setLikes(""+post.getLikes());
        holder.setUnlikes(""+post.getUnlikes());
        holder.setName(post.getAuthors());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView,likes,unlikes,comments,date,name;
        public MyViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.senderName);
            likes= (TextView) itemView.findViewById(R.id.likeCounter);
            unlikes= (TextView) itemView.findViewById(R.id.unlikeCounter);
            comments= (TextView) itemView.findViewById(R.id.commentCounter);
            date= (TextView) itemView.findViewById(R.id.senderDate);
            textView= (TextView) itemView.findViewById(R.id.content);
        }

        void setContent(String s){
            textView.setText(s);
        }


        public void setLikes(String likes) {
            this.likes.setText(likes);
        }

        public void setUnlikes(String unlikes) {
            this.unlikes.setText(unlikes);
        }

        public void setComments(String comments) {
            this.comments.setText(comments);
        }

        public void setDate(String date) {
            this.date.setText(date);
        }

        public void setName(String name) {
            this.name.setText(name);
        }
    }
}
