package pharmaproject.ahmed.example.packagecom.pharmaproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pharmaproject.ahmed.example.packagecom.pharmaproject.database.Information;
import pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper;

public class MainContainerActivity extends AppCompatActivity {

    pharmaproject.ahmed.example.packagecom.pharmaproject.helper.helper helper = new helper(this);
    Toolbar toolbar;
    static DrawerLayout drawlayoutmain;
    static ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container);
        // init custom toolbar


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // add custom toolbar
        setSupportActionBar(toolbar);
        // init navagitionDrawer
        drawlayoutmain = (DrawerLayout) findViewById(R.id.drawlayoutmain);
        // init Hamburger icon
        // This drawable shows a Hamburger icon when drawer is closed and an arrow when drawer is open.
        // It animates between these two states as the drawer opens.
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawlayoutmain, toolbar, 0, 0);
        actionBarDrawerToggle.syncState();
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.navigation_drawer_open);
        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    showBackButton(false);
                } else {
                    showBackButton(true);
                }
            }
        });

        // add Hamburger icon to navigationDrawer
        drawlayoutmain.addDrawerListener(actionBarDrawerToggle);

        helper.goToFragment(new Home(), null, null);
        /************/
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        break;
                    case R.id.addempoyee:
                        helper.goToFragment(new AddEmployee(), "Back to Home", null);
                        break;
                    case R.id.track:
                        helper.goToFragment(new ShowEmployeesOnline(), "Back to Home", null);
                        break;
                    case R.id.profile:
                        helper.goToFragment(new Profile(), "Back to Home", null);
                        break;
                    case R.id.signout:
                        createdialog();
                        break;

                }
                drawlayoutmain.closeDrawers();
                return true;
            }
        });
    }

    private void showBackButton(boolean showBackButton) {
        if (showBackButton)
            actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.navigation_drawer_open);
        else
            actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.navigation_drawer_close);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(showBackButton);
    }


    public void createdialog() {


        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You are going to exit!")
                .setCancelText("No,cancel!")
                .setConfirmText("signout!")
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Information.CurrentUser = "";
                        utils.login(MainContainerActivity.this,false);
                        Intent intent = new Intent(MainContainerActivity.this, Signin.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .show();

    }
}