package net.kaparray.velp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import net.kaparray.velp.R;
import net.kaparray.velp.classes_for_data.OpenTaskLoader;
import net.kaparray.velp.classes_for_data.TaskLoader;

import static net.kaparray.velp.fragments.ProfileFragment.TAG;


public class OpenTaskFragment extends Fragment{

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    Button mTakeTask;

    TextView mNameTask;
    TextView mValueTask;
    TextView mNameUser;

    String KEY_Task;


    TaskLoader taskLoader;

    private int clickCounter = 0;


    int points;
    int point;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fr_opentask, container, false);



        mNameTask = rootView.findViewById(R.id.tv_nameTaskInOpenTask);
        mValueTask = rootView.findViewById(R.id.tv_valueTaskInOpenTask);
        mNameUser = rootView.findViewById(R.id.tv_nameUserInOpenTask);
        mTakeTask = rootView.findViewById(R.id.btn_universalButtonTask);


        mTakeTask.isClickable();

        // Check data in bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            KEY_Task = bundle.getString("TaskKey");
        }



        ValueEventListener postListener = new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mNameTask.setText(dataSnapshot.child("Task").child(KEY_Task).child("nameTask").getValue()+ "");
                mValueTask.setText(dataSnapshot.child("Task").child(KEY_Task).child("valueTask").getValue()+ "");
                mNameUser.setText(dataSnapshot.child("Task").child(KEY_Task).child("nameUser").getValue()+ "");



                taskLoader = dataSnapshot.child("Task").child(KEY_Task).getValue(TaskLoader.class);

                Log.d("0000", taskLoader.getNameTask());


                if(!taskLoader.getUserTakeUID().equals("null")){
                    point = Integer.parseInt(dataSnapshot.child("Users").child(taskLoader.getUserTakeUID()).child("points").getValue()+"");
                }

                Log.d("0000", taskLoader.getNameTask());

                if(taskLoader.getUserUID().equals(user.getUid()) && taskLoader.getAccepted().equals("false")){
                    mTakeTask.setText("Вашу задачу никто не подтвердил");
                    mTakeTask.setBackgroundColor(getResources().getColor(R.color.soSoBlack));
                }else if(taskLoader.getAccepted().equals("end")){
                    mTakeTask.setBackgroundColor(getResources().getColor(R.color.GreenButton));
                    mTakeTask.setText("Закончанно");
                }else{
                    mTakeTask.setText("Взять задачу");
                }

                if(taskLoader.getAccepted().equals("true")){
                    if(!taskLoader.getUserUID().equals(user.getUid()) && !taskLoader.getUserTakeUID().equals(user.getUid())){
                        mTakeTask.setText("Эту задачу кто-то взял");
                    }else if(taskLoader.getUserTakeUID().equals(user.getUid())){
                        mTakeTask.setText("Вы взяли задачу");
                    }else if(taskLoader.getUserUID().equals(user.getUid())){
                        mTakeTask.setText("Закончить задачу");

                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(postListener);






        mTakeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(taskLoader.getUserUID().equals(user.getUid()) && taskLoader.getAccepted().equals("false")){ // Нельзя юоать свои
                    Toast.makeText(getActivity(), "Вы не можите брать свои задачи", Toast.LENGTH_LONG).show();
                } else if(taskLoader.getUserUID().equals(user.getUid()) && taskLoader.getAccepted().equals("true")){ // Закончить задачц
                    // End task
                    point += Integer.parseInt(taskLoader.getPoints());
                    mDatabase.child("Users").child(taskLoader.getUserTakeUID()).child("points").setValue(point+"");
                    mTakeTask.setBackgroundColor(getResources().getColor(R.color.GreenButton));
                    mTakeTask.setText("Закончанно");
                    mDatabase.child("Task").child(KEY_Task).child("accepted").setValue("end");
                } else if(!taskLoader.getUserUID().equals(user.getUid()) && taskLoader.getAccepted().equals("false")){ // пользователь взял задачу
                    Toast.makeText(getActivity(), "Вы взяли эту задачу", Toast.LENGTH_LONG).show();
                    mDatabase.child("Task").child(KEY_Task).child("accepted").setValue("true");
                    mDatabase.child("Task").child(KEY_Task).child("userTakeUID").setValue(user.getUid());
                    clickCounter++;
                } else if (clickCounter > 0 || taskLoader.getUserTakeUID().equals(user.getUid())){ // Не кликай много раз
                    Toast.makeText(getActivity(), "Вы взяли эту задачу", Toast.LENGTH_LONG).show();
                }else if(taskLoader.getAccepted().equals("end")){  // Задача законченна
                    Toast.makeText(getActivity(), "Эту задачу уже закончали", Toast.LENGTH_LONG).show();
                }else if(taskLoader.getAccepted().equals("true")){ // Эту задачу кто-то взял
                    Toast.makeText(getActivity(), "Эту задачу кто-то взял", Toast.LENGTH_LONG).show();
                }
            }
        });


        return rootView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Bundle bundle = getArguments();
        if (bundle != null) {
            bundle.putString("TaskKey", "");
        }



    }
}
