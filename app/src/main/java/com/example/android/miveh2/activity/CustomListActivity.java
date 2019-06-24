package com.example.android.miveh2.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.miveh2.R;
import com.example.android.miveh2.adapter.CustomUsersAdapter;
import com.example.android.miveh2.dialog.CommonDialog;
import com.example.android.miveh2.dialog.PinDialog;
import com.example.android.miveh2.dialog.RatingDialog;
import com.example.android.miveh2.model.Feedback;
import com.example.android.miveh2.model.Help;
import com.example.android.miveh2.model.NextRoomHelp;
import com.example.android.miveh2.model.Room;
import com.example.android.miveh2.model.User;
import com.example.android.miveh2.utils.AppUtils;
import com.example.android.miveh2.utils.LocaleHelper;
import com.example.android.miveh2.utils.PreferenceUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomListActivity extends BaseActivity {

    /*private Spinner spinner;*/
    RelativeLayout spinner2;
    TextView txtSpin;
    String SongItem;
    ImageView mPinView;
    private ImageView ivVolumeUp;
    private ImageView ivVolumeDown;
    private String mUUID = "";
    private String mRoomNumber = "";
    private Button btnHelp;
    private View helpLayout;
    private DatabaseReference dbHelp;
    private Help help;
    private Button btnReady;
    private Button btnDone;

    private String status = "";
    private CommonDialog commonDialog;
    private DatabaseReference dbFeedback;
    private String ratingStatus = "";
    private RatingDialog ratingDialog;
    private TextView tvCurrantRoom;
    private TextView tvStatusOfWorker;

    private SeekBar volumeSeekbar;
    private RelativeLayout rl_music;
    private DatabaseReference dbRoomTable;
    private ArrayList<Room> mRoomsFromServerList = new ArrayList<>();
    private DatabaseReference dbNextRoomHelp;
    private FirebaseDatabase rootRef;
    private String mDate = "";
    private TextView tvCurrantStatus;
    private static int mNextRoomHelpCount = 0;
    MediaPlayer mediaPlayer;
    private int maxVal = 0;
    public static String helpKey;
    private AVLoadingIndicatorView avi;
    private String songName;
    private String language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_customer_help);
        populateUsersList();
        mediaPlayer = new MediaPlayer();

        mDate = AppUtils.getDate();

        bindViews();
        initialization();
        setVolumeControl();


    }


    private void bindViews() {
        spinner2 = (RelativeLayout) findViewById(R.id.spinner2);
        txtSpin = (TextView) findViewById(R.id.txtSpin);
        mPinView = (ImageView) findViewById(R.id.mPinView);

        helpLayout = findViewById(R.id.arrival);
        btnHelp = (Button) findViewById(R.id.button);
        btnDone = (Button) findViewById(R.id.button3);

        btnReady = (Button) findViewById(R.id.button2);
        ivVolumeDown = findViewById(R.id.ivVolumeDown);
        ivVolumeUp = findViewById(R.id.ivVolumeUp);
        volumeSeekbar = (SeekBar) findViewById(R.id.seekbar);

        tvCurrantRoom = findViewById(R.id.number);
        tvStatusOfWorker = findViewById(R.id.ahead);
        tvCurrantStatus = findViewById(R.id.currently);

        rl_music = findViewById(R.id.rl_music);
        avi = findViewById(R.id.avi);

    }

    private void initialization() {

        rootRef = FirebaseDatabase.getInstance();
        dbHelp = rootRef.getReference(AppUtils.HELP_TABLE);
        dbFeedback = rootRef.getReference(AppUtils.FEEDBACK_TABLE);
        dbRoomTable = rootRef.getReference(AppUtils.ROOM_TABLE);
        dbNextRoomHelp = rootRef.getReference(AppUtils.NEXT_ROOM_HELP);


        //setRoomHelpTable(false);
        getRoom();
        setListener();
        getDataFromServer();

    }


    private void setNextRoomHelpValues() {

        showProgressBar(true);
        final List<NextRoomHelp> mNextRoomHelpList = new ArrayList<>();
        dbNextRoomHelp.child(mDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mNextRoomHelpList.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        NextRoomHelp help = data.getValue(NextRoomHelp.class);
                        mNextRoomHelpList.add(help);


                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getAheadOfPeople(mNextRoomHelpList);
                        }
                    });

                }

                showProgressBar(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showProgressBar(false);
            }
        });

    }

    private void getAheadOfPeople(List<NextRoomHelp> mNextRoomHelpList) {
        showProgressBar(true);


        int size = mNextRoomHelpList.size();

        int currantRoomCount = 0;
        boolean isBreak = false;


        List<NextRoomHelp> tempList = new ArrayList<>();


        for (int i = 0; i < size; i++) {

            if (mRoomNumber.equalsIgnoreCase(mNextRoomHelpList.get(i).getRoom_key())) {
                currantRoomCount = mNextRoomHelpList.get(i).getHelp_count_number();
                isBreak = true;
                break;
            }


        }

        if (isBreak) {

            if (status.equalsIgnoreCase(Help.HELP_PRESS))
                helpLayout.setVisibility(View.VISIBLE);
            else helpLayout.setVisibility(View.GONE);


            for (int j = 0; j < size; j++) {

                if (mNextRoomHelpList.get(j).getHelp_status().equalsIgnoreCase(Help.HELP_PRESS)) {
                    if (currantRoomCount > mNextRoomHelpList.get(j).getHelp_count_number()) {
                        tempList.add(mNextRoomHelpList.get(j));
                    }
                }

                if (j == (size - 1)) {

                    if (tempList.size() == 0) {
                        tvCurrantStatus.setText("You are next in line");
                        tvCurrantRoom.setVisibility(View.GONE);
                    } else {
                        tvCurrantRoom.setVisibility(View.VISIBLE);
                        tvCurrantStatus.setText("People ahead of you");
                        tvCurrantRoom.setText("" + tempList.size());
                    }

                }
            }


        }


        showProgressBar(false);
    }


    public void help(View v) {

        getRoom();

        if (mRoomNumber.equalsIgnoreCase("")) {

            commonDialog = new CommonDialog(CustomListActivity.this, getString(R.string.alert), getString(R.string.you_does_not_occupied_any_room), getString(R.string.yes), getString(R.string.str_no), new CommonDialog.OnButtonClickListener() {
                @Override
                public void onOkClick(View view) {

                    if (view.getId() == R.id.tvYes) {
                        PinDialog cdd = new PinDialog(CustomListActivity.this);
                        cdd.show();

                        commonDialog.dismiss();

                    } else {
                        commonDialog.dismiss();
                    }
                }
            });

            commonDialog.show();
        } else {
            if (help == null || !help.getRoom_key().equalsIgnoreCase(mRoomNumber)) {
                help = new Help();
                helpKey = dbHelp.push().getKey();
            }


            help.setUuid(mUUID);
            help.setRoom_key(mRoomNumber);


            String curr_text = btnHelp.getText().toString();


            if (curr_text.equals(getString(R.string.help))) {

                if (!status.equalsIgnoreCase(Help.DONE) || status.equalsIgnoreCase(Help.HELP_CANCEL)) {

                    //Press Help

                    status = Help.HELP_PRESS;
                    help.setHelp_press_time(System.currentTimeMillis());
                    help.setReady_press_time(0l);
                    help.setReady_cancel_time(0l);
                    help.setDone_press_time(0l);
                    help.setHelp_cancel_time(0l);


                    tvStatusOfWorker.setText(getString(R.string.help_is_on_the_way));
                    btnHelp.setText(R.string.cancel_help);
                    btnHelp.setTextColor(Color.YELLOW);

                    addHelpInFireBase(help, true);

                } else {

                    getToast(getString(R.string.must_cancel_other_request), Toast.LENGTH_SHORT).show();
                }
            } else {

                if (status.equalsIgnoreCase(Help.HELP_PRESS) || status.equalsIgnoreCase(Help.READY_CANCEL)) {

                    status = Help.HELP_CANCEL;
                    help.setHelp_cancel_time(System.currentTimeMillis());

                    helpLayout.setVisibility(View.GONE);


                    btnHelp.setText(R.string.help);
                    btnHelp.setTextColor(Color.WHITE);
                    addHelpInFireBase(help, true);
                } else {

                    getToast(getString(R.string.must_cancel_other_request), Toast.LENGTH_SHORT).show();
                }

            }

        }
    }

    private Toast getToast(String string, int lengthShort) {
        return Toast.makeText(this, string, lengthShort);
    }


    public void ready(View v) {


        String curr_text = btnReady.getText().toString();
        if (curr_text.equals(getString(R.string.ready))) {

            if (status.equalsIgnoreCase(Help.HELP_PRESS) || status.equalsIgnoreCase(Help.READY_CANCEL)) {

                btnReady.setText(R.string.cancel_ready);
                btnReady.setTextColor(Color.YELLOW);
                help.setReady_press_time(System.currentTimeMillis());
                status = Help.READY_PRESS;

                help.setReady_cancel_time(0l);
                tvStatusOfWorker.setText(getString(R.string.worker_on_the_way));

                addHelpInFireBase(help, false);
            } else {

                //if()

                getToast("Must press help request first.", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (status.equalsIgnoreCase(Help.READY_PRESS)) {

                btnReady.setText(R.string.ready);
                btnReady.setTextColor(Color.WHITE);
                status = Help.READY_CANCEL;
                help.setReady_cancel_time(System.currentTimeMillis());


                addHelpInFireBase(help, false);
            }


        }


    }

    public void done(final View v) {

        if (status.equalsIgnoreCase(Help.READY_PRESS)) {
            if (btnDone.getText().toString().equals(getString(R.string.done))) {

                tvStatusOfWorker.setText(getString(R.string.room_ready));

                ratingDialog = new RatingDialog(CustomListActivity.this, new RatingDialog.OnButtonClickListener() {
                    @Override
                    public void onOkClick(View view) {
                        if (view.getId() == R.id.llImprovement) {
                            ratingStatus = getString(R.string.could_use_imrove).toLowerCase();
                            openRatingDialog("Thank you!! for share your experience. Can you share with us, that you want to improved ? ");
                        } else if (view.getId() == R.id.llExcellent) {
                            ratingStatus = getString(R.string.excellent).toUpperCase();
                            openRatingDialog("Thank you!! for share your experience. You want to provide us some feedback ? ");
                        } else {
                            ratingStatus = getString(R.string.satisfactory).toUpperCase();
                            openRatingDialog("Thank you!! for share your experience. You want to provide us some feedback ? ");
                        }

                        ratingDialog.dismiss();
                    }
                });
                ratingDialog.setCancelable(false);
                ratingDialog.show();

                btnDone.setText(R.string.done_mess);
                btnDone.setTextColor(Color.YELLOW);

                long millis = help.getHelp_press_time() - help.getReady_press_time();

                int hours = (int) (millis / (1000 * 60 * 60));
                int mins = (int) (millis % (1000 * 60 * 60));
                //  int second = (int) millis

                help.setHelp_to_ready_time(hours + " : " + mins);

                millis = help.getReady_press_time() - help.getDone_press_time();

                hours = (int) (millis / (1000 * 60 * 60));
                mins = (int) (millis % (1000 * 60 * 60));


                help.setReady_to_done_time(hours + ":" + mins);

                status = Help.DONE;
                help.setDone_press_time(System.currentTimeMillis());
                addHelpInFireBase(help, true);

                helpLayout.setVisibility(View.GONE);
                // helpLayout.setVisibility(View.GONE);


            }
        } else {
            getToast("Must press ready request first.", Toast.LENGTH_SHORT).show();
        }
    }


    private void getRoom() {

        showProgressBar(true);
        rootRef.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(AppUtils.ROOM_TABLE)) {

                    mUUID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    mRoomNumber = PreferenceUtils.getInstance(CustomListActivity.this).get(AppUtils.ROOM_NUMBER);
                } else {

                    if (mRoomNumber != null && !mRoomNumber.equalsIgnoreCase("")) {
                        mRoomNumber = "";
                        mUUID = "";
                        recreate();
                    }

                }
                showProgressBar(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showProgressBar(false);
            }
        });

    }

    private void callHelp() {

        showProgressBar(true);

        rootRef.getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(AppUtils.NEXT_ROOM_HELP)) {

                    dbNextRoomHelp.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.hasChild(mDate)) {

                                dbNextRoomHelp.child(mDate).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                        if (dataSnapshot.exists()) {


                                            for (DataSnapshot data : dataSnapshot.getChildren()) {

                                                NextRoomHelp nextRoomHelp = data.getValue(NextRoomHelp.class);


                                                if (maxVal == 0) {
                                                    maxVal = nextRoomHelp.getHelp_count_number();

                                                } else {

                                                    if (maxVal < nextRoomHelp.getHelp_count_number()) {
                                                        maxVal = nextRoomHelp.getHelp_count_number();

                                                    }

                                                }

                                            }

                                            mNextRoomHelpCount = maxVal;
                                            showProgressBar(false);
                                            setNextHelpModel();
                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        showProgressBar(false);
                                    }
                                });

                            } else {
                                showProgressBar(false);
                                setNextHelpModel();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                } else {
                    showProgressBar(false);
                    setNextHelpModel();
                }
                showProgressBar(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showProgressBar(false);
            }
        });


    }

    private void setNextHelpModel() {
        showProgressBar(true);
        NextRoomHelp nextRoomHelp = new NextRoomHelp();
        nextRoomHelp.setDate(mDate);
        nextRoomHelp.setRoom_key(mRoomNumber);
        nextRoomHelp.setHelp_count_number(mNextRoomHelpCount + 1);
        nextRoomHelp.setHelp_status(status);
        nextRoomHelp.setUuid(mUUID);
        addNextHelpInFireBase(nextRoomHelp);
    }


    private void addHelpInFireBase(final Help help, final boolean isAhead) {
        showProgressBar(true);
        try {


            help.setStatus(status);
            dbHelp.child(helpKey).setValue(help).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    if (isAhead) {
                        callHelp();

                    }
                    //  Toast.makeText(CustomListActivity.this, status, Toast.LENGTH_SHORT).show();

                }

            });
            showProgressBar(false);
        } catch (Exception e) {
            showProgressBar(false);
            e.printStackTrace();
        }
    }


    private void addFeedbackInFireBase(Feedback feedback) {
        try {

            String key = dbFeedback.push().getKey();

            dbFeedback.child(key).setValue(feedback).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    getToast("Thank for your feedback", Toast.LENGTH_SHORT).show();


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addNextHelpInFireBase(NextRoomHelp nextRoomHelp) {

        dbNextRoomHelp.child(mDate).child(mRoomNumber).setValue(nextRoomHelp).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                setNextRoomHelpValues();

            }
        });
        showProgressBar(false);
    }


    public void getDataFromServer() {
        showProgressBar(true);

        dbRoomTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRoomsFromServerList.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot datas : dataSnapshot.getChildren()) {
                        Room devicesEvent = datas.getValue(Room.class);
                        devicesEvent.setRoom_key(datas.getKey());
                        mRoomsFromServerList.add(devicesEvent);
                    }
                }
                checkData();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                showProgressBar(false);

            }
        });
    }

    private void checkData() {

        getRoom();
        boolean isBreak = false;
        if (mRoomNumber != null && !mRoomNumber.equalsIgnoreCase("") && mUUID != null && !mUUID.equalsIgnoreCase("")) {
            int size = mRoomsFromServerList.size();
            for (int i = 0; i < size; i++) {

                ///check my currant room number has on server room
                if (!mRoomsFromServerList.get(i).getUuid().equalsIgnoreCase(mUUID)) {
                    if (mRoomNumber.equalsIgnoreCase(mRoomsFromServerList.get(i).getRoom_key())) {
                        PreferenceUtils.getInstance(this).removeAll();
                        mRoomNumber = "";
                        mUUID = "";
                        isBreak = true;
                    }

                }
            }

        }
        showProgressBar(false);

        if (isBreak) {
            recreate();
        }
    }

    private void setListener() {
        final SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String music = sharedPref.getString("current_music", "hey");
        System.out.println(music);

        txtSpin.setText(getString(R.string.choose_music));
        spinner2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(CustomListActivity.this);
            }
        });


        if (PreferenceUtils.getInstance(this).get(AppUtils.ROOM_NUMBER).equalsIgnoreCase("")) {
            PinDialog cdd = new PinDialog(CustomListActivity.this);
            cdd.setCancelable(false);
            cdd.show();
        }

        mPinView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PinDialog cdd = new PinDialog(CustomListActivity.this);
                cdd.show();

            }
        });
    }


    /*public void setDefult() {

            if (status != null && !status.equalsIgnoreCase("")) {
                status = "";
                btnHelp.setText(R.string.help);
                btnHelp.setTextColor(Color.WHITE);
                btnReady.setText(R.string.ready);
                btnReady.setTextColor(Color.WHITE);
                helpLayout.setVisibility(View.GONE);

            }
        }
    }
*/
    private void openRatingDialog(String s) {

        commonDialog = new CommonDialog(CustomListActivity.this, getString(R.string.your_experience), s, getString(R.string.submit), getString(R.string.cancel), true, new CommonDialog.OnButtonClickListenerWithEdit() {


            @Override
            public void onOkClick(View view, String value) {


                Feedback feedback = new Feedback();
                feedback.setRoom_key(mRoomNumber);
                feedback.setFeedback(value);
                feedback.setCurrantDate(System.currentTimeMillis());

                feedback.setExperience_status(ratingStatus);
                addFeedbackInFireBase(feedback);


                commonDialog.dismiss();

                recreate();


            }
        });

        commonDialog.show();
    }

    public void checkbox(View v) {
        Log.v("hey", "heyoooooo");
        CheckBox checkBox;
        TextView text;
        ViewGroup row = (ViewGroup) v.getParent();
        View view = row.getChildAt(1);

        View textView = row.getChildAt(0);
        checkBox = (CheckBox) view;
        text = (TextView) textView;
        if (checkBox.isChecked()) {
            text.setTextColor(getResources().getColor(R.color.disabled));
            checkBox.setChecked(true);
        } else {
            text.setTextColor(getResources().getColor(R.color.auto));
            checkBox.setChecked(false);

        }

    }

    public void textCheckBox(View v) {
        try {
            CheckBox checkBox;
            TextView text;
            ViewGroup row = (ViewGroup) v;
            View view = row.getChildAt(1);
            View textView = row.getChildAt(0);
            checkBox = (CheckBox) view;
            text = (TextView) textView;
            if (checkBox.isChecked()) {
                text.setTextColor(getResources().getColor(R.color.auto));
                checkBox.setChecked(false);
            } else {
                text.setTextColor(getResources().getColor(R.color.disabled));
                checkBox.setChecked(true);
            }
        } catch (Exception e) {

        }
    }

    public void lang(View v) {


        if (mediaPlayer.isPlaying()) {
            clearMediaPlayer();
            rl_music.setVisibility(View.GONE);
        }
        //  AppUtils.setLanguage(CustomListActivity.this, ivVolumeDown, ivVolumeUp);

        Locale current = getResources().getConfiguration().locale;

        //Locale current = Resources.getSystem().getConfiguration().locale;
        boolean locale = current.toString().contains("en");
        if (locale) {
            language = "iw";
            AppUtils.setMirroredEnable(true, ivVolumeDown, ivVolumeUp);
        } else {
            language = "en";
        }

        LocaleHelper.setLocale(this, language);

        recreate();
    }


    public void donate(View view) {


        commonDialog = new CommonDialog(CustomListActivity.this, getString(R.string.alert), "You can donate through the system outside", getString(R.string.okay), "", new CommonDialog.OnButtonClickListener() {
            @Override
            public void onOkClick(View view) {


                commonDialog.dismiss();

            }
        });

        commonDialog.show();
    }


    private void setVolumeControl() {


        final AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        try {


            volumeSeekbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));


            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        ivVolumeUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//To increase media player volume
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);


                volumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            }
        });


        ivVolumeDown.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//To decrease media player volume
                audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);


                volumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            }
        });
    }

    public void showDialog(Activity activity) {

        final String[] company = getResources().getStringArray(R.array.song_arrays);

        final Dialog dialog = new Dialog(activity);
        // dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.song_listview_dialog);

        ListView listView = (ListView) dialog.findViewById(R.id.listview);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.simple_spinner_dropdown_item, R.id.chkTextView, company);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = company[position];


                if (position != 0) {
                    play(position);
                } else {
                    if (mediaPlayer.isPlaying()) {
                        clearMediaPlayer();
                        rl_music.setVisibility(View.GONE);
                    }
                }
                System.out.println(item);

                SongItem = "" + company[position];
                txtSpin.setText(SongItem);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void play(int song) {


        rl_music.setVisibility(View.VISIBLE);

        songName = "";
     /*   switch (song) {
            case "Song Number One":
                songName = "music1";
                break;
            case "Song Number Two":
                songName = "music2";
                break;
            case "Song Number Three":
                songName = "music3";
                break;
            case "Song Number Four":
                songName = "music4";
                break;
            case "Song Number Five":
                songName = "music5";
                break;
            case "Song Number Six":
                songName = "music6";
                break;
            default:
                songName = "music2";
                break;
            case "No Music":
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    rl_music.setVisibility(View.GONE);
                }

                break;
        }*/

        switch (song) {
            case 1:
                songName = "music1";
                break;
            case 2:
                songName = "music2";
                break;
            case 3:
                songName = "music3";
                break;
            case 4:
                songName = "music4";
                break;
            case 5:
                songName = "music5";
                break;
            case 6:
                songName = "music6";
                break;
            default:
                songName = "music2";
                break;
        }

        String filename = "android.resource://" + this.getPackageName() + "/raw/" + songName;
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("current_music", filename);
        editor.apply();


        try {

            clearMediaPlayer();
            mediaPlayer.setDataSource(this, Uri.parse(filename));
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
        }


    }

    private void clearMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = new MediaPlayer();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    private void populateUsersList() {
        // Construct the data source
        ArrayList<User> arrayOfUsers = User.getUsers(this);
        // Create the adapter to convert the array to views
        CustomUsersAdapter adapter = new CustomUsersAdapter(this, arrayOfUsers);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.lvUsers);
        listView.setAdapter(adapter);
    }


    public void showProgressBar(boolean isProgress) {
        if (isProgress) {
            startAnim();
        } else {
            stopAnim();
        }
    }


    void startAnim() {
        avi.setVisibility(View.VISIBLE);
        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        avi.setVisibility(View.GONE);
        avi.hide();
        // or avi.smoothToHide();
    }
}