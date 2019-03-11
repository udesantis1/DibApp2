package com.example.firebaseapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class PastClasses extends AppCompatActivity {

    private RecyclerView courses_list_view;
    private List<Course> course_list;

    private FirebaseFirestore firebaseFirestore;
    private CourseRecyclerAdapter courseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_classes);

        course_list = new ArrayList<>();
        courses_list_view = findViewById(R.id.courses_list_view);

        courseRecyclerAdapter = new CourseRecyclerAdapter(course_list);
        courses_list_view.setLayoutManager(new LinearLayoutManager(this));
        courses_list_view.setAdapter(courseRecyclerAdapter);
        courses_list_view.setHasFixedSize(true);

        firebaseFirestore = FirebaseFirestore.getInstance();
        //SnapshotListener help to retrieve data in real time
        firebaseFirestore.collection("Courses").orderBy("courseName").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges())
                {
                    //check if data is edited
                    if(doc.getType() == DocumentChange.Type.ADDED){

                        String courseID = doc.getDocument().getId();
                        Course course = doc.getDocument().toObject(Course.class).withId(courseID);
                        course_list.add(course);

                        courseRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

}
