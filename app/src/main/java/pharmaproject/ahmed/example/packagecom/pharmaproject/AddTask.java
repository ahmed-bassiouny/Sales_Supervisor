package pharmaproject.ahmed.example.packagecom.pharmaproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import pharmaproject.ahmed.example.packagecom.pharmaproject.database.Information;
import pharmaproject.ahmed.example.packagecom.pharmaproject.database.Task;
import pharmaproject.ahmed.example.packagecom.pharmaproject.database.TaskType;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.Debuger;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper;


/**
 * A simple {@link 2Fragment} subclass.
 */
public class AddTask extends Fragment {
    EditText Address, Time, Description;
    CheckBox Repeat;
    AutoCompleteTextView DoctorName;
    Button save;
    String EMAILTEMP;
    TextView nameEmployee;
    int PLACE_PICKER_REQUEST = 1;
    String addressLan_Lat;
    String [] Doctors;
    pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper helper ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task, container, false);
        DoctorName= (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        Address= (EditText) view.findViewById(R.id.Address);
        Time= (EditText) view.findViewById(R.id.Task_time);
        Description= (EditText) view.findViewById(R.id.Task_Desc);
        save= (Button) view.findViewById(R.id.save);
        nameEmployee=(TextView)view.findViewById(R.id.nameEmployee);
        Repeat= (CheckBox) view.findViewById(R.id.repeat);
        EMAILTEMP = getArguments().getString("KEY");
        nameEmployee.setText(getArguments().getString("nameEmployee"));
        getDoctors();

//        DoctorName.setTypeface(utils.getFont(getContext()));
        DoctorName.setTypeface(utils.getFont(getContext()));
        Address.setTypeface(utils.getFont(getContext()));
        Time.setTypeface(utils.getFont(getContext()));
        Description.setTypeface(utils.getFont(getContext()));
        save.setTypeface(utils.getFont(getContext()));
        nameEmployee.setTypeface(utils.getFont(getContext()));

        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        helper=new helper(getActivity());
        Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlocation(PLACE_PICKER_REQUEST);

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData();
            }
        });
        Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.viewDialogeDate(Time);
            }
        });
    }


    public void getlocation(int PLACE_PICKER_REQUEST) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
        } catch (GooglePlayServicesNotAvailableException e) {
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                addressLan_Lat = place.getLatLng().toString();
                Address.setText(place.getAddress());
            }
        }
    }

    private void SaveData(){
        String doctor=DoctorName.getText().toString().trim();
        String address=Address.getText().toString().trim();
        String time=Time.getText().toString().trim();
        String description=Description.getText().toString().trim();
        if (doctor.isEmpty()) {
            Debuger.Toast(getActivity(), "You did not enter the doctor name! ");
            return;
        }
        if (address.isEmpty()) {
            Debuger.Toast(getActivity(), "Please specify the address! ");
            return;
        }
        if (time.isEmpty()) {
            Debuger.Toast(getActivity(), "You did not enter the timing! ");
            return;
        }

        Task task = new Task();
        task.doctorName=doctor;
        //task.locationDoctor=address;
        task.time_task=time;
        task.description=description;
        task.locationDoctor=addressLan_Lat;
        task.taskType= TaskType.INCOMPLETE;
        task.repeat=Repeat.isChecked();
        task.insert(EMAILTEMP);
        if(!isexist())
            addDoctor();

        getFragmentManager().popBackStack();
    }
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Task");
        MainContainerActivity.drawlayoutmain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
    private void getDoctors(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("PharmaProject").child("doctor");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String doc =dataSnapshot.getValue().toString();
                        Doctors= doc.split(",");
                        Doctors[Doctors.length-1]=Doctors[Doctors.length-1].replace("]","");
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, Doctors);
                        DoctorName.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private boolean isexist(){
        return Arrays.asList(Doctors).contains(DoctorName.getText().toString());
    }
    private void addDoctor(){
        Information.getDatabase().child("doctor").child(Doctors.length+"").setValue(DoctorName.getText().toString());
    }
}





