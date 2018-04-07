package net.kaparray.velp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;


public class AddTaskFragment extends android.support.v4.app.Fragment{

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Task");

    Button mAddTask;
    EditText mTask;
    EditText mValueTask;
    TaskFragment taskFragment;

    String name;
    String photo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Checked theme app
        SharedPreferences preferencesTheme = getActivity().getSharedPreferences("theme",Context.MODE_PRIVATE);
        final String theme = preferencesTheme.getString("THEME"," ");
        // Set Fragment
        final View rootView = inflater.inflate(R.layout.fr_addtask, container, false);
        // Add title
        ((MainActivity) getActivity()).setTitle(getString(R.string.AddTaskTitle));

        mAddTask = rootView.findViewById(R.id.btn_addTask);
        mTask = rootView.findViewById(R.id.et_NameTask);
        mValueTask = rootView.findViewById(R.id.et_valueTask);

        // Set text color
        if (theme.equals("dark")){
            mTask.setTextColor(getResources().getColor(R.color.white));
            mValueTask.setTextColor(getResources().getColor(R.color.white));
        } else if (theme.equals("light")){
            mTask.setTextColor(getResources().getColor(R.color.black));
            mValueTask.setTextColor(getResources().getColor(R.color.black));
        }

        mAddTask.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
               imm.hideSoftInputFromWindow(mAddTask.getWindowToken(),
                       InputMethodManager.HIDE_NOT_ALWAYS);



               if(hasConnection(getContext())) {
                   taskFragment = new TaskFragment();

                   DatabaseReference mUserAccount = myRef.push();
                   mUserAccount.child("key").setValue(mUserAccount.toString()
                           .replace("https://velp-1544e.firebaseio.com/Task/", ""));


                   mUserAccount.child("userUID").setValue(user.getUid());
                   mUserAccount.child("nameTask").setValue(mTask.getText().toString());
                   mUserAccount.child("nameUser").setValue(user.getDisplayName());
                   mUserAccount.child("valueTask").setValue(mValueTask.getText().toString());
                   //mUserAccount.child("photoUser").setValue(user.getPhotoUrl());
                   mUserAccount.child("uniqueIdentificator").setValue(myRef.push().toString()
                           .replaceAll("https://velp-1544e.firebaseio.com/Task/", ""));

                   // Add data about taken user
                   DatabaseReference accepted =  mUserAccount.child("accept");
                   accepted.child("accepted").setValue("false");
                   accepted.child("userUID").setValue("null");

                   Toast.makeText(getContext(), R.string.taskAddInDataBase, Toast.LENGTH_LONG).show();
                   getActivity().getSupportFragmentManager()
                           .beginTransaction()
                           .replace(R.id.container, taskFragment)
                           .addToBackStack(null)
                           .commit();
               }else{
                   Toast.makeText(getActivity(), R.string.noInternet, Toast.LENGTH_LONG).show();
               }
            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mAddTask.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }
}
