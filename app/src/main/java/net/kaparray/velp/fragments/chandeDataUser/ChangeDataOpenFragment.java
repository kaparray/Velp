package net.kaparray.velp.fragments.chandeDataUser;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;

public class ChangeDataOpenFragment extends Fragment{


    String type;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fr_change_data_open, container, false);

        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString("typeData");
        }

        Log.d("12344321", type + "" );

        if (type.equals("age")){
            ((MainActivity) getActivity()).setTitle(getString(R.string.AgeTitle));
        }else if(type.equals("city")){
            ((MainActivity) getActivity()).setTitle(getString(R.string.CityTitle));
        }else if(type.equals("email")){
            ((MainActivity) getActivity()).setTitle(getString(R.string.EmailTitle));
        }else if(type.equals("name")){
            ((MainActivity) getActivity()).setTitle(getString(R.string.NameTitle));
        }else if(type.equals("number")){
            ((MainActivity) getActivity()).setTitle(getString(R.string.PhoneNamberTitle));
        }

        return rootView;
    }
}
