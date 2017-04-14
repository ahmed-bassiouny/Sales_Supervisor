package pharmaproject.ahmed.example.packagecom.pharmaproject.helper;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import pharmaproject.ahmed.example.packagecom.pharmaproject.EmployeeProfile;
import pharmaproject.ahmed.example.packagecom.pharmaproject.R;

import java.util.ArrayList;

import pharmaproject.ahmed.example.packagecom.pharmaproject.database.Employee;
import pharmaproject.ahmed.example.packagecom.pharmaproject.utils;

/**
 * Created by shemoo on 3/10/2017.
 */

public class Adapter_Employees extends RecyclerView.Adapter<Adapter_Employees.CustomViewHolder> {

    private ArrayList <Employee> employees ;
    FragmentActivity fragmentActivity;
    pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper helper ;
    private boolean downloadImage=true;


    public Adapter_Employees(ArrayList <Employee> employees, FragmentActivity fragmentActivity){
        this.employees=employees;
        this.fragmentActivity=fragmentActivity;
        this.helper= new helper(fragmentActivity);
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new CustomViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        Employee employee =employees.get(position);
        holder.name.setText(employee.name);
        if(!employee.lastLocation.isEmpty())
        holder.location.setText(helper.getFullAddress(employee.lastLocation));

        if(employee.online){
            holder.track.setText("On");
            holder.track.setTextColor(Color.GREEN);
        }else{
            holder.track.setText("Off");
            holder.track.setTextColor(Color.RED);
        }
       // helper.loadImage(employee.phone,holder.img_android,fragmentActivity);
        Log.i("employee.email", employee.email);
            helper.loadImage(employee.email, holder.img_android);

    }

    @Override
    public int getItemCount() {

        return employees.size();
    }



    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

       ImageView img_android;
       TextView name,location,track;
        LinearLayout linearItem;
        public CustomViewHolder(final View itemView) {
            super(itemView);
            this.name =(TextView)itemView.findViewById(R.id.text_recycle);
            this.name.setTypeface(utils.getFont(fragmentActivity));
            this.location =(TextView)itemView.findViewById(R.id.location);
            this.location.setTypeface(utils.getFont(fragmentActivity));
            this.track=(TextView)itemView.findViewById(R.id.track);
            this.track.setTypeface(utils.getFont(fragmentActivity));
            this.img_android = (ImageView)itemView.findViewById(R.id.imagev);
            this.linearItem=(LinearLayout)itemView.findViewById(R.id.linearItem);
            linearItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("KEY", employees.get(getAdapterPosition()).email);
                    helper.goToFragment(new EmployeeProfile(),"Back To Home",bundle);
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
                                    employees.get(getAdapterPosition()).deleteEmployee();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(fragmentActivity);
                    builder.setMessage("Are you want delete this employee .. ?").setPositiveButton("Yes", dialogClickListener)
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


