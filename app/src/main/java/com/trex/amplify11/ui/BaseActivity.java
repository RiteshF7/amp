package com.trex.amplify11.ui;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    ProgressDialog progressBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressBarView = new ProgressDialog(this);
        progressBarView.setCancelable(false);
        progressBarView.setTitle("Please Wait");
        progressBarView.setMessage("Please Wait while we are checking...");
        progressBarView.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

//                .setLayoutParams(new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT));
        progressBarView.dismiss();

    }

    public  void showProgressBar(){
        progressBarView.show();
        Toast.makeText(this, "shown", Toast.LENGTH_SHORT).show();
    }

    public void hideProgressBar(){
        progressBarView.dismiss();
    }
}