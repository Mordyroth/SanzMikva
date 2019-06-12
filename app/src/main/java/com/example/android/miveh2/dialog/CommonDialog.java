package com.example.android.miveh2.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.miveh2.R;


public class CommonDialog extends Dialog implements View.OnClickListener {


    OnButtonClickListener onButtonClickListener;

    TextView tvTitle;

    TextView tvYes;

    TextView tvNo;


    TextView tvmessage;

    ImageView ivClose;

    private String message;
    private String title;
    private String posMessage;
    private String navMsg;
    boolean isCloseVisible;


    public CommonDialog(Context context, String title, String message, String posMessage, String navMsg, OnButtonClickListener onButtonClickListener) {
        super(context);
        this.onButtonClickListener = onButtonClickListener;
        this.message = message;
        this.title = title;
        this.posMessage = posMessage;
        this.navMsg = navMsg;


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_common);

        initialization();

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);

        if (title == null || title.equalsIgnoreCase(""))
            tvTitle.setVisibility(View.GONE);
        tvTitle.setText(title);
        tvmessage.setText(message);
        if(navMsg.equalsIgnoreCase(""))
        {
            tvNo.setVisibility(View.GONE);
        }
        tvNo.setText(navMsg);
        tvYes.setText(posMessage);


    }

    private void initialization() {
        tvmessage = findViewById(R.id.tvMessage);
        tvTitle = findViewById(R.id.tv_title);
        tvYes = findViewById(R.id.tvYes);
        tvNo = findViewById(R.id.tvNo);

        setListener();
    }

    private void setListener() {
        tvNo.setOnClickListener(this);
        tvYes.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onButtonClickListener.onOkClick(view);
    }


    public interface OnButtonClickListener {
        void onOkClick(View view);
    }

}