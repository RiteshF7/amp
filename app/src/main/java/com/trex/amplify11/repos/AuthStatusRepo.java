package com.trex.amplify11.repos;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.trex.amplify11.BaseApplication;

public class AuthStatusRepo {

    public static AuthStatusRepo instance;
    MutableLiveData<Boolean> isUserLoggedIn = new MutableLiveData<>();
    private static final String TAG = "AuthStatusRepo";


    public static AuthStatusRepo getInstance(){
        if(instance==null)
        return instance=new AuthStatusRepo();
        else
        return instance;
    }

    public void checkUserStatus(){
        AWSMobileClient.getInstance().initialize(BaseApplication.getAppContext(), new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails userStateDetails) {
                Log.i(TAG, userStateDetails.getUserState().toString());
                switch (userStateDetails.getUserState()){
                    case SIGNED_IN:
                        isUserLoggedIn.postValue(true);
                        break;
                    case SIGNED_OUT:
                        isUserLoggedIn.postValue(false);
                        break;
                    default:
                        AWSMobileClient.getInstance().signOut();
                        isUserLoggedIn.postValue(false);
                        break;
                }

            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, e.toString());
            }
        });
    }

    public MutableLiveData<Boolean> getIsUserLoggedIn() {
        return isUserLoggedIn;
    }
}
