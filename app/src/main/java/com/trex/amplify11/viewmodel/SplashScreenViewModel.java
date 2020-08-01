package com.trex.amplify11.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.trex.amplify11.repos.AuthStatusRepo;
import com.trex.amplify11.repos.UserAuthRepo;

public class SplashScreenViewModel extends ViewModel {

    private AuthStatusRepo authStatusRepo;

    public void intiSplashViewModel(){
        authStatusRepo=AuthStatusRepo.getInstance();
        authStatusRepo.checkUserStatus();
    }

    public LiveData<Boolean> getUserAuthStatus(){
        return authStatusRepo.getIsUserLoggedIn();
    }
}
