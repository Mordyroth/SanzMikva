package com.example.android.miveh2.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.miveh2.utils.AppUtils;
import com.example.android.miveh2.R;
import com.example.android.miveh2.model.FireBaseDBInstanceModel;
import com.example.android.miveh2.model.DevicesEvent;
import com.example.android.miveh2.utils.PreferenceUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RoomNumberDialog extends Dialog implements android.view.View.OnClickListener {

    private final static String TAG = RoomNumberDialog.class.getSimpleName();

    public Activity c;
    public Dialog d;
    public Button yes, no;

    EditText edtRoomCode;
    String allPin = null;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    String userId = null;
    ArrayList<DevicesEvent> listDeviceEvents = null;
    private ProgressDialog mProgressDialog;

    public RoomNumberDialog(Activity a) {
        super(a);
        this.c = a;

        listDeviceEvents = new ArrayList<>();
        mFirebaseInstance = FireBaseDBInstanceModel.getInstance().getmFirebaseInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference(AppUtils.DEVICES_DB);
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
        edtRoomCode.setFilters(new InputFilter[]{
                new InputFilterMinMax(minValue, maxValue),
                new InputFilter.LengthFilter(String.valueOf(maxValue).length()),
        });
        edtRoomCode.setKeyListener(DigitsKeyListener.getInstance("0123456789"));


        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

        getDataFromServer();

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


    public int getValue() {
        try {
            return Integer.parseInt(edtRoomCode.getText().toString());
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                if (getValue() != 0 && getValue() <= 25) {
                    Toast.makeText(c, "" + getValue(), Toast.LENGTH_LONG).show();
                    //c.finish();
                    //setInitiateRoom();

                } else {
                    Log.e("TAG:::", "is not under 25");
                }
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }





    public void getDataFromServer()
    {
        //showProgressDialog();
        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for (DataSnapshot datas : dataSnapshot.getChildren())
                    {
                        DevicesEvent devicesEvent = datas.getValue(DevicesEvent.class);
                        devicesEvent.setRoom_key(datas.getKey());
                        listDeviceEvents.add(devicesEvent);
                    }
                }
                //hideProgressDialog();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read devices", error.toException());
                //hideProgressDialog();
            }
        });
    }


    private void setInitiateRoom() {
        userId = String.valueOf(getValue());

        final DevicesEvent user = new DevicesEvent();



        Log.e("getToken:::",""+PreferenceUtils.getInstance(c.getApplicationContext()).get(AppUtils.FCM_TOKEN));
        user.setToken(PreferenceUtils.getInstance(c.getApplicationContext()).get(AppUtils.FCM_TOKEN));

        user.setUuid(Settings.Secure.getString(c.getContentResolver(), Settings.Secure.ANDROID_ID));

        user.setStatus("Not Occupied");

        if (TextUtils.isEmpty(userId)) {
            userId = mFirebaseDatabase.push().getKey();
        }

        boolean isCheck = getAllDeviceDetials(userId);

        if(!isCheck) {
            try {
                mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        mFirebaseDatabase.child(userId).setValue(user);

                        DevicesEvent user = dataSnapshot.getValue(DevicesEvent.class);
                        if (user == null) {
                            Log.e(TAG, "Devices data is null!");
                            return;
                        } else {
                            Log.e(TAG, "Devices data is changed!" + user.getToken() + ", " + user.getUuid());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e(TAG, "Failed to read devices", error.toException());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean getAllDeviceDetials(String roomNo){
        boolean isValid = false;

        for (int i = 0; i < listDeviceEvents.size(); i++) {
            if(listDeviceEvents.get(i).getRoom_key().equalsIgnoreCase(roomNo) &&
             listDeviceEvents.get(i).getRoom_key().equals(roomNo) &&
             !TextUtils.isEmpty(listDeviceEvents.get(i).getRoom_key()))
            {
                isValid = true;
                break;
            }
            else {
                isValid = false;
            }
        }
        return isValid;
    }

    /*public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(c);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }
        mProgressDialog.show();
    }
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }*/
}
