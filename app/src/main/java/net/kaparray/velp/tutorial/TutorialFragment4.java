package net.kaparray.velp.tutorial;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.kaparray.velp.R;

public class TutorialFragment4 extends android.support.v4.app.Fragment {

    public static TutorialFragment4 newInstance() {return new TutorialFragment4();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_tutorial4, container, false);
    }
}

