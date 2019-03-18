package com.example.firebaseapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import android.widget.RatingBar;

import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public  class RatingRecyclerAdapter extends RecyclerView.Adapter<RatingRecyclerAdapter.ViewHolder> {

    private List<Rating> ratingList;
    public Context context;
    public RatingRecyclerAdapter(List<Rating> ratingList)
    {
        this.ratingList = ratingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rating_list_items, viewGroup, false);
        context = viewGroup.getContext();
        return new RatingRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String  ratingMessage= String.valueOf(ratingList.get(i).getValuee());
        //String commentMessage = String.valueOf(ratingList.get(i).getValuee());
        viewHolder.setComment_message(ratingMessage);

    }
    @Override
    public int getItemCount() {
        if(ratingList != null) {

            return ratingList.size();

        } else {

            return 0;

        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder   {

        private View mView;
        private TextView rating_message;
        RatingBar rbRating;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;




        }

        public void setComment_message(String    message)
        {
            rating_message = mView.findViewById(R.id.rating_message);
            rating_message.setText(message);
        }




    }
}





