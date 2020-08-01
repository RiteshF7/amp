package com.trex.amplify11;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.client.results.UserCodeDeliveryDetails;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.trex.amplify11.ui.BaseActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    static String phoneNumber ="+919910000163";
    static String TAG ="THIS_IS_MAIN";
    EditText otpInput;
    Button confitmOTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        confitmOTP=findViewById(R.id.confirm);
        confitmOTP.setOnClickListener(this);
        otpInput=findViewById(R.id.enter_otp);
        confitmOTP.setOnClickListener(this);
        amplifyInit();
        //signUp();
        //login(); // using reset password
        //signIn(phoneNumber,"12345621139"); //actual login
        showInfo();



    }

    void showInfo(){
        Log.i(TAG, "showInfo: ");
        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails userStateDetails) {
                Log.i(TAG, userStateDetails.getUserState().toString());
                switch (userStateDetails.getUserState()){
                    case SIGNED_IN:
                        AWSMobileClient.getInstance().signOut();
                        Log.i(TAG, "onResult: "+userStateDetails.getDetails().toString());
                        try {
                            Log.i(TAG, "onResulta: "+AWSMobileClient.getInstance().getUserAttributes().toString());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case SIGNED_OUT:
                        Log.i(TAG, "onResult: logout");
                        break;
                    default:
                        AWSMobileClient.getInstance().signOut();
                        Log.i(TAG, "onResult: def");
                        break;
                }

            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, e.toString());
            }
        });
    }

    public void getUserDetails(){
        try {
            AWSMobileClient.getInstance().getUserAttributes();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void setUserAttributes(Map<String, String> userAttributes){
        Map <String ,String> attr = new HashMap<>();
        attr.put("name","ritesh");
        AWSMobileClient.getInstance().updateUserAttributes(attr, new Callback<List<UserCodeDeliveryDetails>>() {
            @Override
            public void onResult(List<UserCodeDeliveryDetails> result) {
                try {
                    Log.i(TAG, "onResultu: "+result+AWSMobileClient.getInstance().getUserAttributes().toString());
                } catch (Exception e) {
                    Log.i(TAG, "onResultuf: "+e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                Log.i(TAG, "onErroru: "+e);
            }
        });

    }

    public void login(){
        Amplify.Auth.resetPassword(
                phoneNumber,
                result -> Log.i(TAG, result.toString()),
                error -> Log.e(TAG, error.toString())
        );
    }

    public void signIn(String username,String password){
        Amplify.Auth.signIn(
                username,
                password,
                result -> Log.i("AuthQuickstart", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete"),
                error -> Log.e("AuthQuickstart", error.toString())
        );
    }

    public void confirmLogin(String code){
        String newPassword ="12345"+code;
        Amplify.Auth.confirmResetPassword(
                newPassword,
                code,
                () -> Log.i("AuthQuickstart", "New password confirmed"),
                error -> Log.e("AuthQuickstart", error.toString())
        );
    }

    private void amplifyInit() {
        // Add this line, to include the Auth plugin.
        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());
            Amplify.Auth.fetchAuthSession(
                    result -> Log.i("AmplifyQuickstart", result.toString()),
                    error -> Log.e("AmplifyQuickstart", error.toString())
            );
            Amplify.Auth.fetchAuthSession(
                    result->{

                        Log.i(TAG, "amplifyInit: "+result);

                    },
                    error ->{
                        Log.i(TAG, "amplifyInit: "+error);

                    }
            );



        } catch (AmplifyException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.confirm:
                Log.i(TAG, "onClick: confirm");
                String otp=otpInput.getText().toString();
                confirmLogin(otp);
                break;

        }
    }

    void signUp(){
        Amplify.Auth.signUp(
                phoneNumber,
                phoneNumber,
                AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.phoneNumber(),phoneNumber).build(),
                result -> {
                    Log.i(TAG, "Result: " + result.toString());
                },
                error -> {
                    Log.e(TAG, "Sign up failed", error);
                }
        );
    }

    void confirmSignUp(String code){
        Log.i(TAG, "confirmSignUp: ");
        Amplify.Auth.confirmSignUp(
                phoneNumber,
                code,
                result ->{
                    Log.i(TAG, result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete");
                } ,
                error ->{
                    Log.e(TAG, error.toString());
                }
        );
    }


}
