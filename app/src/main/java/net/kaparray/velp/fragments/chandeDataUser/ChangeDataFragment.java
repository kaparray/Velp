package net.kaparray.velp.fragments.chandeDataUser;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeDataFragment extends Fragment{


    private ChangeDataOpenFragment changeDataOpenFragment;


    @BindView(R.id.cv_age) CardView cvAge;
    @BindView(R.id.cv_city) CardView cvCity;
    @BindView(R.id.cv_email) CardView cvEmail;
    @BindView(R.id.cv_name) CardView cvName;
    @BindView(R.id.cv_number) CardView cvNumber;
    @BindView(R.id.cv_password) CardView cvPassword;

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


        ((MainActivity) getActivity()).setSettingsFragmentCounter(false);
        ((MainActivity) getActivity()).setChangeDataFragmentCounter(false);
    }


    @OnClick(R.id.cv_city)
    public void submitCity() {
        Bundle bundle = new Bundle();
        bundle.putString("typeData", "city");
        changeDataOpenFragment.setArguments(bundle);
        ((MainActivity) getActivity()).setSettingsFragmentCounter(false);
        ((MainActivity) getActivity()).setChangeDataFragmentCounter(false);
    }



    @OnClick(R.id.cv_email)
    public void submitEmail() {
        Bundle bundle = new Bundle();
        bundle.putString("typeData", "email");
        changeDataOpenFragment.setArguments(bundle);
        ((MainActivity) getActivity()).setSettingsFragmentCounter(false);
        ((MainActivity) getActivity()).setChangeDataFragmentCounter(false);
    }


    @OnClick(R.id.cv_name)
    public void submitName() {
        Bundle bundle = new Bundle();
        bundle.putString("typeData", "name");
        changeDataOpenFragment.setArguments(bundle);
        ((MainActivity) getActivity()).setSettingsFragmentCounter(false);
        ((MainActivity) getActivity()).setChangeDataFragmentCounter(false);
    }


    @OnClick(R.id.cv_number)
    public void submitNumber() {
        Bundle bundle = new Bundle();
        bundle.putString("typeData", "number");
        changeDataOpenFragment.setArguments(bundle);
        ((MainActivity) getActivity()).setSettingsFragmentCounter(false);
        ((MainActivity) getActivity()).setChangeDataFragmentCounter(false);
    }


    @OnClick(R.id.cv_password)
    public void submitPassword() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fr_change_data, container, false);

        // Butter Knife
        ButterKnife.bind(this, rootView);

        changeDataOpenFragment = new ChangeDataOpenFragment();

        return rootView;
    }
}
