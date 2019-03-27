package com.example.firebaseapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CourseRecyclerAdapter2 extends RecyclerView.Adapter<CourseRecyclerAdapter2.ViewHolder> {

    private List<Course> course_list;

    private Context context;
    private FirebaseFirestore firebaseFirestore;


    public CourseRecyclerAdapter2(List<Course> course_list)
    {
        this.course_list = course_list;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;

        private TextView course_name_view;

        private TextView time_text_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setName(String text)
        {
            course_name_view = mView.findViewById(R.id.course_text_view);
            course_name_view.setText(text);
        }

        public void setTime(String text)
        {
            time_text_view = mView.findViewById(R.id.time_text_view);
            time_text_view.setText(text);
        }

    }

    @NonNull
    @Override
    public CourseRecyclerAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.courses_list_item2, viewGroup, false);
        context = viewGroup.getContext();
        firebaseFirestore = FirebaseFirestore.getInstance();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseRecyclerAdapter2.ViewHolder viewHolder, int i) {

        final String courseId = course_list.get(i).CourseID;

        final String course_name = course_list.get(i).getCourseName();
        viewHolder.setName(course_name);

        final String course_time = course_list.get(i).getCourseTime();
        viewHolder.setTime(course_time);



    }

    @Override
    public int getItemCount() {
        return course_list.size();
    }
}
