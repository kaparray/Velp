package net.kaparray.velp.adapters;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.kaparray.velp.R;
import net.kaparray.velp.classes.TaskLoader;

import java.util.List;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskViewHolder> {

    private List<TaskLoader> task_list;

    public TaskRecyclerViewAdapter(List<TaskLoader> task_list) {
        this.task_list = task_list;
    }



    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TaskViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.task_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {

        TaskLoader task = task_list.get(position);

        holder.mNameTask.setText(task.TaskName);
        holder.mValueTask.setText(task.TaskValue);

    }



    @Override
    public int getItemCount() {
        return task_list.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder{

        TextView mNameTask, mValueTask;

        public TaskViewHolder(View itemView) {
            super(itemView);
            mNameTask = itemView.findViewById(R.id.tv_NameTask);
            mValueTask = itemView.findViewById(R.id.tv_ValueTask);

        }
    }
}
