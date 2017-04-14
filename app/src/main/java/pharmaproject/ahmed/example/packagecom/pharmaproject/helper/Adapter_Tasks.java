package pharmaproject.ahmed.example.packagecom.pharmaproject.helper;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import pharmaproject.ahmed.example.packagecom.pharmaproject.R;
import pharmaproject.ahmed.example.packagecom.pharmaproject.ShowTask;

import java.util.ArrayList;

import pharmaproject.ahmed.example.packagecom.pharmaproject.database.Information;
import pharmaproject.ahmed.example.packagecom.pharmaproject.database.Task;
import pharmaproject.ahmed.example.packagecom.pharmaproject.utils;

/**
 * Created by shemoo on 3/10/2017.
 */

public class Adapter_Tasks extends RecyclerView.Adapter<Adapter_Tasks.CustomViewHolder> {

    private ArrayList <Task> tasks ;
    FragmentActivity fragmentActivity;
    String email;
    pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper helper;


    public Adapter_Tasks(ArrayList <Task> tasks, FragmentActivity fragmentActivity,String email){
        this.tasks=tasks;
        this.fragmentActivity=fragmentActivity;
        this.email=email;
        this.helper=new helper(fragmentActivity);
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemtask, parent, false);
        return new CustomViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        Task task=tasks.get(position);
        holder.namedoctor.setText(task.doctorName);
        holder.date.setText(task.time_task);
        holder.taskRatingBar.setProgress(task.rateforEmployee);
        switch (task.taskType){
            case CANCEL:
                holder.tasktype.setText("CANCEL");
                holder.tasktype.setTextColor(Color.RED);
                break;
            case INCOMPLETE:
                holder.tasktype.setText("INCOMPLETE");
                holder.tasktype.setTextColor(Color.BLACK);
                break;
            case PROCESSING:
                holder.tasktype.setText("PROCESSING");
                holder.tasktype.setTextColor(Information.YELLOW);
                break;
            case On_The_Way:
                holder.tasktype.setText("On The Way");
                holder.tasktype.setTextColor(Information.YELLOW);
                break;
            case COMPLETE:
                holder.tasktype.setText("COMPLETE");
                holder.tasktype.setTextColor(Information.GREEN);
                break;
        }
    }

    @Override
    public int getItemCount() {

        return tasks.size();
    }



    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       TextView namedoctor,tasktype,date;
        RatingBar taskRatingBar;
        LinearLayout linearItem;
        public CustomViewHolder(final View itemView) {
            super(itemView);
            this.namedoctor =(TextView)itemView.findViewById(R.id.namedoctor);
            this.tasktype =(TextView)itemView.findViewById(R.id.tasktype);
            this.date=(TextView)itemView.findViewById(R.id.date);
            this.linearItem=(LinearLayout)itemView.findViewById(R.id.linearItem);
            this.taskRatingBar =(RatingBar) itemView.findViewById(R.id.taskRatingBar);
            taskRatingBar.setEnabled(false);

            namedoctor.setTypeface(utils.getFont(fragmentActivity));
            tasktype.setTypeface(utils.getFont(fragmentActivity));
            date.setTypeface(utils.getFont(fragmentActivity));


            linearItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("KEY", tasks.get(getAdapterPosition()).id);
                    bundle.putString("email",email);
                    helper.goToFragment(new ShowTask(),"Back To Tasks",bundle);
                }
            });
            linearItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    tasks.get(getAdapterPosition()).deleteTask(email);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(fragmentActivity);
                    builder.setMessage("Are you want delete this task .. ?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                    return false;
                }
            });
        }


        @Override
        public void onClick(View v) {

            //Snackbar.make(v,"Click detected on item"+getPosition(),Snackbar.LENGTH_LONG).setAction("Action",null).show();


        }
    }



}


