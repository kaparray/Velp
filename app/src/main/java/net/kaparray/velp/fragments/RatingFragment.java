package net.kaparray.velp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;
import net.kaparray.velp.classes_for_data.RatingData;
import net.kaparray.velp.utils.ProgressBarAnimation;

import static android.content.Context.MODE_PRIVATE;


public class RatingFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference myRef;

    public Resources drawable;


    public static boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fr_rating, container, false);
        ((MainActivity) getActivity()).setTitle(getString(R.string.RatingTitle));

        // Back stack
        ((MainActivity) getActivity()).setAddTask(false);

        drawable = getResources();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users").child(user.getUid()).child("rating");

        mRecyclerView = rootView.findViewById(R.id.recyclerView_Rating);

        // Create llm
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Create FirebaseRecyclerAdapter for automatic work with FDB
        FirebaseRecyclerAdapter<RatingData, RatingFragment.TaskViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<RatingData, RatingFragment.TaskViewHolder>(

                RatingData.class,
                R.layout.card_for_rating,
                RatingFragment.TaskViewHolder.class,
                myRef
        ) {
            @Override
            protected void populateViewHolder(TaskViewHolder viewHolder, RatingData model, int position) {

                viewHolder.setTitleName(model.getNameRating(), drawable, getContext());
                viewHolder.setValue(model.getValueRating(), getContext());


            }

        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }





    // ViewHolder for FirebaseRecyclerAdapter
    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public TaskViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }


        // This method return text for name task
        @SuppressLint("SetTextI18n")
        public void setTitleName(String title, Resources resources, Context context) {
            TextView name = mView.findViewById(R.id.tv_nameRating);
            ImageView photo = mView.findViewById(R.id.iv_photoRating);
            TextView val = mView.findViewById(R.id.tv_valTask);

            if (title.equals("help_1_people")) {
                name.setText("Первая Помощь");
                val.setText("Помочь 1 человеку");
                photo.setImageDrawable(resources.getDrawable(R.drawable.ic_1_user));
            }else if(title.equals("help_10_people")){
                name.setText("Хороший марафонец");
                val.setText("Помочь 10 людям");
                photo.setImageDrawable(resources.getDrawable(R.drawable.ic_10_users));
            }else if(title.equals("help_100_people")){
                name.setText("Мастер марафонец");
                val.setText("Помочь 100 людям");
                photo.setImageDrawable(resources.getDrawable(R.drawable.ic_100_users));
            }else if(title.equals("help_1000_people")){
                name.setText("Олимпийский марафонец");
                val.setText("Помочь 1000 людям");
                photo.setImageDrawable(resources.getDrawable(R.drawable.ic_1000_users));
            }



            // Set Theme
            SharedPreferences preferences = context.getSharedPreferences("theme",MODE_PRIVATE);
            String theme = preferences.getString("THEME"," ");


            if (theme.equals("dark")){
                name.setTextColor(Color.WHITE);
            }else if(theme.equals("light")){
                name.setTextColor(Color.BLACK);
            }

        }

        // This method return text for value task
        public void setValue(String value,  Context context) {
            TextView val = mView.findViewById(R.id.tv_valueRating);
            ProgressBar progressBar = mView.findViewById(R.id.prBar_Rating);
            val.setText(value + "%");
            Integer in =  (int)Double.parseDouble(value);
            progressBar.setProgress(in);

            ProgressBarAnimation anim = new ProgressBarAnimation(progressBar, 0,in);
            anim.setDuration(2500);
            progressBar.startAnimation(anim);

            // Set Theme
            SharedPreferences preferences = context.getSharedPreferences("theme",MODE_PRIVATE);
            String theme = preferences.getString("THEME"," ");

            if (theme.equals("dark")){
                val.setTextColor(Color.WHITE);
            }else if(theme.equals("light")){
                val.setTextColor(Color.BLACK);
            }
        }
    }



}
