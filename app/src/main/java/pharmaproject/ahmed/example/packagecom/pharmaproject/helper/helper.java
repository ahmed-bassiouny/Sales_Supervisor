package pharmaproject.ahmed.example.packagecom.pharmaproject.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import pharmaproject.ahmed.example.packagecom.pharmaproject.R;
import pharmaproject.ahmed.example.packagecom.pharmaproject.utils;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ahmed on 20/03/17.
 */

public class helper {
    public static SimpleDateFormat dateformate = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
    public static SimpleDateFormat datetimeformate = new SimpleDateFormat("hh:mm a");
    Calendar date;
    FragmentActivity fragmentActivity;
    static ProgressDialog ProgressDialog;
    public helper(FragmentActivity fragmentActivity){
        this.fragmentActivity=fragmentActivity;
    }
    // value => i will send string from activity to another
    public void goToFragment(Fragment fragment, @Nullable String tag, @Nullable Bundle bundle){

        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_container,fragment);
        if (bundle !=null) {
            //Bundle args = new Bundle();
            //args.putString("KEY", value);
            fragment.setArguments(bundle);
        }
        if(tag !=null)
            fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commit();
    }
    public void loadImage(String phone, final ImageView imageView){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(phone);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(fragmentActivity).load(uri).placeholder(R.drawable.logo).into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("TAG", e.getLocalizedMessage());
            }
        });


    }
    /*public static void loadImage(FragmentActivity fragmentActivity ,String phone,ImageView imageView)
    {

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        StorageReference spaceRef = mStorageRef.child(phone);
        // Load the image using Glide
        Glide.with(fragmentActivity )
                .using(new FirebaseImageLoader())
                .load(spaceRef )
                .into(imageView);

    }*/


    public String getFullAddress(String latlan){
        String [] arr=latlan.replace("lat/lng: (", "").replace(")", "").split(",");
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(fragmentActivity, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(arr[0]), Double.parseDouble(arr[1]), 1);
            return addresses.get(0).getAddressLine(0);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    public void viewDialogeDate(final EditText dynamicedittext) {
        date = Calendar.getInstance();
        //The initial year,month,day of the dialog.
        //first paramanter in DatePickerDialog callBack - How the parent is notified that the date is set
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                null,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH)

        );
        dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year,monthOfYear,dayOfMonth);
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        null,
                        date.get(Calendar.HOUR_OF_DAY),
                        date.get(Calendar.MINUTE),
                        false
                );
                tpd.setOnTimeSetListener(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        date.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        date.set(Calendar.MINUTE,minute);
                        if(date.before(Calendar.getInstance())) {
                            Debuger.Toast(fragmentActivity, "Sorry you must enter valid date");
                            dynamicedittext.setText("");
                        }
                        else
                        dynamicedittext.setText(helper.dateformate.format(date.getTime()));
                    }
                });
                tpd.show(fragmentActivity.getFragmentManager(), "Timepickerdialog");
            }
        });

        dpd.show(fragmentActivity.getFragmentManager(), "Datepickerdialog");
    }


}
