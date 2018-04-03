package net.kaparray.velp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.kaparray.velp.R;



public class OpenTaskFragment extends Fragment{


    TextView mNameTask;
    TextView mValueTask;
    TextView mNameUser;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fr_opentask, container, false);

        mNameTask = rootView.findViewById(R.id.tv_nameTaskInOpenTask);
        mValueTask = rootView.findViewById(R.id.tv_valueTaskInOpenTask);
        mNameUser = rootView.findViewById(R.id.tv_nameUserInOpenTask);

        // Check data in bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            mNameTask.setText(bundle.getString("NameTask"));
            mValueTask.setText(bundle.getString("ValueTask"));
            mNameUser.setText(bundle.getString("NameUser"));
        }



        return rootView;
    }
}
