package pharmaproject.ahmed.example.packagecom.pharmaproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import pharmaproject.ahmed.example.packagecom.pharmaproject.database.Information;
import pharmaproject.ahmed.example.packagecom.pharmaproject.database.Supervisor;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.Debuger;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.Validation;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper;

public class Signup extends AppCompatActivity {
    EditText email, phone, name, password, confirmpassword ;
    AutoCompleteTextView area;
    TextView Register;
    Button proceed;
    pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper helper = new helper(this);
    //initialize  firebase Auth
    private FirebaseAuth mAuth;



    //some variable to check and set data
    String phoneStr, passwordStr, nameStr, areaStr, confirmpasswordStr, emailStr;

    String [] Areas ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        Register = (TextView) findViewById(R.id.Register);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        name = (EditText) findViewById(R.id.nameRe);
        password = (EditText) findViewById(R.id.passwordRe);
        confirmpassword = (EditText) findViewById(R.id.Re_password_Re);
        area = (AutoCompleteTextView) findViewById(R.id.Area);
        proceed = (Button) findViewById(R.id.proceed);
        mAuth = FirebaseAuth.getInstance();

        getAreas();

        Register.setTypeface(utils.getFont(this));
        email.setTypeface(utils.getFont(this));
        phone.setTypeface(utils.getFont(this));
        name.setTypeface(utils.getFont(this));
        password.setTypeface(utils.getFont(this));
        confirmpassword.setTypeface(utils.getFont(this));
        area.setTypeface(utils.getFont(this));
        proceed.setTypeface(utils.getFont(this));




        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                emailStr = email.getText().toString();
//                phoneStr = phone.getText().toString();
//                passwordStr = password.getText().toString();
//                nameStr = name.getText().toString();
//
              areaStr = area.getText().toString();
//                confirmpasswordStr = confirmpassword.getText().toString();
                if (Validation.checking(email, phone, name, password, confirmpassword, Signup.this)) {
                    signUp();
                }
            }
        });



    }

    public void signUp() {
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Debuger.Toast(Signup.this, task.getException().getLocalizedMessage());
                        } else {
                            Debuger.Toast(Signup.this, "Sign up Successful");
                            Information.CurrentUser = email.getText().toString().toLowerCase().replace(".", "*");
                            createSupervisor();
                            finish();
                        }
                    }
                });

    }

    private void createSupervisor() {
        Supervisor supervisor = new Supervisor();
        supervisor.name = nameStr;
        supervisor.phone = phone.getText().toString();
        supervisor.area = areaStr;
        supervisor.insert();
        if(!isexist())
            addarea();
    }

    public void getAreas() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("PharmaProject").child("area");
        ref.addListenerForSingleValueEvent(new ValueEventListener()  {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                String areaa = dataSnapshot.getValue().toString();


                Areas=areaa.split(",");

                Areas[Areas.length-1]=Areas[Areas.length-1].replace("]","");


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Signup.this, android.R.layout.simple_spinner_dropdown_item,Areas);
                area.setAdapter(adapter);
                    }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(Signup.this , databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
            });
    }
    private boolean isexist(){
        return Arrays.asList(Areas).contains(area.getText().toString());
    }
    private void addarea(){
        Information.getDatabase().child("area").child(Areas.length+"").setValue(area.getText().toString());
    }
}
