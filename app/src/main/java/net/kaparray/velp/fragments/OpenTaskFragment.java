package net.kaparray.velp.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import net.kaparray.velp.R;
import net.kaparray.velp.classes_for_data.RatingData;
import net.kaparray.velp.classes_for_data.TaskLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


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
    @BindView(R.id.tv_time) TextView mTime;
    @BindView(R.id.tv_pointsInOpenFragment) TextView mPoints;
    @BindView(R.id.iv_photoTask) ImageView mPhoto;


    // String variable
    String KEY_Task;
    String photo;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fr_opentask, container, false);


        ButterKnife.bind(this, rootView);


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
                }catch (Exception e){
                    Log.d("Error", e+"");
                }


                name = dataSnapshot.child("Users").child(taskLoader.getUserUID()).child("name").getValue() + "";
                mDatabase.child("Task").child(taskLoader.getKey()).child("nameUser").setValue(name);


                 if(!taskLoader.getUserTakeUID().equals("none")){
                    point = Integer.parseInt(dataSnapshot.child("Users").child(taskLoader.getUserTakeUID()).child("points").getValue()+"");
                }

                if(taskLoader.getAccepted().equals("end")){
                    mTakeTask.setBackgroundResource(R.drawable.button_round_green);
                    mTakeTask.setText(R.string.Finished);
                }else if(taskLoader.getUserUID().equals(user.getUid()) && taskLoader.getAccepted().equals("false")){
                    mTakeTask.setText(R.string.NotGot);
                    mTakeTask.setBackgroundResource(R.drawable.button_round_grey);
                 }else{
                    mTakeTask.setText(R.string.TakeTask);
                }

                if(taskLoader.getAccepted().equals("true")){
                    if(!taskLoader.getUserUID().equals(user.getUid()) && !taskLoader.getUserTakeUID().equals(user.getUid())){
                        mTakeTask.setText(R.string.AlreadyTaken);
                    }else if(taskLoader.getUserTakeUID().equals(user.getUid())){
                        mTakeTask.setText(R.string.Taken);
                    }else if(taskLoader.getUserUID().equals(user.getUid())){
                        mTakeTask.setText(R.string.Finish);

                    }
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
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_round));
                        }
                    } else if(photo.equals("ic_boy1")){
                        try {
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy1));
                        }catch (Exception e){
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_round));
                        }
                    }else if(photo.equals("ic_girl")){
                        try {
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl));
                        }catch (Exception e){
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_round));
                        }
                    }else if(photo.equals("ic_girl1")){
                        try {
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl1));
                        }catch (Exception e){
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_round));
                        }
                    }else if(photo.equals("ic_man1")){
                        try {
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_man1));
                        }catch (Exception e){
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_round));
                        }
                    }else if(photo.equals("ic_man2")){
                        try {
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_man2));
                        }catch (Exception e){
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_round));
                        }
                    }else if(photo.equals("ic_man3")){
                        try {
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_man3));
                        }catch (Exception e){
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_round));
                        }
                    }else if(photo.equals("ic_man4")){
                        try {
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_man4));
                        }catch (Exception e){
                            mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_round));
                        }
                    }else if(photo.equals("velp")){
                        mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_round));
                    }
                }catch (NullPointerException e){
                    mPhoto.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_round));
                }


                try {
                    helped = Integer.parseInt(dataSnapshot.child("Users").child(taskLoader.getUserTakeUID()).child("helped").getValue() + "");
                }catch (Exception e){

                }




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
                if(taskLoader.getUserUID().equals(user.getUid()) && taskLoader.getAccepted().equals("false")){ // Нельзя юоать свои
                    Toast.makeText(getActivity(), R.string.Own, Toast.LENGTH_LONG).show();
                } else if(taskLoader.getUserUID().equals(user.getUid()) && taskLoader.getAccepted().equals("true")){ // Закончить задачц
                    // End task
                    point += Integer.parseInt(taskLoader.getPoints());
                    mDatabase.child("Users").child(taskLoader.getUserTakeUID()).child("points").setValue(point+"");
                    mTakeTask.setBackgroundResource(R.drawable.button_round_green);
                    mTakeTask.setText(R.string.Finished);

                    helped++;

                    mDatabase.child("Users").child(taskLoader.getUserTakeUID()).child("helped").setValue(helped+"");


//                 // add to user ochivments

                    float help_10_people = Float.parseFloat(ratingData.get(0).getValueRating());
                    if(help_10_people  <= 100) {
                        help_10_people += 10;
                        mDatabase.child("Users").child(taskLoader.getUserTakeUID()).child("rating").child(ratingData.get(0).getKey()).child("valueRating").setValue(help_10_people + "");
                    }


                    float help_100_people = Float.parseFloat(ratingData.get(1).getValueRating());
                    if(help_100_people  <= 100) {
                        help_100_people += 1;
                        mDatabase.child("Users").child(taskLoader.getUserTakeUID()).child("rating").child(ratingData.get(1).getKey()).child("valueRating").setValue(help_100_people + "");
                    }


                    float help_1000_people = Float.parseFloat(ratingData.get(2).getValueRating());
                    if(help_1000_people <= 100) {
                        help_1000_people += 0.1;
                        mDatabase.child("Users").child(taskLoader.getUserTakeUID()).child("rating").child(ratingData.get(2).getKey()).child("valueRating").setValue(help_1000_people + "");
                    }


                    mDatabase.child("Task").child(KEY_Task).child("done").setValue(user.getUid()+"");
                    mDatabase.child("Task").child(KEY_Task).child("accepted").setValue("end");


                } else if(!taskLoader.getUserUID().equals(user.getUid()) && taskLoader.getAccepted().equals("false")){ // пользователь взял задачу
                    Toast.makeText(getActivity(), R.string.Taken, Toast.LENGTH_LONG).show();
                    mDatabase.child("Task").child(KEY_Task).child("accepted").setValue("true");
                    mDatabase.child("Task").child(KEY_Task).child("userTakeUID").setValue(user.getUid());
                    clickCounter++;


                } else if (clickCounter > 0 || taskLoader.getUserTakeUID().equals(user.getUid())){ // Не кликай много раз
                    Toast.makeText(getActivity(), R.string.Taken, Toast.LENGTH_LONG).show();
                }else if(taskLoader.getAccepted().equals("end")){  // Задача законченна
                    Toast.makeText(getActivity(), R.string.AlreadyFinished, Toast.LENGTH_LONG).show();
                }else if(taskLoader.getAccepted().equals("true")){ // Эту задачу кто-то взял
                    Toast.makeText(getActivity(), R.string.AlreadyTaken, Toast.LENGTH_LONG).show();
                }
            }
        });


        return rootView;
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
