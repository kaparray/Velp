package net.kaparray.velp.Auth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;


public class EmailAuthActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    EditText mPassword;
    EditText mEmail;
    Button mLogIn;
    Button mRegistration;
    TextView mForgetPas;
    public static final String TAG = "Email_Login_Incorrect";
    public ProgressDialog mProgressDialog;
    private Animation anim;
    private ImageView imageView;
    // Text view
    private TextView mForgetPass;

    @SuppressLint("CutPasteId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("theme",MODE_PRIVATE);
        String side = preferences.getString("THEME"," ");

        if (side.equals("dark")){
            setTheme(R.style.Theme_Design_NoActionBar);
        } else if (side.equals("light")){
            setTheme(R.style.AppTheme_NoActionBar);
        }

        setContentView(R.layout.ac_authemail);




        mAuth = FirebaseAuth.getInstance();

        mPassword = findViewById(R.id.et_passSignIn);
        mEmail =  findViewById(R.id.et_emailSignIn);

        mLogIn = findViewById(R.id.btn_loginSignIn);
        mRegistration = findViewById(R.id.btn_registrationSignIn);

        mForgetPas = findViewById(R.id.tv_forgetPass);

        imageView = (ImageView) findViewById(R.id.iv_ic_app);
        anim = AnimationUtils.loadAnimation(this, R.anim.rotate_animaton);





        mLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mLogIn.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                // Animation
                showProgressDialog();

                try{
                    mAuth.signInWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                            .addOnCompleteListener(EmailAuthActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        hideProgressDialog();
                                        // Go to MainActivity!!
                                        MainPage();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        hideProgressDialog();
                                        Toast.makeText(EmailAuthActivity.this, "Authentication failed. Please try again!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } catch (Exception e) {
                    Log.e("Error", "Error auth");
                    hideProgressDialog();
                    showMessage(R.string.ToastInSingIn
                    );
                }
            }
        });


        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmailAuthActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });


        mForgetPass = findViewById(R.id.tv_forgetPass);
        mForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mEmail.getText().toString().equals("")) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(mEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(EmailAuthActivity.this, "Message send to your email", Toast.LENGTH_LONG).show();
                                        Log.d(TAG, "Email sent.");
                                    }else{
                                        Toast.makeText(EmailAuthActivity.this, "Error message send", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                }else{
                    Toast.makeText(EmailAuthActivity.this, "Enter email", Toast.LENGTH_LONG).show();
                }
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.startAnimation(anim);
            }
        });

    }

    public void MainPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(EmailAuthActivity.this, string, Toast.LENGTH_LONG).show();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
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

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

}

