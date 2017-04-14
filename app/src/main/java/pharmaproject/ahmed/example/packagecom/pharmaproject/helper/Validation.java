package pharmaproject.ahmed.example.packagecom.pharmaproject.helper;

import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pharmaproject.ahmed.example.packagecom.pharmaproject.R;

/**
 * Created by ahmed on 20/03/17.
 */

public class Validation {

    public static final Pattern PASSWORD_PATTERN =
            Pattern.compile( "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    static String phonePattern = "[0]{1}+[1]{1}+[0-9]{9}";
    static String  emailStr;
    static String  phoneStr;
    static String  passwordStr;
    static String  nameStr;
    static String confirmpasswordStr;
   static Animation shake;
    static Vibrator  vibe;
   static EditText email1,phone1,password1,name1,confirmpassword1;


    public static  boolean checking(EditText email, EditText phone, EditText name , EditText password, EditText confirmpassword , Context context)
    {
         shake = AnimationUtils.loadAnimation(context, R.anim.shake);
        vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        email1 =email;
        phone1= phone;
        password1= password;
        confirmpassword1= confirmpassword;

        emailStr = email.getText().toString();
          phoneStr = phone.getText().toString();
           passwordStr = password.getText().toString();
          nameStr = name.getText().toString();
           confirmpasswordStr = confirmpassword.getText().toString();
        vibe.vibrate(50);
        boolean validate= false ;
        if (emailStr.isEmpty()|| phoneStr.isEmpty() || passwordStr.isEmpty()||nameStr.isEmpty()) {
            {
                //Debuger.Toast(context, "please fill All data");
                if(emailStr.isEmpty())

                {
                    email.setError("enter email");
                    email.startAnimation(shake);}
                 if (phoneStr.isEmpty() )
                {
                    phone.startAnimation(shake);
                    phone.setError("enter phone");


                }
                 if (nameStr.isEmpty())
                {
                    name.startAnimation(shake);
                    name.setError("enter name");
                }
                 if( passwordStr.isEmpty())
                {
                    password.startAnimation(shake);
                    password.setError("enter password");
                }

            }
        }else if (  ValidateMobile()&& ValidateEmailAddress() && ValidatePassword())
        {




            if (  passwordStr.equals( confirmpasswordStr))
            {
                validate = true;
            }
            else
            {

                password.startAnimation(shake);
                password.setError("missmatch password");
                confirmpassword.startAnimation(shake);


            }


        }




        return validate ;
    }
    public static  boolean CheakDataSignin(String phone,String password,Context context)
    {
        boolean validate= false ;
        if (phone.isEmpty() || password.isEmpty()) {
            Debuger.Toast(context,"please fill in all empty fields");
        }
        else {
            validate=true;
        }

        return validate ;
    }

    public static boolean ValidateEmailAddress()
    {
        boolean  isValidMail=true;
        String strEmailAddress = emailStr;

        Matcher matcherObj = VALID_EMAIL_ADDRESS_REGEX.matcher(strEmailAddress);

        if (!matcherObj.matches()||strEmailAddress.isEmpty()) {


            isValidMail=false;
            email1.setError("enter valid email");
            email1.startAnimation(shake);

        }
        return isValidMail;
    }
    public static boolean ValidatePassword()
    {
        boolean  isValidPassword=true;
        String strPassword =  passwordStr;

        Matcher matcherObj = PASSWORD_PATTERN.matcher(strPassword);

        if (!matcherObj.matches()||strPassword.isEmpty()) {
            //Debuger.Toast(getActivity(), "Your password must include at least one digit,one upperCase character,one lowerCase character,one special character and its length should take at least eight places");
            isValidPassword=false;
            password1.setError("password contain upper lower case spical char");
            password1.startAnimation(shake);
        }
        return isValidPassword;
    }


    public static boolean ValidateName()
    {
        boolean  isValidName=true;
        String strName =nameStr;
        if (strName.isEmpty()) {

            isValidName=false;
        }
        return isValidName;
    }

    private static boolean ValidateMobile() {
        boolean check=true;
        String strPhone =phoneStr;
        if(!Pattern.matches("[a-zA-Z]+", strPhone)||strPhone.isEmpty()) {
            if(strPhone.length() !=11) {
                check = false;
                phone1.setError("it's not mobile phone");
                phone1.startAnimation(shake);

            } else {
                check = true;

            }
        } else {
            check=false;
        }
        return check;
    }


}
