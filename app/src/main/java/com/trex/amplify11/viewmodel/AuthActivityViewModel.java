package com.trex.amplify11.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trex.amplify11.repos.UserAuthRepo;

public class AuthActivityViewModel extends ViewModel {


    private UserAuthRepo userAuthRepo;
    private MutableLiveData<String> goTOActivity = new MutableLiveData<>();


    public void initViewModel(){
        userAuthRepo =UserAuthRepo.getInstance();
        userAuthRepo.amplifyInit();
    }


    public void confirmOtp(String phoneNum,String code,String type){
        userAuthRepo.confirmCode(type,code,phoneNum);
    }

    public LiveData<String> isUserLoginWithPhoneSuccessful(){
        return userAuthRepo.getIsLoginWithPhoneNumberSuccessful();
    }

    public void signInWithPhoneNumber(String phoneNumber){
        userAuthRepo.loginWithPhoneNumber(phoneNumber);
    }

    public MutableLiveData<String> getGoTOActivity() {
        return goTOActivity;
    }

    public void goTOActivity(String activity){

    }
}
