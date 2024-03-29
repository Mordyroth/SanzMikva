package com.example.android.miveh2.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.android.miveh2.R;


public class RatingDialog extends Dialog implements View.OnClickListener {


    OnButtonClickListener onButtonClickListener;

    private String TAG = RatingDialog.class.getSimpleName();

    private LinearLayout llImprovement, llSatisfactory, llExcellent;

    private ImageView ivClose;
    private Button btnNeedMoreTime;

    public RatingDialog(Context context, OnButtonClickListener onButtonClickListener) {
        super(context);
        this.onButtonClickListener = onButtonClickListener;


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        super.onCreate(savedInstanceState);
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);
        setContentView(R.layout.rating_common);

        initialization();

        //getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


    }

    private void initialization() {

        llExcellent = findViewById(R.id.llExcellent);
        llSatisfactory = findViewById(R.id.llSatisfactory);
        llImprovement = findViewById(R.id.llImprovement);
        ivClose = findViewById(R.id.ivClose);
        btnNeedMoreTime = findViewById(R.id.btnNeedMoreTime);

        setListener();
    }

    private void setListener() {

        llExcellent.setOnClickListener(this);
        llImprovement.setOnClickListener(this);
        llSatisfactory.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        btnNeedMoreTime.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.ivClose || v.getId() == R.id.btnNeedMoreTime) {
            dismiss();
        }

        onButtonClickListener.onOkClick(v);
    }


    public interface OnButtonClickListener {
        void onOkClick(View view);
    }


}