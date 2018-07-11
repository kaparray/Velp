package net.kaparray.velp.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import net.kaparray.velp.Auth.RegistrationActivity;
import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static net.kaparray.velp.fragments.ProfileFragment.TAG;


public class AddTaskFragment extends android.support.v4.app.Fragment{

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Task");

    // Map
    private GoogleMap googleMap;
    @BindView(R.id.mapForAddTask) MapView mMapView;

    // View
    @BindView(R.id.btn_addTask) Button mAddTask;
    @BindView(R.id.et_NameTask) EditText mTask;
    @BindView(R.id.et_valueTask) EditText mValueTask;
    @BindView(R.id.et_pointsTask) EditText mPointsTask;

    //Fragment
    TaskFragment taskFragment;
    BonusFragment bonusFragment;


    // Global variables
    String name;
    String photo;
    String points;
    boolean counter;
    boolean counterMin;
    int pointUser;

    LatLng myLocation = new LatLng(0,0);
    Marker markerLocation;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user data in Firebase
                pointUser =  Integer.parseInt((String)dataSnapshot.child("Users").child(user.getUid()).child("points").getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);
    }

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


        // Back stack
        ((MainActivity) getActivity()).setAddTask(false);

        //Butter Knife
        ButterKnife.bind(this, rootView);


        // Set text color
        if (theme.equals("dark")) {
            mTask.setHintTextColor(getResources().getColor(R.color.soSoBlack));
            mValueTask.setHintTextColor(getResources().getColor(R.color.soSoBlack));
            mPointsTask.setHintTextColor(getResources().getColor(R.color.soSoBlack));
        }



        counter = true;
        counterMin = true;

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                photo = dataSnapshot.child("Users").child(user.getUid()).child("photo").getValue() + "";
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);


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





        mAddTask.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               try{
                   if (pointUser > 0 && pointUser > Integer.parseInt(mPointsTask.getText().toString()) &&
                           Integer.parseInt(mPointsTask.getText().toString()) >= 0 && Integer.parseInt(mPointsTask.getText().toString()) <= 50000) {

                       if (!mPointsTask.getText().toString().equals("") && !mTask.getText().toString().equals("") &&
                               !mValueTask.getText().toString().equals("")) {
                           InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                           imm.hideSoftInputFromWindow(mAddTask.getWindowToken(),
                                   InputMethodManager.HIDE_NOT_ALWAYS);


                           AlertDialog.Builder locationAlertDialog = new AlertDialog.Builder(getActivity());
                           locationAlertDialog.setTitle(getString(R.string.Title_AlretDialogAddTask));
                           locationAlertDialog.setCancelable(false);
                           locationAlertDialog.setIcon(R.drawable.ic_map);
                           locationAlertDialog.setMessage(getString(R.string.Text_AlretDialogAddTask));
                           // if set location in the task
                           locationAlertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                               @RequiresApi(api = Build.VERSION_CODES.O)
                               @Override
                               public void onClick(DialogInterface dialogInterface, int i) {
                                   dialogInterface.cancel();

                                   if (hasConnection(getContext())) {
                                       taskFragment = new TaskFragment();

                                       final DatabaseReference mUserAccount = myRef.push();
                                       mUserAccount.child("key").setValue(mUserAccount.toString()
                                               .replace("https://velp-1544e.firebaseio.com/Task/", ""));

                                       mUserAccount.child("points").setValue(mPointsTask.getText().toString() + "");
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
                                       mUserAccount.child("photo").setValue(photo);
                                       mUserAccount.child("done").setValue("false");

                                       mUserAccount.child("doublePoints").setValue("false");     // double pints



                                       //Date and time
                                       @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd\n HH:mm");
                                       String currentDateandTime = sdf.format(new Date());

                                       mUserAccount.child("time").setValue(currentDateandTime + "");

                                       mUserAccount.child("locationLatitude").setValue(markerLocation.getPosition().latitude);
                                       mUserAccount.child("locationLongitude").setValue(markerLocation.getPosition().longitude);

                                       Geocoder geocoder;
                                       List<Address> addresses = null;
                                       geocoder = new Geocoder(getContext(), Locale.getDefault());

                                       try {
                                           addresses = geocoder.getFromLocation(markerLocation.getPosition().latitude, markerLocation.getPosition().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                       } catch (IOException e) {
                                           e.printStackTrace();
                                       }

                                       String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                       String city = addresses.get(0).getLocality();
                                       String state = addresses.get(0).getAdminArea();
                                       String country = addresses.get(0).getCountryName();
                                       String postalCode = addresses.get(0).getPostalCode();
                                       String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                                       mUserAccount.child("address").setValue(address);     // address
                                       mUserAccount.child("city").setValue(city);     // city
                                       mUserAccount.child("state").setValue(state);     // state
                                       mUserAccount.child("country").setValue(country);     // country
                                       mUserAccount.child("postalCode").setValue(postalCode);     // postalCode
                                       mUserAccount.child("knownName").setValue(knownName);     // knownName

                                       ValueEventListener postListener = new ValueEventListener() {
                                           @Override
                                           public void onDataChange(DataSnapshot dataSnapshot) {
                                               if (counterMin) {
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
                                               .commit();
                                   } else {
                                       Toast.makeText(getActivity(), R.string.noInternet, Toast.LENGTH_LONG).show();
                                   }
                               }
                           });
                           // if not set location in the task
                           locationAlertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   if (hasConnection(getContext())) {
                                       taskFragment = new TaskFragment();

                                       final DatabaseReference mUserAccount = myRef.push();
                                       mUserAccount.child("key").setValue(mUserAccount.toString()
                                               .replace("https://velp-1544e.firebaseio.com/Task/", ""));

                                       mUserAccount.child("points").setValue(mPointsTask.getText().toString() + "");
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
                                       mUserAccount.child("photo").setValue(photo);
                                       mUserAccount.child("done").setValue("false");


                                       //Date and time
                                       @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd\n HH:mm");
                                       String currentDateandTime = sdf.format(new Date());

                                       mUserAccount.child("time").setValue(currentDateandTime + "");


                                       ValueEventListener postListener = new ValueEventListener() {
                                           @Override
                                           public void onDataChange(DataSnapshot dataSnapshot) {
                                               if (counter) {
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
                                               .commit();
                                   } else {
                                       Toast.makeText(getActivity(), R.string.noInternet, Toast.LENGTH_LONG).show();
                                   }
                               }
                           });
                           locationAlertDialog.show();
                       } else {
                           Toast.makeText(getActivity(), R.string.Fill, Toast.LENGTH_SHORT).show();
                       }
                   } else if (pointUser < Integer.parseInt(mPointsTask.getText().toString()) || pointUser < 0) {
                       // No pints get free points for user
                       AlertDialog.Builder freeBounuceAlertDialog = new AlertDialog.Builder(getActivity());
                       freeBounuceAlertDialog.setTitle(getString(R.string.Title_AlretDialogFreePoints));
                       freeBounuceAlertDialog.setMessage(getString(R.string.Text_AlretDialogFreePoints));
                       freeBounuceAlertDialog.setCancelable(false);
                       freeBounuceAlertDialog.setIcon(R.drawable.free_points);
                       // if set location in the task
                       freeBounuceAlertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               // open free points

                               bonusFragment = new BonusFragment();

                               getActivity().getSupportFragmentManager()
                                       .beginTransaction()
                                       .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                       .replace(R.id.container, bonusFragment)
                                       .commit();

                           }
                       });
                       freeBounuceAlertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {

                           }
                       });
                       freeBounuceAlertDialog.show();

                   } else if (Integer.parseInt(mPointsTask.getText().toString()) <= 0) {
                       Toast.makeText(getActivity(), getString(R.string.remunerationLower), Toast.LENGTH_SHORT).show();
                   } else if (Integer.parseInt(mPointsTask.getText().toString()) >= 50000) {
                       Toast.makeText(getActivity(), getString(R.string.remunerationBiger), Toast.LENGTH_SHORT).show();
                       mPointsTask.setText("");
                   }
               }catch (NumberFormatException e){
                   Toast.makeText(getActivity(), getString(R.string.remunerationBiger), Toast.LENGTH_SHORT).show();
                   mPointsTask.setText("");
               }
               
               ((MainActivity) getActivity()).setAddTask(true);
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
