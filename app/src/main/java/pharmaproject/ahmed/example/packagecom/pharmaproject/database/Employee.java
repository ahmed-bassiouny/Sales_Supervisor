package pharmaproject.ahmed.example.packagecom.pharmaproject.database;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pharmaproject.ahmed.example.packagecom.pharmaproject.LatLong;
import pharmaproject.ahmed.example.packagecom.pharmaproject.ShowTask;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.Adapter_Employees;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper;
import pharmaproject.ahmed.example.packagecom.pharmaproject.utils;

/**
 * Created by ahmed on 19/03/17.
 */

public class Employee {

    public String phone;
    public String name;
    public String email;
    public int rate;
    public boolean online = false;
    public String lastLocation = "";
    public String CountOfTasks = "0";
    public int timeTrack = 1;
    public String id ;
    private ProgressDialog progressDialog;
    private ValueEventListener postListener;
    public void insert() {
        getRoot().setValue(this);
    }

    public void update() {
        getRoot().child("phone").setValue(phone);
        getRoot().child("email").setValue(email);
        getRoot().child("name").setValue(name);
        getRoot().child("rate").setValue(rate);
        getRoot().child("timeTrack").setValue(timeTrack);
        getRoot().child("Repeat").setValue(ShowTask.weekly);
    }

    public void deleteEmployee() {
        getRoot().removeValue();
    }

    public void getEmployeesForHome(final RecyclerView recyclerView, final FragmentActivity fragmentActivity, final SwipeRefreshLayout swipeRefreshLayout) {
         postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Employee> employees = new ArrayList<>();
                for (DataSnapshot dss : dataSnapshot.child("Supervisor").child(Information.CurrentUser).getChildren()) {
                    if (dss.hasChildren()) {
                        Employee employee = dss.getValue(Employee.class);
                        employees.add(employee);
                    }
                }
                if (employees.size() > 0) {
                    Adapter_Employees mAdapter = new Adapter_Employees(employees, fragmentActivity);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                } else {
                    swipeRefreshLayout.setVisibility(View.GONE);
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                swipeRefreshLayout.setRefreshing(false);
            }
        };
        Information.getDatabase().addValueEventListener(postListener);
    }

    public void getEmployee( final TextView name, final TextView emailtxt, final TextView phone,
                            final ImageView employeePhoto, final EditText time_track,
                            final FragmentActivity fragmentActivity, final GoogleMap googleMap) {
        utils.showProgess(fragmentActivity);
         postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Employee employee = dataSnapshot.getValue(Employee.class);
                name.setText(employee.name);
                emailtxt.setText(employee.email);
                phone.setText(employee.phone);
                lastLocation = employee.lastLocation;
                time_track.setText(employee.timeTrack + "");
                utils.loadImage(employee.id, employeePhoto,fragmentActivity);
                if (googleMap != null)
                    employee.updateMap(googleMap);
                utils.dismissProgress();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("mytag", databaseError.getMessage());
                utils.dismissProgress();
            }
        };
        getRoot().addValueEventListener(postListener);
    }

    private DatabaseReference getRoot() {
        return Information.getDatabase().child("Supervisor").child(Information.CurrentUser).child(id);
    }

    public void updateMap(GoogleMap googleMap)

    {
        if (!lastLocation.isEmpty()) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(LatLong.getLat(lastLocation), LatLong.getLongt(lastLocation)))
                    .title(name));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(LatLong.getLat(lastLocation), LatLong.getLongt(lastLocation)), 15), 1000, null);
        }

    }
    public void removeListner(){
        Information.getDatabase().removeEventListener(postListener);
    }


}
