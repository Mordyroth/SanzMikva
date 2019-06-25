package com.example.android.miveh2.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.example.android.miveh2.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        initialization();
    }

    private void initialization() {

    }

    public void showProgressBar(boolean isProgress) {


        if (progressDialog == null)
            progressDialog = new ProgressDialog(this, R.style.AppTheme_ProgressDialog_Theme);

        progressDialog.setCancelable(false);
        // progressDialog.setTitle("please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        if (isProgress)
            progressDialog.show();
        else
            progressDialog.dismiss();
    }


}
