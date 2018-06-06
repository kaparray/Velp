package net.kaparray.velp.Auth.Registration;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import net.kaparray.velp.R;
import net.kaparray.velp.utils.ProgressDialogActivity;

public class RegistrationFragment3 extends android.support.v4.app.Fragment {


    Button mRegister;

    ImageView mPhoto1;
    ImageView mPhoto2;
    ImageView mPhoto3;
    ImageView mPhoto4;
    ImageView mPhoto5;
    ImageView mPhoto6;
    ImageView mPhoto7;
    ImageView mPhoto8;

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


        mRegister = rootView.findViewById(R.id.btn_finishReg);

        mAuth = FirebaseAuth.getInstance();


        mPhoto1 = rootView.findViewById(R.id.imageView3);
        mPhoto2 = rootView.findViewById(R.id.imageView4);
        mPhoto3 = rootView.findViewById(R.id.imageView5);
        mPhoto5 = rootView.findViewById(R.id.imageView7);
        mPhoto6 = rootView.findViewById(R.id.imageView8);
        mPhoto7 = rootView.findViewById(R.id.imageView6);



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
                  
                showProgressDialog();

                    mAuth.createUserWithEmailAndPassword(((RegistrationActivity) getActivity()).getEmail(), ((RegistrationActivity) getActivity()).getPassword())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful() ) {

                                        if(!photo.equals("")) {
                                            // Sign in success, update UI with the signed-in user's information
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            DatabaseReference mUserAccount = myRef.child(user.getUid());
                                            mUserAccount.child("email").setValue(((RegistrationActivity) getActivity()).getEmail());
                                            mUserAccount.child("password").setValue(((RegistrationActivity) getActivity()).getPassword());
                                            mUserAccount.child("name").setValue(((RegistrationActivity) getActivity()).getName());
                                            mUserAccount.child("phone").setValue(((RegistrationActivity) getActivity()).getPhone());
                                            mUserAccount.child("age").setValue(((RegistrationActivity) getActivity()).getAge());
                                            mUserAccount.child("city").setValue(((RegistrationActivity) getActivity()).getCity());
                                            mUserAccount.child("points").setValue("100");
                                            mUserAccount.child("helped").setValue("0");     // Сколько людям помог
                                            mUserAccount.child("failedTheAssignment").setValue("0");      // Сколько проволил заданий
                                            mUserAccount.child("rating").setValue("0");   //Рейтинг
                                            mUserAccount.child("level").setValue("0");  // Уорвень
                                            mUserAccount.child("status").setValue("user");
                                            mUserAccount.child("photo").setValue(photo);


                                            DatabaseReference rating0 = mUserAccount.child("rating").push();
                                            rating0.child("nameRating").setValue("help_1_people");
                                            rating0.child("valueRating").setValue("0");
                                            rating0.child("key").setValue(rating0.getKey());

                                            DatabaseReference rating1 = mUserAccount.child("rating").push();
                                            rating1.child("nameRating").setValue("help_10_people");
                                            rating1.child("valueRating").setValue("0");
                                            rating1.child("key").setValue(rating1.getKey());


                                            DatabaseReference rating2 = mUserAccount.child("rating").push();
                                            rating2.child("nameRating").setValue("help_100_people");
                                            rating2.child("valueRating").setValue("0");
                                            rating2.child("key").setValue(rating2.getKey());

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
                                        Toast.makeText(getActivity(), "You have not filled in the data", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                hideProgressDialog();
            }

        });






        return rootView;
    }



    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage("Загрузка..");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }



    public void toSignIn(){
        Intent intent = new Intent(getActivity(), AuthActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}
