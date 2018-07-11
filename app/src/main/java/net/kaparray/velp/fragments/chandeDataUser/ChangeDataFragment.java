package net.kaparray.velp.fragments.chandeDataUser;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeDataFragment extends Fragment{

    // Firebase
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private ChangeDataOpenFragment changeDataOpenFragment;
    public ProgressDialog mProgressDialog;


    // Tag
    public String TAG = "Error";


    // View
    @BindView(R.id.tv_changeEmailEmail) TextView tvEmail;
    @BindView(R.id.tv_changeNameName) TextView tvName;
    @BindView(R.id.tv_changeNumberNumber) TextView tvNumber;
    @BindView(R.id.tv_changeAgeAge) TextView tvAge;
    @BindView(R.id.tv_changeCityCity) TextView tvCity;


    ////////////////////////////////////
    //                                //
    //    On Click on Butter Knife    //
    //                                //
    ////////////////////////////////////

    @OnClick(R.id.cv_age)
    public void submitAge() {
        Bundle bundle = new Bundle();
        bundle.putString("typeData", "age");
        changeDataOpenFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, changeDataOpenFragment)
                .commit();



    }


    @OnClick(R.id.cv_city)
    public void submitCity() {
        Bundle bundle = new Bundle();
        bundle.putString("typeData", "city");
        changeDataOpenFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, changeDataOpenFragment)
                .commit();


    }



    @OnClick(R.id.cv_email)
    public void submitEmail() {
        Bundle bundle = new Bundle();
        bundle.putString("typeData", "email");
        changeDataOpenFragment.setArguments(bundle);

        AlertDialog.Builder bonusAlretDialog = new AlertDialog.Builder(getActivity());
        bonusAlretDialog.setTitle(getString(R.string.Title_AlretDialogEmailChange));
        bonusAlretDialog.setCancelable(false);
        bonusAlretDialog.setIcon(R.drawable.ic_bonus);
        bonusAlretDialog.setMessage(getString(R.string.Text_AlretDialogEmailChange) + " " + user.getEmail());
        bonusAlretDialog.setPositiveButton(R.string.Understood, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        bonusAlretDialog.show();


    }


    @OnClick(R.id.cv_name)
    public void submitName() {
        Bundle bundle = new Bundle();
        bundle.putString("typeData", "name");
        changeDataOpenFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, changeDataOpenFragment)
                .commit();


    }


    @OnClick(R.id.cv_number)
    public void submitNumber() {
        Bundle bundle = new Bundle();
        bundle.putString("typeData", "number");
        changeDataOpenFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, changeDataOpenFragment)
                .commit();

    }


    @OnClick(R.id.cv_password)
    public void submitPassword() {

        showProgressDialog();

        FirebaseAuth.getInstance().sendPasswordResetEmail(user.getEmail().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.messageSend), Toast.LENGTH_LONG).show();
                            Log.d(TAG, "Email sent.");
                        } else {
                            Toast.makeText(getActivity(), "Error message send", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        hideProgressDialog();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fr_change_data, container, false);

        // Butter Knife
        ButterKnife.bind(this, rootView);

        ((MainActivity) getActivity()).setTitle(getString(R.string.ChangeDataTitle));

        changeDataOpenFragment = new ChangeDataOpenFragment();


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user data in Firebase
                String email = (String) dataSnapshot.child("Users").child(user.getUid()).child("email").getValue();
                String name = (String)dataSnapshot.child("Users").child(user.getUid()).child("name").getValue();
                String number = (String)dataSnapshot.child("Users").child(user.getUid()).child("phone").getValue();
                String age = (String)dataSnapshot.child("Users").child(user.getUid()).child("age").getValue();
                String city = (String)dataSnapshot.child("Users").child(user.getUid()).child("city").getValue();

                tvEmail.setText(email);
                tvName.setText(name);
                tvNumber.setText(number);
                tvAge.setText(age);
                tvCity.setText(city);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);

        return rootView;
    }



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
}
