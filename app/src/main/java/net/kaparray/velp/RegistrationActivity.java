package net.kaparray.velp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegistrationActivity extends AppCompatActivity{


    private static final String TAG = "Email_Login_Incorrect";
    private FirebaseAuth mAuth;

    Button mRegistr;
    EditText mEmail;
    EditText mPassword1;
    EditText mPassword2;
    EditText mName;
    EditText mPhone;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_registration);

        mAuth = FirebaseAuth.getInstance();

        mRegistr = findViewById(R.id.btn_registration);
        mEmail = findViewById(R.id.et_email);
        mPassword1 = findViewById(R.id.et_pass1);
        mPassword2 = findViewById(R.id.et_pass2);
        mName = findViewById(R.id.et_name);
        mPhone = findViewById(R.id.et_phone);


        mRegistr.setOnClickListener(new View.OnClickListener() {
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
