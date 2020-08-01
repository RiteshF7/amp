package com.trex.amplify11.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.trex.amplify11.R;
import com.trex.amplify11.ui.BaseActivity;

import javax.xml.transform.Result;

public class HomeScreen extends BaseActivity {

    TextView details;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        details=findViewById(R.id.details);
        logout=findViewById(R.id.logout);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        getUserInfo getUserInfoobj = new getUserInfo();
        getUserInfoobj.execute("this");


        logout.setOnClickListener(v -> {
            AWSMobileClient.getInstance().signOut();
            finish();
        });
    }


    class getUserInfo extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            getUserDetails();
            return null;
        }
    }

    public void getUserDetails(){
        try {
            details.setText(AWSMobileClient.getInstance().getUserAttributes().toString());
            Log.i("test", "getUserDetails: "+ AWSMobileClient.getInstance().getUserAttributes().toString());

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("test", "getUserDetails: "+ e);

        }
    }


}
