package pharmaproject.ahmed.example.packagecom.pharmaproject;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import pharmaproject.ahmed.example.packagecom.pharmaproject.database.Employee;
import pharmaproject.ahmed.example.packagecom.pharmaproject.database.Information;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowEmployeesOnline extends Fragment implements OnMapReadyCallback ,
        GoogleMap.OnMarkerClickListener,GoogleMap.OnMapClickListener{
    MapView mapView;
    GoogleMap myGoogleMap = null;
    ScrollView employeeData ;
    TextView employephone, Email,offline;
    ImageView imageView;
    ValueEventListener postListener;



    List<Employee>employeeArrayforData = new ArrayList<>();
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_employees_online, container, false);

        employeeData = (ScrollView)view.findViewById(R.id.employeesmap_showemployee) ;
        Email = (TextView)view.findViewById(R.id.employeename);

        //add mapView to fragment
        mapView = (MapView) view.findViewById(R.id.mapView);

        mapView.getMapAsync(this);

        mapView.onCreate(savedInstanceState);
        imageView= (ImageView)view.findViewById(R.id.image_showemploye);
        employephone= (TextView)view.findViewById(R.id.employeephone);
        offline=(TextView)view.findViewById(R.id.employeeoffline);




        return view;
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        myGoogleMap = googleMap;
       /* List<Employee> employeeArray = new ArrayList<>();
        Employee v1 = new Employee();
        v1.name = "mostafa";
        v1.lastLocation = "lat/lng:(31.247900,29.986839)";

        employeeArray.add(v1);*/
        getDataEmployee();
        myGoogleMap.setPadding(100,100,100,100);
        myGoogleMap.setOnMarkerClickListener(this);
        myGoogleMap.setOnMapClickListener(this);
        mapView.onResume();


//        myGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//
//            private final View contents = getActivity().getLayoutInflater().inflate(R.layout.custom_information_marker, null);
//            @Override
//            public View getInfoWindow(Marker marker) {
//                String[]  employeenumber= marker.getTitle().split(":");
//
//                //Log.i("****",employeenumber[0]);
//                TextView name= (TextView) contents.findViewById(R.id.employeenamecutoummarker);
//                ImageView imageView = (ImageView)contents.findViewById(R.id.image_showemployecutominfo);
//                TextView phone=(TextView) contents.findViewById(R.id.employeephonecutomtitle) ;
//                TextView email=(TextView) contents.findViewById(R.id.employeeemailcutomtitle) ;
//                name.setText(employeeArrayforData.get(Integer.parseInt(employeenumber[0])).name);
//                //Picasso.with(getActivity()).load("http://i.imgur.com/DvpvklR.png").into(imageView);
//                mhelper.loadImage(employeeArrayforData.get(Integer.parseInt(employeenumber[0])).email,imageView);
//                phone.setText(employeeArrayforData.get(Integer.parseInt(employeenumber[0])).phone);
//                email.setText(employeeArrayforData.get(Integer.parseInt(employeenumber[0])).email.replace("*","."));
//
//
//                return contents;
//            }
//
//            @Override
//            public View getInfoContents(Marker marker) {
//
//
//            return null;
//            }
//        });



    }
    //  you must call this method in on change lisener
    private void updateMyMarkers(List<MarkerOptions> markers) {
        if(myGoogleMap == null) return;

        myGoogleMap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (MarkerOptions m : markers) {
            myGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(), 15), 1000, null);
            myGoogleMap.addMarker(new MarkerOptions()
                    .position(m.getPosition())
                    .title(m.getTitle())/*.snippet(m.getSnippet())*/.icon(m.getIcon()));
            builder.include(m.getPosition());
        }

        LatLngBounds bounds = builder.build();
        int padding = 0; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        myGoogleMap.animateCamera(cu);
    }


    // method to add  palce holder of employeee to map
    public void markerCreator(List<Employee>employeeArray) {

        employeeArrayforData= employeeArray;
        List<MarkerOptions> markers = new ArrayList<MarkerOptions>();
        for (int i = 0; i < employeeArray.size(); i++) {
            if(employeeArray.get(i).lastLocation.isEmpty())
                continue;
//            markers.add((new MarkerOptions()
//                    .position(new LatLng(LatLong.getLat(employeeArray.get(i).lastLocation.replace(": (",":(")),
//                            LatLong.getLongt(employeeArray.get(i).lastLocation.replace(": (",":("))))
//                    .title(employeeArray.get(i).name)));
//            updateMyMarkers(markers);
                        Log.i("****",employeeArray.get(i).lastLocation);
                          Log.i("****","hi");
            if (employeeArray.get(i).online)

            {
                markers.add((new MarkerOptions()
                        .position(new LatLng(LatLong.getLat(employeeArray.get(i).lastLocation),
                            LatLong.getLongt(employeeArray.get(i).lastLocation)))
                        .title(i+":"+employeeArray.get(i).name))
                        .snippet(("Rate = "+employeeArray.get(i).rate+","+employeeArray.get(i).email
                )).alpha(0.9f).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_picker_green)));

            }
            else{
                markers.add((new MarkerOptions()
                        .position(new LatLng(LatLong.getLat(employeeArray.get(i).lastLocation),
                                LatLong.getLongt(employeeArray.get(i).lastLocation)))
                        .title(i+":"+employeeArray.get(i).name))
                        .snippet(("Rate = "+employeeArray.get(i).rate+""+employeeArray.get(i).email
                )).alpha(0.1f).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_picker_red)));
            }

            updateMyMarkers(markers);
        }
    }
    private void getDataEmployee(){

         postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Employee>employees= new ArrayList<>();
                for(DataSnapshot dss:dataSnapshot.child("Supervisor").child(Information.CurrentUser).getChildren()){
                    if(dss.hasChildren()) {
                        Employee employee = dss.getValue(Employee.class);
                        employees.add(employee);
                    }
                }
                markerCreator(employees);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("mytag", databaseError.getMessage());
            }
        };
        Information.getDatabase().addValueEventListener(postListener);
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Track Employee");
        MainContainerActivity.drawlayoutmain.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        String[]  employeenumber= marker.getTitle().split(":");


        Email.setText(employeeArrayforData.get(Integer.parseInt(employeenumber[0])).email);
        employephone.setText(employeeArrayforData.get(Integer.parseInt(employeenumber[0])).phone);


        if (employeeArrayforData.get(Integer.parseInt(employeenumber[0])).online) {
            offline.setText("online");
            offline.setTextColor(Color.GREEN);
        }
        else
        {
            offline.setText("offline");
            offline.setTextColor(Color.RED);
        }
        employeeData.setVisibility(View.VISIBLE);


        //Picasso.with(getActivity()).load("http://i.imgur.com/DvpvklR.png").into(imageView);
       utils.loadImage(employeeArrayforData.get(Integer.parseInt(employeenumber[0])).email,imageView,getActivity());
//
    return false;

    }

    @Override
    public void onMapClick(LatLng latLng) {

        //getDataEmployee();
        employeeData.setVisibility(View.GONE);

    }

    @Override
    public void onStop() {
        super.onStop();
        Information.getDatabase().removeEventListener(postListener);
    }
}
