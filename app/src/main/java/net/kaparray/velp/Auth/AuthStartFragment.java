package net.kaparray.velp.Auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.kaparray.velp.R;

/**
 * Created by kaparray on 14.03.2018.
 */

public class AuthStartFragment extends Fragment{


    AuthEmailFragment emailFragment;
    AuthGoogleFragment GoogleFragment;
    AuthStartFragment startFragment;

    private Button mEmailAuth;
    private Button mGoogleAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fr_authstart, container, false);

        GoogleFragment = new AuthGoogleFragment();
        emailFragment = new AuthEmailFragment();

        // Find and set listener on click for email fragment
        mEmailAuth = rootView.findViewById(R.id.btn_emailAuth);
        mEmailAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                        .replace(R.id.containerAuth, emailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });


        // Find and set listener on click for google fragment
        mGoogleAuth = rootView.findViewById(R.id.btn_GoogleAuth);
        mGoogleAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                        .replace(R.id.containerAuth, GoogleFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
    }
}