package net.kaparray.velp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;
import net.kaparray.velp.adapters.TaskRecyclerViewAdapter;
import net.kaparray.velp.classes.TaskLoader;

import java.util.List;


public class TaskFragment extends Fragment{

    FloatingActionButton fab;
    AddTaskFragment addTaskFragment;
    //private RecyclerView mRecyclerView;
    private List<TaskLoader> result;
    private TaskRecyclerViewAdapter adapter;

    private FirebaseDatabase database;
    private DatabaseReference myRef;


    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        database = FirebaseDatabase.getInstance();

        addTaskFragment = new AddTaskFragment();


        final View rootView = inflater.inflate(R.layout.fr_task, container, false);
        // Add title
        ((MainActivity) getActivity()).setTitle(getString(R.string.TaskTitle));


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


        updateList();

        RecyclerView mRecyclerView = rootView.findViewById(R.id.rating_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);


        mRecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new TaskRecyclerViewAdapter(result);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setAdapter(adapter);



        return rootView;
    }

    private void updateList(){
        myRef = database.getReference("task");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    result.add(snapshot.getValue(TaskLoader.class));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                TaskLoader loader = dataSnapshot.getValue(TaskLoader.class);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private int getItemIndex(TaskLoader taskLoader) {

        int index = -1;

        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).key.equals(taskLoader.key)) {
                index = i;
                break;
            }
        }
        return index;
    }


}
