package com.example.firebaseapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class LessonsActivity extends AppCompatActivity {

    private String course_id;
    private List<Lesson> lesson_list;
    private RecyclerView lessons_list_view;
    private TextView lessons_title;
    private LessonRecyclerAdapter lessonRecyclerAdapter;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);

        lesson_list = new ArrayList<>();

        course_id = getIntent().getStringExtra("course_id");
        lessons_list_view = findViewById(R.id.lessons_list_view);
        lessons_title = findViewById(R.id.lessons_title);

        lessonRecyclerAdapter = new LessonRecyclerAdapter(lesson_list);
        lessons_list_view.setLayoutManager(new LinearLayoutManager(this));
        lessons_list_view.setAdapter(lessonRecyclerAdapter);
        lessons_list_view.setHasFixedSize(true);


        firebaseFirestore = FirebaseFirestore.getInstance();
        //Sistemare
        //lessons_title.setText("Lista delle lezioni di " + firebaseFirestore.collection("Courses").document(course_id).get().toString());

        firebaseFirestore.collection("Courses/" + course_id + "/Lessons").addSnapshotListener(LessonsActivity.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(queryDocumentSnapshots.isEmpty())
                {
                    Toast.makeText(LessonsActivity.this, "No lessons for this course", Toast.LENGTH_LONG).show();
                }

                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges())
                {
                    //check if data is edited
                    if(doc.getType() == DocumentChange.Type.ADDED){

                        String lessonId = doc.getDocument().getId();
                        Lesson lesson = doc.getDocument().toObject(Lesson.class).withId(lessonId);
                        lesson_list.add(lesson);

                        lessonRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }
}
