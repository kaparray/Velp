package net.kaparray.velp.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
import net.kaparray.velp.classes_for_data.MarkerData;
import net.kaparray.velp.classes_for_data.TaskLoader;

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fr_map, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.map);
        ((MainActivity) getActivity()).setTitle(getString(R.string.MapTitle));
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

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


                for (int i = 0; i < markerData.size(); i++){
                    LatLng latLng =  new LatLng(markerData.get(i).getLocationLongitude(),markerData.get(i).getLocationLatitude());
                    // Add new marker in map
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(markerData.get(i).getNameTask()));

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
                                        .addToBackStack(null)
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


}
