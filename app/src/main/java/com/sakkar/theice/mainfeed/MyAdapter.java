package com.sakkar.theice.mainfeed;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sakkar.theice.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.support.v7.recyclerview.R.styleable.RecyclerView;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<Posts> list;
    private LayoutInflater inflater;
    Context context;
    Posts post;
    Operation operation;

    public MyAdapter(ArrayList<Posts> list,LayoutInflater inflater,Context context) {
        this.list = list;
        this.context=context;
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
        holder.setReceivedImage(post.getImageDownloadUri());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView,likes,unlikes,comments,date,name;
        ImageView receivedImage;
        LinearLayout like,unlike;
        public MyViewHolder(View itemView) {
            super(itemView);
            name= (TextView) itemView.findViewById(R.id.senderName);
            likes= (TextView) itemView.findViewById(R.id.likeCounter);
            unlikes= (TextView) itemView.findViewById(R.id.unlikeCounter);
            comments= (TextView) itemView.findViewById(R.id.commentCounter);
            date= (TextView) itemView.findViewById(R.id.senderDate);
            textView= (TextView) itemView.findViewById(R.id.content);
            receivedImage= (ImageView) itemView.findViewById(R.id.sendImage);
            like= (LinearLayout) itemView.findViewById(R.id.like);
            unlike= (LinearLayout) itemView.findViewById(R.id.unlike);
            like.setOnClickListener(this);
            unlike.setOnClickListener(this);
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

        public void setReceivedImage(String receivedImage) {
            if(receivedImage==null) {
                this.receivedImage.setImageDrawable(null);
                return;
            }
            Picasso.with(context).load(receivedImage).into(this.receivedImage);
        }

        @Override
        public void onClick(View view) {
            if(operation!=null) {
                switch (view.getId()) {
                    case R.id.like:
                        operation.like(getAdapterPosition());
                        break;
                    case R.id.unlike:
                        operation.unlike(getAdapterPosition());
                        break;
                    default:
                        break;
                }
            }
            else {
                Toast.makeText(context,"No listener added",Toast.LENGTH_LONG).show();
            }
        }
    }

    interface Operation{
        void like(int postNo);
        void unlike(int postNo);
    }
}
