package com.example.android.miveh2.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
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
    boolean isEdittextVisible = false;
    private EditText etFeedbacks;
    private OnButtonClickListenerWithEdit OnButtonClickListenerWithEdit;


    public CommonDialog(Context context, String title, String message, String posMessage, String navMsg, OnButtonClickListener onButtonClickListener) {
        super(context);
        this.onButtonClickListener = onButtonClickListener;
        this.message = message;
        this.title = title;
        this.posMessage = posMessage;
        this.navMsg = navMsg;


    }

    public CommonDialog(Context context, String title, String message, String posMessage, String navMsg, boolean isEdittextVisible, OnButtonClickListenerWithEdit OnButtonClickListenerWithValue) {
        super(context);
        this.OnButtonClickListenerWithEdit = OnButtonClickListenerWithValue;
        this.message = message;
        this.title = title;
        this.posMessage = posMessage;
        this.navMsg = navMsg;
        this.isEdittextVisible = isEdittextVisible;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        super.onCreate(savedInstanceState);
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_common);

        initialization();




        if (title == null || title.equalsIgnoreCase(""))
            tvTitle.setVisibility(View.GONE);
        tvTitle.setText(title);
        tvmessage.setText(message);
        if (navMsg.equalsIgnoreCase("")) {
            tvNo.setVisibility(View.GONE);
        }
        tvNo.setText(navMsg);
        tvYes.setText(posMessage);
        etFeedbacks.setVisibility(isEdittextVisible ? View.VISIBLE : View.GONE);


    }

    private void initialization() {
        tvmessage = findViewById(R.id.tvMessage);
        tvTitle = findViewById(R.id.tv_title);
        tvYes = findViewById(R.id.tvYes);
        tvNo = findViewById(R.id.tvNo);
        etFeedbacks = findViewById(R.id.etFeedback);

        setListener();
    }

    private void setListener() {
        tvNo.setOnClickListener(this);
        tvYes.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (isEdittextVisible)
            OnButtonClickListenerWithEdit.onOkClick(view, etFeedbacks.getText().toString().trim());
        else
            onButtonClickListener.onOkClick(view);

    }


    public interface OnButtonClickListener {
        void onOkClick(View view);


    }

    public interface OnButtonClickListenerWithEdit {


        void onOkClick(View view, String value);
    }

}