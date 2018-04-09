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





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("theme",MODE_PRIVATE);
        String side = preferences.getString("THEME"," ");

        // Set theme
        if (side.equals("dark")){
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



        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmailValid() && isPasswordsValid()) {
                    mAuth.createUserWithEmailAndPassword(mEmail.getText().toString(), mPassword1.getText().toString())
                            .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
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
                                        //mUserAccount.child("status").setValue("photo");

                                        DatabaseReference rating1 = mUserAccount.child("rating").push();
                                        rating1.child("nameRating").setValue("help_10_people");
                                        rating1.child("valueRating").setValue("0");

                                        DatabaseReference rating2 = mUserAccount.child("rating").push();
                                        rating2.child("nameRating").setValue("help_10_people");
                                        rating2.child("valueRating").setValue("0");

                                        DatabaseReference rating3 = mUserAccount.child("rating").push();
                                        rating3.child("nameRating").setValue("help_100_people");
                                        rating3.child("valueRating").setValue("0");

                                        DatabaseReference rating4 = mUserAccount.child("rating").push();
                                        rating4.child("nameRating").setValue("help_1000_people");
                                        rating4.child("valueRating").setValue("0");

                                        DatabaseReference rating5 = mUserAccount.child("rating").push();
                                        rating5.child("nameRating").setValue("Help_10_people_over_50");
                                        rating5.child("valueRating").setValue("0");

//                                        rating.child("Help_100_people_over_50").setValue("0");
//                                        rating.child("Get_100_points").setValue("0");
//                                        rating.child("Get_1000_points").setValue("0");
//                                        rating.child("Get_the_10th_level").setValue("0");
//                                        rating.child("Get the 50th level").setValue("0");
//                                        rating.child("Get_the_100_level").setValue("0");
//                                        rating.child("Help on 3 events").setValue("0");
//                                        rating.child("Help_on_5_events").setValue("0");
//                                        rating.child("Help_on_10_events").setValue("0");
//                                        rating.child("Help_on_30_events").setValue("0");
//                                        rating.child("Help_on_50_events").setValue("0");
//                                        rating.child("Help_on_100_events").setValue("0");

                                        UserProfileChangeRequest profileUpdatesName = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(mName.getText().toString()).build();
                                        user.updateProfile(profileUpdatesName);
                                        toSignIn();

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