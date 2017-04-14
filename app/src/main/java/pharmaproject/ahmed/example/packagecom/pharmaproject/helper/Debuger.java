package pharmaproject.ahmed.example.packagecom.pharmaproject.helper;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ahmed on 20/03/17.
 */

public class Debuger {

    public static void Toast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
