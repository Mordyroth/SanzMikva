package com.example.android.miveh2.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
    private Context context;


    public CommonDialog(Context context, String title, String message, String posMessage, String navMsg, OnButtonClickListener onButtonClickListener) {
        super(context);
        this.onButtonClickListener = onButtonClickListener;
        this.message = message;
        this.title = title;
        this.posMessage = posMessage;
        this.navMsg = navMsg;
        this.context = context;


    }

    public CommonDialog(Context context, String title, String message, String posMessage, String navMsg, boolean isEdittextVisible, OnButtonClickListenerWithEdit OnButtonClickListenerWithValue) {
        super(context);
        this.OnButtonClickListenerWithEdit = OnButtonClickListenerWithValue;
        this.message = message;
        this.title = title;
        this.posMessage = posMessage;
        this.navMsg = navMsg;
        this.isEdittextVisible = isEdittextVisible;
        this.context = context;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        super.onCreate(savedInstanceState);
        setCancelable(false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_common);

        initialization();





    }

    private void initialization() {
        tvmessage = findViewById(R.id.tvMessage);
        tvTitle = findViewById(R.id.tv_title);
        tvYes = findViewById(R.id.tvYes);
        tvNo = findViewById(R.id.tvNo);
        etFeedbacks = findViewById(R.id.etFeedback);


        if (title == null || title.equalsIgnoreCase(""))
            tvTitle.setVisibility(View.GONE);

        if (title.equalsIgnoreCase(context.getString(R.string.str_warning))) {
            tvTitle.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
        tvTitle.setText(title);
        tvmessage.setText(message);
        if (navMsg.equalsIgnoreCase("")) {
            tvNo.setVisibility(View.GONE);
        }
        tvNo.setText(navMsg);
        tvYes.setText(posMessage);
        etFeedbacks.setVisibility(isEdittextVisible ? View.VISIBLE : View.GONE);
        if(isEdittextVisible)
        {
            if (etFeedbacks.getText().toString().length() == 0) {
                tvYes.setEnabled(false);
                tvYes.setAlpha(0.5f);
            }
        }
        setListener();
    }

    private void setListener() {
        tvNo.setOnClickListener(this);
        tvYes.setOnClickListener(this);
        etFeedbacks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEdittextVisible) {
                    if (etFeedbacks.getText().toString().length() == 0) {
                        tvYes.setEnabled(false);
                        tvYes.setAlpha(0.5f);
                    } else {
                        tvYes.setEnabled(true);
                        tvYes.setAlpha(1.0f);
                    }
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (isEdittextVisible)
            OnButtonClickListenerWithEdit.onOkClick(view, etFeedbacks.getText().toString().trim(), this);
        else
            onButtonClickListener.onOkClick(view, this);

    }


    public interface OnButtonClickListener {
        void onOkClick(View view, CommonDialog commonDialog);


    }


    public interface OnButtonClickListenerWithEdit {


        void onOkClick(View view, String value, CommonDialog commonDialog);


    }

}