package com.junhee.android.review_mp3;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity implements ListFragment.OnListFragmentInteractionListener, PermissonControl.CallBack {


    FrameLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissonControl.checkVersion(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissonControl.onResult(this, requestCode, grantResults);
    }

    @Override
    public void init() {
        setView();
        setFragment(ListFragment.newInstance(1));
    }

    public void setView(){
        layout = (FrameLayout) findViewById(R.id.layout);
    }

    private void setFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.layout, fragment);
        transaction.commit();
    }

    private void addFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // TODO
    @Override
    public void goDetailInteration(int position) {
    }
}
