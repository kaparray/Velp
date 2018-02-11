package net.kaparray.velp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import net.kaparray.velp.Auth.AuthActivity;
import net.kaparray.velp.R;

public class SettingsFragment extends Fragment{

    private FirebaseAuth mAuth;

    Button mSignOut;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fr_settings, container, false);


        mSignOut = rootView.findViewById(R.id.btn_signOut);

        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                logOut();
            }
        });


        return rootView;
    }

    public void logOut(){
        Intent intent = new Intent(getActivity(), AuthActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
