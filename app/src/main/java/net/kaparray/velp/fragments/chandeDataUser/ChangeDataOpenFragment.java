package net.kaparray.velp.fragments.chandeDataUser;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeDataOpenFragment extends Fragment{


    // Tag
    public String TAG = "Error";

    // Firebase
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String type; // type of change data


    ChangeDataFragment changeDataFragment;

    View rootView;

    // View
    @BindView(R.id.et_changeData) EditText mChangeText;
    @BindView(R.id.btn_ApplyData) Button mApplyData;
    @BindView(R.id.tv_type) TextView mTextType;
    @BindView(R.id.tv_little_description) TextView mTextDescription;


    // Variables
    String email;
    String name;
    String age;
    String city;
    String number;



    @OnClick(R.id.btn_ApplyData)
    public void apply(){


        if (type.equals("age")){

            // Change age
            mDatabase.child("Users").child(user.getUid()).child("age").setValue(mChangeText.getText().toString());

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(mChangeText.getText().toString()).build();
            user.updateProfile(profileUpdates);

            // Hide keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(rootView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.container, changeDataFragment)
                    .commit();


        }else if(type.equals("city")){


            // Change phone number
            mDatabase.child("Users").child(user.getUid()).child("city").setValue(mChangeText.getText().toString());

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(mChangeText.getText().toString()).build();
            user.updateProfile(profileUpdates);

            // Hide keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(rootView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.container, changeDataFragment)
                    .commit();



        }else if(type.equals("email")){

        }else if(type.equals("name")){
            // Change name
            mDatabase.child("Users").child(user.getUid()).child("name").setValue(mChangeText.getText().toString());

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(mChangeText.getText().toString()).build();
            user.updateProfile(profileUpdates);




            Query myTopPostsQuery = mDatabase.child("Task").orderByChild("userUID").equalTo(user.getUid());
            myTopPostsQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    mDatabase.child("Task").child(dataSnapshot.child("key").getValue()+"")
                            .child("nameUser").setValue(mChangeText.getText().toString());
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            // Hide keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(rootView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.container, changeDataFragment)
                    .commit();



        }else if(type.equals("number")){

            // Change phone number
            mDatabase.child("Users").child(user.getUid()).child("phone").setValue(mChangeText.getText().toString());

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(mChangeText.getText().toString()).build();
            user.updateProfile(profileUpdates);

            // Hide keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(rootView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.container, changeDataFragment)
                    .commit();


        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fr_change_data_open, container, false);

        //Butter Knife
        ButterKnife.bind(this, rootView);

        changeDataFragment = new ChangeDataFragment();

        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString("typeData");
        }


        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user data in Firebase
                email = (String) dataSnapshot.child("Users").child(user.getUid()).child("email").getValue();
                name = (String)dataSnapshot.child("Users").child(user.getUid()).child("name").getValue();
                number = (String)dataSnapshot.child("Users").child(user.getUid()).child("phone").getValue();
                age = (String)dataSnapshot.child("Users").child(user.getUid()).child("age").getValue();
                city = (String)dataSnapshot.child("Users").child(user.getUid()).child("city").getValue();





                if (type.equals("age")){
                    mChangeText.setText(age);
                }else if(type.equals("city")){
                    mChangeText.setText(city);
                }else if(type.equals("name")){
                    mChangeText.setText(name);
                }else if(type.equals("number")){
                    mChangeText.setText(number);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);

        ///////////////////////////////
        //  Output data in fragment  //
        ///////////////////////////////

        if (type.equals("age")){
            ((MainActivity) getActivity()).setTitle(getString(R.string.AgeTitle));
            mChangeText.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_CLASS_NUMBER);
            mTextType.setText(getResources().getString(R.string.changeAge));
            mTextDescription.setText(getResources().getString(R.string.changeAgeDescription));
        }else if(type.equals("city")){
            ((MainActivity) getActivity()).setTitle(getString(R.string.CityTitle));
            mChangeText.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
            mTextType.setText(getResources().getString(R.string.changeCity));
            mTextDescription.setText(getResources().getString(R.string.changeCityDescription));
        }else if(type.equals("name")){
            ((MainActivity) getActivity()).setTitle(getString(R.string.NameTitle));
            mChangeText.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
            mTextType.setText(getResources().getString(R.string.changeName));
            mTextDescription.setText(getResources().getString(R.string.changeNameDescription));
        }else if(type.equals("number")){
            ((MainActivity) getActivity()).setTitle(getString(R.string.PhoneNamberTitle));
            mChangeText.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_CLASS_PHONE);
            mTextType.setText(getResources().getString(R.string.changePhone));
            mTextDescription.setText(getResources().getString(R.string.changePhoneDescription));
        }

        return rootView;
    }
}
