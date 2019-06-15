package com.example.android.miveh2.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.android.miveh2.R;


public class RatingDialog extends Dialog implements View.OnClickListener {


    OnButtonClickListener onButtonClickListener;

    private String TAG = RatingDialog.class.getSimpleName();

    private LinearLayout llImprovement, llSatisfactory, llExcellent;


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
        setContentView(R.layout.rating_common);

        initialization();

        //getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


    }

    private void initialization() {

        llExcellent = findViewById(R.id.llExcellent);
        llSatisfactory = findViewById(R.id.llSatisfactory);
        llImprovement = findViewById(R.id.llImprovement);

        setListener();
    }

    private void setListener() {

        llExcellent.setOnClickListener(this);
        llImprovement.setOnClickListener(this);
        llSatisfactory.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {



        onButtonClickListener.onOkClick(v);
    }


    public interface OnButtonClickListener {
        void onOkClick(View view);
    }


}