package pharmaproject.ahmed.example.packagecom.pharmaproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.Debuger;

public class ResetPassword extends AppCompatActivity {
    EditText email;
    Button resetpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        email=(EditText)findViewById(R.id.email);
        setTitle("Enter Your Mail");
        resetpassword=(Button)findViewById(R.id.resetpassword);
        email.setTypeface(utils.getFont(this));
        resetpassword.setTypeface(utils.getFont(this));
        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!email.getText().toString().isEmpty())
                    resetPassword(email.getText().toString());
                else
                    Debuger.Toast(ResetPassword.this,"Please Insert You Email");
            }
        });
    }
    private void resetPassword(String email){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Debuger.Toast(ResetPassword.this,"Email sent. Check Your Inbox");
                            finish();
                        }else{
                            Debuger.Toast(ResetPassword.this,task.getException().getLocalizedMessage());
                        }
                    }
                });
    }
}
