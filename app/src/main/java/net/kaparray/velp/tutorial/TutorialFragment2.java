package net.kaparray.velp.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.kaparray.velp.R;

public class TutorialFragment2 extends Fragment {
    public static TutorialFragment2 newInstance() {
        return new TutorialFragment2();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_tutorial2, container, false);
    }
}

