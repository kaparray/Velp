package net.kaparray.velp.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;
import net.kaparray.velp.classes_for_data.MarkerData;

import java.util.ArrayList;

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
    OpenTaskFragment openTaskFragment;

    ArrayList<MarkerData> markerData = new ArrayList<>();


    private ProgressBar mProgressBar;



    // Declare a variable for the cluster manager.
    private ClusterManager<MyItem> mClusterManager;

    private void setUpClusterer(GoogleMap map) {
        // Position the map.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(55.716911, 37.610526), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<MyItem>(getContext(), map);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        map.setOnCameraIdleListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {


        for (int i = 0; i < markerData.size(); i++){
            // Add new marker in map

            MyItem offsetItem = new MyItem(markerData.get(i).getLocationLongitude(),markerData.get(i).getLocationLatitude());
            mClusterManager.addItem(offsetItem);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fr_map, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.map);
        ((MainActivity) getActivity()).setTitle(getString(R.string.MapTitle));
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately


        mProgressBar = rootView.findViewById(R.id.progressBarInMap);
        mProgressBar.setVisibility(View.VISIBLE);



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



                Query myMostViewedPostsQuery = mDatabase.child("Task");
                myMostViewedPostsQuery.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.d("0000", dataSnapshot.child("locationLatitude").getValue() + "");
                        if(dataSnapshot.child("locationLatitude").getValue() != null){

                            markerData.add(new MarkerData((double)dataSnapshot.child("locationLongitude").getValue(),
                                    (double)dataSnapshot.child("locationLatitude").getValue(),
                                    dataSnapshot.child("key").getValue()+"", dataSnapshot.child("nameTask").getValue()+""));

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


                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        for (int i = 0; i < markerData.size(); i++){
                            if(markerData.get(i).getKey() == marker.getTag()){

                                openTaskFragment =  new OpenTaskFragment();

                                Bundle bundle = new Bundle();
                                bundle.putString("TaskKey", marker.getTag()+"");
                                openTaskFragment.setArguments(bundle);

                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .replace(R.id.container, openTaskFragment)
                                        .commit();
                            }
                        }

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