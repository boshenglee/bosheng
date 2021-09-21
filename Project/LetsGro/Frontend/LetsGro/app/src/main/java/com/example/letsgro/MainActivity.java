package com.example.letsgro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


    Analysis analysisFragment = new Analysis();
    History historyFragment = new History();
    Receipt receiptFragment = new Receipt();
    ToBuy toBuyFragment = new ToBuy();
    Dashboard dashboardFragment = new Dashboard();
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        String accountID = getIntent().getStringExtra("accountID");

        toBuyFragment =  ToBuy.newInstance(accountID,null);
        analysisFragment = Analysis.newInstance(accountID,null);
        historyFragment = History.newInstance(accountID,null);
        receiptFragment = Receipt.newInstance(accountID,null);
        dashboardFragment = Dashboard.newInstance(accountID,null);


        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>Dashboard</font>"));
        //Change the color of the action bar
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        getSupportFragmentManager().beginTransaction().replace(R.id.container, dashboardFragment ).commit();
        setUpNavDrawer();

    }

    /**
     * Change view when pressing on the item on the bottom navigation
     */
    private void setUpNavDrawer(){
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, dashboardFragment ).commit();
                        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>Dashboard</font>"));
                        return true;
                    case R.id.analysis:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, analysisFragment ).commit();
                        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>Analysis</font>"));
                        return true;
                    case R.id.history:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container,historyFragment).commit();
                        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>History</font>"));
                        return true;
                    case R.id.receipt:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, receiptFragment ).commit();
                        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>Receipt</font>"));
                        return true;
                    case R.id.toBuy:
                        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.container, toBuyFragment ).commit();
                        getSupportActionBar().setTitle(Html.fromHtml("<font color='#000000'>To Buy</font>"));
                        return true;
                }
                return false;
            }
        });
    }
}