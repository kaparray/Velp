package net.kaparray.velp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.kaparray.velp.Auth.AuthActivity;
import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;
import net.kaparray.velp.fragments.chandeDataUser.ChangeDataFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;


public class SettingsFragment extends Fragment{

    // Firebase
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    public ProgressDialog mProgressDialog;
    @BindView(R.id.sw_theme) Switch mChangeTheme;
    @BindView(R.id.btn_signOut) Button mLogOut;
    @BindView(R.id.btn_dellUser) Button mDell;



    @OnClick(R.id.btn_changeUserData)
    public void submit() {
        ChangeDataFragment changeDataFragment = new ChangeDataFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .replace(R.id.container, changeDataFragment)
                .commit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fr_settings, container, false);
        // Add title
        ((MainActivity) getActivity()).setTitle(getString(R.string.SettingsTitle));

        // Back stack
        ((MainActivity) getActivity()).setAddTask(false);

        // Butter Knife
        ButterKnife.bind(this, rootView);

        mLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), AuthActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        // Check button
        SharedPreferences preferences = getActivity().getSharedPreferences("theme",MODE_PRIVATE);
        String theme = preferences.getString("THEME"," ");
        if (theme.equals("dark")){
            mChangeTheme.setChecked(true);
            mChangeTheme.setText(getText(R.string.dark));
        } else if (theme.equals("light")){
            mChangeTheme.setChecked(false);
            mChangeTheme.setText(getText(R.string.light));
        }

        mChangeTheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences preferences = getActivity().getSharedPreferences("theme", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                if (isChecked) {
                    // The toggle is enabled
                    editor.putString("THEME", "dark");
                    getActivity().setTheme(R.style.Theme_Design_NoActionBar);
                } else {
                    // The toggle is disabled
                    editor.putString("THEME", "light");
                    getActivity().setTheme(R.style.AppTheme_NoActionBar);
                }
                editor.apply();
                // Add in preferences
                SharedPreferences preferencesView = getActivity().getSharedPreferences("view", MODE_PRIVATE);
                SharedPreferences.Editor editorView = preferencesView.edit();
                editorView.putString("VIEW", "settings");
                editorView.apply();

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        mDell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                user.delete();
                Intent intent = new Intent(getActivity(), AuthActivity.class);
                startActivity(intent);
            }
        });




        return rootView;
    }





    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage("Загрузка..");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
