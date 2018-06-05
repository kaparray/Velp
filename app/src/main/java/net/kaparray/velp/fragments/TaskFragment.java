package net.kaparray.velp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;
import net.kaparray.velp.fragments.Task.AcceptedTaskFragment;
import net.kaparray.velp.fragments.Task.NotAcceptedTaskFragment;
import net.kaparray.velp.fragments.Task.EndTaskFragment;
import net.kaparray.velp.fragments.Task.MyTaskFragment;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

import static net.kaparray.velp.R.layout.fr_task;


public class TaskFragment extends Fragment{

    // Work with view
    @BindView(R.id.navigationView) BottomNavigationView navigation;


    private AcceptedTaskFragment acceptedTaskFragment;
    private MyTaskFragment myTaskFragment;
    private EndTaskFragment endTaskFragment;
    private AddTaskFragment addTaskFragment;
    private NotAcceptedTaskFragment notAcceptedTaskFragment;

    @BindView(R.id.fab) FloatingActionButton fab;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_accepted:
                    acceptedTaskFragment = new AcceptedTaskFragment();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.task, acceptedTaskFragment)
                            .commit();
                    ((MainActivity) getActivity()).setFragmentCounter(false);
                    return true;
                case R.id.navigation_my:
                    myTaskFragment = new MyTaskFragment();
                    ((MainActivity) getActivity()).setTaskFragmentCounter("false");
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.task, myTaskFragment)
                            .commit();
                    ((MainActivity) getActivity()).setFragmentCounter(false);

                    return true;
                case R.id.navigation_end:
                    endTaskFragment = new EndTaskFragment();
                    ((MainActivity) getActivity()).setTaskFragmentCounter("false");
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.task, endTaskFragment)
                            .commit();
                    ((MainActivity) getActivity()).setFragmentCounter(false);
                    return true;
                case R.id.navigation_notaccepted:
                    notAcceptedTaskFragment = new NotAcceptedTaskFragment();
                    ((MainActivity) getActivity()).setTaskFragmentCounter("false");
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.task, notAcceptedTaskFragment)
                            .commit();
                    ((MainActivity) getActivity()).setFragmentCounter(false);
                    return true;
            }
            return false;
        }
    };




    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(fr_task, container, false);

        ButterKnife.bind(this, rootView);

        ((MainActivity) getActivity()).setTaskFragmentCounter("true");

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(navigation);



        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Add fragment for add task
                        addTaskFragment = new AddTaskFragment();

                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .replace(R.id.container, addTaskFragment)
                                .commit();

                        ((MainActivity) getActivity()).setFragmentCounter(false);
                        ((MainActivity) getActivity()).setTaskFragmentCounter("none");
                    }
                });

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();


        acceptedTaskFragment = new AcceptedTaskFragment();


        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.task, acceptedTaskFragment)
                .commit();
    }



}


class BottomNavigationViewHelper {
    @SuppressLint("RestrictedApi")
    public static void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BottomNav", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BottomNav", "Unable to change value of shift mode", e);
        }
    }
}
