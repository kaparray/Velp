package net.kaparray.velp.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;
import net.kaparray.velp.classes_for_data.RatingData;
import net.kaparray.velp.classes_for_data.TaskLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;


public class OpenTaskFragment extends Fragment{

    // Firebase
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // View
    View rootView;

    // View
    @BindView(R.id.tv_nameTaskInOpenTask) TextView mNameTask;
    @BindView(R.id.btn_universalButtonTask) Button mTakeTask;
    @BindView(R.id.tv_valueTaskInOpenTask) TextView mValueTask;
    @BindView(R.id.tv_nameUserInOpenTask) TextView mNameUser;
    @BindView(R.id.tv_phoneUserInOpenTask) TextView mPhoneUser;
    @BindView(R.id.tv_time) TextView mTime;
    @BindView(R.id.tv_pointsInOpenFragment) TextView mPoints;
    @BindView(R.id.iv_photoTask) ImageView mPhoto;
    @BindView(R.id.iv_check) ImageView mCheck;
    @BindView(R.id.fab_call) FloatingActionButton mCall;
    @BindView(R.id.cv_TutorialDirections) CardView mCardTutorial;
    @BindView(R.id.cv_TutorialGetTask) CardView mCardGetTask;
    @BindView(R.id.tv_textGetTask) TextView mTextGetTask;

    // String variable
    String KEY_Task;
    String photo;
    String mPhone;

    //Animation
    Animation animVisibleCard;
    Animation animGoneCard;


    // Task Loader Fragment
    TaskLoader taskLoader;
    List<RatingData> ratingData;

    // Counter
    private int clickCounter = 0;


    // Map
    private GoogleMap googleMap;
    private MapView mMapView;


    int point;
    int helped;
    String name;


    // Fragments
    ProfileFragment profileFragment;


    @OnClick(R.id.btn_okTutorialGetTask)
    void getTaskTut(){
        mCardGetTask.startAnimation(animGoneCard);

        // For settings
        SharedPreferences preferences = getActivity().getSharedPreferences("TutorialGetTask", MODE_PRIVATE);
        SharedPreferences.Editor editorView = preferences.edit();
        editorView.putString("TutorialGetTask", "true");
        editorView.apply();

        mCardGetTask.setVisibility(View.GONE);
    }


    @OnClick(R.id.iv_photoTask)
    public void openProfile(){

        profileFragment = new ProfileFragment();

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, profileFragment)
                .commit();

        // This is magic bundle. I transit data in DB to OpenTaskFragment
        Bundle bundle = new Bundle();
        bundle.putString("UserUID", taskLoader.getUserUID());
        profileFragment.setArguments(bundle);
    }

    @OnClick(R.id.fab_call)
    public void submit() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},24);

            mTextGetTask.setText(getResources().getString(R.string.callDemoAgein));
            mCardGetTask.setVisibility(View.VISIBLE);
            mCardGetTask.startAnimation(animVisibleCard);

        }else {

            // Custom Tutorial
            SharedPreferences preferencesUserDemo = Objects.requireNonNull(getActivity()).getSharedPreferences("DemoUser", MODE_PRIVATE);
            String userDemo = preferencesUserDemo.getString("DemoUser", "false");


            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + mPhone));
            startActivity(intent);

            if (userDemo.equals("false")) {
                if (taskLoader.getDone().equals("demo") && taskLoader.getAccepted().equals("demo")
                        && taskLoader.getUserTakeUID().equals("demo")) {
                    // End Demo task

                    point += Integer.parseInt(taskLoader.getPoints());

                    mDatabase.child("Users").child(user.getUid()).child("points").setValue(point + "");
                    mTakeTask.setBackgroundResource(R.drawable.button_round_green);
                    mCheck.setImageResource(R.drawable.ic_demo_24dp);
                    mTakeTask.setText(R.string.Finished);


                    // For settings
                    SharedPreferences preferences = getActivity().getSharedPreferences("DemoUser", MODE_PRIVATE);
                    SharedPreferences.Editor editorView = preferences.edit();
                    editorView.putString("DemoUser", "true");
                    editorView.apply();

                    mTakeTask.setBackgroundResource(R.drawable.button_round_grey);
                    mCheck.setImageResource(R.drawable.baseline_done_all_24px);
                    mTakeTask.setText(R.string.Complete);

                    mTextGetTask.setText(getResources().getString(R.string.coolTutorial));
                    mCardGetTask.setVisibility(View.VISIBLE);
                    mCardGetTask.startAnimation(animVisibleCard);

                }
            }


        }
    }


    @OnClick(R.id.btn_okTutorialDirections)
    void tutorial(){
        mCardTutorial.startAnimation(animGoneCard);

        // For settings
        SharedPreferences preferences = getActivity().getSharedPreferences("AlertTaskDirections", MODE_PRIVATE);
        SharedPreferences.Editor editorView = preferences.edit();
        editorView.putString("AlertTaskDirections", "true");
        editorView.apply();

        mCardTutorial.setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fr_opentask, container, false);


        // Back stack
        ((MainActivity) getActivity()).setAddTask(false);

        ButterKnife.bind(this, rootView);


        // Animation
        animVisibleCard = AnimationUtils.loadAnimation(getContext(),R.anim.beta_animation_games);
        animGoneCard = AnimationUtils.loadAnimation(getContext(),R.anim.beta_animation_back);

        mCall.setVisibility(View.GONE);
        mTakeTask.isClickable();

        // Check data in bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            KEY_Task = bundle.getString("TaskKey");
        }

        ratingData = new ArrayList<>();


        ValueEventListener postListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                taskLoader = dataSnapshot.child("Task").child(KEY_Task).getValue(TaskLoader.class);


                try {
                    mNameTask.setText(taskLoader.getNameTask() + "");
                    mValueTask.setText(taskLoader.getValueTask() + "");
                    mNameUser.setText(dataSnapshot.child("Users").child(taskLoader.getUserUID()).child("name").getValue() + "");
                    mPoints.setText(taskLoader.getPoints() + " points");
                    mTime.setText(taskLoader.getTime() + "");
                    mPhoneUser.setText(R.string.Show);
                }catch (Exception e){
                    Log.d("Error", e+"");
                }

                try {
                    mPhone = dataSnapshot.child("Users").child(taskLoader.getUserUID()).child("phone").getValue() + "";
                }catch (Exception e){

                }

                try {
                    if(taskLoader.getDone().equals("demo") && taskLoader.getAccepted().equals("demo")
                            && taskLoader.getUserTakeUID().equals("demo")){
                        point = Integer.parseInt(dataSnapshot.child("Users").child(user.getUid()).child("points").getValue()+"");
                    } else if(!taskLoader.getUserTakeUID().equals("none")){
                        point = Integer.parseInt(dataSnapshot.child("Users").child(taskLoader.getUserTakeUID()).child("points").getValue()+"");
                    }

                    if(taskLoader.getDone().equals("demo") && taskLoader.getAccepted().equals("demo")
                            && taskLoader.getUserTakeUID().equals("demo")) {

                        mPhoneUser.setText(mPhone);
                        mTakeTask.setBackgroundResource(R.drawable.button_round);
                        mTakeTask.setText(R.string.TakeTask);
                        mCheck.setImageResource(R.drawable.baseline_done_all_24px);
                        mCall.setVisibility(View.VISIBLE);

                    }else{
                        if (taskLoader.getAccepted().equals("end")) {
                            mPhoneUser.setText(mPhone);
                            mTakeTask.setBackgroundResource(R.drawable.button_round_green);
                            mTakeTask.setText(R.string.Finished);
                            mCheck.setImageResource(R.drawable.baseline_done_all_24px);
                            mCall.setVisibility(View.VISIBLE);
                        } else if (taskLoader.getUserUID().equals(user.getUid()) && taskLoader.getAccepted().equals("false")) {
                            mTakeTask.setText(R.string.NotGot);
                            mCheck.setImageResource(R.drawable.baseline_query_builder_24px);
                            mTakeTask.setBackgroundResource(R.drawable.button_round_grey);
                            mPhoneUser.setText(mPhone);
                        } else {
                            mTakeTask.setText(R.string.TakeTask);
                            mCheck.setImageResource(R.drawable.baseline_lock_open_24px);

                        }

                        if (taskLoader.getAccepted().equals("true")) {
                            if (!taskLoader.getUserUID().equals(user.getUid()) && !taskLoader.getUserTakeUID().equals(user.getUid())) {
                                mTakeTask.setText(R.string.AlreadyTaken);
                                mCheck.setImageResource(R.drawable.baseline_lock_24px);

                            } else if (taskLoader.getUserTakeUID().equals(user.getUid())) {
                                mTakeTask.setText(R.string.Refuse);
                                mTakeTask.setBackgroundResource(R.drawable.button_round_ligt_red);
                                mPhoneUser.setText(mPhone);
                                mCheck.setImageResource(R.drawable.baseline_done_24px);
                                mCall.setVisibility(View.VISIBLE);
                            } else if (taskLoader.getUserUID().equals(user.getUid())) {
                                mTakeTask.setText(R.string.Finish);
                                mCheck.setImageResource(R.drawable.baseline_done_24px);
                                mPhoneUser.setText(mPhone);
                                mCall.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                }catch (Exception e){
                    Log.d("Error", "Status not set");
                }


                try {
                    photo = taskLoader.getPhoto();
                }catch (Exception e){
                    Log.d("Error-Message", "Photo form server is null");
                }



                try {
                    if(photo.equals("ic_boy")){
                        try {
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy));
                        }catch (Exception e){
                            mPhoto.setImageResource(R.drawable.ic_launcher_round);
                        }
                    } else if(photo.equals("ic_boy1")){
                        try {
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy1));
                        }catch (Exception e){
                            mPhoto.setImageResource(R.drawable.ic_launcher_round);
                        }
                    }else if(photo.equals("ic_girl")){
                        try {
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl));
                        }catch (Exception e){
                            mPhoto.setImageResource(R.drawable.ic_launcher_round);
                        }
                    }else if(photo.equals("ic_girl1")){
                        try {
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl1));
                        }catch (Exception e){
                            mPhoto.setImageResource(R.drawable.ic_launcher_round);
                        }
                    }else if(photo.equals("ic_man1")){
                        try {
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_man1));
                        }catch (Exception e){
                            mPhoto.setImageResource(R.drawable.ic_launcher_round);
                        }
                    }else if(photo.equals("ic_man2")){
                        try {
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_man2));
                        }catch (Exception e){
                            mPhoto.setImageResource(R.drawable.ic_launcher_round);
                        }
                    }else if(photo.equals("ic_man3")){
                        try {
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_man3));
                        }catch (Exception e){
                            mPhoto.setImageResource(R.drawable.ic_launcher_round);
                        }
                    }else if(photo.equals("ic_man4")){
                        try {
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_man4));
                        }catch (Exception e){
                            mPhoto.setImageResource(R.drawable.ic_launcher_round);
                        }
                    }else if(photo.equals("velp")){
                        mPhoto.setImageResource(R.drawable.ic_launcher_round);
                    }else if(photo.equals("demo")){
                        mPhoto.setImageResource(R.drawable.ic_image2vector);
                    }
                }catch (Exception e){

                }


                try {
                        helped = Integer.parseInt(dataSnapshot.child("Users").child(taskLoader.getUserTakeUID()).child("helped").getValue() + "");
                }catch (Exception e){

                }



                try {
                    Query myTopPostsQuery = mDatabase.child("Users").child(taskLoader.getUserTakeUID()).child("rating").orderByChild("key");
                    myTopPostsQuery.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                            ratingData.add(new RatingData(dataSnapshot.child("nameRating").getValue() + "",
                                    dataSnapshot.child("valueRating").getValue() + "",
                                    dataSnapshot.child("key").getValue() + ""));

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
                }catch (Exception e){
                    Log.d("Error", "Open task Fragment" + e);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(postListener);







        mMapView = (MapView) rootView.findViewById(R.id.mapForOpenTask);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }

                googleMap.getUiSettings().setZoomControlsEnabled(true);


                LatLng myLocation = new LatLng(taskLoader.getLocationLatitude(), taskLoader.getLocationLongitude());
                googleMap.addMarker(new MarkerOptions().position(myLocation).title(taskLoader.getNameTask())).setTag(taskLoader.getKey());

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });



        mTakeTask.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                try {

                    // Custom Tutorial
                    SharedPreferences preferencesUserDemo = Objects.requireNonNull(getActivity()).getSharedPreferences("DemoUser", MODE_PRIVATE);
                    String userDemo = preferencesUserDemo.getString("DemoUser", "false");


                    if(userDemo.equals("false")){

                        if(taskLoader.getDone().equals("demo") && taskLoader.getAccepted().equals("demo")
                                && taskLoader.getUserTakeUID().equals("demo")){
                            // End Demo task
                            mTakeTask.setBackgroundResource(R.drawable.button_round_ligt_red);
                            mCheck.setImageResource(R.drawable.baseline_done_all_24px);
                            mTakeTask.setText(R.string.Refuse);

                            mTextGetTask.setText(getResources().getString(R.string.callDemo));
                            mCardGetTask.setVisibility(View.VISIBLE);
                            mCardGetTask.startAnimation(animVisibleCard);
                        }
                    }else{
                        Toast.makeText(getContext(),getResources().getString(R.string.youCompleteDemo), Toast.LENGTH_SHORT).show();
                    }



                        if (taskLoader.getUserUID().equals(user.getUid()) && taskLoader.getAccepted().equals("false")) { // Нельзя юоать свои
                            Toast.makeText(getActivity(), R.string.Own, Toast.LENGTH_LONG).show();
                            mDatabase.child("Task").child(KEY_Task).child("userTakeUID").setValue(user.getUid());
                            mCheck.setImageResource(R.drawable.baseline_query_builder_24px);
                        } else if (taskLoader.getUserUID().equals(user.getUid()) && taskLoader.getAccepted().equals("true")) { // Закончить задачц
                            // End task

                            if(taskLoader.getDoublePoints().equals("true")){
                                point += (Integer.parseInt(taskLoader.getPoints()) * 2);

                            }else{
                                point += Integer.parseInt(taskLoader.getPoints());
                            }
                            mDatabase.child("Users").child(taskLoader.getUserTakeUID()).child("points").setValue(point + "");
                            mTakeTask.setBackgroundResource(R.drawable.button_round_green);
                            mCheck.setImageResource(R.drawable.baseline_done_all_24px);
                            mTakeTask.setText(R.string.Finished);


                            helped++;

                            mDatabase.child("Users").child(taskLoader.getUserTakeUID()).child("helped").setValue(helped + "");


    //                 // add to user ochivments


                            float help_1_people = Float.parseFloat(ratingData.get(0).getValueRating());
                            if (help_1_people < 100) {
                                mDatabase.child("Users").child(taskLoader.getUserTakeUID()).child("rating").child(ratingData.get(0).getKey()).child("valueRating").setValue(100.0 + "");
                            }

                            float help_10_people = Float.parseFloat(ratingData.get(0).getValueRating());
                            if (help_10_people < 100) {
                                help_10_people += 10;
                                mDatabase.child("Users").child(taskLoader.getUserTakeUID()).child("rating").child(ratingData.get(1).getKey()).child("valueRating").setValue(help_10_people + "");
                            }


                            float help_100_people = Float.parseFloat(ratingData.get(1).getValueRating());
                            if (help_100_people < 100) {
                                help_100_people += 1;
                                mDatabase.child("Users").child(taskLoader.getUserTakeUID()).child("rating").child(ratingData.get(2).getKey()).child("valueRating").setValue(help_100_people + "");
                            }


                            float help_1000_people = Float.parseFloat(ratingData.get(2).getValueRating());
                            if (help_1000_people < 100) {
                                help_1000_people += 0.1;
                                mDatabase.child("Users").child(taskLoader.getUserTakeUID()).child("rating").child(ratingData.get(3).getKey()).child("valueRating").setValue(help_1000_people + "");
                            }


                            mDatabase.child("Task").child(KEY_Task).child("done").setValue(taskLoader.getUserTakeUID() + "");
                            mDatabase.child("Task").child(KEY_Task).child("accepted").setValue("end");


                        } else if (!taskLoader.getUserUID().equals(user.getUid()) && taskLoader.getAccepted().equals("false")) { // пользователь взял задачу
                            Toast.makeText(getActivity(), R.string.Taken, Toast.LENGTH_LONG).show();
                            mDatabase.child("Task").child(KEY_Task).child("accepted").setValue("true");
                            mDatabase.child("Task").child(KEY_Task).child("userTakeUID").setValue(user.getUid());
                            clickCounter++;


                        } else if (clickCounter > 0 || taskLoader.getUserTakeUID().equals(user.getUid()) && taskLoader.getDone().equals("false") ) { // Не кликай много раз
                            /* Refuse task */
                            // if you click in db margin acepted and userTakeUID set to null

                            AlertDialog.Builder freeBounuceAlertDialog = new AlertDialog.Builder(getActivity());
                            freeBounuceAlertDialog.setTitle(getString(R.string.Title_AlretDialogRefuse));
                            freeBounuceAlertDialog.setMessage(getString(R.string.Text_AlretDialogFreeRefuse));
                            freeBounuceAlertDialog.setCancelable(false);
                            freeBounuceAlertDialog.setIcon(R.drawable.ic_refuse);
                            // if set location in the task
                            freeBounuceAlertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mDatabase.child("Task").child(KEY_Task).child("accepted").setValue("false");
                                    mDatabase.child("Task").child(KEY_Task).child("userTakeUID").setValue("none");
                                    mTakeTask.setBackgroundResource(R.drawable.button_round);

                                }
                            });
                            freeBounuceAlertDialog.setNegativeButton(R.string.no, new  DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            freeBounuceAlertDialog.show();

                            mDatabase.child("Task").child(KEY_Task).child("userTakeUID").setValue(user.getUid());

                        } else if (taskLoader.getAccepted().equals("end")) {  // Задача законченна
                            Toast.makeText(getActivity(), R.string.AlreadyFinished, Toast.LENGTH_LONG).show();
                            mCheck.setImageResource(R.drawable.baseline_lock_24px);
                        } else if (taskLoader.getAccepted().equals("true")) { // Эту задачу кто-то взял
                            Toast.makeText(getActivity(), R.string.AlreadyTaken, Toast.LENGTH_LONG).show();
                            mCheck.setImageResource(R.drawable.baseline_lock_24px);
                        }

                }catch (Exception e){
                Toast.makeText(getContext(), "Hold on", Toast.LENGTH_LONG).show();
            }
            }
        });



        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();

        // Custom Tutorial
        SharedPreferences preferencesIcon = Objects.requireNonNull(getActivity()).getSharedPreferences("AlertTaskDirections",MODE_PRIVATE);
        String alertTaskIcon = preferencesIcon.getString("AlertTaskDirections","false");


        // Custom Tutorial
        SharedPreferences preferencesGetTask = Objects.requireNonNull(getActivity()).getSharedPreferences("TutorialGetTask",MODE_PRIVATE);
        String alertTaskGet = preferencesGetTask.getString("TutorialGetTask","false");


        if(alertTaskGet.equals("false")){
            mCardGetTask.setVisibility(View.VISIBLE);
            mCardGetTask.startAnimation(animVisibleCard);
        }else{
            if(alertTaskGet.equals("false")) {
                mCardTutorial.setVisibility(View.VISIBLE);
                mCardTutorial.startAnimation(animVisibleCard);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Bundle bundle = getArguments();
        if (bundle != null) {
            bundle.putString("TaskKey", "");
        }
        taskLoader = null;
    }
}
