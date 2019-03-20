package com.example.firebaseapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class LessonRecyclerAdapter extends RecyclerView.Adapter<LessonRecyclerAdapter.ViewHolder> {

    public List<Lesson> lessonsList;
    public Context context;

    final AdminUser admin = new AdminUser();

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
    public void onBindViewHolder(@NonNull final LessonRecyclerAdapter.ViewHolder viewHolder, int i) {

        final FirebaseFirestore firebaseFirestore;
        firebaseFirestore = FirebaseFirestore.getInstance();

        //getting user info
        FirebaseAuth firebaseAuth;

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //getting current user
        final FirebaseUser user = firebaseAuth.getCurrentUser();


        //saving user info
        admin.setEmail(user.getEmail());


        //per controllo admin
        final FirebaseFirestore mFireStore;

        final String lessonId = lessonsList.get(i).LessonID;
        final String courseId = lessonsList.get(i).getCourseID();
        final int position = i;

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

        //da rendere visibile solo all'admin
        viewHolder.setImage_delete();




        //cercare se l'utente corrente è un admin o no
        mFireStore = FirebaseFirestore.getInstance();


        mFireStore.collection("AdminUsers")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        boolean find = false;
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


                            viewHolder.image_delete.setVisibility(View.VISIBLE);


                            viewHolder.image_delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    new AlertDialog.Builder(context)
                                            .setTitle("Delete Lesson")
                                            .setMessage("Do you want to delete this lesson?")
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    if(admin.getCourseId().equals(courseId))
                                                    {

                                                    firebaseFirestore.document("Courses/"+courseId+"/Lessons/"+lessonId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(context, "Lesson removed", Toast.LENGTH_SHORT).show();
                                                            //aggiorna pagina
                                                            lessonsList.remove(position);
                                                            notifyDataSetChanged();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    } else Toast.makeText(context, "Error, teacher not allowed", Toast.LENGTH_SHORT).show();
                                                }
                                            }).setNegativeButton(android.R.string.no, null).show();

                                }
                            });
                        } else viewHolder.image_delete.setVisibility(View.GONE);
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
        private ImageView image_delete;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            mView = itemView;
        }

        public void setLesson_name(String name)
        {
            lesson_name = mView.findViewById(R.id.lesson_text_view);
            lesson_name.setText(name);
        }

        public void setImage_delete()
        {
            image_delete = mView.findViewById(R.id.image_delete);
        }

    }
}
