package com.example.firebaseapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import google.zxing.integration.android.IntentIntegrator;
import google.zxing.integration.android.IntentResult;

public class PostQR extends AppCompatActivity implements View.OnClickListener{


    private Button scanBtn;
    private TextView formatTxt, contentTxt;
    private List<Course> course_list;
    private CourseRecyclerAdapter courseRecyclerAdapter;
    public Context contextQR;
    public static Bundle bundleQR;

    //Firestore
    private static final String TAG = "Name: ";
    private FirebaseFirestore mFireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_qr);

        scanBtn = (Button)findViewById(R.id.scan_button);
        formatTxt = (TextView)findViewById(R.id.scan_format);
        contentTxt = (TextView)findViewById(R.id.scan_content);

        scanBtn.setOnClickListener(this);




    }

    public Bundle getBundle(String lesson, String course){

        bundleQR.putString("LessonID", lesson);
        bundleQR.putString("CourseID", course);
        return bundleQR;
    }


    public void onClick(View v){
        if(v.getId()==R.id.scan_button){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            final String scanContent = scanningResult.getContents();
            final String lesson = scanContent.substring(0,20);
            final String course = scanContent.substring(20,40);
            final String scanFormat = scanningResult.getFormatName();




            //Firestore
            mFireStore = FirebaseFirestore.getInstance();
            mFireStore.collection("Courses/"+course+"/Lessons").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if(e != null){
                        Log.d(TAG, "error : "+ e.getMessage());
                    }

                    boolean isSubject = false;

                    for(DocumentSnapshot doc : documentSnapshots){
                        String lessonID = doc.getId();


                        if(lessonID.equals(lesson)) {

                            isSubject = true;
                            break;
                        }

                    }
                    if(isSubject){

                        //contentTxt.setText("Stai seguendo Mobile" );
                        //Bundle bundle = new Bundle();
                        //bundle.putString("CourseID", course);
                        //bundle.putString("LessonID", lesson);
                        //getBundle(lesson, course);
                        Intent intentQR = new Intent(getApplicationContext(),CommentsActivity.class);
                        intentQR.putExtra("CourseID", course);
                        intentQR.putExtra("LessonID", lesson);

                       startActivity(intentQR);
                        //intent.putExtras(bundle);

                    }else contentTxt.setText("Name not found: Lesson: " + lesson + " Course: " +course);
                }
            });



           /* if(!(bundleQR.isEmpty())){
                intent = new Intent(getApplicationContext(), CommentsActivity.class);
                intent.putExtras(bundleQR);

            }*/




        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
