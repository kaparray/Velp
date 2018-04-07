package net.kaparray.velp.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;
import net.kaparray.velp.classes_for_data.RatingData;
import net.kaparray.velp.classes_for_data.TaskLoader;

import static android.content.Context.MODE_PRIVATE;


public class RatingFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference myRef;


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

                viewHolder.setTitleName(model.getNameRating());
                viewHolder.setValue(model.getValueRating());


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
        public void setTitleName(String title) {
            TextView name = mView.findViewById(R.id.textView3);
            name.setText(title);
        }

        // This method return text for value task
        public void setValue(String value) {
            TextView val = mView.findViewById(R.id.tv_valueRating);
            val.setText(value);
        }
    }



}
