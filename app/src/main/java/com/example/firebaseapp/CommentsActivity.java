package com.example.firebaseapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
    private String userMail = null;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String lesson_id;
    private String course_id;
    private Button rateButton;
    private TextView countUser;

    //For QR Variables
    private String lessonQR;
    private String courseQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        //Initializing the firestore instance
        firebaseFirestore = FirebaseFirestore.getInstance();



        //retrieving the ID from the lesson and course linked to these comments
        lesson_id = getIntent().getStringExtra("lesson_id");
        course_id = getIntent().getStringExtra("course_id");


        //retrieving info from QRCode
        lessonQR = getIntent().getStringExtra("LessonID");
        courseQR = getIntent().getStringExtra("CourseID");
        userMail = getIntent().getStringExtra("Email");


        String path = "Courses/" + course_id + "/Lessons/" + lesson_id + "/Comments";
        String pathUsers = "Courses/"+course_id+"/Lessons/"+lesson_id;


        comment_field = findViewById(R.id.comment_field);
        comment_send_btn = findViewById(R.id.comment_send_btn);
        comment_list = findViewById(R.id.comment_list);
        rateButton=findViewById(R.id.buttonRate);
        countUser = findViewById(R.id.count_user_field);

        comment_field.setVisibility(View.GONE);
        comment_send_btn.setVisibility(View.GONE);
        rateButton.setVisibility(View.GONE);





        commentsList = new ArrayList<>();

        //Recycler setup
        commentRecyclerAdapter = new CommentRecyclerAdapter(commentsList);
        comment_list.setHasFixedSize(true);
        comment_list.setLayoutManager(new LinearLayoutManager(this));
        comment_list.setAdapter(commentRecyclerAdapter);

        //check if user is here from scanning QR or from list of Lessons
        if(lessonQR != null && courseQR != null){
            path = "Courses/" + courseQR + "/Lessons/" + lessonQR + "/Comments";
            firebaseFirestore.document("Courses/"+courseQR+"/Lessons/"+lessonQR).update("usersList", FieldValue.arrayUnion(userMail));

            pathUsers = "Courses"+ courseQR + "/Lessons/" + lessonQR;
        }


        //used for the count of the users
        firebaseFirestore.document(pathUsers).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Lesson lesson = documentSnapshot.toObject(Lesson.class);

                String count = Integer.toString(lesson.getUsersList().size());

                countUser.setText(count);
            }
        });


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


            //Da attivare solo se l'user email è presente nella lista degli user

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();


        firebaseFirestore.document(pathUsers).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Lesson lesson = documentSnapshot.toObject(Lesson.class);
                for(String item : lesson.getUsersList())
                {
                    if(item.equalsIgnoreCase(user.getEmail()))
                    {
                        comment_field.setVisibility(View.VISIBLE);
                        comment_send_btn.setVisibility(View.VISIBLE);
                        rateButton.setVisibility(View.VISIBLE);
                        break;
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

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentRating= new Intent(getApplicationContext(),RatingActivity.class);
                intentRating.putExtra("course_id",  course_id);
                intentRating.putExtra("lesson_id", lesson_id);
                startActivity(intentRating);
            }
        });

    }

}
