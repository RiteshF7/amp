package com.trex.amplify11.ui;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.cognitoauth.Auth;
import com.trex.amplify11.R;
import com.trex.amplify11.databinding.ActivityAuthBinding;
import com.trex.amplify11.fragments.EnterOtpDialogFrag;
import com.trex.amplify11.viewmodel.AuthActivityViewModel;

public class AuthActivity extends BaseActivity {



    FragmentTransaction fragmentTransaction;
    AuthActivityViewModel authActivityViewModel;
    ActivityAuthBinding activityAuthBinding;
    Button login;
    EditText phoneNumber;
    private TextView loginWithEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAuthBinding = DataBindingUtil.setContentView(this,R.layout.activity_auth);
        getSupportActionBar().hide();
        activityAuthBinding.setPhoneNumbrHint(" Phone Number");
        authActivityViewModel = ViewModelProviders.of(this).get(AuthActivityViewModel.class);
        authActivityViewModel.initViewModel();
        phoneNumber=findViewById(R.id.phone_num);
        login=findViewById(R.id.login);
        loginWithEmail=findViewById(R.id.login_with_email);
        loginWithEmail.setOnClickListener(v->{
            startActivity(new Intent(AuthActivity.this,LoginWithEmail.class));
        });

        authActivityViewModel.isUserLoginWithPhoneSuccessful().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String loginStatus) {
                Toast.makeText(AuthActivity.this, loginStatus, Toast.LENGTH_SHORT).show();
                switch(loginStatus){
                    case "otpRequestSignUp":
                        showOtpFragment(loginStatus,"+91"+phoneNumber.getText().toString());
                    return;
                    case "otpRequestLogin":
                        showOtpFragment(loginStatus,"+91"+phoneNumber.getText().toString());
                        return;
                    case "signInSuccessful":
                        startActivity(new Intent(AuthActivity.this, HomeScreen.class));
                        finish();
                        return;
                    case "LoginError":
                        Toast.makeText(AuthActivity.this, loginStatus, Toast.LENGTH_SHORT).show();
                        return;
                    default:
                        break;
                }
            }
        });



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authActivityViewModel.signInWithPhoneNumber("+91"+phoneNumber.getText().toString());
                Toast.makeText(getBaseContext(), "click", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void showOtpFragment(String type,String phoneNum){
        EnterOtpDialogFrag enterOTP = new EnterOtpDialogFrag();
        Bundle bundle = new Bundle();
        bundle.putBoolean("notAlertDialog", true);
        bundle.putString("type",type);
        bundle.putString("phoneNum",phoneNum);
        enterOTP.setArguments(bundle);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);

        enterOTP.show(fragmentTransaction, "dialog");
    }

}