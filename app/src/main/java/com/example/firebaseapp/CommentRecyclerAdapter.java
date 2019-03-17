package com.example.firebaseapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.ViewHolder>{

    private List<Comment> commentsList;
    public Context context;

    public CommentRecyclerAdapter(List<Comment> commentsList)
    {
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comments_list_item, viewGroup, false);
        context = viewGroup.getContext();
        return new CommentRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String commentMessage = commentsList.get(i).getMessage();
        viewHolder.setComment_message(commentMessage);

        try {
            java.text.DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String dateString = df.format(commentsList.get(i).getTimestamp().getTime());
            viewHolder.setComment_time(dateString);
        } catch (Exception e) {

            Toast.makeText(context, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public int getItemCount() {
        if(commentsList != null) {

            return commentsList.size();

        } else {

            return 0;

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView comment_message;
        private TextView comment_time;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setComment_message(String message)
        {
            comment_message = mView.findViewById(R.id.comment_message);
            comment_message.setText(message);
        }

        public void setComment_time(String date)
        {
            comment_time = mView.findViewById(R.id.comment_time);
            comment_time.setText(date);
        }
    }
}
