package pharmaproject.ahmed.example.packagecom.pharmaproject.database;

import android.graphics.Color;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ahmed on 19/03/17.
 */

public class Information {
    public static String DATABASE_NAME="PharmaProject";
    public static DatabaseReference mDatabase;
    public static String CurrentUser;
    public static int GREEN =Color.parseColor("#1AB110");
    public static int YELLOW =Color.parseColor("#E6D30A");
    public static DatabaseReference getDatabase(){
        if(mDatabase==null)
        mDatabase = FirebaseDatabase.getInstance().getReference(DATABASE_NAME);
        return mDatabase;
    }
}
