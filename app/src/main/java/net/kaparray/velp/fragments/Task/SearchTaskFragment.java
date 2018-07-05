package net.kaparray.velp.fragments.Task;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;
import net.kaparray.velp.classes_for_data.TaskLoader;
import net.kaparray.velp.fragments.OpenTaskFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchTaskFragment extends Fragment{

    // Firebase
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @BindView(R.id.tv_NoInternetSearch) TextView mTextNoInternetSearch;
    @BindView(R.id.pb_Search) ProgressBar progressBar;

    @BindView(R.id.et_search) EditText mTextSearch;
    @BindView(R.id.rv_Search) RecyclerView mRecyclerView;


    ArrayList<TaskLoader> loderer; // so funny name for variable

    // Fragment
    private OpenTaskFragment openTaskFragment;

    MyAdapter adapter;

    View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fr_search, container, false);

        // Add title
        ((MainActivity) getActivity()).setTitle(getString(R.string.AcceptedTaskTitle));


        //Butter Knife
        ButterKnife.bind(this, rootView);

        loderer = new ArrayList<TaskLoader>();
        search(" ");


        // onClick Search in keyboard
        mTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(mTextSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });


        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        progressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);

        return rootView;
    }


    void search(final String text){

        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);

        if(hasConnection(getContext())){
            loderer.clear();

            Query myTopPostsQuery = mDatabase.child("Task");
            myTopPostsQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    if(dataSnapshot.child("nameTask").getValue().toString().toLowerCase().contains(text)) {
                        loderer.add(dataSnapshot.getValue(TaskLoader.class));
                    }

                    if(loderer.size() > 0) {
                        //Set adapter
                        adapter = new MyAdapter(loderer, getContext());
                        mRecyclerView.setAdapter(adapter);


                        adapter.setOnItemClickListener(new MyAdapter.ClickListener() {
                            public static final String TAG = "fb";

                            @Override
                            public void onItemClick(int position, View v) {
                                Log.d(TAG, "onItemClick position: " + position);

                                openTaskFragment = new OpenTaskFragment();

                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .replace(R.id.container, openTaskFragment)
                                        .commit();


                                // This is magic bundle. I transit data in DB to OpenTaskFragment
                                Bundle bundle = new Bundle();
                                bundle.putString("TaskKey", loderer.get(position).getKey());
                                openTaskFragment.setArguments(bundle);
                            }

                            @Override
                            public void onItemLongClick(int position, View v) {
                                Log.d(TAG, "onItemLongClick pos = " + position);
                            }
                        });

                        mRecyclerView.setVisibility(View.VISIBLE);
                        mTextNoInternetSearch.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);

                    }else{
                        mRecyclerView.setVisibility(View.GONE);
                        mTextNoInternetSearch.setVisibility(View.VISIBLE);
                        mTextNoInternetSearch.setText(getResources().getString(R.string.noResults));
                    }


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
        }else{ //  no enternet connection
            mRecyclerView.setVisibility(View.GONE);
            mTextNoInternetSearch.setVisibility(View.VISIBLE);
            mTextNoInternetSearch.setText(getResources().getString(R.string.noInternet));
        }

    }


    // Enternet connection
    public static boolean hasConnection(final Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected())
        {
            return true;
        }
        return false;
    }
}


class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<TaskLoader> loaders;
    private static ClickListener clickListener;


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener   {
        // each data item is just a string in this case
        public View mView;
        public Context context;

        public ViewHolder(View v, Context context) {
            super(v);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mView = v;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }


        // This method return text for name task
        public void setTitleName(String title) {
            TextView name = mView.findViewById(R.id.tv_nameTask);
            name.setText(title);

            try {
                if(title.length() < 28){
                    name.setText(title);
                }else{
                    name.setText(title.substring(0, 25) + "...");
                }

            } catch (NullPointerException e){
                name.setText("Error");
            }
        }

        // This method return text for value task
        public void setValue(String value) {
            TextView val = mView.findViewById(R.id.tv_nameValue);

            try {
                if(value.length() < 30){
                    val.setText(value);
                }else{
                    val.setText(value.substring(0, 27) + "...");
                }
            } catch (NullPointerException e){
                val.setText("Error");
            }
        }

        // This method return text for name user
        public void setUser(final String userr) {
            TextView us = mView.findViewById(R.id.tv_userTask);
            try {
                us.setText(userr);
            } catch (NullPointerException e){
                us.setText("Error");
            }

        }


        public void setPhoto(String photo, Resources resources){
            ImageView ph = mView.findViewById(R.id.iv_photoTask);

            try {
                if (photo.equals("ic_boy")) {
                    ph.setImageDrawable(resources.getDrawable(R.drawable.ic_boy));
                } else if (photo.equals("ic_boy1")) {
                    ph.setImageDrawable(resources.getDrawable(R.drawable.ic_boy1));
                } else if (photo.equals("ic_girl")) {
                    ph.setImageDrawable(resources.getDrawable(R.drawable.ic_girl));
                } else if (photo.equals("ic_girl1")) {
                    ph.setImageDrawable(resources.getDrawable(R.drawable.ic_girl1));
                } else if (photo.equals("ic_man1")) {
                    ph.setImageDrawable(resources.getDrawable(R.drawable.ic_man1));
                } else if (photo.equals("ic_man2")) {
                    ph.setImageDrawable(resources.getDrawable(R.drawable.ic_man2));
                } else if (photo.equals("ic_man3")) {
                    ph.setImageDrawable(resources.getDrawable(R.drawable.ic_man3));
                } else if (photo.equals("ic_man4")) {
                    ph.setImageDrawable(resources.getDrawable(R.drawable.ic_man4));
                }
            }catch (NullPointerException e){
                ph.setImageDrawable(resources.getDrawable(R.drawable.ic_launcher_round));
            }
        }
    }



    public void setOnItemClickListener(ClickListener clickListener) {
        MyAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }

    public Context contextT;
    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<TaskLoader> myDataset, Context context) {
        loaders = myDataset;
        contextT = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_for_task, parent, false);

        ViewHolder vh = new ViewHolder(v,contextT);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.setPhoto(loaders.get(position).getPhoto(),contextT.getResources());
        holder.setUser(loaders.get(position).getNameUser());
        holder.setTitleName(loaders.get(position).getNameTask());
        holder.setValue(loaders.get(position).getValueTask());
        holder.setPhoto(loaders.get(position).getPhoto(), contextT.getResources());

    }




    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return loaders.size();
    }
}
