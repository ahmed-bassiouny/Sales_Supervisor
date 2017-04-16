package pharmaproject.ahmed.example.packagecom.pharmaproject;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pharmaproject.ahmed.example.packagecom.pharmaproject.database.Information;
import pharmaproject.ahmed.example.packagecom.pharmaproject.database.Task;
import pharmaproject.ahmed.example.packagecom.pharmaproject.database.TaskType;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper;


/**
 * A simple {@link @Fragment} subclass.
 */
public class ShowTask extends Fragment implements OnMapReadyCallback {

    EditText Doc_name,Address,Task_time,Task_Desc,Task_duration;
    CheckBox Repeat;
    public static boolean weekly=false;
    SeekBar typetaskbar;
    TextView typetasktxt;
    RatingBar ratingBar;
    int ID;String EMAIL;
    ImageView editask,canceltask,feedback;
    Task task ;
    pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper helper ;
    int PLACE_PICKER_REQUEST = 1;

    //map part

    MapView mapView;
    //map part
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_show_task, container, false);
        Doc_name=(EditText)view.findViewById(R.id.Doc_name);
        Address=(EditText)view.findViewById(R.id.Address);
        Task_time=(EditText)view.findViewById(R.id.Task_time);
        Task_Desc=(EditText)view.findViewById(R.id.Task_Desc);
        Task_duration= (EditText) view.findViewById(R.id.TaskDuration);
        //***********
        Doc_name.setTypeface(utils.getFont(getContext()));
        Address.setTypeface(utils.getFont(getContext()));
        Task_time.setTypeface(utils.getFont(getContext()));
        Task_Desc.setTypeface(utils.getFont(getContext()));
        Task_duration.setTypeface(utils.getFont(getContext()));

        typetaskbar=(SeekBar) view.findViewById(R.id.typetaskbar);
        //********
        typetasktxt=(TextView) view.findViewById(R.id.typetasktxt);
        typetasktxt.setTypeface(utils.getFont(getContext()));

        //******
        Repeat= (CheckBox) view.findViewById(R.id.Repeat);
        //***********
       // scrollInfo=(ScrollView) view.findViewById(R.id.scrollInfo);
        //******
        editask=(ImageView)view.findViewById(R.id.editask);
        canceltask=(ImageView)view.findViewById(R.id.canceltask);
        feedback=(ImageView)view.findViewById(R.id.feedback);

        ratingBar=(RatingBar)view.findViewById(R.id.ratingBar);
        //map part

        mapView = (MapView) view.findViewById(R.id.scrollInfo);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);
        if(task==null)
            task = new Task();
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        helper = new helper(getActivity());
        ID = getArguments().getInt("KEY");
        EMAIL=getArguments().getString("email");

        editask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typetasktxt.getText().toString().equals("INCOMPLETE")||typetasktxt.getText().toString().equals("On The Way"))
                    changeEnabled_SaveData();
                else
                    changeEnabled_SaveDataRate();
            }
        });
        Task_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.viewDialogeDate(Task_time);
            }
        });
        Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getlocation(PLACE_PICKER_REQUEST);
            }
        });
        typetaskbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        canceltask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTaskWithDialog();
            }
        });
        Repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(Repeat.isChecked()) {
                 weekly=true;
             }else{
                 weekly=false;
             }
            }
        });
    }
    private void SaveData(){
        task.id=ID;
        task.doctorName=Doc_name.getText().toString();
        task.description=Task_Desc.getText().toString();
        task.time_task=Task_time.getText().toString();
        task.rateforEmployee=ratingBar.getProgress();
        //return;
        task.update(EMAIL,false);
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
                task.locationDoctor = place.getLatLng().toString();
                Address.setText(place.getAddress());
            }
        }
    }

    private void changeEnabled_SaveData(){
        Doc_name.setEnabled(!Doc_name.isEnabled());
        Task_Desc.setEnabled(!Task_Desc.isEnabled());
        Task_time.setEnabled(!Task_time.isEnabled());
        Address.setEnabled(!Address.isEnabled());
        //ratingBar.setIsIndicator(!ratingBar.isIndicator());
        if(Doc_name.isEnabled()) {
            editask.setImageResource(R.drawable.ok);
        }else {
            editask.setImageResource(R.drawable.ph_edit);
            SaveData();
        }
    }
    private void changeEnabled_SaveDataRate(){
        ratingBar.setIsIndicator(!ratingBar.isIndicator());
        if(!ratingBar.isIndicator()) {
            editask.setImageResource(R.drawable.ok);
        }else {
            editask.setImageResource(R.drawable.ph_edit);
            SaveData();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        utils.showProgess(getContext());
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Task");
        MainContainerActivity.drawlayoutmain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        task.getTask(EMAIL,ID,Doc_name,Address,Task_time,Task_Desc,typetaskbar,typetasktxt,getActivity(),canceltask,editask,feedback,googleMap,ratingBar,Task_duration);
        mapView.onResume();
    }

    public void cancelTaskWithDialog() {
        new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You are going to cancel task!")
                .setCancelText("No")
                .setConfirmText("Yes")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        task.taskType= TaskType.CANCEL;
                        task.id=ID;
                        task.update(EMAIL,true);
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();

    }

    @Override
    public void onStop() {
        super.onStop();
        task.removeListener(EMAIL);
    }
}
