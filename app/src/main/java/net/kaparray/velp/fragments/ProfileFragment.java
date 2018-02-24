package net.kaparray.velp.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;

public class ProfileFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fr_profile, container, false);
// Add title
        ((MainActivity) getActivity()).setTitle(getString(R.string.ProfileTitle));

        return rootView;
    }
}
