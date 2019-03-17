package com.example.firebaseapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class CommentsActivity extends AppCompatActivity {


    private EditText comment_field;
    private ImageView comment_send_btn;

    private RecyclerView comment_list;
    private List<Comment> commentsList;
    private CommentRecyclerAdapter commentRecyclerAdapter;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseFirestore firebaseFirestore2;
    private String lesson_id;
    private String course_id;

    //For QR Variables
    private String lessonQR;
    private String courseQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        //Initializing the firestore instance
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore2 = FirebaseFirestore.getInstance();


        //retrieving the ID from the lesson and course linked to these comments
        lesson_id = getIntent().getStringExtra("lesson_id");
        course_id = getIntent().getStringExtra("course_id");


        //retrieving info from QRCode
        lessonQR = getIntent().getStringExtra("LessonID");
        courseQR = getIntent().getStringExtra("CourseID");

        String path = "Courses/" + course_id + "/Lessons/" + lesson_id + "/Comments";



        comment_field = findViewById(R.id.comment_field);
        comment_send_btn = findViewById(R.id.comment_send_btn);
        comment_list = findViewById(R.id.comment_list);

        commentsList = new ArrayList<>();

        //Recycler setup
        commentRecyclerAdapter = new CommentRecyclerAdapter(commentsList);
        comment_list.setHasFixedSize(true);
        comment_list.setLayoutManager(new LinearLayoutManager(this));
        comment_list.setAdapter(commentRecyclerAdapter);


        if(lessonQR != null && courseQR != null){
            path = "Courses/" + courseQR + "/Lessons/" + lessonQR + "/Comments";
        }

            //retrieving all comments linked to the lesson
            firebaseFirestore.collection(path).orderBy("timestamp").addSnapshotListener(CommentsActivity.this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                    if (queryDocumentSnapshots.isEmpty()) {
                        commentsList.clear();
                        Toast.makeText(CommentsActivity.this, "No comments for this lesson", Toast.LENGTH_LONG).show();
                    }

                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            String commentId = doc.getDocument().getId(); //useless?
                            Comment comments = doc.getDocument().toObject(Comment.class); // useless?
                            commentsList.add(comments);
                            commentRecyclerAdapter.notifyDataSetChanged();  //real time changes
                        }
                    }
                }
            });

            comment_send_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String path = "Courses/" + course_id + "/Lessons/" + lesson_id + "/Comments";
                    if(lessonQR != null && courseQR != null){
                        path = "Courses/" + courseQR + "/Lessons/" + lessonQR + "/Comments";
                    }

                    Date currentTime = Calendar.getInstance().getTime();
                    String comment_message = comment_field.getText().toString();

                    Map<String, Object> commentsMap = new HashMap<>();
                    commentsMap.put("message", comment_message);
                    commentsMap.put("timestamp", currentTime);

                    firebaseFirestore.collection(path).add(commentsMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(CommentsActivity.this, "Error posting comment", Toast.LENGTH_SHORT).show();
                            } else {
                                comment_field.setText("");
                            }
                        }
                    });
                }

            });

    }
}
