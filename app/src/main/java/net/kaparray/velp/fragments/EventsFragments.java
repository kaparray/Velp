package net.kaparray.velp.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;
import net.kaparray.velp.classes_for_data.EventLoader;
import net.kaparray.velp.classes_for_data.TaskLoader;
import net.kaparray.velp.fragments.Task.MyTaskFragment;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;


public class EventsFragments extends Fragment{

    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    private DatabaseReference mFirebaseRef;

    ArrayList<EventLoader> loderer; // so funny name for variable

    @BindView(R.id.cv_TutorialEvents) CardView mTutorialEvents;

    Animation animVisibleCard;
    Animation animGoneCard;



    @OnClick(R.id.btn_okTutorialEvents)
    void tutorial(){
        mTutorialEvents.startAnimation(animGoneCard);

        // For settings
        SharedPreferences preferences = getActivity().getSharedPreferences("AlertTaskEvents", MODE_PRIVATE);
        SharedPreferences.Editor editorView = preferences.edit();
        editorView.putString("AlertTaskEvents", "true");
        editorView.apply();

        mTutorialEvents.setVisibility(View.GONE);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fr_event, container, false);
        ((MainActivity) getActivity()).setTitle(getString(R.string.EventTitle));

        // Back stack
        ((MainActivity) getActivity()).setAddTask(false);

        loderer = new ArrayList<EventLoader>();

        //Butter knife
        ButterKnife.bind(this, rootView);

        mRecyclerView = rootView.findViewById(R.id.rvEvent);
        // Find branch in firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mFirebaseRef = database.getReference("Event");


        // Animation
        animVisibleCard = AnimationUtils.loadAnimation(getContext(),R.anim.beta_animation_games);
        animGoneCard = AnimationUtils.loadAnimation(getContext(),R.anim.beta_animation_back);


        // Create llm
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        return rootView;
    }



    @Override
    public void onStart() {
        super.onStart();


        // Custom Tutorial
        SharedPreferences preferencesEvents = Objects.requireNonNull(getActivity()).getSharedPreferences("AlertTaskEvents",MODE_PRIVATE);
        String alertEvents = preferencesEvents.getString("AlertTaskEvents","false");

        if(alertEvents.equals("false")){
            mTutorialEvents.setVisibility(View.VISIBLE);
            mTutorialEvents.startAnimation(animVisibleCard);
        }



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

                loderer.add(model);


            }
            @Override
            public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                final TaskViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new MyTaskFragment.TaskViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(loderer.get(position).getLinkEvent()));
                        startActivity(browserIntent);
                    }


                    @Override
                    public void onItemLongClick(View view, final int position) {
//                        if (user != null && loderer.get(position).getUserUID().equals(user.getUid())) {
//                            AlertDialog.Builder AlretDialog = new AlertDialog.Builder(getActivity());
//                            AlretDialog.setTitle(getString(R.string.Title_AlretDialogDeleteTask));
//                            AlretDialog.setCancelable(false);
//                            // Set Theme
//                            SharedPreferences preferences = getActivity().getSharedPreferences("theme", MODE_PRIVATE);
//                            String theme = preferences.getString("THEME", " ");
//
//                            if (theme.equals("dark")) {
//                                AlretDialog.setIcon(R.drawable.ic_delete_white); // add delete icon
//                            } else if (theme.equals("light")) {
//                                AlretDialog.setIcon(R.drawable.ic_delete_black_24dp); // add delete icon
//                            }
//
//                            AlretDialog.setMessage(getString(R.string.Text_AlretDialogDeleteTask));
//                            AlretDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.cancel();
//                                }
//                            });
//                            AlretDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    Query applesQuery = mFirebaseRef.orderByChild("uniqueIdentificator").equalTo(loderer.get(position).getUniqueIdentificator());
//
//                                    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(DataSnapshot dataSnapshot) {
//                                            for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
//                                                appleSnapshot.getRef().removeValue();
//                                                loderer.remove(position); // remove form array list
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onCancelled(DatabaseError databaseError) {
//                                            Log.e(TAG, "onCancelled", databaseError.toException());
//                                            Toast.makeText(getActivity(), "Ooops! Error database",
//                                                    Toast.LENGTH_SHORT).show();
//                                        }
//                                    });
//                                    dialogInterface.cancel();
//                                }
//                            });
//                            AlretDialog.show();
//                        } else {
//                            Toast.makeText(getActivity(), R.string.noRootForChange, Toast.LENGTH_SHORT).show();
//                        }
                    }
                });
                return viewHolder;
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    // ViewHolder for FirebaseRecyclerAdapter
    public static class TaskViewHolder extends RecyclerView.ViewHolder{
        View mView;
        private MyTaskFragment.TaskViewHolder.ClickListener mClickListener;


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

             if(photo.equals("Mary_Children")) {
                 photo0.setImageDrawable(resources.getDrawable(R.drawable.mary_children));
             }else if(photo.equals("Night_run")){
                 photo0.setImageDrawable(resources.getDrawable(R.drawable.night_run));
             }
        }

        //Interface to send callbacks...
        public interface ClickListener{
            public void onItemClick(View view, int position);
            public void onItemLongClick(View view, int position);
        }

        public void setOnClickListener(MyTaskFragment.TaskViewHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }
    }

}
