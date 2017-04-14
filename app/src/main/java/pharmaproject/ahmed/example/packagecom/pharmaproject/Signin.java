package pharmaproject.ahmed.example.packagecom.pharmaproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.fabric.sdk.android.Fabric;
import pharmaproject.ahmed.example.packagecom.pharmaproject.database.Information;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.Debuger;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.Validation;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper;

public class Signin extends AppCompatActivity {
    EditText email, password;
    Button signin;
    TextView register, resetpassword;
    pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper helper = new helper(this);
    //initialize  firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_signin);
        if(!isConnected()){
            startActivity(new Intent(Signin.this,isConnected.class));
            finish();
        }

        initFirebaseLoginSetListener();

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        signin = (Button) findViewById(R.id.signin);
        register = (TextView) findViewById(R.id.register);
        resetpassword = (TextView) findViewById(R.id.resetpassword);


        email.setTypeface(utils.getFont(this));
        password.setTypeface(utils.getFont(this));
        signin.setTypeface(utils.getFont(this));
        register.setTypeface(utils.getFont(this));
        resetpassword.setTypeface(utils.getFont(this));


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validation.CheakDataSignin(email.getText().toString(), password.getText().toString(), Signin.this)) {
                            signin();

                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signin.this, Signup.class);
                startActivity(intent);
            }
        });
        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signin.this, ResetPassword.class));
            }
        });
    }

    public void initFirebaseLoginSetListener() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Information.CurrentUser=user.getEmail().replace(".","*");
                    Intent intent = new Intent(Signin.this, MainContainerActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    public void signin() {
        final ProgressDialog progressDialog=ProgressDialog.show(Signin.this,"Please Wait","",true,false);
        String phoneStr = email.getText().toString();
        String passwordDa = password.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(phoneStr, passwordDa)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Debuger.Toast(Signin.this,task.getException().getLocalizedMessage());
                        } else {
                            Debuger.Toast(Signin.this, "Sign in Successful ");
                            Information.CurrentUser=email.getText().toString().replace(".","*");
                            Intent intent = new Intent(Signin.this, MainContainerActivity.class);
                            startActivity(intent);
                        }
                        progressDialog.dismiss();
                    }

                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private boolean isConnected(){
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
