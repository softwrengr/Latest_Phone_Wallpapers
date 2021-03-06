package com.squaresdevelopers.latestphonewallpapers.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.squaresdevelopers.latestphonewallpapers.R;
import com.squaresdevelopers.latestphonewallpapers.fragments.AboutFragment;
import com.squaresdevelopers.latestphonewallpapers.fragments.HomeFragment;
import com.squaresdevelopers.latestphonewallpapers.fragments.LikeFragment;
import com.squaresdevelopers.latestphonewallpapers.utils.GeneralUtils;
import com.squaresdevelopers.latestphonewallpapers.utils.ShareUtils;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GeneralUtils.connectFragment(this,new HomeFragment());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_home){
            GeneralUtils.connectFragmentWithDrawer(NavigationActivity.this,new HomeFragment());
        }
        else if (id == R.id.nav_liked) {
           GeneralUtils.connectFragmentWithDrawer(NavigationActivity.this,new LikeFragment());
        }else if (id == R.id.nav_feedback) {
          loadGooglePlay();
        } else if (id == R.id.nav_share) {
            startActivity(ShareUtils.shareApp());
        }
        else if (id == R.id.nav_policy) {
         startActivity(new Intent(ShareUtils.loadPrivacyPolicy()));
        }
        else if (id == R.id.nav_about) {
            GeneralUtils.connectFragmentWithDrawer(NavigationActivity.this,new AboutFragment());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadGooglePlay(){
        try {
            startActivity(ShareUtils.loadApp(this));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }

}
