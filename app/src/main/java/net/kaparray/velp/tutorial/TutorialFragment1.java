package net.kaparray.velp.tutorial;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.kaparray.velp.R;


public class TutorialFragment1 extends Fragment {





    public static TutorialFragment1 newInstance(int page) {
        TutorialFragment1 fragment = new TutorialFragment1();
        Bundle args=new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public TutorialFragment1() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fr_tutorial1, container, false);

        return rootView;
    }



}

