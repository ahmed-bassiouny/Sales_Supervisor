package pharmaproject.ahmed.example.packagecom.pharmaproject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pharmaproject.ahmed.example.packagecom.pharmaproject.database.Employee;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.Debuger;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddEmployee extends Fragment {
    Button add_employee_btn;
    TextView title;
    EditText nameEdit, phone, password, email;
    ImageView chooseimage;
    ImageView imageEmployee;
    //private FirebaseAuth mAuth;
    private int chooseimageResult = 100;

    public final Pattern PASSWORD_PATTERN =
           Pattern.compile( "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", Pattern.CASE_INSENSITIVE);

    public  final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    Uri uriImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_employee, container, false);
        title= (TextView) view.findViewById(R.id.title);
        add_employee_btn = (Button) view.findViewById(R.id.add_employee_btn);
        nameEdit = (EditText) view.findViewById(R.id.nameEdit);
        phone = (EditText) view.findViewById(R.id.phone);
        password = (EditText) view.findViewById(R.id.password);
        email = (EditText) view.findViewById(R.id.email);
        chooseimage = (ImageView) view.findViewById(R.id.chooseimage);
        imageEmployee = (ImageView) view.findViewById(R.id.imageEmployee);
        //mAuth = FirebaseAuth.getInstance();


        add_employee_btn.setTypeface(utils.getFont(getContext()));
        nameEdit.setTypeface(utils.getFont(getContext()));
        phone.setTypeface(utils.getFont(getContext()));
        password.setTypeface(utils.getFont(getContext()));
        email.setTypeface(utils.getFont(getContext()));
        title.setTypeface(utils.getFont(getContext()));

        return view;
    }
    public boolean ValidateEmailAddress()
    {
       boolean  isValidMail=true;
        String strEmailAddress = email.getText().toString().trim();

        Matcher matcherObj = VALID_EMAIL_ADDRESS_REGEX.matcher(strEmailAddress);

        if (!matcherObj.matches()||strEmailAddress.isEmpty()) {

            Debuger.Toast(getActivity(), "Your Email is invalid");
            isValidMail=false;
        }
        return isValidMail;
    }
    public boolean ValidatePassword()
    {
        boolean  isValidPassword=true;
        String strPassword = password.getText().toString().trim();

        Matcher matcherObj = PASSWORD_PATTERN.matcher(strPassword);

        if (!matcherObj.matches()||strPassword.isEmpty()) {
            Debuger.Toast(getActivity(), "Your password must include at least one digit,one upperCase character,one lowerCase character,one special character and its length should take at least eight places");
            isValidPassword=false;
        }
        return isValidPassword;
    }


    public boolean ValidateName()
    {
        boolean  isValidName=true;
        String strName = nameEdit.getText().toString().trim();
        if (strName.isEmpty()) {
            Debuger.Toast(getActivity(), "Your name is valid");
            isValidName=false;
        }
        return isValidName;
    }

    private boolean ValidateMobile() {
        boolean check=true;
        String strPhone = phone.getText().toString().trim();
        if(!Pattern.matches("[a-zA-Z]+", strPhone)||strPhone.isEmpty()) {
            if(phone.length() !=11) {
                check = false;
                Debuger.Toast(getActivity(), "Your number is invalid");
            } else {
                check = true;
            }
        } else {
            check=false;
        }
        return check;
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        add_employee_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ValidateEmailAddress()&&ValidatePassword()&&ValidateName()&&ValidateMobile()){

                            createNewEmployee();

                }else{
                    Debuger.Toast(getActivity(), "Please Double check your data entered");
                }

            }
        });
        chooseimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, chooseimageResult);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == chooseimageResult && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                uriImage= data.getData();
                imageEmployee.setImageURI(uriImage);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void uploadImage(String email) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        progressDialog.setTitle("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StorageReference filepath = storageReference.child(email);
        filepath.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                Debuger.Toast(getActivity(),"Upload Done");
                getFragmentManager().popBackStack();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Debuger.Toast(getActivity(),e.getLocalizedMessage());
                getFragmentManager().popBackStack();
            }
        });

    }
    public void createNewEmployee()
    {
            Employee employee = new Employee();
            employee.name = nameEdit.getText().toString();
            employee.phone = phone.getText().toString();
            employee.password = password.getText().toString();
            employee.email = email.getText().toString().replace(".","*");
            employee.insert();
            if(uriImage!=null)
                uploadImage( employee.email);
            else{
                getFragmentManager().popBackStack();
            }
            Debuger.Toast(getActivity(),"Add Employee Successful");;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Add Employee");
        MainContainerActivity.drawlayoutmain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
}

