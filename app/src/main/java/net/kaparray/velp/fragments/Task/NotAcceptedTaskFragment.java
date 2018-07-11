package net.kaparray.velp.fragments.Task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;
import net.kaparray.velp.classes_for_data.TaskLoader;
import net.kaparray.velp.fragments.OpenTaskFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;


public class NotAcceptedTaskFragment extends Fragment{
    // String
    private static final String TAG = "All right";

    // Util for check click
    private NotAcceptedTaskFragment.TaskViewHolder.ClickListener mClickListener;

    // Fragment
    private OpenTaskFragment openTaskFragment;

    // Firebase
    private DatabaseReference mFirebaseRef;
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    boolean counrter = true;

    ArrayList<TaskLoader> loderer; // so funny name for variable

    // View
    @BindView(R.id.tv_NoInternet) TextView mTextNoInternet;
    @BindView(R.id.rvTask) RecyclerView mRecyclerView;
    @BindView(R.id.progressBarTaskFragment) ProgressBar progressBar;

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


    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fr_for_task, container, false);
        // Add title
        ((MainActivity) getActivity()).setTitle(getString(R.string.NotAcceptedTaskTitle));


        //Butter Knife
        ButterKnife.bind(this, rootView);

        loderer = new ArrayList<TaskLoader>();

        // Find branch in firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mFirebaseRef = database.getReference("Task");


        mTextNoInternet.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);



        // Create llm
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));

        openTaskFragment = new OpenTaskFragment();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(hasConnection(getContext())) {
            // Create FirebaseRecyclerAdapter for automatic work with FDB
            FirebaseRecyclerAdapter<TaskLoader, TaskViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TaskLoader, TaskViewHolder>(

                    TaskLoader.class,
                    R.layout.card_for_task,
                    TaskViewHolder.class,
                    mFirebaseRef.orderByChild("accepted").equalTo("false")
            ) {

                @Override
                protected void onDataChanged() {

                    NotAcceptedTaskFragment.super.onStart();

                    new CountDownTimer(3000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            if(loderer.size() <= 0 ){
                                counrter = true;
                            }else {
                                counrter = false;

                            }

                        }

                        public void onFinish() {
                            if(counrter){

                                progressBar.setVisibility(View.GONE);
                                mTextNoInternet.setVisibility(View.VISIBLE);
                                mTextNoInternet.setText("No task");
                                mRecyclerView.setVisibility(View.GONE);
                            }else {

                            }


                        }
                    }.start();
                }

                @Override
                protected void populateViewHolder(TaskViewHolder viewHolder, final TaskLoader model, int position) {
                    // This is real magic      ___
                    //                     ⎺\_(◦-◦)_/⎺
                    //                          ▲
                    //                          ▼
                    //                        _| |_


                    viewHolder.setTitleName(model.getNameTask());
                    viewHolder.setValue(model.getValueTask());
                    viewHolder.setUser(model.getNameUser());
                    viewHolder.setPhoto(model.getPhoto(), getResources());
                    viewHolder.setStatus(model.getAccepted(), model.getDone(), model.getUserTakeUID(), getContext());



                    loderer.add(model);

                    // Hide progressBar
                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    final TaskViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                    viewHolder.setOnClickListener(new TaskViewHolder.ClickListener() {
                        @Override
                        public void onItemClick(View view, final int position) {

                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .replace(R.id.container, openTaskFragment)
                                    .commit();


                            // This is magic bundle. I transit data in DB to OpenTaskFragment
                            Bundle bundle = new Bundle();
                            bundle.putString("TaskKey", loderer.get(position).getKey());
                            openTaskFragment.setArguments(bundle);
                        }



                        @Override
                        public void onItemLongClick(View view, final int position) {
                            if (user != null && loderer.get(position).getUserUID().equals(user.getUid())) {
                                AlertDialog.Builder AlretDialog = new AlertDialog.Builder(getActivity());
                                AlretDialog.setTitle(getString(R.string.Title_AlretDialogDeleteTask));
                                AlretDialog.setCancelable(false);
                                // Set Theme
                                SharedPreferences preferences = getActivity().getSharedPreferences("theme",MODE_PRIVATE);
                                String theme = preferences.getString("THEME"," ");

                                if (theme.equals("dark")){
                                    AlretDialog.setIcon(R.drawable.ic_delete_white); // add delete icon
                                } else if (theme.equals("light")){
                                    AlretDialog.setIcon(R.drawable.ic_delete_black_24dp); // add delete icon
                                }

                                AlretDialog.setMessage(getString(R.string.Text_AlretDialogDeleteTask));
                                AlretDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                AlretDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Query applesQuery = mFirebaseRef.orderByChild("uniqueIdentificator").equalTo(loderer.get(position).getUniqueIdentificator());

                                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                                    appleSnapshot.getRef().removeValue();
                                                    loderer.remove(position); // remove form array list
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Log.e(TAG, "onCancelled", databaseError.toException());
                                                Toast.makeText(getActivity(), "Ooops! Error database",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        dialogInterface.cancel();
                                    }
                                });
                                AlretDialog.show();
                            } else {
                                Toast.makeText(getActivity(), R.string.noRootForChange, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    return viewHolder;
                }
            };
            mRecyclerView.setAdapter(firebaseRecyclerAdapter);
        }else{
            progressBar.setVisibility(View.GONE);
            mTextNoInternet.setVisibility(View.VISIBLE);
            mTextNoInternet.setText(getString(R.string.noInternet));
            mTextNoInternet.setTextSize(0, 50);
        }



        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                if(loderer.size() <= 0 ){
                    counrter = true;
                }else {
                    counrter = false;

                }

            }

            public void onFinish() {
                if(counrter){

                    progressBar.setVisibility(View.GONE);
                    mTextNoInternet.setVisibility(View.VISIBLE);
                    mTextNoInternet.setText("No task");
                    mRecyclerView.setVisibility(View.GONE);
                }else {

                }


            }
        }.start();
    }





    // ViewHolder for FirebaseRecyclerAdapter
    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        View mView;
        private ClickListener mClickListener;

        public TaskViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            //listener set on ENTIRE ROW, you may set on individual components within a row.
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mClickListener.onItemLongClick(v, getAdapterPosition());
                    return true;
                }
            });
        }



        // This method return text for name task
        public void setTitleName(String title) {
            TextView name = mView.findViewById(R.id.tv_nameTask);
            name.setText(title);

            try {
                if(title.length() < 28){
                    name.setText(title);
                }else{
                    name.setText(title.substring(0, 25) + "...");
                }

            } catch (NullPointerException e){
                name.setText("Error");
            }
        }

        // This method return text for value task
        public void setValue(String value) {
            TextView val = mView.findViewById(R.id.tv_nameValue);

            try {
                if(value.length() < 30){
                    val.setText(value);
                }else{
                    val.setText(value.substring(0, 27) + "...");
                }
            } catch (NullPointerException e){
                val.setText("Error");
            }
        }

        // This method return text for name user
        public void setUser(final String userr) {
            TextView us = mView.findViewById(R.id.tv_userTask);
            try {
                us.setText(userr);
            } catch (NullPointerException e){
                us.setText("Error");
            }

        }


        public void setPhoto(String photo, Resources resources){
            ImageView ph = mView.findViewById(R.id.iv_photoTask);

            try {
                if (photo.equals("ic_boy")) {
                    ph.setImageDrawable(resources.getDrawable(R.drawable.ic_boy));
                } else if (photo.equals("ic_boy1")) {
                    ph.setImageDrawable(resources.getDrawable(R.drawable.ic_boy1));
                } else if (photo.equals("ic_girl")) {
                    ph.setImageDrawable(resources.getDrawable(R.drawable.ic_girl));
                } else if (photo.equals("ic_girl1")) {
                    ph.setImageDrawable(resources.getDrawable(R.drawable.ic_girl1));
                } else if (photo.equals("ic_man1")) {
                    ph.setImageDrawable(resources.getDrawable(R.drawable.ic_man1));
                } else if (photo.equals("ic_man2")) {
                    ph.setImageDrawable(resources.getDrawable(R.drawable.ic_man2));
                } else if (photo.equals("ic_man3")) {
                    ph.setImageDrawable(resources.getDrawable(R.drawable.ic_man3));
                } else if (photo.equals("ic_man4")) {
                    ph.setImageDrawable(resources.getDrawable(R.drawable.ic_man4));
                }
            }catch (NullPointerException e){
                ph.setImageDrawable(resources.getDrawable(R.drawable.ic_launcher_round));
            }
        }


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        public void setStatus(String status, String statusDone, String userTakeUID,  Context context){
            ImageView ph = mView.findViewById(R.id.iv_status);

            if(statusDone.equals("true") && userTakeUID.equals(user.getUid())){
                ph.setImageDrawable(context.getResources().getDrawable(R.drawable.baseline_done_all_24px));
            }else if(statusDone.equals("true") && !userTakeUID.equals(user.getUid())){
                ph.setImageDrawable(context.getResources().getDrawable(R.drawable.baseline_done_all_24px));
            }else if(statusDone.equals("false")){
                if(status.equals("false")){
                    ph.setImageDrawable(context.getResources().getDrawable(R.drawable.baseline_lock_open_24px));
                }else if(status.equals("true") && userTakeUID.equals(user.getUid())){
                    ph.setImageDrawable(context.getResources().getDrawable(R.drawable.baseline_done_24px));
                }else if(status.equals("true")){
                    ph.setImageDrawable(context.getResources().getDrawable(R.drawable.baseline_lock_24px));
                }
            }else if(statusDone.equals(user.getUid()) && status.equals("end")){
                ph.setImageDrawable(context.getResources().getDrawable(R.drawable.baseline_done_all_24px));
            }
        }

        //Interface to send callbacks...
        public interface ClickListener{
            public void onItemClick(View view, int position);
            public void onItemLongClick(View view, int position);
        }

        public void setOnClickListener(TaskViewHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }

    }



}