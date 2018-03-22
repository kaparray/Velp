package net.kaparray.velp.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import net.kaparray.velp.classes.TaskLoader;


import java.util.ArrayList;

import static net.kaparray.velp.R.layout.fr_task;


public class TaskFragment extends Fragment{

    private static final String TAG = "All right";
    private DatabaseReference mFirebaseRef;

    private TaskViewHolder.ClickListener mClickListener;
    private OpenTaskFragment openTaskFragment;

    FloatingActionButton fab;
    AddTaskFragment addTaskFragment;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");

    ArrayList<String> taskarray;
    ArrayList<String> userarray;


    String name;

    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(fr_task, container, false);
        // Add title
        ((MainActivity) getActivity()).setTitle(getString(R.string.TaskTitle));

        taskarray = new ArrayList<String>();
        userarray = new ArrayList<String>();

        // Find branch in firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mFirebaseRef = database.getReference("Task");




        // Add fragment for add task
        addTaskFragment = new AddTaskFragment();

        mRecyclerView = rootView.findViewById(R.id.rvTask);
        progressBar = rootView.findViewById(R.id.progressBarTaskFragment);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.container, addTaskFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });

        // Create llm
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));

        openTaskFragment = new OpenTaskFragment();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Create FirebaseRecyclerAdapter for automatic work with FDB
        FirebaseRecyclerAdapter<TaskLoader, TaskViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<TaskLoader, TaskViewHolder>(

                TaskLoader.class,
                R.layout.card_for_task,
                TaskViewHolder.class,
                mFirebaseRef
        ) {
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


                taskarray.add(model.getUniqueIdentificator());
                userarray.add(model.getUserUID());


                    // Hide progressBar
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final TaskViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new TaskViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN )
                                .replace(R.id.container, openTaskFragment)
                                .addToBackStack(null)
                                .commit();
                    }

                    @Override
                    public void onItemLongClick(View view, final int position) {
                        Toast.makeText(getActivity(), "Item long clicked at " + position, Toast.LENGTH_SHORT).show();
                        if (user != null && userarray.get(position).equals(user.getUid())){
                            AlertDialog.Builder AlretDialog = new AlertDialog.Builder(getActivity());
                            AlretDialog.setTitle(getString(R.string.Title_AlretDialogBonus));
                            AlretDialog.setCancelable(false);
                            AlretDialog.setIcon(R.drawable.ic_bonus);
                            AlretDialog.setMessage(getString(R.string.Text_AlretDialogBonus));
                            AlretDialog.setPositiveButton("Понятно", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Query applesQuery = mFirebaseRef.orderByChild("uniqueIdentificator").equalTo(taskarray.get(position)) ;

                                    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                                    appleSnapshot.getRef().removeValue();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            Log.e(TAG, "onCancelled", databaseError.toException());
                                        }
                                    });
                                    dialogInterface.cancel();
                                }
                            });
                            AlretDialog.show();
                        }
                    }
                });
                return viewHolder;
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
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
        }

        // This method return text for value task
        public void setValue(String value) {
            TextView val = mView.findViewById(R.id.tv_nameValue);
            val.setText(value);
        }

        // This method return text for name user
        public void setUser(final String userr) {
            TextView us = mView.findViewById(R.id.tv_userTask);
            us.setText(userr);
        }
//        public void setPhoto(String photo){
//            ImageView ph = mView.findViewById(R.id.iv_photoTask);
//            Glide.with(mView.getContext()).load(photo).into(ph);
//        }


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
