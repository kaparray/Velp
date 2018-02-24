package net.kaparray.velp.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.kaparray.velp.MainActivity;
import net.kaparray.velp.R;


public class BonusFragment extends android.support.v4.app.Fragment {

    Button mTutorial;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fr_bonus, container, false);
// Add title
        ((MainActivity) getActivity()).setTitle(getString(R.string.BonusTitle));

        AlertDialog.Builder bonusAlretDialog = new AlertDialog.Builder(getActivity());
        bonusAlretDialog.setTitle(getString(R.string.Title_AlretDialogBonus));
        bonusAlretDialog.setCancelable(false);
        bonusAlretDialog.setIcon(R.drawable.ic_bonus);
        bonusAlretDialog.setMessage(getString(R.string.Text_AlretDialogBonus));
        bonusAlretDialog.setPositiveButton("Понятно", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        bonusAlretDialog.show();


        return rootView;
    }
}
