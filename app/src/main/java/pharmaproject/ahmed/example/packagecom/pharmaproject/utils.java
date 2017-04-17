package pharmaproject.ahmed.example.packagecom.pharmaproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper;

/**
 * Created by ahmed on 26/03/17.
 */

public class utils {

    public static Typeface myTypeFace;
    static ProgressDialog ProgressDialog;
    private static String SHarePreferenceName="LOG";
    static SharedPreferences sharedPref ;
    public static Typeface getFont(Context ctx) {
        if (myTypeFace == null) {
            myTypeFace = Typeface.createFromAsset(ctx.getAssets(), "CaviarDreams.ttf");
        }
        return myTypeFace;
    }
    public static void showProgess(Context context){
        if (ProgressDialog ==null) {
            ProgressDialog = new  ProgressDialog(context);
            ProgressDialog.setTitle("Please Wait");
            ProgressDialog.setMessage("Data Loading");
            ProgressDialog.setCancelable(false);
        }
        ProgressDialog.show();
    }
    public static void dismissProgress(){
        ProgressDialog.cancel();
    }

    public static String getAndroidID(Context context){
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static boolean isloged(Context context){
        if(sharedPref==null)
            sharedPref =context.getSharedPreferences(SHarePreferenceName,Context.MODE_PRIVATE);

        return sharedPref.getBoolean("loged",false);
    }
    public static void login(Context context,boolean login){
        if(sharedPref==null)
        sharedPref =context.getSharedPreferences(SHarePreferenceName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("loged", login);
        editor.commit();
    }

    public static void loadImage(String phone, final ImageView imageView,final FragmentActivity fragmentActivity){
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
    public static String dateAfter7Day(String oldDate){
        String newDate;
        try {
            Date date =helper.dateformate.parse(oldDate);
            Calendar calendar =Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE,7);
            newDate=helper.dateformate.format(calendar.getTime());
        } catch (ParseException e) {
            newDate=oldDate;
        }
        return newDate;
    }
}
