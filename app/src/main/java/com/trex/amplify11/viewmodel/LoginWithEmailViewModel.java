package com.trex.amplify11.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.trex.amplify11.repos.LoginWithEmailRepo;

public class LoginWithEmailViewModel extends ViewModel {

    private LoginWithEmailRepo repo;

    public void intiViewModel(){
        repo = LoginWithEmailRepo.getInstance();
    }

    public void signUp(String username,String password){
    repo.signUpWithEmail(username,password);
    }

    public void login(String username,String password){
        repo.loginWithEmail(username,password);
    }

    public void confirmOtp(String mail,String pass,String code){
        repo.confirmOtpFromMail(mail,pass,code);
    }

    public LiveData<String> getUserAuthStatus() {
        return repo.getIsLoginWithPhoneNumberSuccessful();
    }
}
