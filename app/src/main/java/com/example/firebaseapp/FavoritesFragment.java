package com.example.firebaseapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

//this fragment contains the list of the courses
public class FavoritesFragment extends Fragment {

    private RecyclerView courses_list_view;
    private List<Course> course_list;

    private FirebaseFirestore firebaseFirestore;
    private CourseRecyclerAdapter courseRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_favorites, container,false);


        course_list = new ArrayList<>();
        courses_list_view = view.findViewById(R.id.courses_list_view);

        courseRecyclerAdapter = new CourseRecyclerAdapter(course_list);
        courses_list_view.setLayoutManager(new LinearLayoutManager(getContext()));
        courses_list_view.setAdapter(courseRecyclerAdapter);
        courses_list_view.setHasFixedSize(true);

        firebaseFirestore = FirebaseFirestore.getInstance();
        //SnapshotListener helps to retrieve data in real time
        firebaseFirestore.collection("Courses").orderBy("courseName").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

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

        return view;
    }
}
