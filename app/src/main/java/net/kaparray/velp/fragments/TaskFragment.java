package net.kaparray.velp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;
import net.kaparray.velp.fragments.Task.GamesTaskFragment;
import net.kaparray.velp.fragments.Task.NotAcceptedTaskFragment;
import net.kaparray.velp.fragments.Task.MyTaskFragment;
import net.kaparray.velp.fragments.Task.SearchTaskFragment;
import net.kaparray.velp.fragments.Task.TakenTaskFragment;

import java.lang.reflect.Field;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;
import static net.kaparray.velp.R.layout.fr_task;


public class TaskFragment extends Fragment{

    // Work with view
    @BindView(R.id.navigationView) BottomNavigationView navigation;


    private SearchTaskFragment acceptedTaskFragment;
    private MyTaskFragment myTaskFragment;
    private GamesTaskFragment gamesTaskFragment;
    private AddTaskFragment addTaskFragment;
    private TakenTaskFragment takenTaskFragment;
    private NotAcceptedTaskFragment notAcceptedTaskFragment;

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.cv_TutorialIcon) CardView cvTutorial;
    @BindView(R.id.btn_okTutorial) Button mOkTutorial;

    @BindView(R.id.cv_TutorialDemo) CardView mCardDemoTask;

    Animation animVisible;
    Animation animGone;

    Animation animVisibleCard;
    Animation animGoneCard;


    @OnClick(R.id.btn_okTutorialDemo)
    void tutorialDemo(){
        mCardDemoTask.startAnimation(animGoneCard);

        // For settings
        SharedPreferences preferences = getActivity().getSharedPreferences("DemoAnim", MODE_PRIVATE);
        SharedPreferences.Editor editorView = preferences.edit();
        editorView.putString("DemoAnim", "true");
        editorView.apply();

        mCardDemoTask.setVisibility(View.GONE);
    }


    @OnClick(R.id.btn_okTutorial)
    void clickTutorialOk(){
        cvTutorial.startAnimation(animGoneCard);

        // For settings
        SharedPreferences preferences = getActivity().getSharedPreferences("AlertTaskIcon", MODE_PRIVATE);
        SharedPreferences.Editor editorView = preferences.edit();
        editorView.putString("AlertTaskIcon", "true");
        editorView.apply();

        cvTutorial.setVisibility(View.GONE);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_accepted:


                    if(fab.getVisibility() == View.GONE) {
                        fab.setVisibility(View.VISIBLE);
                        fab.startAnimation(animVisible);
                    }
                    acceptedTaskFragment = new SearchTaskFragment();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.task, acceptedTaskFragment)
                            .commit();
                    return true;
                case R.id.navigation_my:
                    if(fab.getVisibility() == View.GONE) {
                        fab.setVisibility(View.VISIBLE);
                        fab.startAnimation(animVisible);
                    }

                    myTaskFragment = new MyTaskFragment();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.task, myTaskFragment)
                            .commit();

                    return true;
                case R.id.navigation_end:
                    if(fab.getVisibility() == View.VISIBLE) {
                        fab.setVisibility(View.GONE);
                        fab.startAnimation(animGone);
                    }

                    gamesTaskFragment = new GamesTaskFragment();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.task, gamesTaskFragment)
                            .commit();
                    return true;
                case R.id.navigation_not_accepted:

                    if(fab.getVisibility() == View.GONE) {
                        fab.setVisibility(View.VISIBLE);
                        fab.startAnimation(animVisible);
                    }
                    notAcceptedTaskFragment = new NotAcceptedTaskFragment();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.task, notAcceptedTaskFragment)
                            .commit();
                    return true;
                case R.id.navigation_taken:
                    if(fab.getVisibility() == View.GONE) {
                        fab.setVisibility(View.VISIBLE);
                        fab.startAnimation(animVisible);
                    }
                    takenTaskFragment = new TakenTaskFragment();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.task, takenTaskFragment)
                            .commit();
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


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(navigation);

        // Animation
        animVisible = AnimationUtils.loadAnimation(getContext(),R.anim.fab_anim);
        animGone = AnimationUtils.loadAnimation(getContext(),R.anim.fab_anim_gone);
        animVisibleCard = AnimationUtils.loadAnimation(getContext(),R.anim.beta_animation_games);
        animGoneCard = AnimationUtils.loadAnimation(getContext(),R.anim.beta_animation_back);




        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Check if no view has focus:
                        View vieww = Objects.requireNonNull(getActivity()).getCurrentFocus();
                        if (vieww != null) {
                            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(vieww.getWindowToken(), 0);
                        }

                        // Add fragment for add task
                        addTaskFragment = new AddTaskFragment();

                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .replace(R.id.container, addTaskFragment)
                                .commit();
                        ((MainActivity) getActivity()).setAddTask(false);

                    }
                });

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();

        fab.startAnimation(animVisible);


        acceptedTaskFragment = new SearchTaskFragment();

        // Alert Close Tutorial
        SharedPreferences preferencesIcon = Objects.requireNonNull(getActivity()).getSharedPreferences("AlertTaskIcon",MODE_PRIVATE);
        String alertTaskIcon = preferencesIcon.getString("AlertTaskIcon","false");

        // Alert Close Tutorial
        SharedPreferences preferencesDemo = Objects.requireNonNull(getActivity()).getSharedPreferences("DemoAnim",MODE_PRIVATE);
        String alertTaskDemo = preferencesDemo.getString("DemoAnim","false");

        if(alertTaskDemo.equals("false")) {
            mCardDemoTask.setVisibility(View.VISIBLE);
            mCardDemoTask.startAnimation(animVisibleCard);
        }else if(alertTaskIcon.equals("false")){
            cvTutorial.setVisibility(View.VISIBLE);
            cvTutorial.startAnimation(animVisibleCard);
        }

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setTransition( FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.task, acceptedTaskFragment)
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
