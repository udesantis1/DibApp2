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
    //Recycler
    private String lesson_id="";
    private String course_id="";
    private RecyclerView comment_list;
    private List<Rating> commentsList;
    private RatingRecyclerAdapter ratingRecyclerAdapter;
    private EditText comment_field;
    ArrayList <Integer> valori=new ArrayList<>();


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
        lesson_id = getIntent().getStringExtra("lesson_id");
        course_id = getIntent().getStringExtra("course_id");


        comment_list = findViewById(R.id.comment_list);
        commentsList = new ArrayList<>();

        //Recycler setup
        ratingRecyclerAdapter = new RatingRecyclerAdapter(commentsList);
        comment_list.setHasFixedSize(true);
        comment_list.setLayoutManager(new LinearLayoutManager(this));
        comment_list.setAdapter(ratingRecyclerAdapter);

        final String path = "Courses/" + course_id + "/Lessons/" + lesson_id + "/Rates";

        //retrieving all rates linked to the lesson
        mFirestore.collection(path).orderBy("timestamp").addSnapshotListener(RatingActivity.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if(queryDocumentSnapshots.isEmpty())
                {
                    commentsList.clear();
                    Toast.makeText(RatingActivity.this, "No rating for this lesson", Toast.LENGTH_LONG).show();
                }
                for(DocumentChange doc: queryDocumentSnapshots.getDocumentChanges())
                {
                    if(doc.getType() == DocumentChange.Type.ADDED)
                    {
                        String commentId = doc.getDocument().getId(); //useless?
                        Rating ratings = doc.getDocument().toObject(Rating.class); // useless?
                        commentsList.add(ratings);
                        ratingRecyclerAdapter.notifyDataSetChanged();  //real time changes
                    }
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int b = (int) ratingBar.getRating();

                Map<String, Object> ratin = new HashMap<>();
                ratin.put("Rating", b);
                ratin.put("timestamp", FieldValue.serverTimestamp());

                /* mFirestore.collection("Commenti").add(ratin).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                     @Override
                     public void onSuccess(DocumentReference documentReference) {
                         Toast.makeText(MainActivity.this,"Ratin add",Toast.LENGTH_SHORT).show();
                     }
                 });
                        */


                mFirestore.collection(path).add(ratin).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(RatingActivity.this, "Error posting rate", Toast.LENGTH_SHORT).show();
                        } else {
                            ratingBar.setRating(0);
                        }
                    }
                });

                mFirestore.collection(path).get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot d : list) {
                                        Rating p = d.toObject(Rating.class);
                                        commentsList.add(p);
                                    }
                                    ratingRecyclerAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                btnSubmit.setEnabled(false);

                Toast.makeText(RatingActivity.this, "Thanks for rate", Toast.LENGTH_SHORT).show();
                ratingDisplayTextView.setText("Your rating is:" + ratingBar.getRating());

            }
        });


    }
}

