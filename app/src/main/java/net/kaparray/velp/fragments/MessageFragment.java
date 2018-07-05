package net.kaparray.velp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.kaparray.velp.R;

import butterknife.BindView;

public class MessageFragment extends Fragment{


    @BindView(R.id.recyclerViewMessage) RecyclerView mRecycler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_message, container, false);




        return rootView;
    }
}
