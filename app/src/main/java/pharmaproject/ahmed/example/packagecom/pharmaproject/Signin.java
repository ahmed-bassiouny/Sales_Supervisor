package pharmaproject.ahmed.example.packagecom.pharmaproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.fabric.sdk.android.Fabric;
import pharmaproject.ahmed.example.packagecom.pharmaproject.database.Information;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.Debuger;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.Validation;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper;

public class Signin extends AppCompatActivity {
    EditText email;
    Button signin;
    TextView getandroidid;
    pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper helper = new helper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_signin);
        if(!isConnected()){
            startActivity(new Intent(Signin.this,isConnected.class));
            finish();
        }
        if (utils.isloged(this)) {
            Information.CurrentUser=utils.getAndroidID(this);
            Intent intent = new Intent(Signin.this, MainContainerActivity.class);
            startActivity(intent);
            finish();
        }
        email = (EditText) findViewById(R.id.email);
        signin = (Button) findViewById(R.id.signin);
        getandroidid = (TextView) findViewById(R.id.getandroidid);


        email.setTypeface(utils.getFont(this));
        signin.setTypeface(utils.getFont(this));
        getandroidid.setTypeface(utils.getFont(this));


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validation.CheakDataSignin(email.getText().toString(), Signin.this)) {
                    ProgressDialog progressDialog = ProgressDialog.show(Signin.this, "Please Wait", "", true, false);
                    signinfakeaccount(email.getText().toString(),progressDialog );
                }
            }
        });
        getandroidid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SweetAlertDialog(Signin.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText(utils.getAndroidID(Signin.this))
                        .setConfirmText("ok")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.cancel();
                            }
                        })
                        .show();
            }
        });
    }
    private boolean isConnected(){
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public void signinfakeaccount(final String email,final ProgressDialog progressDialog) {
            Information.getDatabase().addListenerForSingleValueEvent((new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot childDataSnapshot : dataSnapshot.child("Supervisor").getChildren()) {
                        if (childDataSnapshot.getKey().equals(utils.getAndroidID(getApplicationContext()))){
                            if (dataSnapshot.child("Supervisor").child(utils.getAndroidID(getApplicationContext()))
                                    .child("email").getValue().toString().equals(email)){
                                if(dataSnapshot.child("Supervisor").child(utils.getAndroidID(getApplicationContext()))
                                        .child("access").getValue(Boolean.class)){
                                    Information.CurrentUser=utils.getAndroidID(getApplicationContext());
                                    utils.login(Signin.this,true);
                                    Intent intent = new Intent(Signin.this, MainContainerActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Debuger.Toast(Signin.this,"Not Allow to access");
                                }

                            }else{
                                Debuger.Toast(Signin.this,"Email is invalid");
                            }
                        }
                    }
                    progressDialog.dismiss();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(Signin.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }));

    }
}
