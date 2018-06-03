package net.kaparray.velp.tutorial;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.kaparray.velp.R;

public class TutorialFragment4 extends android.support.v4.app.Fragment {

    private int pageNumber;

    public static TutorialFragment4 newInstance(int page) {
        TutorialFragment4 fragment = new TutorialFragment4();
        Bundle args=new Bundle();
        args.putInt("num", page);
        fragment.setArguments(args);
        return fragment;
    }

    public TutorialFragment4() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments() != null ? getArguments().getInt("num") : 1;
    }
    static String getTitle(Context context, int position) {
        return "Страница № " + String.valueOf(position+1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_tutorial4, container, false);

//        TextView tvLabel = (TextView) rootView.findViewById(R.id.pager_header);
//        tvLabel.setText(page + " -- " + title);
        return rootView;
    }
}

