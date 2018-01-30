package net.kaparray.velp.tutorial;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.kaparray.velp.R;


public class TutorialFragment3 extends android.support.v4.app.Fragment {


    public static TutorialFragment3 newInstance() {return new TutorialFragment3();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_tutorial3, container, false);
    }
}

