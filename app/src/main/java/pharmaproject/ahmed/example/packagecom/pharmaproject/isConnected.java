package pharmaproject.ahmed.example.packagecom.pharmaproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class isConnected extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_connected);
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference();
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.child("PharmaProject").child("connect").getValue(Boolean.class);
                if (connected) {
                    startActivity(new Intent(isConnected.this,Signin.class));
                    finish();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
