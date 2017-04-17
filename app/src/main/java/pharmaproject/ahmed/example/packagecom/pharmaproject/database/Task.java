package pharmaproject.ahmed.example.packagecom.pharmaproject.database;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import pharmaproject.ahmed.example.packagecom.pharmaproject.LatLong;
import pharmaproject.ahmed.example.packagecom.pharmaproject.R;
import pharmaproject.ahmed.example.packagecom.pharmaproject.ShowFeedback;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.Adapter_Tasks;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.Debuger;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper;
import pharmaproject.ahmed.example.packagecom.pharmaproject.utils;

/**
 * Created by ahmed on 19/03/17.
 */

public class Task {
    public int id;
    public String doctorName; //
    public String description;//
    // information about doctor and task
    public String locationDoctor; //
    public String time_task;
    // onformation about employee (preparing task)
    public String locationPreparing="";
    public String time_prepareTask="";
    // onformation about employee (end task)
    public String locationEmployee="";
    public String time_endTask="";
    public TaskType taskType;
    public String feedback="No Feedback";
    public int rateforEmployee;
    public int rateforDoctor;
    public boolean repeat;
    public String Reasonforcancel="";
    //***********************
    private int numberOfTasks;
    private int sum;
    private ValueEventListener postListener;
    private helper helper;
    public void insert(String id_employee){
            getCountOfTasks(id_employee,this);
    }
    public void update(String id_employee,boolean cancel){
        if(cancel){
            getRoot(id_employee).child(id+"").child("taskType").setValue(taskType);
        }else{
            getRoot(id_employee).child(id+"").child("doctorName").setValue(doctorName);
            getRoot(id_employee).child(id+"").child("description").setValue(description);
            getRoot(id_employee).child(id+"").child("time_task").setValue(time_task);
            getRoot(id_employee).child(id+"").child("rateforEmployee").setValue(rateforEmployee);
            getRoot(id_employee).child(id+"").child("Reasonforcancel").setValue(Reasonforcancel);
        }
    }
    public void updateRepeat(String id_employee,boolean repeat){
            getRoot(id_employee).child(id+"").child("repeat").setValue(repeat);
    }
    public void deleteTask(String id_employee){
        getRoot(id_employee).child(id+"").removeValue();
    }

    public void getTasks(final RecyclerView recyclerView, final FragmentActivity fragmentActivity, final String id_employee,
                         final SwipeRefreshLayout mSwipeRefreshLayout, final RatingBar averageRatingbar, final TaskType taskTypeselected){
        sum=0;
         postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<Task> tasks = new ArrayList<>();
                for(DataSnapshot dss:dataSnapshot.getChildren()){
                    if(dss.hasChildren()) {
                        Task task = dss.getValue(Task.class);
                        if(taskTypeselected==TaskType.All) {
                            sum += task.rateforEmployee;
                            tasks.add(task);
                        }else if(taskTypeselected==task.taskType){
                            sum += task.rateforEmployee;
                            tasks.add(task);
                        }
                        if(task.repeat&&task.taskType==TaskType.COMPLETE){
                            id=task.id;
                            updateRepeat(id_employee,false);
                            insertNewTaskForRepeat(id_employee,task);
                        }
                    }
                }
                if(tasks.size()>0){
                    Adapter_Tasks adapter_tasks = new Adapter_Tasks(tasks,fragmentActivity,id_employee);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter_tasks);
                    averageRatingbar.setProgress(sum/tasks.size());
                }else{
                    recyclerView.setAdapter(null);
                }

                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Debuger.Toast(fragmentActivity,databaseError.getMessage());
                mSwipeRefreshLayout.setRefreshing(false);
            }
        };
        getRoot(id_employee).orderByChild("time_task").addListenerForSingleValueEvent(postListener);
    }

    public void getTask(final String id_employee, final int task_id, final EditText Doc_name,
                        final EditText Address, final EditText Task_time,
                        final EditText Task_Desc,
                        final SeekBar typetaskbar, final TextView typetasktxt,
                        final FragmentActivity fragmentActivity,
                        final ImageView canceltask,
                        final ImageView feedbackimg, final GoogleMap googleMap,
                        final RatingBar ratingBar, final EditText Task_duration,
                        final CheckBox repeat,final EditText reasonforcancel){
        if(helper==null);
            helper=new helper(fragmentActivity);
        utils.showProgess(fragmentActivity);
        postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Task task = dataSnapshot.child(task_id+"").getValue(Task.class);
                Doc_name.setText(task.doctorName);
                locationDoctor=task.locationDoctor;
                locationEmployee=task.locationEmployee;
                time_endTask=task.time_endTask;
                taskType=task.taskType;
                time_prepareTask=task.time_prepareTask;
                locationPreparing=task.locationPreparing;
                reasonforcancel.setText(task.Reasonforcancel);
                repeat.setChecked(task.repeat);
                if(!task.locationDoctor.isEmpty())
                Address.setText(helper.getFullAddress(task.locationDoctor));
                Task_time.setText(task.time_task);
                Task_Desc.setText(task.description);
                switch (task.taskType){
                    case INCOMPLETE:
                        typetaskbar.setProgress(0);
                        typetasktxt.setText("INCOMPLETE");
                        canceltask.setVisibility(View.VISIBLE);
                        break;
                    case On_The_Way:
                        typetaskbar.setProgress(1);
                        typetasktxt.setText("On The Way");
                        canceltask.setVisibility(View.VISIBLE);
                        break;
                    case PROCESSING:
                        typetaskbar.setProgress(2);
                        typetasktxt.setText("PROCESSING");
                        canceltask.setVisibility(View.INVISIBLE);
                        break;
                    case COMPLETE:
                        typetaskbar.setProgress(3);
                        typetasktxt.setText("COMPLETE");
                        canceltask.setVisibility(View.INVISIBLE);
                        Task_duration.setText(getTiming(task.time_task,task.time_endTask));
                        break;
                    case CANCEL:
                        typetaskbar.setProgress(0);
                        typetasktxt.setText("CANCEL");
                        canceltask.setVisibility(View.INVISIBLE);
                        break;
                }


                feedbackimg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(task.feedback.equals("No Feedback")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(fragmentActivity);
                            builder.setMessage("No Feedback")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //do things
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }else{
                            Bundle bundle = new Bundle();
                            bundle.putString("nameoftask",task.doctorName);
                            bundle.putString("feedback",task.feedback);
                            bundle.putInt("rateforDoctor",task.rateforDoctor);
                            helper.goToFragment(new ShowFeedback(),"Show Task",bundle);
                        }
                    }
                });
                ratingBar.setProgress(task.rateforEmployee);
                task.updatemap(googleMap);
                utils.dismissProgress();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("mytag", databaseError.getMessage());
                utils.dismissProgress();
            }
        };
        getRoot(id_employee).addValueEventListener(postListener);

    }
    private void getCountOfTasks(final String id_employee, final Task task){
         postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String temp=dataSnapshot.child("Supervisor").child(Information.CurrentUser).child(id_employee).child("CountOfTasks").getValue(String.class);
                numberOfTasks=Integer.parseInt(temp);
                numberOfTasks++;
                task.id=numberOfTasks;
                Log.i("id", task.id+"");
                getRoot(id_employee).child(numberOfTasks+"").setValue(task);
                getRoot(id_employee).child("CountOfTasks").setValue(numberOfTasks+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                numberOfTasks=0;
            }
        };
        Information.getDatabase().addListenerForSingleValueEvent(postListener);
    }
    private DatabaseReference getRoot(String id_employee){
        return Information.getDatabase().child("Supervisor").child(Information.CurrentUser).child(id_employee);
    }

    // Hossam Code
    public  void  updatemap(GoogleMap googleMap)
    {
        if(!locationPreparing.isEmpty()) {
            try {
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(LatLong.getLat(locationPreparing), LatLong.getLongt(locationPreparing)))
                        .title(helper.datetimeformate.format(helper.dateformate.parse(time_prepareTask))).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_picker_green)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LatLong.getLat(locationPreparing), LatLong.getLongt(locationPreparing)), 15), 1000, null);
        }
        if(!locationEmployee.isEmpty()) {
            try {
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(LatLong.getLat(locationEmployee), LatLong.getLongt(locationEmployee)))
                        .title(helper.datetimeformate.format(helper.dateformate.parse(time_endTask))).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_picker_blue)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            PolylineOptions rectOptions = new PolylineOptions()
                    .add(new LatLng(LatLong.getLat(locationPreparing), LatLong.getLongt(locationPreparing)))
                    .add(new LatLng(LatLong.getLat(locationEmployee), LatLong.getLongt(locationEmployee)));
            googleMap.addPolyline(rectOptions);
        }

    }

    public String getTiming(String a,String b){
        //a=time_task;
      //  b=time_endTask;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        Date convertedStartDate = new Date();
        try {
            convertedStartDate = dateFormat.parse(a);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        Date convertedEndDate = new Date();
        try {
            convertedEndDate = dateFormat.parse(b);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        long different = convertedEndDate.getTime() - convertedStartDate.getTime();
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;


        String Output=elapsedHours+"Hours ,"+elapsedMinutes+" Minutes ";

        return Output;
    }
    public void removeListener(){
        Information.getDatabase().removeEventListener(postListener);
    }
    private void insertNewTaskForRepeat(String id_employee,Task task){
        Task newtask = new Task();
        newtask.doctorName=task.doctorName;;
        newtask.time_task=utils.dateAfter7Day(task.time_task);
        newtask.description=task.description;
        newtask.locationDoctor=task.locationDoctor;
        newtask.taskType= TaskType.INCOMPLETE;
        newtask.repeat=task.repeat;
        getCountOfTasks(id_employee,newtask);
    }
}
