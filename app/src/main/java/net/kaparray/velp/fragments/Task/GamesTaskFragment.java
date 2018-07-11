package net.kaparray.velp.fragments.Task;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;
import net.kaparray.velp.classes_for_data.TaskLoader;
import net.kaparray.velp.fragments.OpenTaskFragment;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GamesTaskFragment extends Fragment{


    // Firebase
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    ArrayList<TaskLoader> loderer; // so funny name for variable


    // Fragment
    OpenTaskFragment openTaskFragment;

    @BindView(R.id.btn_rand)
    Button mButtonRand;
    @BindView(R.id.cv_TaskGames)
    CardView mCardInfoGames;
    @BindView(R.id.tv_nameTaskInOpenTaskMap)
    TextView mNameTask;
    @BindView(R.id.tv_valueTaskInOpenTaskMap)
    TextView mValueTask;
    @BindView(R.id.tv_nameUserInOpenTaskMap)
    TextView mNameUser;
    @BindView(R.id.tv_pointsInOpenFragmentMap)
    TextView mPoints;
    @BindView(R.id.tv_timeMap)
    TextView mTime;
    @BindView(R.id.iv_photoTaskMap)
    ImageView mPhotoUser;
    Animation anim;
    Animation animButton;

    int n;

    @OnClick(R.id.btn_rand)
    void rnd() {

        if(loderer.size() > 0) {

            Random rand = new Random();

            n = rand.nextInt(loderer.size());

            mDatabase.child("Task").child(loderer.get(n).getKey()).child("accepted").setValue("true");
            mDatabase.child("Task").child(loderer.get(n).getKey()).child("userTakeUID").setValue(user.getUid());
            mDatabase.child("Task").child(loderer.get(n).getKey()).child("doublePoints").setValue("true");



            mNameTask.setText(loderer.get(n).getNameTask());
            mValueTask.setText(loderer.get(n).getValueTask());
            mNameUser.setText(loderer.get(n).getNameUser());
            mPoints.setText(loderer.get(n).getPoints() + getString(R.string.point));
            mTime.setText(loderer.get(n).getTime());

            String photo = loderer.get(n).getPhoto();

            try {
                if (photo.equals("ic_boy")) {
                    mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy));
                } else if (photo.equals("ic_boy1")) {
                    mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy1));
                } else if (photo.equals("ic_girl")) {
                    mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl));
                } else if (photo.equals("ic_girl1")) {
                    mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl1));
                } else if (photo.equals("ic_man1")) {
                    mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_man1));
                } else if (photo.equals("ic_man2")) {
                    mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_man2));
                } else if (photo.equals("ic_man3")) {
                    mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_man3));
                } else if (photo.equals("ic_man4")) {
                    mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_man4));
                }
            } catch (Exception w) {
                try {
                    mPhotoUser.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_round));
                } catch (Exception e) {
                    Log.d("Error-Message", e.getStackTrace() + "");
                }

            }

            anim = AnimationUtils.loadAnimation(getContext(), R.anim.beta_animation_games);
            mCardInfoGames.startAnimation(anim);

            animButton = AnimationUtils.loadAnimation(getContext(), R.anim.button_anim);


            mButtonRand.startAnimation(animButton);

            mCardInfoGames.setVisibility(View.VISIBLE);
            mButtonRand.setText(getResources().getString(R.string.allRight));

        }else{
            mButtonRand.setText("none open task");

        }

    }

    @OnClick(R.id.cv_TaskGames)
    void card(){
        openTaskFragment = new OpenTaskFragment();

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, openTaskFragment)
                .commit();


        // This is magic bundle. I transit data in DB to OpenTaskFragment
        Bundle bundle = new Bundle();
        bundle.putString("TaskKey", loderer.get(n).getKey());
        openTaskFragment.setArguments(bundle);
    }


    @Override
    public void onStart() {
        super.onStart();


        Query myTopPostsQuery = mDatabase.child("Task");
        myTopPostsQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if ((dataSnapshot.child("accepted").getValue() + "").equals("false")) {
                    loderer.add(dataSnapshot.getValue(TaskLoader.class));
                }



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fr_games, container, false);

        // For view
        ButterKnife.bind(this, rootView);

        // init array list
        loderer = new ArrayList<TaskLoader>();

        // Add title
        ((MainActivity) getActivity()).setTitle(getString(R.string.GamesTitle));




        return rootView;
    }
}
