package com.example.android.miveh2.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.android.miveh2.model.User;
import com.example.android.miveh2.utils.AppUtils;
import com.example.android.miveh2.utils.PreferenceUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;

public class CustomListActivity extends BaseActivity {
    MediaPlayer mediaPlayer = new MediaPlayer();
    /*private Spinner spinner;*/
    RelativeLayout spinner2;
    TextView txtSpin;
    String SongItem;
    ImageView mPinView;
    private ImageView ivVolumeUp;
    private ImageView ivVolumeDown;
    private String mUUID;
    private String mRoomNumber;
    private Button btnHelp;
    private View helpLayout;
    private DatabaseReference dbHelp;
    private Help help;
    private Button btnReady;
    private Button btnDone;

    private String status = Help.HELP_PRESS;
    private CommonDialog commonDialog;
    private DatabaseReference dbFeedback;
    private String ratingStatus = "";
    private RatingDialog ratingDialog;
    private TextView tvCurrantRoom;
    private TextView tvStatusOfWorker;
    private Button btnDonate;
    private SeekBar volumeSeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_customer_help);
        populateUsersList();


        bindViews();
        setVolumeControl();
        initialization();

    }

    private void bindViews() {
        spinner2 = (RelativeLayout) findViewById(R.id.spinner2);
        txtSpin = (TextView) findViewById(R.id.txtSpin);
        mPinView = (ImageView) findViewById(R.id.mPinView);

        helpLayout = findViewById(R.id.arrival);
        btnHelp = (Button) findViewById(R.id.button);
        btnDone = (Button) findViewById(R.id.button3);

        btnReady = (Button) findViewById(R.id.button2);
        ivVolumeDown = findViewById(R.id.ivUolumeDown);
        ivVolumeUp = findViewById(R.id.ivUolumeUp);
        volumeSeekbar = (SeekBar) findViewById(R.id.seekbar);

        tvCurrantRoom = findViewById(R.id.number);
        tvStatusOfWorker = findViewById(R.id.ahead);
        btnDonate = findViewById(R.id.donate);

    }

    private void initialization() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbHelp = database.getReference(AppUtils.HELP_TABLE);
        dbFeedback = database.getReference(AppUtils.FEEDBACK_TABLE);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
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


    @SuppressLint("SetTextI18n")
    public void help(View v) {


        mUUID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        mRoomNumber = PreferenceUtils.getInstance(this).get(AppUtils.ROOM_NUMBER);
        tvCurrantRoom.setText("" + mRoomNumber);


        if (mRoomNumber.equalsIgnoreCase("")) {

            commonDialog = new CommonDialog(CustomListActivity.this, getString(R.string.alert), "You does not Occupied any room,so are you want to occupied any Room??", getString(R.string.yes), getString(R.string.str_no), new CommonDialog.OnButtonClickListener() {
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
        }

        if (help == null || !help.getRoom_key().equalsIgnoreCase(mRoomNumber)) {
            help = new Help();
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

                helpLayout.setVisibility(View.VISIBLE);
                tvStatusOfWorker.setText(getString(R.string.help_is_on_the_way));
                btnHelp.setText(R.string.cancel_help);
                btnHelp.setTextColor(Color.YELLOW);
                addHelpInFireBase(help);
            }
        } else {

            if (status.equalsIgnoreCase(Help.HELP_PRESS) || status.equalsIgnoreCase(Help.READY_CANCEL)) {

                status = Help.HELP_CANCEL;
                help.setHelp_cancel_time(System.currentTimeMillis());

                helpLayout.setVisibility(View.GONE);
                btnHelp.setText(R.string.help);
                btnHelp.setTextColor(Color.WHITE);
                addHelpInFireBase(help);
            }

        }


    }


    public void ready(View v) {


        String curr_text = btnReady.getText().toString();
        if (curr_text.equals(getString(R.string.ready))) {

            if (status.equalsIgnoreCase(Help.HELP_PRESS) || status.equalsIgnoreCase(Help.READY_CANCEL)) {

                helpLayout.setVisibility(View.VISIBLE);
                btnReady.setText(R.string.cancel_ready);
                btnReady.setTextColor(Color.YELLOW);
                help.setReady_press_time(System.currentTimeMillis());
                status = Help.READY_PRESS;

                help.setReady_cancel_time(0l);
                tvStatusOfWorker.setText(getString(R.string.worker_on_the_way));

                addHelpInFireBase(help);
            }
        } else {
            if (status.equalsIgnoreCase(Help.READY_PRESS)) {
                helpLayout.setVisibility(View.GONE);
                btnReady.setText(R.string.ready);
                btnReady.setTextColor(Color.WHITE);
                status = Help.READY_CANCEL;
                help.setReady_cancel_time(System.currentTimeMillis());


                addHelpInFireBase(help);
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
                addHelpInFireBase(help);

                // helpLayout.setVisibility(View.GONE);


            }
        }
    }

    private void openRatingDialog(String s) {
        commonDialog = new CommonDialog(CustomListActivity.this, getString(R.string.your_experience), s, getString(R.string.submit), getString(R.string.cancel), true, new CommonDialog.OnButtonClickListenerWithEdit() {


            @Override
            public void onOkClick(View view, String value) {


                Feedback feedback = new Feedback();
                feedback.setRoom_key(mRoomNumber);
                feedback.setFeedback(value);

                feedback.setExperience_status(ratingStatus);
                addFeedbackInFireBase(feedback);

                commonDialog.dismiss();


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        recreate();
                    }
                }, 1000);


            }
        });

        commonDialog.show();
    }

    private void addHelpInFireBase(final Help help) {
        try {
            help.setStatus(status);
            dbHelp.child(mRoomNumber).setValue(help).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //  Toast.makeText(CustomListActivity.this, status, Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addFeedbackInFireBase(Feedback feedback) {
        try {

            dbFeedback.child(mRoomNumber).setValue(feedback).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(CustomListActivity.this, "Thank for your feedback", Toast.LENGTH_SHORT).show();


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play(String song) {

        ivVolumeUp.setVisibility(View.VISIBLE);
        ivVolumeDown.setVisibility(View.VISIBLE);
        volumeSeekbar.setVisibility(View.VISIBLE);

        String songName;
        switch (song) {
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
        }

        String filename = "android.resource://" + this.getPackageName() + "/raw/" + songName;
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("current_music", filename);
        editor.commit();
        mediaPlayer = new MediaPlayer();
        if (mediaPlayer.isPlaying()) {
            //stop or pause your media player mediaPlayer.stop(); or mediaPlayer.pause();
            mediaPlayer.stop();
            Toast.makeText(this, "pause", Toast.LENGTH_LONG).show();
        }
        try {
            mediaPlayer.setDataSource(this, Uri.parse(filename));
        } catch (Exception e) {
        }
        try {
            mediaPlayer.prepare();
        } catch (Exception e) {
        }
        mediaPlayer.start();


    }

    public void music(View V) {
        //spinner.performClick();
        txtSpin.setText(SongItem);
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

        Locale current = getResources().getConfiguration().locale;
        boolean locale = current.toString().startsWith("en");
        if (locale) {
            //Toast.makeText(this, current.toString(), Toast.LENGTH_LONG).show();
            Locale myLocale = new Locale("iw");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            conf.setLocale(new Locale("iw"));
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(this, CustomListActivity.class);
            startActivity(refresh);
            finish();
        } else {
            Locale myLocale = new Locale("en");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            conf.setLocale(new Locale("en"));
            res.updateConfiguration(conf, dm);
            Intent refresh = new Intent(this, CustomListActivity.class);
            startActivity(refresh);
            finish();
        }

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

                Object item = parent.getItemAtPosition(position);
                if (position != 0) {
                    play(item.toString());
                } else {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        ivVolumeUp.setVisibility(View.VISIBLE);
                        ivVolumeDown.setVisibility(View.VISIBLE);
                    }
                }
                System.out.println(item.toString());

                SongItem = "" + company[position];
                txtSpin.setText(SongItem);

                dialog.dismiss();
            }
        });

        dialog.show();
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
}