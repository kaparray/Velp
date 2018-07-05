package net.kaparray.velp.Auth.Registration;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import net.kaparray.velp.Auth.RegistrationActivity;
import net.kaparray.velp.R;
import net.kaparray.velp.classes_for_data.TaskLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationFragment4 extends android.support.v4.app.Fragment {

    // Map
    private GoogleMap googleMap;
    @BindView(R.id.mapView_Register)
    MapView mapView;

    Marker markerLocation;
    LatLng myLocation;



    @OnClick(R.id.btn_done4)
    public void submit4() {




        RegistrationFragment3 registrationFragment3 = new RegistrationFragment3();

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.main_content, registrationFragment3)
                .addToBackStack(null)
                .commit();
        ((RegistrationActivity) getActivity()).setLocationLongitude(markerLocation.getPosition().longitude+"");
        ((RegistrationActivity) getActivity()).setLocationLatitude(markerLocation.getPosition().latitude+"");
    }



    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fr_registration4, container, false);

        ButterKnife.bind(this, rootView);


        mapView.onCreate(savedInstanceState);

        mapView.onResume(); // needed to get the map to display immediately


        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    Log.d("0000", "WTF");
                }

                googleMap.getUiSettings().setZoomControlsEnabled(true);



                LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();

                Location location = locationManager.getLastKnownLocation(locationManager
                        .getBestProvider(criteria, false));
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();




                markerLocation = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title("set marker for task" + latitude + "  " + longitude));

                markerLocation.setDraggable(true);


                googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        markerLocation.setPosition(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude));
                    }
                });

                googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {
                        // For dropping a marker at a point on the Map
                        myLocation = new LatLng(location.getLatitude(), location.getLongitude());

                        // For zooming automatically to the location of the marker
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(12).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                });
            }
        });



        return rootView;
    }



    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}



