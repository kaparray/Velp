package net.kaparray.velp.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toolbar;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;
import net.kaparray.velp.tutorial.TutorialActivity;

public class AboutFragment extends android.support.v4.app.Fragment{

    Button mTutorial;
    Button mWork;
    Toolbar mToolBar;
    WorkFragment workFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fr_about, container, false);

        mTutorial = rootView.findViewById(R.id.btn_tutorial);
        mWork = rootView.findViewById(R.id.btn_work);

        // Back stack
        ((MainActivity) getActivity()).setAddTask(false);

    // Add title
        ((MainActivity) getActivity()).setTitle(getString(R.string.AboutTitle));

        mTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getActivity().getSharedPreferences("TUTORIAL", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("activity_executed", false);
                editor.apply();
                Intent intent = new Intent(getActivity(), TutorialActivity.class);
                startActivity(intent);
            }
        });

        mWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workFragment = new WorkFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.container, workFragment)
                        .commit();
            }
        });

        return rootView;
    }
}
