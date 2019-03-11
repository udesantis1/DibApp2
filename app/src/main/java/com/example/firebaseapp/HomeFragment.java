package com.example.firebaseapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private Button buttonQR, buttonSchedule, buttonClasses, buttonLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container,false);

        buttonQR = (Button) view.findViewById(R.id.buttonScanQR);
        buttonSchedule = (Button) view.findViewById(R.id.buttonSchedule);
        buttonClasses = (Button) view.findViewById(R.id.buttonOldClasses);
        buttonLogout = (Button) view.findViewById(R.id.buttonLogout);

        //adding listener to button

        buttonQR.setOnClickListener(this);
        buttonClasses.setOnClickListener(this);
        buttonSchedule.setOnClickListener(this);
        buttonLogout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        if(view == buttonLogout)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), Login.class));
            getActivity().finish();

        }

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
    }
}
