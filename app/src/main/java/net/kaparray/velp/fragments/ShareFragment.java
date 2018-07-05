package net.kaparray.velp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareFragment extends Fragment{



    @OnClick(R.id.btn_share)
    void clickShare(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBody = "Присоединяйтесь к проекту Velp. Здесь Вы сможете по-настоящему помочь людям и получить за это награду! #Velp\n" +
                "https://play.google.com/store/apps/details?id=net.kaparray.velp";

        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(shareIntent, "Поделиться профилем"));
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_share, container,false);

        ButterKnife.bind(this, rootView);

        // Back stack
        ((MainActivity) getActivity()).setAddTask(false);

        return rootView;
    }
}
