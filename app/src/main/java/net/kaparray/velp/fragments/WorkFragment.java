package net.kaparray.velp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;


public class WorkFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fr_work, container, false);
        ((MainActivity) getActivity()).setTitle(getString(R.string.title_work));



        return rootView;
    }
}
