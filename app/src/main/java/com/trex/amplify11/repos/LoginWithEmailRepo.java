package com.trex.amplify11.repos;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.trex.amplify11.GlobalConstants;
import com.trex.amplify11.ui.LoginWithEmail;

public class LoginWithEmailRepo {
    private static final String TAG = "LoginWithEmailRepo";
    private MutableLiveData<String> isLoginWithPhoneNumberSuccessful = new MutableLiveData<>();
    public static LoginWithEmailRepo instance;

    public static LoginWithEmailRepo getInstance(){
        if(instance==null)
            return instance=new LoginWithEmailRepo();
        else
            return instance;
    }

    public void loginWithEmail(String username,String password){
        signIn(username,password);
    }

    public void signUpWithEmail(String username,String password){
            signUpWithMail(username,password);
    }

    public void confirmOtpFromMail(String email,String password,String code){
        confirmSignUp(email,password,code);
    }


    private void signIn(String username,String password){
        Log.i(TAG, "confirmLogin: "+username+" : "+password);
        Amplify.Auth.signIn(
                username.trim(),
                password.trim(),
                result -> {
                    Log.i("AuthQuickstart", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");

                    if(result.isSignInComplete()){
                        isLoginWithPhoneNumberSuccessful.postValue(GlobalConstants.SIGN_IN_SUCCESS);
                    }
                    else {
                        isLoginWithPhoneNumberSuccessful.postValue(GlobalConstants.SIGN_IN_FAIL);
                        Log.i(TAG, "signIn: error");
                    }
                },
                error ->{
                    Log.e(TAG, error.toString());
                }
        );
    }


    private void signUpWithMail(String email,String password){
        Log.i(TAG, "signUp: ");
        Amplify.Auth.signUp(
                email,
                password,
                AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(),email).build(),
                result -> {
                    Log.i(TAG, "Result: " + result.toString());
                    isLoginWithPhoneNumberSuccessful.postValue(GlobalConstants.SIGN_IN_REQUEST_OTP_SIGNUP);
                },
                error -> {
                    Log.i(TAG, "signUp: error"+error);
                    String errorMessage = error.getCause().toString();

                }
        );
    }

    private void confirmSignUp(String email,String password,String code){

        Amplify.Auth.confirmSignUp(
                email,
                code,
                result ->{
                    Log.i(TAG, result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete");
                    signIn(email,password);
                } ,
                error ->{
                    Log.e(TAG, error.toString());
                }
        );
    }

    public LiveData<String> getIsLoginWithPhoneNumberSuccessful() {
        return isLoginWithPhoneNumberSuccessful;
    }
}
