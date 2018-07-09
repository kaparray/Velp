package net.kaparray.velp.fragments;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;
import net.kaparray.velp.classes_for_data.TaskLoader;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;
import static net.kaparray.velp.fragments.ProfileFragment.TAG;

public class MapFragment extends Fragment {


    // Map
    private GoogleMap googleMap;
    private MapView mMapView;
    // Database FireBase
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    // List for all marker's in map
    ArrayList<Marker> arrayListMarker = new ArrayList<>();
    // New open task fragment

    ArrayList<TaskLoader> markerData = new ArrayList<>();


    private ProgressBar mProgressBar;
    OpenTaskFragment openTaskFragment;


    @BindView(R.id.cv_TaskMap)
    CardView mCardInfo;
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
    @BindView(R.id.cv_mapTutorial)
    CardView mTutorial;
    @BindView(R.id.btn_okTutorialMap)
    Button mOkTurorial;

    Animation anim;

    String KEY;

    String lat;
    String log;

    View rootView;

    //Animation
    Animation animVisibleCard;
    Animation animGoneCard;


    // Declare a variable for the cluster manager.
    private ClusterManager<MyItem> mClusterManager;

    private void setUpClusterer(final GoogleMap map) {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user data in Firebase
                lat = (String) dataSnapshot.child("Users").child(user.getUid()).child("locationLatitude").getValue();
                log = (String) dataSnapshot.child("Users").child(user.getUid()).child("locationLongitude").getValue();

                // Position the map.
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(lat), Double.parseDouble(log)), 10));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);


        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        try {
            mClusterManager = new ClusterManager<MyItem>(getContext(), map);
        } catch (Exception e) {

        }

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        map.setOnCameraIdleListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        try {
            addItems();
        } catch (Exception e) {

        }


        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem myItem) {

                ((MainActivity) getActivity()).setMapCard(true);

                Double log = myItem.getPosition().latitude;
                Double lat = myItem.getPosition().longitude;

                for (int i = 0; i < markerData.size(); i++) {
                    if (lat == markerData.get(i).getLocationLatitude() && log == markerData.get(i).getLocationLongitude()) {
                        mNameTask.setText(markerData.get(i).getNameTask());
                        mValueTask.setText(markerData.get(i).getValueTask());
                        mNameUser.setText(markerData.get(i).getNameUser());
                        mPoints.setText(markerData.get(i).getPoints() + " points");
                        mTime.setText(markerData.get(i).getTime());

                        KEY = markerData.get(i).getKey();

                        String photo = markerData.get(i).getPhoto();

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
                    }
                }


                anim = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_animation);
                mCardInfo.startAnimation(anim);

                mCardInfo.setVisibility(View.VISIBLE);


                return false;
            }
        });
    }

    private void addItems() {


        for (int i = 0; i < markerData.size(); i++) {
            // Add new marker in map

            MyItem offsetItem = new MyItem(markerData.get(i).getLocationLongitude(), markerData.get(i).getLocationLatitude());

            mClusterManager.addItem(offsetItem);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fr_map, container, false);

        ButterKnife.bind(this, rootView);

        // Back stack
        ((MainActivity) getActivity()).setAddTask(false);

        mMapView = (MapView) rootView.findViewById(R.id.map);
        ((MainActivity) getActivity()).setTitle(getString(R.string.MapTitle));
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately


        mProgressBar = rootView.findViewById(R.id.progressBarInMap);
        mProgressBar.setVisibility(View.VISIBLE);


        // Animation
        animVisibleCard = AnimationUtils.loadAnimation(getContext(), R.anim.beta_animation_games);
        animGoneCard = AnimationUtils.loadAnimation(getContext(), R.anim.beta_animation_back);

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        openTaskFragment = new OpenTaskFragment();

        mCardInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.container, openTaskFragment)
                        .commit();

                ((MainActivity) getActivity()).setMapCard(false);


                // This is magic bundle. I transit data in DB to OpenTaskFragment
                Bundle bundle = new Bundle();
                bundle.putString("TaskKey", KEY);
                openTaskFragment.setArguments(bundle);
            }
        });


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


                Query myMostViewedPostsQuery = mDatabase.child("Task");
                myMostViewedPostsQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if (dataSnapshot.child("locationLatitude").getValue() != null) {

                            markerData.add(new TaskLoader(dataSnapshot.child("nameTask").getValue() + "", dataSnapshot.child("valueTask").getValue() + "",
                                    dataSnapshot.child("nameUser").getValue() + "", dataSnapshot.child("points").getValue() + "",
                                    dataSnapshot.child("acepted").getValue() + "",
                                    (double) Double.parseDouble(dataSnapshot.child("locationLongitude").getValue() + ""), (double) Double.parseDouble(dataSnapshot.child("locationLatitude").getValue() + ""),
                                    dataSnapshot.child("time").getValue() + "",
                                    dataSnapshot.child("photo").getValue() + "", dataSnapshot.child("key").getValue() + ""));


                        }

                        setUpClusterer(googleMap);


                        // Hide progress bar
                        mProgressBar.setVisibility(View.GONE);
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
        });


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        markerData.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        markerData.clear();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @OnClick(R.id.btn_okTutorialMap)
    void tutorial(){
        mTutorial.startAnimation(animGoneCard);

        // For settings
        SharedPreferences preferences = getActivity().getSharedPreferences("AlertMap", MODE_PRIVATE);
        SharedPreferences.Editor editorView = preferences.edit();
        editorView.putString("AlertMap", "true");
        editorView.apply();

        mTutorial.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Custom Tutorial
        SharedPreferences preferencesIcon = Objects.requireNonNull(getActivity()).getSharedPreferences("AlertMap", MODE_PRIVATE);
        String alertTaskIcon = preferencesIcon.getString("AlertMap", "false");


        if (alertTaskIcon.equals("false")) {
            mTutorial.setVisibility(View.VISIBLE);
            mTutorial.startAnimation(animVisibleCard);
        }
    }
}

class MyItem implements ClusterItem {
    private final LatLng mPosition;

    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }



}
