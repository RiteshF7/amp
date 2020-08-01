package com.trex.amplify11.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.trex.amplify11.R;
import com.trex.amplify11.viewmodel.LoginWithEmailViewModel;

public class EnterOtpFromEmail extends DialogFragment {




    String email, pass;
    private EditText otpText;
    LoginWithEmailViewModel sharedViewModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enter_otp_dialog, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = ViewModelProviders.of(getActivity()).get(LoginWithEmailViewModel.class);
        otpText=view.findViewById(R.id.otp_text);
        Bundle bundle = getArguments();
        if (bundle != null) {
            email = bundle.getString("email");
            pass = bundle.getString("pass");
        }
        view.findViewById(R.id.confirm_otp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otpText.getText().toString();
                sharedViewModel.confirmOtp(email,pass,code);
            }
        });


    }
}