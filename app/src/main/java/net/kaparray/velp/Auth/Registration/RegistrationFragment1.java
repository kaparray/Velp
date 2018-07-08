package net.kaparray.velp.Auth.Registration;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.kaparray.velp.Auth.RegistrationActivity;
import net.kaparray.velp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationFragment1 extends android.support.v4.app.Fragment {



    EditText mEmail;
    EditText mPassword1;
    EditText mPassword2;

    @BindView(R.id.tv_mark) TextView mMark;

    @OnClick(R.id.btn_done1)
    public void submit() {

        if(mPassword1.getText().toString().equals(mPassword2.getText().toString()) && isEmailValid() && isPasswordsValid()
                && mPassword1.length() >= 6) {
            RegistrationFragment2 registrationFragment2 = new RegistrationFragment2();

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.main_content, registrationFragment2)
                    .addToBackStack(null)
                    .commit();

            ((RegistrationActivity) getActivity()).setEmail(mEmail.getText().toString());
            ((RegistrationActivity) getActivity()).setPassword(mPassword1.getText().toString());
            ((RegistrationActivity) getActivity()).setPasswordAg(mPassword2.getText().toString());
        }else if(!mPassword1.getText().toString().equals(mPassword2.getText().toString())){
            Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
        }else if(!isEmailValid()){
            Toast.makeText(getActivity(), "Email is not valid", Toast.LENGTH_SHORT).show();
        }else if (!isPasswordsValid()){
            Toast.makeText(getActivity(), "Password is not valid", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fr_registration1, container, false);

        ButterKnife.bind(this, rootView);

        // Find view element
        mEmail = rootView.findViewById(R.id.et_email);
        mPassword1 = rootView.findViewById(R.id.et_pass1);
        mPassword2 = rootView.findViewById(R.id.et_pass2);


        return rootView;
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


}
