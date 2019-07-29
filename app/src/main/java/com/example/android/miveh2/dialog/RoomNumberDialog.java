package com.example.android.miveh2.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RoomNumberDialog extends Dialog implements android.view.View.OnClickListener {

    private final static String TAG = RoomNumberDialog.class.getSimpleName();
    private final DatabaseReference dbNextRoomHelp;
    private final DatabaseReference dbHelp, dbSetting;
    private final FirebaseDatabase rootRef;

    public Activity activity;

    public Button yes, no;

    private EditText edtRoomCode;


    private DatabaseReference dbRoomTable;

    private String mRoomNumber = null;
    private ArrayList<Room> mRoomsFromServerList = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private String mUUID;
    private CommonDialog commonDialog;

    private ProgressBar progressBar;
    private boolean isRoomUUidSame = false;
    private ProgressDialog progressDialog;

    public RoomNumberDialog(Activity a) {
        super(a);
        this.activity = a;


        rootRef = FireBaseDBInstanceModel.getInstance().getmFirebaseInstance();
        dbRoomTable = rootRef.getReference(AppUtils.ROOM_TABLE);
        dbHelp = rootRef.getReference(AppUtils.HELP_TABLE);

        dbNextRoomHelp = rootRef.getReference(AppUtils.NEXT_ROOM_HELP);
        dbSetting = rootRef.getReference(AppUtils.SETTING_TABLE);
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
        int maxValue = 19;


        edtRoomCode.setInputType(InputType.TYPE_CLASS_NUMBER);
        edtRoomCode.setFilters(new InputFilter[]{
                new InputFilterMinMax(minValue, maxValue),
                new InputFilter.LengthFilter(String.valueOf(maxValue).length()),
        });
        edtRoomCode.setKeyListener(DigitsKeyListener.getInstance("0123456789"));

        edtRoomCode.requestFocus();

        progressBar = findViewById(R.id.ivProgress);


        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);
        getDataFromServer();

        edtRoomCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edtRoomCode.setSelection(edtRoomCode.getText().length());

            }
        });

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

                if (!edtRoomCode.getText().toString().isEmpty()) {
                    if (getRoomNumber() != 0 && getRoomNumber() <= 19) {

                        progressBar.setVisibility(View.VISIBLE);
                        setInitiateRoom();
                        dismiss();


                    } else {
                        Log.e("TAG:::", "is not under 19");
                    }
                } else {
                    Toast.makeText(activity, activity.getString(R.string.str_room_number), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }

    }


    public void getDataFromServer() {
        //showProgressDialog();
        showProgressBar(true);
        dbRoomTable.addValueEventListener(new ValueEventListener() {
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

                showProgressBar(false);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                showProgressBar(false);
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Failed to read devices", error.toException());
                //hideProgressDialog();
            }
        });


    }


    private void setInitiateRoom() {
        AppUtils.hideKeyboard(activity);

        mRoomNumber = String.valueOf(getRoomNumber());
        mUUID = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);

        final Room room = new Room();
        Log.e("getToken:::", "" + PreferenceUtils.getInstance(activity.getApplicationContext()).get(AppUtils.FCM_TOKEN));
        room.setUuid(mUUID);
        room.setStatus(activity.getString(R.string.occupied));
        room.setRoom_key(mRoomNumber);

        if (TextUtils.isEmpty(mRoomNumber)) {
            mRoomNumber = dbRoomTable.push().getKey();
        }

        boolean isRoomExist = checkRoomExist(mRoomNumber);


        if (isRoomUUidSame) {

            CommonDialog commonDialog = new CommonDialog(getContext(), activity.getString(R.string.alert), activity.getString(R.string.uuid_room_occupied), activity.getString(R.string.okay), "", new CommonDialog.OnButtonClickListener() {
                @Override
                public void onOkClick(View view, CommonDialog commonDialog) {
                    commonDialog.dismiss();
                    PreferenceUtils.getInstance(getContext()).save(AppUtils.ROOM_NUMBER, mRoomNumber);
                    RoomNumberDialog roomNnmberDialog = new RoomNumberDialog(activity);
                    roomNnmberDialog.show();

                }
            });

            commonDialog.show();

            isRoomUUidSame = false;

        } else if (isRoomExist) {


            CommonDialog commonDialog = new CommonDialog(getContext(), activity.getString(R.string.str_warning), activity.getString(R.string.aledy_occupied), activity.getString(R.string.str_countinue), activity.getString(R.string.str_cancel), new CommonDialog.OnButtonClickListener() {
                @Override
                public void onOkClick(View view, CommonDialog commonDialog) {
                    if (view.getId() == R.id.tvYes) {

                        commonDialog.dismiss();
                        addRoomInFireBase(room);


                    } else {
                        commonDialog.dismiss();
                        PreferenceUtils.getInstance(getContext()).save(AppUtils.ROOM_NUMBER, mRoomNumber);
                        RoomNumberDialog roomNnmberDialog = new RoomNumberDialog(activity);
                        roomNnmberDialog.show();
                    }
                }


            });

            commonDialog.show();


        } else {


            addRoomInFireBase(room);
        }
    }

    private void addRoomInFireBase(final Room room) {

        final String mDate = AppUtils.getDate();


        showProgressBar(true);


        final Thread thread = new Thread() {

            @Override
            public void run() {
                rootRef.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(AppUtils.NEXT_ROOM_HELP)) {
                            Query applesQuery = dbRoomTable.orderByChild("uuid").equalTo(mUUID);
                            removePriveousData(applesQuery);
                        }
                        if (dataSnapshot.hasChild(AppUtils.HELP_TABLE)) {

                            Query applesQuery = dbHelp.orderByChild("uuid").equalTo(mUUID);
                            removePriveousData(applesQuery);

                            //.orderByChild("status").equalTo(Help.HELP_PRESS);
                            // removePriveousData(applesQuery1);
                        }
                        if (dataSnapshot.hasChild(AppUtils.NEXT_ROOM_HELP)) {
                            Query applesQuery2 = dbNextRoomHelp.child(mDate).orderByChild("uuid").equalTo(mUUID);
                            removePriveousData(applesQuery2);
                        }

                        if (dataSnapshot.hasChild(AppUtils.SETTING_TABLE)) {
                            Query applesQuery3 = dbSetting.orderByChild("uuid").equalTo(mUUID);
                            removePriveousData(applesQuery3);
                        }


                        try {
                            dbRoomTable.child(mRoomNumber).setValue(room).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(activity, activity.getString(R.string.occupied_successfully), Toast.LENGTH_SHORT).show();
                                    PreferenceUtils.getInstance(getContext()).save(AppUtils.ROOM_RESET, true);
                                    PreferenceUtils.getInstance(getContext()).save(AppUtils.ROOM_NUMBER, mRoomNumber);

                                    showProgressBar(false);
                                    dismiss();
                                    activity.recreate();


                    /*Intent intent = new Intent(getContext(), CustomListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);*/
                                }
                            });
                        } catch (Exception e) {
                            showProgressBar(false);
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

        };
        thread.start();


    }


    private void removePriveousData(Query applesQuery) {
        showProgressBar(true);
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }
                showProgressBar(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
                showProgressBar(false);
            }
        });
    }

    private boolean checkRoomExist(String roomNo) {


        boolean isValid = false;
        isRoomUUidSame = false;

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


    public void showProgressBar(boolean isProgress) {


       /* if (progressDialog == null)
            progressDialog = new ProgressDialog(getContext(), R.style.AppTheme_ProgressDialog_Theme);

        progressDialog.setCancelable(false);
        // progressDialog.setTitle("please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        if (isProgress)
            progressDialog.show();
        else
            progressDialog.dismiss();*/
    }
}
