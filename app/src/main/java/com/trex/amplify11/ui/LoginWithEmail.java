package com.trex.amplify11.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.trex.amplify11.GlobalConstants;
import com.trex.amplify11.R;
import com.trex.amplify11.fragments.EnterOtpDialogFrag;
import com.trex.amplify11.fragments.EnterOtpFromEmail;
import com.trex.amplify11.viewmodel.LoginWithEmailViewModel;

public class LoginWithEmail extends BaseActivity {

    private static final String TAG = "LoginWithEmail";
    private String _email="";
    private String _pass="";
    private LoginWithEmailViewModel viewModel;
    private EditText loginMail,loginPassword,signupMail,signupPassword;
    private Button login,signUp;
    private FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_email);
        viewModel= ViewModelProviders.of(this).get(LoginWithEmailViewModel.class);
        getSupportActionBar().hide();
        viewModel.intiViewModel();
        login=findViewById(R.id.login_btn);
        signUp=findViewById(R.id.signup_btn);
        loginMail = findViewById(R.id.login_mail);
        loginPassword=findViewById(R.id.login_pass);
        signupMail=findViewById(R.id.signup_email);
        signupPassword=findViewById(R.id.signup_pass);

        findViewById(R.id.email_or).setOnClickListener(v->{
            signUpWithMail("riteshf7@gmail.com","1234567890");
        });


        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _email = signupMail.getText().toString().trim();
                _pass = signupPassword.getText().toString().trim();
                viewModel.signUp(_email,_pass);
            }
        });

        login.setOnClickListener(v->{
            _email=loginMail.getText().toString().trim();
            _pass=loginPassword.getText().toString().trim();
            viewModel.login(_email,_pass);
        });

        viewModel.getUserAuthStatus().observe(this, new Observer<String>() {

            @Override
            public void onChanged(String status) {
                switch (status){
                    case "signInSuccessful":
                        startActivity(new Intent(LoginWithEmail.this,HomeScreen.class));
                        finish();
                        break;
                    case "error":
                        Toast.makeText(LoginWithEmail.this, status, Toast.LENGTH_SHORT).show();
                        break;
                    case "otpRequestSignUp":
                        showOtpFragment(_email,_pass);
                        break;

                }
            }
        });


    }

    private void showOtpFragment(String email,String pass){
        EnterOtpFromEmail enterOTP = new EnterOtpFromEmail();
        Bundle bundle = new Bundle();
        bundle.putBoolean("notAlertDialog", true);
        bundle.putString("email",email);
        bundle.putString("pass",pass);
        enterOTP.setArguments(bundle);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);

        enterOTP.show(fragmentTransaction, "dialog");
    }

    private void signUpWithMail(String email,String password){
        Log.i(TAG, "signUp: ");

        Amplify.Auth.signUp(
                email,
                password,
                AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(),email).build(),
                result -> {
                    Log.i(TAG, "Result: " + result.toString());
                    Toast.makeText(this, "code sent successfully check it!", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Log.i(TAG, "signUp: error"+error);
                    String errorMessage = error.getCause().toString();

                }
        );
    }
}