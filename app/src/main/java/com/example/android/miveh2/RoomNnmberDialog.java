package com.example.android.miveh2;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RoomNnmberDialog extends Dialog implements android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button yes, no;

    EditText edtRoomCode;
    String allPin = null;

    public RoomNnmberDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.room_number_dialog);

        setCancelable(false);

        edtRoomCode = (EditText) findViewById(R.id.edtRoomCode);

        /*edtRoomCode.addTextChangedListener(new TextWatcher() {
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

            }
        });*/


        int minValue = 1;
        int maxValue = 25;


        edtRoomCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtRoomCode.setFilters(new InputFilter[] {
                        new InputFilterMinMax(minValue, maxValue),
                        new InputFilter.LengthFilter(String.valueOf(maxValue).length())
                });
        edtRoomCode.setKeyListener(DigitsKeyListener.getInstance("0123456789"));



        /*edtCode4.addTextChangedListener(new TextWatcher() {
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


                    ActivityUtils.hideKeyboard(c);
                    verifyCode(enteredPin);
                }
                else if (s.length() == 0) {
                    setFocus(edtCode3);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }


    public class InputFilterMinMax implements InputFilter {

        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
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

    public void verifyCode(String enteredPin){
        allPin = enteredPin;
        Toast.makeText(c , ""+allPin, Toast.LENGTH_LONG).show();
        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                Toast.makeText(c , ""+allPin, Toast.LENGTH_LONG).show();
                //c.finish();
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}
