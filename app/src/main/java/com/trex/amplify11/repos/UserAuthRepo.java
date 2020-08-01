package com.trex.amplify11.repos;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.trex.amplify11.BaseApplication;

public class UserAuthRepo {




    private static final String TAG = "UserAuthRepo";
    public static UserAuthRepo instance;
    MutableLiveData<String> isLoginWithPhoneNumberSuccessful = new MutableLiveData<>();



    public static UserAuthRepo getInstance(){

        if(instance==null){
            return instance=new UserAuthRepo();
        }
        else {
            return instance;
        }
    }

    public void loginWithPhoneNumber(String phoneNumber){
        isLoginWithPhoneNumberSuccessful.postValue("value received : "+phoneNumber);
        signUp(phoneNumber);

    }

    public MutableLiveData<String> getIsLoginWithPhoneNumberSuccessful() {
        return isLoginWithPhoneNumberSuccessful;
    }

    void signUp(String phNumber){
        Log.i(TAG, "signUp: ");
        Amplify.Auth.signUp(
                phNumber,
                phNumber,
                AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.phoneNumber(),phNumber).build(),
                result -> {
                    Log.i(TAG, "Result: " + result.toString());
                    //startActivityForResult(new Intent(LoginWithPhoneNumber.this,ConfirmOTP.class),SIGNUP_LAUNCH_CODE);
                    //showOtpFragment();
                    isLoginWithPhoneNumberSuccessful.postValue("otpRequestSignUp");
                },
                error -> {
                    Log.i(TAG, "signUp: error"+error);
                    String errorMessage = error.getCause().toString();
                    if(errorMessage.contains("UsernameExistsException")){
                        login(phNumber);
                    }

                }
        );
    }



    public void login(String phNumber){
        Log.i(TAG, "login: ");
        Amplify.Auth.resetPassword(
                phNumber,
                result -> {
                    Log.i(TAG, result.toString());
                    isLoginWithPhoneNumberSuccessful.postValue("otpRequestLogin");
                },
                error -> Log.e(TAG, error.toString())
        );
    }

    public void confirmCode(String type,String code,String phoneNum){
        Log.i(TAG, "confirmCode: ");
        switch (type){
            case "otpRequestLogin":
                Log.i(TAG, "confirmCode: login");
                confirmLogin(phoneNum,code);
                break;
            case "otpRequestSignUp":
                confirmSignUp(phoneNum,code);
                break;
        }
    }

    public void confirmLogin(String phoneNum,String code){
        Log.i(TAG, "confirmLoginerror: "+code);
        String newPassword ="12345"+code;
        Amplify.Auth.confirmResetPassword(
                newPassword,
                code,
                () -> {
                    Log.i(TAG, "confirmLogin: "+phoneNum+" : "+newPassword);
                    Log.i("AuthQuickstart", "New password confirmed");
                    signIn(phoneNum,newPassword);
                },
                error -> Log.e("confirmLoginerror", error.toString())
        );
    }

    void confirmSignUp(String phNumber,String code){

        String newPassword="12345"+code;
        Log.i(TAG, "confirmSignUp with : "+phNumber+code);
        Amplify.Auth.confirmSignUp(
                phNumber,
                //TODO should be code insted of phone number .test it!
                code,
                result ->{
                    Log.i(TAG, result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete");
                    signIn(phNumber,newPassword);
                } ,
                error ->{
                    Log.e(TAG, error.toString());
                }
        );
    }

    public void signIn(String username,String password){
        Log.i(TAG, "confirmLogin: "+username+" : "+password);
        Amplify.Auth.signIn(
                username.trim(),
                password.trim(),
                result -> {
                    Log.i("AuthQuickstart", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");
                    if(result.isSignInComplete()){
                       isLoginWithPhoneNumberSuccessful.postValue("signInSuccessful");
                    }
                    else {
                        isLoginWithPhoneNumberSuccessful.postValue("Error");
                        Log.i(TAG, "signIn: error");
                    }
                },
                error ->{
                    Log.e(TAG, error.toString());
                }
        );
    }

    public void amplifyInit() {
        // Add this line, to include the Auth plugin.
        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(BaseApplication.getAppContext());

        } catch (AmplifyException e) {
            e.printStackTrace();
        }

    }
}
