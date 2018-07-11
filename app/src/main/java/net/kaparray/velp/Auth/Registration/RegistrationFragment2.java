package net.kaparray.velp.Auth.Registration;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import net.kaparray.velp.Auth.RegistrationActivity;
import net.kaparray.velp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationFragment2 extends android.support.v4.app.Fragment {





    EditText mName;
    EditText mPhone;
    EditText mAge;



    @OnClick(R.id.btn_done2)
    public void submit2() {

        if(!mName.getText().toString().equals("") && !mPhone.getText().toString().equals("") && !mAge.getText().toString().equals("")) {
            RegistrationFragment4 registrationFragment4 = new RegistrationFragment4();

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.main_content, registrationFragment4)
                    .addToBackStack(null)
                    .commit();

            ((RegistrationActivity) getActivity()).setName(mName.getText().toString());
            ((RegistrationActivity) getActivity()).setPhone(mPhone.getText().toString());
            ((RegistrationActivity) getActivity()).setAge(mAge.getText().toString());

        }else{
            Toast.makeText(getActivity(), getResources().getString(R.string.dataF), Toast.LENGTH_SHORT).show();
        }

    }



    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fr_registration2, container, false);

        ButterKnife.bind(this, rootView);

        mName = rootView.findViewById(R.id.et_name);
        mPhone = rootView.findViewById(R.id.et_phone);
        mAge = rootView.findViewById(R.id.et_age);



        return rootView;
    }

}
