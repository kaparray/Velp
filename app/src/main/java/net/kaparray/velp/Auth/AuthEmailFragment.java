package net.kaparray.velp.Auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;


public class AuthEmailFragment extends Fragment{


    private FirebaseAuth mAuth;
    EditText mPassword;
    EditText mEmail;
    Button mLogIn;
    Button mRegistration;
    LinearLayout mLinearLayout;
    TextView mForgetPas;
    public static final String TAG = "Email_Login_Incorrect";
    public ProgressDialog mProgressDialog;
    private Animation anim;
    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fr_authemail, container, false);

        mAuth = FirebaseAuth.getInstance();

        mPassword = rootView.findViewById(R.id.et_passSignIn);
        mEmail =  rootView.findViewById(R.id.et_emailSignIn);

        mLogIn = rootView.findViewById(R.id.btn_loginSignIn);
        mRegistration = rootView.findViewById(R.id.btn_registrationSignIn);

        mForgetPas = rootView.findViewById(R.id.tv_forgetPass);

        imageView = (ImageView) rootView.findViewById(R.id.iv_ic_app);
        anim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_animaton);



        mLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mLogIn.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                // Animation
                showProgressDialog();

                try{
                    mAuth.signInWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
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
                                        Toast.makeText(getActivity(), "Authentication failed. Please try again!",
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
                Intent intent = new Intent(getActivity(), RegistrationActivity.class);
                startActivity(intent);
            }
        });


        mForgetPas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.startAnimation(anim);
            }
        });


        return rootView;
    }

    public void MainPage(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
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


