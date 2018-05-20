package net.kaparray.velp.Auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import net.kaparray.velp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RegistrationActivity extends AppCompatActivity{


    private static final String TAG = "Email_Login_Incorrect";
    private FirebaseAuth mAuth;

    Button mRegister;
    EditText mEmail;
    EditText mPassword1;
    EditText mPassword2;
    EditText mName;
    EditText mPhone;
    EditText mAge;
    EditText mCity;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");


    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @BindView(R.id.iv_photo1) ImageView mPhoto1;
    @BindView(R.id.iv_photo2) ImageView mPhoto2;
    @BindView(R.id.iv_photo3) ImageView mPhoto3;
    @BindView(R.id.iv_photo4) ImageView mPhoto4;
    @BindView(R.id.iv_photo5) ImageView mPhoto5;
    @BindView(R.id.iv_photo6) ImageView mPhoto6;
    @BindView(R.id.iv_photo7) ImageView mPhoto7;
    @BindView(R.id.iv_photo8) ImageView mPhoto8;


    String photo = "";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("theme",MODE_PRIVATE);
        String side = preferences.getString("THEME"," ");

        // Set theme
        if (side.equals("dark")){
            setTheme(R.style.Theme_Design_NoActionBar);
            setTheme(R.style.Theme_Design_NoActionBar);
        } else if (side.equals("light")){
            setTheme(R.style.AppTheme_NoActionBar);
        }
        // Set layout
        setContentView(R.layout.ac_registration);

        // Find view element
        mRegister = findViewById(R.id.btn_registration);
        mEmail = findViewById(R.id.et_email);
        mPassword1 = findViewById(R.id.et_pass1);
        mPassword2 = findViewById(R.id.et_pass2);
        mName = findViewById(R.id.et_name);
        mPhone = findViewById(R.id.et_phone);
        mAge = findViewById(R.id.et_age);
        mCity = findViewById(R.id.et_city);

        mAuth = FirebaseAuth.getInstance();


        // Set theme
        if (side.equals("dark")){
            mRegister.setTextColor(getResources().getColor(R.color.white));
            mEmail.setTextColor(getResources().getColor(R.color.white));
            mPassword1.setTextColor(getResources().getColor(R.color.white));
            mPassword2.setTextColor(getResources().getColor(R.color.white));
            mName.setTextColor(getResources().getColor(R.color.white));
            mPhone.setTextColor(getResources().getColor(R.color.white));
            mAge.setTextColor(getResources().getColor(R.color.white));
            mCity.setTextColor(getResources().getColor(R.color.white));
        } else if (side.equals("light")){
           //
        }

        ButterKnife.bind(this);

        mPhoto1.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy));
        mPhoto2.setImageDrawable(getResources().getDrawable(R.drawable.ic_boy1));
        mPhoto3.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl));
        mPhoto4.setImageDrawable(getResources().getDrawable(R.drawable.ic_girl1));
        mPhoto5.setImageDrawable(getResources().getDrawable(R.drawable.ic_man4));
        mPhoto6.setImageDrawable(getResources().getDrawable(R.drawable.ic_man1));
        mPhoto7.setImageDrawable(getResources().getDrawable(R.drawable.ic_man2));
        mPhoto8.setImageDrawable(getResources().getDrawable(R.drawable.ic_man3));


        mPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo = "ic_boy";
                mPhoto1.setBackground(getResources().getDrawable(R.drawable.button_round));
                mPhoto2.setBackground(null);
                mPhoto3.setBackground(null);
                mPhoto4.setBackground(null);
                mPhoto5.setBackground(null);
                mPhoto6.setBackground(null);
                mPhoto7.setBackground(null);
                mPhoto8.setBackground(null);
            }
        });


        mPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo = "ic_boy1";
                mPhoto2.setBackground(getResources().getDrawable(R.drawable.button_round));
                mPhoto1.setBackground(null);
                mPhoto3.setBackground(null);
                mPhoto4.setBackground(null);
                mPhoto5.setBackground(null);
                mPhoto6.setBackground(null);
                mPhoto7.setBackground(null);
                mPhoto8.setBackground(null);
            }
        });

        mPhoto3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo = "ic_girl";
                mPhoto3.setBackground(getResources().getDrawable(R.drawable.button_round));
                mPhoto2.setBackground(null);
                mPhoto1.setBackground(null);
                mPhoto4.setBackground(null);
                mPhoto5.setBackground(null);
                mPhoto6.setBackground(null);
                mPhoto7.setBackground(null);
                mPhoto8.setBackground(null);
            }
        });


        mPhoto4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo = "ic_girl1";
                mPhoto4.setBackground(getResources().getDrawable(R.drawable.button_round));
                mPhoto2.setBackground(null);
                mPhoto3.setBackground(null);
                mPhoto1.setBackground(null);
                mPhoto5.setBackground(null);
                mPhoto6.setBackground(null);
                mPhoto7.setBackground(null);
                mPhoto8.setBackground(null);
            }
        });


        mPhoto5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo = "ic_man4";
                mPhoto5.setBackground(getResources().getDrawable(R.drawable.button_round));
                mPhoto2.setBackground(null);
                mPhoto3.setBackground(null);
                mPhoto4.setBackground(null);
                mPhoto1.setBackground(null);
                mPhoto6.setBackground(null);
                mPhoto7.setBackground(null);
                mPhoto8.setBackground(null);
            }
        });


        mPhoto6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo = "ic_man1";
                mPhoto6.setBackground(getResources().getDrawable(R.drawable.button_round));
                mPhoto2.setBackground(null);
                mPhoto3.setBackground(null);
                mPhoto4.setBackground(null);
                mPhoto5.setBackground(null);
                mPhoto1.setBackground(null);
                mPhoto7.setBackground(null);
                mPhoto8.setBackground(null);
            }
        });

        mPhoto7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo = "ic_man2";
                mPhoto7.setBackground(getResources().getDrawable(R.drawable.button_round));
                mPhoto2.setBackground(null);
                mPhoto3.setBackground(null);
                mPhoto4.setBackground(null);
                mPhoto5.setBackground(null);
                mPhoto6.setBackground(null);
                mPhoto1.setBackground(null);
                mPhoto8.setBackground(null);
            }
        });


        mPhoto8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo = "ic_man3";
                mPhoto8.setBackground(getResources().getDrawable(R.drawable.button_round));
                mPhoto2.setBackground(null);
                mPhoto3.setBackground(null);
                mPhoto4.setBackground(null);
                mPhoto5.setBackground(null);
                mPhoto6.setBackground(null);
                mPhoto7.setBackground(null);
                mPhoto1.setBackground(null);
            }
        });







        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmailValid() && isPasswordsValid()) {
                    mAuth.createUserWithEmailAndPassword(mEmail.getText().toString(), mPassword1.getText().toString())
                            .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if (!mEmail.getText().toString().equals("") && !mPassword1.getText().toString().equals("") &&
                                                !mName.getText().toString().equals("") && !mAge.getText().toString().equals("") && !mAge.getText().toString().equals("")
                                                && !mCity.getText().toString().equals("") && !photo.equals("")) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "createUserWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            DatabaseReference mUserAccount = myRef.child(user.getUid());
                                            mUserAccount.child("email").setValue(mEmail.getText().toString());
                                            mUserAccount.child("password").setValue(mPassword1.getText().toString());
                                            mUserAccount.child("name").setValue(mName.getText().toString());
                                            mUserAccount.child("phone").setValue(mPhone.getText().toString());
                                            mUserAccount.child("age").setValue(mAge.getText().toString());
                                            mUserAccount.child("city").setValue(mCity.getText().toString());
                                            mUserAccount.child("points").setValue("100");
                                            mUserAccount.child("helped").setValue("0");     // Сколько людям помог
                                            mUserAccount.child("failedTheAssignment").setValue("0");      // Сколько проволил заданий
                                            mUserAccount.child("rating").setValue("0");   //Рейтинг
                                            mUserAccount.child("level").setValue("0");  // Уорвень
                                            mUserAccount.child("status").setValue("user");
                                            mUserAccount.child("photo").setValue(photo);


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
                                                    .setDisplayName(mName.getText().toString()).build();
                                            user.updateProfile(profileUpdatesName);
                                            toSignIn();
                                        }else{
                                            Toast.makeText(RegistrationActivity.this, "Вы не заполнили все поля!",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(RegistrationActivity.this, "You email or mpassword is not valid", Toast.LENGTH_LONG).show();
                }
            }
        });


    }




    private boolean isEmailValid() {
        return !TextUtils.isEmpty(mEmail.getText().toString()) &&
                Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString()).matches();
    }

    private boolean isPasswordsValid() {
        String password = mPassword1.getText().toString();
        String passwordAgain = mPassword2.getText().toString();

        return password.equals(passwordAgain)
                && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(passwordAgain);
    }

    public void toSignIn(){
        Intent intent = new Intent(RegistrationActivity.this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
}