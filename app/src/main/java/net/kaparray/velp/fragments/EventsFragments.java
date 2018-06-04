package net.kaparray.velp.fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;
import net.kaparray.velp.classes_for_data.EventLoader;


public class EventsFragments extends Fragment{

    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private DatabaseReference mFirebaseRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fr_event, container, false);
        ((MainActivity) getActivity()).setTitle(getString(R.string.EventTitle));

        mRecyclerView = rootView.findViewById(R.id.rvEvent);
        // Find branch in firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mFirebaseRef = database.getReference("Event");

        // Create llm
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        return rootView;
    }



    @Override
    public void onStart() {
        super.onStart();

        // Create FirebaseRecyclerAdapter for automatic work with FDB
        FirebaseRecyclerAdapter<EventLoader, EventsFragments.TaskViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<EventLoader, EventsFragments.TaskViewHolder>(

                EventLoader.class,
                R.layout.card_for_event,
                EventsFragments.TaskViewHolder.class,
                mFirebaseRef
        ) {
            @Override
            protected void populateViewHolder(EventsFragments.TaskViewHolder viewHolder, final EventLoader model, int position) {
                // This is real magic      ___
                //                     ⎺\_(◦-◦)_/⎺
                //                          ▲
                //                          ▼
                //                        _| |_



                viewHolder.setTitleName(model.getNameEvent());
                viewHolder.setValue(model.getValueEvent());
                viewHolder.setUser(model.getOrganizerEvent());
                viewHolder.setPhoto(model.getPhotoLoad(), getResources());



            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    // ViewHolder for FirebaseRecyclerAdapter
    public static class TaskViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public TaskViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        // This method return text for name task
        public void setTitleName(String title){
            TextView name = mView.findViewById(R.id.tv_NameEvent);
            name.setText(title);
        }
        // This method return text for value task
        public void setValue(String value){
            TextView val = mView.findViewById(R.id.tv_ValueEvent);
            val.setText(value);
        }
        // This method return text for name user
        public void setUser(final String user){
            TextView us = mView.findViewById(R.id.tv_OrganizerEvent);
            us.setText(user);
        }

        public void setPhoto(String photo, Resources resources){
            ImageView photo0 = mView.findViewById(R.id.iv_photoEvents);
            photo0.setImageDrawable(resources.getDrawable(R.drawable.night_run));
        }

    }

}
