package pharmaproject.ahmed.example.packagecom.pharmaproject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Pattern;

import pharmaproject.ahmed.example.packagecom.pharmaproject.database.Employee;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.Debuger;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmployeeProfile extends Fragment implements OnMapReadyCallback {

    EditText email, phone, name, time_track,id;
    ImageView employeePhoto, editProfile;

    String ID_TEMP;
    ImageView addtask, showtasks, callemployee;
    pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper helper;
    Uri uriImage;
    Employee employee;

    public final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", Pattern.CASE_INSENSITIVE);


    private int chooseimageResult = 100;
    boolean imageChange = false;

    MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_profile, container, false);
        email = (EditText) view.findViewById(R.id.Em_Pro_email);
        phone = (EditText) view.findViewById(R.id.Em_Pro_phone);
        employeePhoto = (ImageView) view.findViewById(R.id.Em_pro_profile_image);
        editProfile = (ImageView) view.findViewById(R.id.editProfile);
        name = (EditText) view.findViewById(R.id.EM_pro_Name);
        time_track = (EditText) view.findViewById(R.id.time_track);
        id = (EditText) view.findViewById(R.id.id);

        addtask = (ImageView) view.findViewById(R.id.addtask);
        showtasks = (ImageView) view.findViewById(R.id.showtasks);
        callemployee = (ImageView) view.findViewById(R.id.callemployee);
        helper = new helper(getActivity());

        mapView = (MapView) view.findViewById(R.id.mapView_employee);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);

        name.setTypeface(utils.getFont(getContext()));
        email.setTypeface(utils.getFont(getContext()));
        phone.setTypeface(utils.getFont(getContext()));
        time_track.setTypeface(utils.getFont(getContext()));
        id.setTypeface(utils.getFont(getContext()));
        if (employee == null)
            employee = new Employee();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ID_TEMP = getArguments().getString("KEY");
        id.setText(ID_TEMP);
        addtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("KEY", ID_TEMP);
                bundle.putString("nameEmployee", name.getText().toString());
                helper.goToFragment(new AddTask(), "Back To Profile", bundle);

            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!phone.isEnabled()) {
                    editProfile.setImageResource(R.drawable.ok);
                    changeEnabled();
                } else {
                    if (ValidateName() && ValidateMobile() && ValidateTime()) {
                        editProfile.setImageResource(R.drawable.ph_edit);
                        changeEnabled();
                        SaveData();
                    }
                }

            }
        });
        showtasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("KEY", ID_TEMP);
                bundle.putString("NAME", name.getText().toString());
                helper.goToFragment(new ListOfTasks(), "Back To Profile", bundle);

            }
        });
        callemployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone.getText().toString()));
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    startActivity(callIntent);
                }


            }
        });
        employeePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!phone.isEnabled())
                    return;

                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, chooseimageResult);
            }
        });
    }

    private void changeEnabled() {
        phone.setEnabled(!phone.isEnabled());
        name.setEnabled(!name.isEnabled());
        time_track.setEnabled(!time_track.isEnabled());
    }

    private void SaveData() {
        Employee employee = new Employee();
        employee.id=id.getText().toString();
        employee.name = name.getText().toString();
        employee.phone = phone.getText().toString();
        employee.email = email.getText().toString();
        employee.timeTrack = Integer.parseInt(time_track.getText().toString());
        employee.update();
        if (imageChange)
            uploadImage(employee.id);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == chooseimageResult && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                uriImage = data.getData();
                employeePhoto.setImageURI(uriImage);
                imageChange = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void uploadImage(String id) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog.setTitle("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StorageReference filepath = storageReference.child(id);
        filepath.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Debuger.Toast(getActivity(), "Upload Done");
                getFragmentManager().popBackStack();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Debuger.Toast(getActivity(), e.getLocalizedMessage());
                getFragmentManager().popBackStack();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapView.onResume();
        employee.id=ID_TEMP;
        employee.getEmployee(name, email, phone, employeePhoto, time_track, getActivity(), googleMap);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Employee Profile");
        MainContainerActivity.drawlayoutmain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }


    private boolean ValidateMobile() {
        boolean check;
        String strPhone = phone.getText().toString().trim();
        if (!strPhone.startsWith("01") || phone.length() != 11) {
            check = false;
            Debuger.Toast(getActivity(), "Your number is invalid");
        } else {
            check = true;
        }
        return check;
    }

    /*public boolean ValidatePassword()
    {
        boolean  isValidPassword=true;
        String strPassword = password.getText().toString().trim();

        Matcher matcherObj = PASSWORD_PATTERN.matcher(strPassword);

        if (!matcherObj.matches()||strPassword.isEmpty()) {
            Debuger.Toast(getActivity(), "Your password is invalid");
            isValidPassword=false;
        }
        return isValidPassword;
    }*/


    public boolean ValidateTime() {
        boolean isValidTime;
        String time = time_track.getText().toString();
        if (time.isEmpty()) {
            Debuger.Toast(getActivity(), "Please Enter Minutes");
            isValidTime = false;
        } else {
            int timet = Integer.parseInt(time_track.getText().toString());
            if (timet > 0 && timet < 11) {

                isValidTime = true;
                Log.i("in Validate Time", time);
            } else {
                Debuger.Toast(getActivity(), "Your Minutes > 0 and < 10");
                isValidTime = false;
            }
        }
        return isValidTime;
    }

    public boolean ValidateName() {
        boolean isValidName = true;

        String emloyeename = name.getText().toString();
        if (emloyeename.isEmpty()) {
            Debuger.Toast(getActivity(), "Please Enter Name Employee");
            isValidName = false;
        }


        return isValidName;
    }

    @Override
    public void onStop() {
        super.onStop();
        employee.removeListner();
    }
}


