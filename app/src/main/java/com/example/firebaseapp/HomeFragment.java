package com.example.firebaseapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import static android.content.Context.CLIPBOARD_SERVICE;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private Button buttonQR, buttonSchedule, buttonClasses, buttonCreate, buttonCopy;
    private FirebaseFirestore firebaseFirestore;
    private TextView qrID_txt;

    ClipboardManager clipboardManager;
    ClipData clipData;

    //getting user info
    private FirebaseAuth firebaseAuth;

    //Firestore
    private FirebaseFirestore mFireStore;
    private static final String TAG = "Name: ";

    //admin per memorizzare info
    final AdminUser admin = new AdminUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container,false);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //getting current user
        final FirebaseUser user = firebaseAuth.getCurrentUser();


        //saving user info
        admin.setEmail(user.getEmail());
        //admin.setCourseId(user.getUid());

        buttonQR = (Button) view.findViewById(R.id.buttonScanQR);
        buttonSchedule = (Button) view.findViewById(R.id.buttonSchedule);
        buttonClasses = (Button) view.findViewById(R.id.buttonOldClasses);
        buttonCreate = (Button) view.findViewById(R.id.buttonCreateLesson);
        qrID_txt = (TextView) view.findViewById(R.id.QRID_txt);
        buttonCopy = (Button) view.findViewById(R.id.btn_copy);

        //Link per il QR
        clipboardManager = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
        buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String qr = qrID_txt.getText().toString();
                clipData = ClipData.newPlainText("QR", qr);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getContext(), "Text copied!",Toast.LENGTH_SHORT).show();
            }
        });




        //cercare se l'utente corrente è un admin o no
        mFireStore = FirebaseFirestore.getInstance();


        mFireStore.collection("AdminUsers")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        boolean find = false;
                        if(e != null){
                            Log.d(TAG, "error : "+ e.getMessage());
                        }

                        for(DocumentSnapshot doc : documentSnapshots){
                            String email = doc.getString("email");



                            if(email.equals(admin.getEmail())) {
                                find = true;
                                //getting admin's courseID
                                admin.setCourseId(doc.getString("courseId"));
                                break;
                            }
                            else{
                                find = false;
                            }
                        }
                        if (find){
                            buttonCreate.setVisibility(View.VISIBLE);
                        }
                    }
                });

        firebaseFirestore = FirebaseFirestore.getInstance();

        //adding listener to button

        buttonQR.setOnClickListener(this);
        buttonClasses.setOnClickListener(this);
        buttonSchedule.setOnClickListener(this);
        buttonCreate.setOnClickListener(this);
        buttonCreate.setVisibility(View.GONE);
        qrID_txt.setVisibility(View.GONE);
        buttonCopy.setVisibility(View.GONE);

        return view;
    }



    @Override
    public void onClick(View view) {

        if(view == buttonQR)
        {

            startActivity(new Intent(getContext(), PostQR.class));
        }
        if(view == buttonSchedule)
        {

            startActivity(new Intent(getContext(), Schedule.class));
        }
        if(view == buttonClasses)
        {

            startActivity(new Intent(getContext(), PastClasses.class));
        }

        if(view == buttonCreate)
        {

            new AlertDialog.Builder(getContext())
                    .setTitle("Create Lesson")
                    .setMessage("Do you really want to create a new lesson?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            final Lesson lesson = new Lesson(admin.getCourseId());
                            firebaseFirestore.collection("Courses/"+lesson.getCourseID()+"/Lessons").add(lesson).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(getContext(), "Lesson added", Toast.LENGTH_SHORT).show();

                                        //Firestore per ottenere id corso e lezione per il qr
                                        mFireStore = FirebaseFirestore.getInstance();
                                        mFireStore.collection("Courses/"+lesson.getCourseID()+"/Lessons").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                                                if(e != null){
                                                    Log.d(TAG, "error : "+ e.getMessage());
                                                }

                                                String lessonId = null;
                                                boolean isSubject = false;

                                                for(DocumentSnapshot doc : documentSnapshots){
                                                    String lessonName = doc.getString("lesson_name");
                                                    lessonId = doc.getId();


                                                    if(lessonName.equals(lesson.getLesson_name())) {

                                                        isSubject = true;
                                                        break;
                                                    }

                                                }
                                                if(isSubject){

                                                    qrID_txt.setText(lessonId + lesson.getCourseID());
                                                    qrID_txt.setVisibility(View.VISIBLE);
                                                    buttonCopy.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        });

                                    }
                                }
                            });
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }
}
