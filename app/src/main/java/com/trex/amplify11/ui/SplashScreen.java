package com.trex.amplify11.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.trex.amplify11.R;
import com.trex.amplify11.viewmodel.AuthActivityViewModel;
import com.trex.amplify11.viewmodel.SplashScreenViewModel;

public class SplashScreen extends BaseActivity {


    SplashScreenViewModel splashScreenViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        splashScreenViewModel = ViewModelProviders.of(this).get(SplashScreenViewModel.class);
        splashScreenViewModel.intiSplashViewModel();

        splashScreenViewModel.getUserAuthStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isUserLoggedIn) {
                Toast.makeText(SplashScreen.this, ""+isUserLoggedIn, Toast.LENGTH_SHORT).show();
                if (isUserLoggedIn){
                    startActivity(new Intent(SplashScreen.this,HomeScreen.class));
                    finish();
                }
                else {
                    startActivity(new Intent(SplashScreen.this,AuthActivity.class));
                    finish();
                }

            }
        });


    }
}