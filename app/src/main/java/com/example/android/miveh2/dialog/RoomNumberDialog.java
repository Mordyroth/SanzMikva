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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.miveh2.R;
import com.example.android.miveh2.model.FireBaseDBInstanceModel;
import com.example.android.miveh2.model.Room;
import com.example.android.miveh2.utils.AppUtils;
import com.example.android.miveh2.utils.PreferenceUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RoomNumberDialog extends Dialog implements android.view.View.OnClickListener {

    private final static String TAG = RoomNumberDialog.class.getSimpleName();

    public Activity activity;
    public Dialog dialog;
    public Button yes, no;

    private EditText edtRoomCode;
    String allPin = null;

    private DatabaseReference mFireBaseDatabase;

    private String mRoomNumber = null;
    private ArrayList<Room> mRoomsFromServerList = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private String mUUID;
    private CommonDialog commonDialog;

    private ProgressBar progressBar;
    private boolean isRoomUUidSame = false;

    public RoomNumberDialog(Activity a) {
        super(a);
        this.activity = a;


        FirebaseDatabase mFirebaseInstance = FireBaseDBInstanceModel.getInstance().getmFirebaseInstance();
        mFireBaseDatabase = mFirebaseInstance.getReference(AppUtils.ROOM_TABLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.room_number_dialog);

        setCancelable(false);

        edtRoomCode = (EditText) findViewById(R.id.edtRoomCode);

        int minValue = 1;
        int maxValue = 25;


        edtRoomCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtRoomCode.setFilters(new InputFilter[]{
                new InputFilterMinMax(minValue, maxValue),
                new InputFilter.LengthFilter(String.valueOf(maxValue).length()),
        });
        edtRoomCode.setKeyListener(DigitsKeyListener.getInstance("0123456789"));

        progressBar = findViewById(R.id.ivProgress);


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


    public int getRoomNumber() {
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
                if (getRoomNumber() != 0 && getRoomNumber() <= 25) {

                    progressBar.setVisibility(View.VISIBLE);
                    getDataFromServer();


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


    public void getDataFromServer() {
        //showProgressDialog();

        mFireBaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                        Room devicesEvent = datas.getValue(Room.class);
                        devicesEvent.setRoom_key(datas.getKey());
                        mRoomsFromServerList.add(devicesEvent);
                    }
                }
                progressBar.setVisibility(View.GONE);
                setInitiateRoom();
                //hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Failed to read devices", error.toException());
                //hideProgressDialog();
            }
        });
    }


    private void setInitiateRoom() {


        mRoomNumber = String.valueOf(getRoomNumber());
        mUUID = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);

        final Room room = new Room();
        Log.e("getToken:::", "" + PreferenceUtils.getInstance(activity.getApplicationContext()).get(AppUtils.FCM_TOKEN));
        room.setUuid(mUUID);
        room.setStatus(activity.getString(R.string.occupied));

        if (TextUtils.isEmpty(mRoomNumber)) {
            mRoomNumber = mFireBaseDatabase.push().getKey();
        }

        boolean isRoomExist = checkRoomExist(mRoomNumber);


        if (isRoomUUidSame) {

            commonDialog = new CommonDialog(getContext(), activity.getString(R.string.alert), activity.getString(R.string.uuid_room_occupied), activity.getString(R.string.okay), "", new CommonDialog.OnButtonClickListener() {
                @Override
                public void onOkClick(View view) {
                    commonDialog.dismiss();


                }
            });

            commonDialog.show();

        } else if (isRoomExist) {


            commonDialog = new CommonDialog(getContext(), activity.getString(R.string.alert), activity.getString(R.string.aledy_occupied), activity.getString(R.string.yes), activity.getString(R.string.str_no), new CommonDialog.OnButtonClickListener() {
                @Override
                public void onOkClick(View view) {

                    if (view.getId() == R.id.tvYes) {
                        addRoomInFireBase(room);
                        commonDialog.dismiss();

                    } else {
                        commonDialog.dismiss();
                    }
                }
            });

            commonDialog.show();


        } else {

            addRoomInFireBase(room);
        }
    }

    private void addRoomInFireBase(final Room room) {
        try {
            mFireBaseDatabase.child(mRoomNumber).setValue(room).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(activity, activity.getString(R.string.occupied_successfully), Toast.LENGTH_SHORT).show();
                    PreferenceUtils.getInstance(getContext()).save(AppUtils.ROOM_NUMBER, Integer.parseInt(mRoomNumber));

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkRoomExist(String roomNo) {


        boolean isValid = false;

        for (int i = 0; i < mRoomsFromServerList.size(); i++) {


            if (mRoomsFromServerList.get(i).getUuid().equalsIgnoreCase(mUUID) && mRoomsFromServerList.get(i).getRoom_key().equalsIgnoreCase(roomNo)) {
                isRoomUUidSame = true;
                break;
            } else if (mRoomsFromServerList.get(i).getRoom_key().equalsIgnoreCase(roomNo)) {
                isValid = true;
                break;

            } else {
                isValid = false;
            }


        }
        return isValid;
    }





    /*public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(activity);
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
