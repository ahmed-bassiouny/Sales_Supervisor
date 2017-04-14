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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pharmaproject.ahmed.example.packagecom.pharmaproject.LatLong;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.Adapter_Employees;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.Debuger;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper;
import pharmaproject.ahmed.example.packagecom.pharmaproject.utils;

/**
 * Created by ahmed on 19/03/17.
 */

public class Employee {

    public String phone;
    public String name;
    public String password;
    public String lastLocation = "";
    public String email;
    public String ip;
    public int rate;
    public boolean online = false;
    public String CountOfTasks = "0";
    public int timeTrack = 1;
    public String IMEI="";
    private helper helperobj;

    public void insert() {
        getRoot().setValue(this);
    }

    public void update() {
        getRoot().child("phone").setValue(phone);
        getRoot().child("password").setValue(password);
        getRoot().child("name").setValue(name);
        getRoot().child("rate").setValue(rate);
        getRoot().child("timeTrack").setValue(timeTrack);
        getRoot().child("IMEI").setValue(IMEI);
    }

    public void deleteEmployee() {
        getRoot().removeValue();
    }

    public void getEmployeesForHome(final RecyclerView recyclerView, final FragmentActivity fragmentActivity, final SwipeRefreshLayout swipeRefreshLayout) {
        ValueEventListener postListener = new ValueEventListener() {
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

    public void getEmployee(final String email, final TextView name, final TextView emailtxt, final TextView phone,
                            final TextView password, final ImageView employeePhoto,
                            final EditText time_track,final EditText imei,
                            final FragmentActivity fragmentActivity, final GoogleMap googleMap) {
        helperobj = new helper(fragmentActivity);
        final ProgressDialog progressDialog = ProgressDialog.show(fragmentActivity,"Please Wait","",true,false);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Employee employee = dataSnapshot.child("Supervisor").child(Information.CurrentUser).child(email).getValue(Employee.class);
                name.setText(employee.name);
                emailtxt.setText(employee.email.replace("*", "."));
                phone.setText(employee.phone);
                lastLocation = employee.lastLocation;
                time_track.setText(employee.timeTrack + "");
                password.setText(employee.password);
                imei.setText(employee.IMEI);
                helperobj.loadImage(employee.email, employeePhoto);
                if (googleMap != null)
                    employee.updateMap(googleMap);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("mytag", databaseError.getMessage());
                progressDialog.dismiss();
            }
        };
        Information.getDatabase().addValueEventListener(postListener);
    }

    private DatabaseReference getRoot() {
        return Information.getDatabase().child("Supervisor").child(Information.CurrentUser).child(email);
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
}
