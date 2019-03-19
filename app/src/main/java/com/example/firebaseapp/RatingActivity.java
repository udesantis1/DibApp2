package com.example.firebaseapp;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class RatingActivity extends AppCompatActivity {

    //RatingBar
    private RatingBar ratingBar;
    private Button btnSubmit;//Button send rating
    TextView ratingDisplayTextView; // View number rate
    //Firestore
    FirebaseFirestore mFirestore;

    private String lesson_id="";
    private String course_id="";
    private List<Rating> ratingList;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        //Initializing the firestore instance
        mFirestore=FirebaseFirestore.getInstance();
        //Inizializing item
        ratingBar=(RatingBar) findViewById(R.id.ratingBar);
        btnSubmit= (Button)findViewById(R.id.btn_send);
        ratingDisplayTextView= (TextView)findViewById(R.id.textView);
        //retrieving id from the lesson and course linked to these rate
        lesson_id = getIntent().getStringExtra("lessonID");
        course_id = getIntent().getStringExtra("courseID");
        ratingList = new ArrayList<>();

       final String path = "Courses/" + course_id + "/Lessons/" + lesson_id + "/Rates";
       int i=0;
       if(i!=1) {
           btnSubmit.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   int b = (int) ratingBar.getRating();
                   Map<String, Object> ratin = new HashMap<>();
                   ratin.put("Rating", b);
                   mFirestore.collection("Courses/" + course_id + "/Lessons/" + lesson_id + "/Rates").add(ratin).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                       @Override
                       public void onComplete(@NonNull Task<DocumentReference> task) {
                           if (!task.isSuccessful()) {
                               Toast.makeText(RatingActivity.this, "Error posting rate", Toast.LENGTH_SHORT).show();
                           } else {
                               ratingBar.setRating(0);
                           }
                       }
                   });

                   btnSubmit.setEnabled(false);
                   Toast.makeText(RatingActivity.this, "Thanks for rate", Toast.LENGTH_SHORT).show();
                   ratingDisplayTextView.setText("Your rating is:" + ratingBar.getRating());

               }
           });
                i++;
       }else{
           btnSubmit.setEnabled(false);

       }



    }
}

