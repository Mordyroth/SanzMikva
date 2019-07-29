package com.example.android.miveh2.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.miveh2.R;
import com.example.android.miveh2.utils.AppUtils;

public class PinDialog extends Dialog implements View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;

    EditText edtCode1;
    EditText edtCode2;
    EditText edtCode3;
    EditText edtCode4;
    String allPin = null;
    RoomNumberDialog roomNnmberDialog;

    public PinDialog(Activity a) {
        super(a);
        this.c = a;
        roomNnmberDialog = new RoomNumberDialog(c);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pinview_dialog);

        setCancelable(false);

        edtCode1 = (EditText) findViewById(R.id.edtCode1);
        edtCode2 = (EditText) findViewById(R.id.edtCode2);
        edtCode3 = (EditText) findViewById(R.id.edtCode3);
        edtCode4 = (EditText) findViewById(R.id.edtCode4);
        //  edtCode1.performClick();


        //   AppUtils.showKeyboard(c);
        edtCode1.requestFocus();

        edtCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) {
                    setPin(s, edtCode1, edtCode2);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredPin = edtCode1.getText().toString().trim();
                enteredPin += (edtCode2.getText().toString().trim());
                enteredPin += (edtCode3.getText().toString().trim());
                enteredPin += (edtCode4.getText().toString().trim());

                if (!TextUtils.isEmpty(edtCode1.getText().toString().trim()) && !TextUtils.isEmpty(edtCode2.getText().toString().trim()) && !TextUtils.isEmpty(edtCode3.getText().toString().trim())) {
                    AppUtils.hideKeyboard(c);
                    verifyCode(enteredPin);

                    if (!roomNnmberDialog.isShowing())
                        roomNnmberDialog.show();
                }

            }
        });

        edtCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) {
                    setPin(s, edtCode2, edtCode3);
                } else if (s.length() == 0) {
                    setFocus(edtCode1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredPin = edtCode1.getText().toString().trim();
                enteredPin += (edtCode2.getText().toString().trim());
                enteredPin += (edtCode3.getText().toString().trim());
                enteredPin += (edtCode4.getText().toString().trim());

                if (!TextUtils.isEmpty(edtCode1.getText().toString().trim()) && !TextUtils.isEmpty(edtCode2.getText().toString().trim()) && !TextUtils.isEmpty(edtCode3.getText().toString().trim()) && !TextUtils.isEmpty(edtCode4.getText().toString().trim())) {
                    AppUtils.hideKeyboard(c);
                    verifyCode(enteredPin);

                    if (!roomNnmberDialog.isShowing())
                        roomNnmberDialog.show();
                }

            }
        });


        edtCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 1) {
                    setPin(s, edtCode3, edtCode4);
                } else if (s.length() == 0) {
                    setFocus(edtCode2);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredPin = edtCode1.getText().toString().trim();
                enteredPin += (edtCode2.getText().toString().trim());
                enteredPin += (edtCode3.getText().toString().trim());
                enteredPin += (edtCode4.getText().toString().trim());

                if (!TextUtils.isEmpty(edtCode1.getText().toString().trim()) && !TextUtils.isEmpty(edtCode2.getText().toString().trim()) && !TextUtils.isEmpty(edtCode3.getText().toString().trim()) && !TextUtils.isEmpty(edtCode4.getText().toString().trim())) {
                    AppUtils.hideKeyboard(c);
                    verifyCode(enteredPin);

                    if (!roomNnmberDialog.isShowing())
                        roomNnmberDialog.show();
                }

            }
        });

        edtCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    String enteredPin = edtCode1.getText().toString().trim();
                    enteredPin += (edtCode2.getText().toString().trim());
                    enteredPin += (edtCode3.getText().toString().trim());
                    enteredPin += (edtCode4.getText().toString().trim());

                    if (!TextUtils.isEmpty(edtCode1.getText().toString().trim()) && !TextUtils.isEmpty(edtCode2.getText().toString().trim()) && !TextUtils.isEmpty(edtCode3.getText().toString().trim()) && !TextUtils.isEmpty(edtCode4.getText().toString().trim())) {
                        AppUtils.hideKeyboard(c);
                        verifyCode(enteredPin);

                        if (!roomNnmberDialog.isShowing())
                            roomNnmberDialog.show();
                    }
                } else if (s.length() == 0) {
                    setFocus(edtCode3);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);


        /*int hight = (int) getContext().getResources().getDimension(R.dimen._50sdp);
        int width = (int) getContext().getResources().getDimension(R.dimen._60sdp);

        Pinview pin = (Pinview) findViewById(R.id.pinview);
        pin.setPinBackgroundRes(R.drawable.sample_background);
        pin.setPinHeight(hight);
        pin.setPinWidth(width);*/
        // AppUtils.hideKeyboard(c);


       /* pin.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                //Make api calls here or what not
                AppUtils.hideKeyboard(c);
                verifyCode(pinview.getValue());

                RoomNumberDialog roomNnmberDialog = new RoomNumberDialog(c);
                roomNnmberDialog.show();
            }
        });*/


    }


    private void setFocus(EditText editText) {
        editText.requestFocus();
        editText.setSelection(editText.getText().toString().length());
    }

    private void setPin(CharSequence s, EditText edtCode1, EditText edtCode2) {
        edtCode1.setText(String.valueOf(s.toString().charAt(0)));
        edtCode2.requestFocus();
        edtCode2.setText(String.valueOf(s.toString().charAt(1)));
        edtCode2.setSelection(edtCode2.getText().toString().length());
    }

    public void verifyCode(String enteredPin) {
        allPin = enteredPin;
        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                Toast.makeText(c, "" + allPin, Toast.LENGTH_LONG).show();
                //c.finish();
                break;
            case R.id.btn_no:
                dismiss();
                break;

        }
        dismiss();
    }

}
