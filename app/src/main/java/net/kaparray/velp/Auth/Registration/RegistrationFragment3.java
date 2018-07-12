package net.kaparray.velp.Auth.Registration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.kaparray.velp.Auth.AuthActivity;
import net.kaparray.velp.Auth.RegistrationActivity;
import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationFragment3 extends android.support.v4.app.Fragment {


    Button mRegister;

    ImageView mPhoto1;
    ImageView mPhoto2;
    ImageView mPhoto3;
    ImageView mPhoto5;
    ImageView mPhoto6;
    ImageView mPhoto7;

    ProgressBar mPB;
    @BindView(R.id.tv_choose)
    TextView mTextChoose;

    View rootView;


    String photo = "";



    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");


    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fr_registration3, container, false);

        ButterKnife.bind(this, rootView);

        mPB = rootView.findViewById(R.id.progressBarRegFin);

        mRegister = rootView.findViewById(R.id.btn_finishReg);

        mAuth = FirebaseAuth.getInstance();


        mPhoto1 = rootView.findViewById(R.id.imageView3);
        mPhoto2 = rootView.findViewById(R.id.imageView4);
        mPhoto3 = rootView.findViewById(R.id.imageView5);
        mPhoto5 = rootView.findViewById(R.id.imageView7);
        mPhoto6 = rootView.findViewById(R.id.imageView8);
        mPhoto7 = rootView.findViewById(R.id.imageView6);



        // set for normal position
        mPhoto1.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy));
        mPhoto2.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy1));
        mPhoto3.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl));
        mPhoto7.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl1));
        mPhoto5.setImageDrawable(getResources().getDrawable(R.drawable.ic_man4));
        mPhoto6.setImageDrawable(getResources().getDrawable(R.drawable.ic_man1));

        mPhoto1.setImageDrawable(null);
        mPhoto2.setImageDrawable(null);
        mPhoto3.setImageDrawable(null);
        mPhoto7.setImageDrawable(null);
        mPhoto5.setImageDrawable(null);
        mPhoto6.setImageDrawable(null);

        mPhoto1.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy));
        mPhoto2.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy1));
        mPhoto3.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl));
        mPhoto7.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl1));
        mPhoto5.setImageDrawable(getResources().getDrawable(R.drawable.ic_man4));
        mPhoto6.setImageDrawable(getResources().getDrawable(R.drawable.ic_man1));


        mPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo = "ic_boy";
                mPhoto1.setBackground(getResources().getDrawable(R.drawable.button_round));
                mPhoto2.setBackground(null);
                mPhoto3.setBackground(null);
                mPhoto5.setBackground(null);
                mPhoto6.setBackground(null);
                mPhoto7.setBackground(null);
            }
        });


        mPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo = "ic_boy1";
                mPhoto2.setBackground(getResources().getDrawable(R.drawable.button_round));
                mPhoto1.setBackground(null);
                mPhoto3.setBackground(null);
                mPhoto5.setBackground(null);
                mPhoto6.setBackground(null);
                mPhoto7.setBackground(null);
            }
        });

        mPhoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo = "ic_girl";
                mPhoto3.setBackground(getResources().getDrawable(R.drawable.button_round));
                mPhoto2.setBackground(null);
                mPhoto1.setBackground(null);
                mPhoto5.setBackground(null);
                mPhoto6.setBackground(null);
                mPhoto7.setBackground(null);
            }
        });


        mPhoto7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo = "ic_girl1";
                mPhoto7.setBackground(getResources().getDrawable(R.drawable.button_round));
                mPhoto2.setBackground(null);
                mPhoto3.setBackground(null);
                mPhoto1.setBackground(null);
                mPhoto5.setBackground(null);
                mPhoto6.setBackground(null);
            }
        });


        mPhoto5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo = "ic_man4";
                mPhoto5.setBackground(getResources().getDrawable(R.drawable.button_round));

                mPhoto2.setBackground(null);
                mPhoto3.setBackground(null);
                mPhoto1.setBackground(null);
                mPhoto6.setBackground(null);
                mPhoto7.setBackground(null);
            }
        });


        mPhoto6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo = "ic_man1";
                mPhoto6.setBackground(getResources().getDrawable(R.drawable.button_round));

                mPhoto2.setBackground(null);
                mPhoto3.setBackground(null);
                mPhoto1.setBackground(null);
                mPhoto5.setBackground(null);
                mPhoto7.setBackground(null);
            }
        });







        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPB.setVisibility(View.VISIBLE);
                mPhoto1.setVisibility(View.GONE);
                mPhoto2.setVisibility(View.GONE);
                mPhoto3.setVisibility(View.GONE);
                mPhoto7.setVisibility(View.GONE);
                mPhoto5.setVisibility(View.GONE);
                mPhoto6.setVisibility(View.GONE);
                mTextChoose.setVisibility(View.GONE);

                mAuth.createUserWithEmailAndPassword(((RegistrationActivity) getActivity()).getEmail(), ((RegistrationActivity) getActivity()).getPassword())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) { if (task.isSuccessful() ) {


                                if(!photo.equals("")) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    DatabaseReference mUserAccount = myRef.child(user.getUid());
                                    mUserAccount.child("email").setValue(((RegistrationActivity) getActivity()).getEmail()); // email
                                    mUserAccount.child("password").setValue(((RegistrationActivity) getActivity()).getPassword()); // password
                                    mUserAccount.child("name").setValue(((RegistrationActivity) getActivity()).getName());  // name
                                    mUserAccount.child("phone").setValue(((RegistrationActivity) getActivity()).getPhone()); // phone
                                    mUserAccount.child("age").setValue(((RegistrationActivity) getActivity()).getAge()); // age
                                    mUserAccount.child("locationLongitude").setValue(((RegistrationActivity) getActivity()).getLocationLongitude()); // Longitude user
                                    mUserAccount.child("locationLatitude").setValue(((RegistrationActivity) getActivity()).getLocationLatitude()); // Latitude user
                                    mUserAccount.child("points").setValue("100"); // points
                                    mUserAccount.child("helped").setValue("0");     // how many people helped
                                    mUserAccount.child("failedTheAssignment").setValue("0");      // Сколько проволил заданий
                                    mUserAccount.child("rating").setValue("0");   //Рейтинг
                                    mUserAccount.child("level").setValue("0");  // Уорвень
                                    mUserAccount.child("status").setValue("user"); // status of  user
                                    mUserAccount.child("photo").setValue(photo);    // set photo
                                    mUserAccount.child("lastFreePoints").setValue("0");     // time for free points


                                    Geocoder geocoder;
                                    List<Address> addresses = null;
                                    geocoder = new Geocoder(getContext(), Locale.getDefault());

                                    try {
                                        addresses = geocoder.getFromLocation(Double.parseDouble(((RegistrationActivity) getActivity()).getLocationLatitude()), Double.parseDouble(((RegistrationActivity) getActivity()).getLocationLongitude()), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
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







                                    // rating 1
                                    DatabaseReference rating0 = mUserAccount.child("rating").push();
                                    rating0.child("nameRating").setValue("help_1_people");
                                    rating0.child("valueRating").setValue("0");
                                    rating0.child("key").setValue(rating0.getKey());

                                    // rating 2
                                    DatabaseReference rating1 = mUserAccount.child("rating").push();
                                    rating1.child("nameRating").setValue("help_10_people");
                                    rating1.child("valueRating").setValue("0");
                                    rating1.child("key").setValue(rating1.getKey());

                                    // rating 3
                                    DatabaseReference rating2 = mUserAccount.child("rating").push();
                                    rating2.child("nameRating").setValue("help_100_people");
                                    rating2.child("valueRating").setValue("0");
                                    rating2.child("key").setValue(rating2.getKey());

                                    // rating 4
                                    DatabaseReference rating3 = mUserAccount.child("rating").push();
                                    rating3.child("nameRating").setValue("help_1000_people");
                                    rating3.child("valueRating").setValue("0");
                                    rating3.child("key").setValue(rating3.getKey());


                                    UserProfileChangeRequest profileUpdatesName = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(((RegistrationActivity) getActivity()).getName()).build();
                                    user.updateProfile(profileUpdatesName);
                                    toSignIn();

                                    }
                            } else {
                                    Toast.makeText(getActivity(), getResources().getString(R.string.dataF), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


        return rootView;
    }




    public void toSignIn(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}
