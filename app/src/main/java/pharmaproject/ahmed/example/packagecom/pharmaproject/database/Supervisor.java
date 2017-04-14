package pharmaproject.ahmed.example.packagecom.pharmaproject.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.*;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pharmaproject.ahmed.example.packagecom.pharmaproject.ResetPassword;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.Adapter_Employees;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.Debuger;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper;

/**
 * Created by ahmed on 19/03/17.
 */

public class Supervisor {

    public String name;
    public String phone;
    public String area;

    public void insert(){
        getRoot().setValue(this);
    }
    private DatabaseReference getRoot(){
        return Information.getDatabase().child("Supervisor").child(Information.CurrentUser);
    }
    public void UpdateAnotherinfo(final Context context){
        getRoot().child("name").setValue(name);
        getRoot().child("phone").setValue(phone);
        getRoot().child("area").setValue(area);
        Debuger.Toast(context,"Updated");
    }
    public void getSupervisor(final TextView email,
                              final EditText name, final EditText phone,
                              final EditText area, final ImageView imageprofile,
                              final FragmentActivity fragmentActivity) {
        final helper helper = new helper(fragmentActivity);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Supervisor supervisor=dataSnapshot.child("Supervisor").child(Information.CurrentUser).getValue(Supervisor.class);
                String temp=Information.CurrentUser;
                email.setText(temp.replace("*","."));
                name.setText(supervisor.name);
                phone.setText(supervisor.phone);
                area.setText(supervisor.area);
                helper.loadImage(FirebaseAuth.getInstance().getCurrentUser().getEmail(),imageprofile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        Information.getDatabase().addValueEventListener(postListener);
    }

}
