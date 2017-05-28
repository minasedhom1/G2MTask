package com.example.lenovo.g2mtask;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HomeActivity extends AppCompatActivity {


    public Fragment fragment = null;
    public Class fragmentClass = null;
    public FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

         keyhash(); //for facebook

        try {
            loadFirstFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            fragmentClass = FacebookTasksFrag.class;
                            break;
                        case R.id.navigation_dashboard:
                            fragmentClass = SearchCountriesFrag.class;
                            break;
                        case R.id.navigation_notifications:
                          fragmentClass= ReadContactsFrag.class;
                             break;
                        case R.id.navigation_home2:
                          fragmentClass=AddEventCalenderFrag.class;
                            break;
                        case R.id.navigation_dashboard2:
                            fragmentClass=CacheImageFrag.class;
                            break;
                    }
              try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                fragmentManager.beginTransaction().replace(R.id.frag_holder, fragment).commit();
                    return true;
                }


        });
    }
    void keyhash() {
        try {
            PackageInfo info = getApplicationContext().getPackageManager().getPackageInfo(
                    "com.example.lenovo.g2mtask",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String s=Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:", s);
        }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash:", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash:",e.getMessage());
        }}


    void loadFirstFragment() throws Exception{
        fragmentClass=FacebookTasksFrag.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentManager.beginTransaction().replace(R.id.frag_holder, fragment).commit();
    }

}
