package com.example.firebaseapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.krishna.fileloader.FileLoader;
import com.krishna.fileloader.listener.FileRequestListener;
import com.krishna.fileloader.pojo.FileResponse;
import com.krishna.fileloader.request.FileLoadRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {


    private PDFView pdfView;
    private ProgressBar progressBar;
    private String url;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search, container,false);


        pdfView = view.findViewById(R.id.pdfViewer);
        progressBar = view.findViewById(R.id.progress_bar);
        url = "https://www.uniba.it/ricerca/dipartimenti/informatica/didattica/corsi-di-laurea/informatica-tps-270/OrarioITPSIIsem201819c.pdf";

        progressBar.setVisibility(View.VISIBLE);
        FileLoader.with(getContext()).load(url)
                .fromDirectory("PDFFiles", FileLoader.DIR_INTERNAL)
                .asFile(new FileRequestListener<File>() {
                    @Override
                    public void onLoad(FileLoadRequest fileLoadRequest, FileResponse<File> fileResponse) {
                        progressBar.setVisibility(View.GONE);

                        File pdfFile = fileResponse.getBody();

                        pdfView.fromFile(pdfFile).password(null).defaultPage(0).enableSwipe(true)
                                .swipeHorizontal(false).enableDoubletap(true)
                                .onTap(new OnTapListener() {
                                    @Override
                                    public boolean onTap(MotionEvent e) {
                                        return true;
                                    }
                                }).onRender(new OnRenderListener() {
                            @Override
                            public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {

                                pdfView.fitToWidth();
                            }
                        })
                                .enableAnnotationRendering(true)
                                .invalidPageColor(Color.WHITE)
                                .load();

                    }

                    @Override
                    public void onError(FileLoadRequest fileLoadRequest, Throwable throwable) {

                        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });

        return view;
    }
}
