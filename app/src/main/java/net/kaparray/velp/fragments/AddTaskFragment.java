package net.kaparray.velp.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


public class AddTaskFragment extends android.support.v4.app.Fragment{

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Task");

    // Map
    private GoogleMap googleMap;
    private MapView mMapView;


    Button mAddTask;
    EditText mTask;
    EditText mValueTask;
    EditText mPointsTask;
    TaskFragment taskFragment;

    String name;
    String photo;
    String points;
    boolean counter;
    boolean counterMin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Checked theme app
        SharedPreferences preferencesTheme = getActivity().getSharedPreferences("theme",Context.MODE_PRIVATE);
        final String theme = preferencesTheme.getString("THEME"," ");
        // Set Fragment
        final View rootView = inflater.inflate(R.layout.fr_addtask, container, false);
        // Add title
        ((MainActivity) getActivity()).setTitle(getString(R.string.AddTaskTitle));

        mAddTask = rootView.findViewById(R.id.btn_addTask);
        mTask = rootView.findViewById(R.id.et_NameTask);
        mValueTask = rootView.findViewById(R.id.et_valueTask);
        mPointsTask = rootView.findViewById(R.id.et_pointsTask);

        // Set text color
        if (theme.equals("dark")) {
            mTask.setHintTextColor(getResources().getColor(R.color.soSoBlack));
            mValueTask.setHintTextColor(getResources().getColor(R.color.soSoBlack));
            mPointsTask.setHintTextColor(getResources().getColor(R.color.soSoBlack));
        }

        counter = true;
        counterMin = true;


        mMapView = (MapView) rootView.findViewById(R.id.mapForAddTask);
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
                    Log.d("0000", "WTF");
                }

                googleMap.getUiSettings().setZoomControlsEnabled(true);


                googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {
                        // For dropping a marker at a point on the Map
                        LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());

                        // For zooming automatically to the location of the marker
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(12).build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                });

            }
        });






        mAddTask.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
               imm.hideSoftInputFromWindow(mAddTask.getWindowToken(),
                       InputMethodManager.HIDE_NOT_ALWAYS);



               AlertDialog.Builder locationAlertDialog = new AlertDialog.Builder(getActivity());
               locationAlertDialog.setTitle(getString(R.string.Title_AlretDialogAddTask));
               locationAlertDialog.setCancelable(false);
               locationAlertDialog.setIcon(R.drawable.ic_map);
               locationAlertDialog.setMessage(getString(R.string.Text_AlretDialogAddTask));
               // if set location in the task
               locationAlertDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                   @RequiresApi(api = Build.VERSION_CODES.O)
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.cancel();

                       if(hasConnection(getContext())) {
                           taskFragment = new TaskFragment();

                           final DatabaseReference mUserAccount = myRef.push();
                           mUserAccount.child("key").setValue(mUserAccount.toString()
                                   .replace("https://velp-1544e.firebaseio.com/Task/", ""));

                           mUserAccount.child("points").setValue(mPointsTask.getText().toString()+"");
                           mUserAccount.child("userUID").setValue(user.getUid());
                           mUserAccount.child("nameTask").setValue(mTask.getText().toString());
                           mUserAccount.child("nameUser").setValue(user.getDisplayName());
                           mUserAccount.child("valueTask").setValue(mValueTask.getText().toString());
                           //mUserAccount.child("photoUser").setValue(user.getPhotoUrl());
                           mUserAccount.child("uniqueIdentificator").setValue(myRef.push().toString()
                                   .replaceAll("https://velp-1544e.firebaseio.com/Task/", ""));
                           // Add data about taken user
                           mUserAccount.child("accepted").setValue("false");
                           mUserAccount.child("userTakeUID").setValue("none");


                           //Date and time
                           @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd\n HH:mm");
                           String currentDateandTime = sdf.format(new Date());

                           mUserAccount.child("time").setValue(currentDateandTime + "");

                           // Location
                           googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                               @Override
                               public void onMyLocationChange(Location location) {
                                   if(counter) {
                                       Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("It's Me!"));
                                       marker.isVisible();
                                       mUserAccount.child("locationLatitude").setValue(location.getLatitude());
                                       mUserAccount.child("locationLongitude").setValue(location.getLongitude());
                                       counter = false;
                                   }

                               }
                           });




                           ValueEventListener postListener = new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                   if(counterMin) {
                                   // Get user data in Firebase
                                   points = (String) dataSnapshot.child("Users").child(user.getUid()).child("points").getValue();
                                   int pointsInt = Integer.parseInt(points);
                                   int pointsTask = Integer.parseInt(mPointsTask.getText().toString());
                                   int ans = pointsInt - pointsTask;
                                   mDatabase.child("Users").child(user.getUid()).child("points").setValue(ans + "");
                                       counterMin = false;
                                   }
                               }

                               @Override
                               public void onCancelled(DatabaseError databaseError) {

                               }
                           };
                           mDatabase.addValueEventListener(postListener);



                           Toast.makeText(getContext(), R.string.taskAddInDataBase, Toast.LENGTH_LONG).show();
                           getActivity().getSupportFragmentManager()
                                   .beginTransaction()
                                   .replace(R.id.container, taskFragment)
                                   .addToBackStack(null)
                                   .commit();
                       }else{
                           Toast.makeText(getActivity(), R.string.noInternet, Toast.LENGTH_LONG).show();
                       }
                   }
               });
               // if not set location in the task
               locationAlertDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       if(hasConnection(getContext())) {
                           taskFragment = new TaskFragment();

                           DatabaseReference mUserAccount = myRef.push();
                           mUserAccount.child("key").setValue(mUserAccount.toString()
                                   .replace("https://velp-1544e.firebaseio.com/Task/", ""));

                           mUserAccount.child("points").setValue(mPointsTask.getText().toString()+"");
                           mUserAccount.child("userUID").setValue(user.getUid());
                           mUserAccount.child("nameTask").setValue(mTask.getText().toString());
                           mUserAccount.child("nameUser").setValue(user.getDisplayName());
                           mUserAccount.child("valueTask").setValue(mValueTask.getText().toString());
                           //mUserAccount.child("photoUser").setValue(user.getPhotoUrl());
                           mUserAccount.child("uniqueIdentificator").setValue(myRef.push().toString()
                                   .replaceAll("https://velp-1544e.firebaseio.com/Task/", ""));
                           // Add data about taken user
                           mUserAccount.child("accepted").setValue("false");
                           mUserAccount.child("userTakeUID").setValue("none");


                           ValueEventListener postListener = new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                   if(counter) {
                                       // Get user data in Firebase
                                       points = (String) dataSnapshot.child("Users").child(user.getUid()).child("points").getValue();

                                       int pointsInt = Integer.parseInt(points);
                                       int pointsTask = Integer.parseInt(mPointsTask.getText().toString());

                                       int ans = pointsInt - pointsTask;
                                       mDatabase.child("Users").child(user.getUid()).child("points").setValue(ans + "");

                                       counter = false;
                                   }
                               }

                               @Override
                               public void onCancelled(DatabaseError databaseError) {

                               }
                           };
                           mDatabase.addValueEventListener(postListener);



                           Toast.makeText(getContext(), R.string.taskAddInDataBase, Toast.LENGTH_LONG).show();
                           getActivity().getSupportFragmentManager()
                                   .beginTransaction()
                                   .replace(R.id.container, taskFragment)
                                   .addToBackStack(null)
                                   .commit();
                       }else{
                           Toast.makeText(getActivity(), R.string.noInternet, Toast.LENGTH_LONG).show();
                       }
                   }
               });
               locationAlertDialog.show();


            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mAddTask.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }


    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }
}
