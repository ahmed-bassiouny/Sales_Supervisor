package pharmaproject.ahmed.example.packagecom.pharmaproject;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;

import pharmaproject.ahmed.example.packagecom.pharmaproject.database.Supervisor;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.Debuger;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {

   TextView Admin_Email;
    EditText Admin_Name,Admin_Area,Admin_Phone;
    Button Edit;
    Supervisor supervisor;
    ImageView changephoto,imageprofile;
    private StorageReference mstorage;
    private final static int galleryIntent=2;
    Uri uri_image;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_profile, container, false);
        Admin_Email=(TextView) view.findViewById(R.id.Admin_Email);
        Admin_Name=(EditText)view.findViewById(R.id.Admin_Name);
        Admin_Area=(EditText)view.findViewById(R.id.Admin_Area);
        Admin_Phone=(EditText)view.findViewById(R.id.Admin_Phone);
        Edit = (Button)view.findViewById(R.id.Edit);
        changephoto= (ImageView) view.findViewById(R.id.changephoto);
        imageprofile= (ImageView) view.findViewById(R.id.imageprofile);
        mstorage= FirebaseStorage.getInstance().getReference();
        Admin_Email.setTypeface(utils.getFont(getActivity()));
        Admin_Name.setTypeface(utils.getFont(getActivity()));
        Admin_Area.setTypeface(utils.getFont(getActivity()));
        Admin_Phone.setTypeface(utils.getFont(getActivity()));
        Edit.setTypeface(utils.getFont(getActivity()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        supervisor=new Supervisor();
        supervisor.getSupervisor(Admin_Email,Admin_Name,Admin_Phone,Admin_Area,imageprofile,getActivity());
        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEnabled();
                if(!Admin_Name.isEnabled()) {
                    getData();
                    supervisor.UpdateAnotherinfo(getActivity());
                    if(uri_image !=null)
                        uploadImage();
                    Edit.setText("Edit");
                }else{
                    Edit.setText("Save");
                }
            }
        });
        changephoto.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent = new Intent(Intent.ACTION_PICK,
                     android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
             intent.setType("image/*");
             startActivityForResult(intent,galleryIntent);

         }
     });
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==galleryIntent && resultCode==getActivity().RESULT_OK){
           uri_image=data.getData();
            imageprofile.setImageURI(uri_image);
        }
    }

    private void getData(){
        supervisor.name=Admin_Name.getText().toString().replace(".","*");
        supervisor.phone=Admin_Phone.getText().toString();
        supervisor.area=Admin_Area.getText().toString();
    }

    public void changeEnabled(){
       // Admin_Email.setEnabled(!Admin_Email.isEnabled());
        Admin_Name.setEnabled(!Admin_Name.isEnabled());
        Admin_Area.setEnabled(!Admin_Area.isEnabled());
        Admin_Phone.setEnabled(!Admin_Phone.isEnabled());
        if(Admin_Name.isEnabled())
            changephoto.setVisibility(View.VISIBLE);
        else
            changephoto.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Profile");
        MainContainerActivity.actionBarDrawerToggle.syncState();
        MainContainerActivity.drawlayoutmain.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
    private void uploadImage(){
        final ProgressDialog progress = ProgressDialog.show(getActivity(), "Please Wait",
                "Loading Data", true);
        StorageReference filePath=mstorage.child(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        filePath.putFile(uri_image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //Uri downloadUrl = data.getData() //taskSnapshot.getDownloadUrl();
                Debuger.Toast(getActivity(),"Upload Done!");
                progress.dismiss();
            }}).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Debuger.Toast(getActivity(),"Upload Unsuccessful");
                progress.dismiss();
            }});
    }

}
