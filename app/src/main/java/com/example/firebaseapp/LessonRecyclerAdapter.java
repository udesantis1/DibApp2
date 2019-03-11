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

public class LessonRecyclerAdapter extends RecyclerView.Adapter<LessonRecyclerAdapter.ViewHolder> {

    public List<Lesson> lessonsList;
    public Context context;

    public LessonRecyclerAdapter(List<Lesson> lessonsList)
    {
        this.lessonsList = lessonsList;
    }

    @NonNull
    @Override
    public LessonRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lessons_list_item, viewGroup, false);
        context = viewGroup.getContext();
        return  new LessonRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonRecyclerAdapter.ViewHolder viewHolder, int i) {



        final String lessonId = lessonsList.get(i).LessonID;
        final String courseId = lessonsList.get(i).getCourseID();



        String lessonName = lessonsList.get(i).getLesson_name();
        viewHolder.setLesson_name(lessonName);

        viewHolder.lesson_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentsActivity.class);
                intent.putExtra("lesson_id", lessonId);
                intent.putExtra("course_id", courseId);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return lessonsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private View mView;
        private TextView lesson_name;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            mView = itemView;
        }

        public void setLesson_name(String name)
        {
            lesson_name = mView.findViewById(R.id.lesson_text_view);
            lesson_name.setText(name);
        }
    }
}
