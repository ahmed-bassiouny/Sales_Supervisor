package pharmaproject.ahmed.example.packagecom.pharmaproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
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
import java.util.List;
import java.util.Locale;

/**
 * Created by ahmed on 26/03/17.
 */

public class utils {

    public static Typeface myTypeFace;
    static ProgressDialog ProgressDialog;
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

}