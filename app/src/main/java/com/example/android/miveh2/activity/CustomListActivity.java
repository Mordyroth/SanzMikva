package com.example.android.miveh2.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.android.miveh2.R;
import com.example.android.miveh2.connection.ApiClient;
import com.example.android.miveh2.connection.ApiInterface;
import com.example.android.miveh2.customeclass.MyWorker;
import com.example.android.miveh2.dialog.CommonDialog;
import com.example.android.miveh2.dialog.PinDialog;
import com.example.android.miveh2.dialog.RatingDialog;
import com.example.android.miveh2.model.Feedback;
import com.example.android.miveh2.model.Help;
import com.example.android.miveh2.model.NextRoomHelp;
import com.example.android.miveh2.model.Room;
import com.example.android.miveh2.model.SunsetModel;
import com.example.android.miveh2.model.User;
import com.example.android.miveh2.utils.AppUtils;
import com.example.android.miveh2.utils.LocaleHelper;
import com.example.android.miveh2.utils.PreferenceUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomListActivity extends BaseActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

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

    private Help help;
    private Button btnReady;
    private Button btnDone;

    private String status = "";

    private DatabaseReference dbFeedback;
    private String ratingStatus = "";
    private RatingDialog ratingDialog;
    private TextView tvCurrantRoom;
    private TextView tvStatusOfWorker;

    private SeekBar volumeSeekbar;
    private RelativeLayout rl_music;

    private ArrayList<Room> mRoomsFromServerList = new ArrayList<>();
    private DatabaseReference dbNextRoomHelp;
    private FirebaseDatabase rootRef;
    private String mDate = "";
    private TextView tvCurrantStatus;
    private static int mNextRoomHelpCount = 0;
    static MediaPlayer mediaPlayer;
    private int maxVal = 0;

    private AVLoadingIndicatorView avi;
    private String songName;
    private String language;
    private DatabaseReference dbHelpHistory, dbHelp, dbRoomTable;


    private double longitude = -74.211052;
    private double latitude = 42.091315;


    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private TextView tvSunset;
    private TextView tvSunset1;
    private boolean isDisplay = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_customer_help);
        populateUsersList();
        clearMediaPlayer();

        mDate = AppUtils.getDate();

        // mediaPlayer = new MediaPlayer();
        clearMediaPlayer();
        bindViews();
        initialization();
        setVolumeControl();

        PeriodicWorkRequest periodicWorkRequest =
                new PeriodicWorkRequest.Builder(MyWorker.class, 50, TimeUnit.MINUTES)
                        .build();

        WorkManager.getInstance().
                enqueue(periodicWorkRequest);


        String rationale = "Please provide location permission so that you can get sunset time";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Info")
                .setSettingsDialogTitle("Warning");

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        Permissions.check(this/*context*/, permissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                // do your task.
                mGoogleApiClient = new GoogleApiClient.Builder(CustomListActivity.this)
                        // The next two lines tell the new client that “this” current class will handle connection stuff
                        .addConnectionCallbacks(CustomListActivity.this)
                        .addOnConnectionFailedListener(CustomListActivity.this)
                        //fourth line adds the LocationServices API endpoint from GooglePlayServices
                        .addApi(LocationServices.API)
                        .build();

                // Create the LocationRequest object
                mLocationRequest = LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                        .setFastestInterval(1 * 1000); // 1 second, in milliseconds
            }
        });
        // do your task.
        mGoogleApiClient = new GoogleApiClient.Builder(CustomListActivity.this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(CustomListActivity.this)
                .addOnConnectionFailedListener(CustomListActivity.this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        if (PreferenceUtils.getInstance(this).getBollean(AppUtils.ROOM_RESET)) {

            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);

            String toDay = "";

            switch (day) {
                case Calendar.SUNDAY:
                    // Current day is Sunday
                    toDay = "Sunday";
                    break;
                case Calendar.MONDAY:
                    toDay = "Monday";
                    break;
                case Calendar.TUESDAY:
                    toDay = "Tuesday";
                    // etc.
                    break;
                case Calendar.WEDNESDAY:
                    toDay = "Wednesday";
                    // etc.
                    break;
                case Calendar.THURSDAY:
                    toDay = "Thursday";
                    // etc.
                    break;
                case Calendar.FRIDAY:
                    toDay = "Friday";
                    // etc.
                    break;

                case Calendar.SATURDAY:
                    toDay = "Saturday";
                    // etc.
                    break;
            }

            String message = getString(R.string.str_did_you_do) + toDay + getString(R.string.str_before);


            final String finalToDay = toDay;
            CommonDialog commonDialog = new CommonDialog(CustomListActivity.this, getString(R.string.alert), message, getString(R.string.yes), getString(R.string.str_no), new CommonDialog.OnButtonClickListener() {
                @Override
                public void onOkClick(View view, CommonDialog commonDialog) {

                    if (view.getId() != R.id.tvNo) {

                        String msg = "In order to do a Tevilah tonight you must have done a hefsak last" + finalToDay + "before shkia or as otherwise instructed by a Rav";

                        CommonDialog dialog = new CommonDialog(CustomListActivity.this, getString(R.string.alert), msg,
                                "Continue", "", new CommonDialog.OnButtonClickListener() {
                            @Override
                            public void onOkClick(View view, CommonDialog commonDialog) {

                                commonDialog.dismiss();

                            }
                        });
                        dialog.show();

                        commonDialog.dismiss();
                        //In order to do a Tevilah tonight you must have done a hefsak last Monday before shkia or as otherwise instructed by a Rav

                    } else {
                        commonDialog.dismiss();
                    }
                }
            });
            commonDialog.show();
            PreferenceUtils.getInstance(this).save(AppUtils.ROOM_RESET, false);


        }
        getSunSineTime(latitude, longitude);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }


    }

    /**
     * If connected get lat and long
     */
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {


            latitude = location.getLatitude();
            longitude = location.getLongitude();

            //  Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();

            getSunSineTime(latitude, longitude);
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        // Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }


    private void getSunSineTime(double latitude, double longitude) {


        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<SunsetModel> call = (Call<SunsetModel>) apiService.getSunsetResponse(latitude, longitude, "today");
        call.enqueue(new Callback<SunsetModel>() {
            @Override
            public void onResponse(Call<SunsetModel> call, Response<SunsetModel> response) {
                //     Toast.makeText(locationTrack, "" + response, Toast.LENGTH_SHORT).show();

                SunsetModel sunsetModel = response.body();

                try {

                    tvSunset.setText(" שקיעה " + AppUtils.getLocalDate(sunsetModel.getResults().getSunset()));

                } catch (Exception e) {
                    e.printStackTrace();

                }


                try {

                    if (sunsetModel.getResults() != null) {
                        String myTime = AppUtils.getLocalDate(sunsetModel.getResults().getSunset());
                        SimpleDateFormat df = new SimpleDateFormat("h:mm");
                        Date d = null;
                        d = df.parse(myTime);

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(d);
                        cal.add(Calendar.MINUTE, 20);
                        String newTime = df.format(cal.getTime());


                        try {

                            tvSunset1.setText(" זמן טבילה " + newTime);

                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }

                } catch (ParseException e) {

                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<SunsetModel> call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(com.example.android.miveh2.activity.CustomListActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


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
        tvSunset = findViewById(R.id.tvSunset);
        tvSunset1 = findViewById(R.id.tvSunset1);

    }

    private void initialization() {

        rootRef = FirebaseDatabase.getInstance();
        dbHelp = rootRef.getReference(AppUtils.HELP_TABLE);
        dbFeedback = rootRef.getReference(AppUtils.FEEDBACK_TABLE);
        dbRoomTable = rootRef.getReference(AppUtils.ROOM_TABLE);
        dbNextRoomHelp = rootRef.getReference(AppUtils.NEXT_ROOM_HELP);
        dbHelpHistory = rootRef.getReference(AppUtils.HELP_HISTORY_TABLE);


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

                            try {
                                getAheadOfPeople(mNextRoomHelpList);
                            } catch (Exception ex) {
                                // Here we are logging the exception to see why it happened.
                                Log.e("my app", ex.toString());
                            }


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
                        tvCurrantStatus.setText(getString(R.string.str_some_one_will_be_right_with_you));
                        tvCurrantRoom.setVisibility(View.GONE);
                    } else {
                        tvCurrantRoom.setVisibility(View.VISIBLE);

                        tvCurrantRoom.setText("" + tempList.size());

                        if (tempList.size() > 1) {
                            tvCurrantStatus.setText(getString(R.string.people_ahead));
                        } else {
                            tvCurrantStatus.setText(getString(R.string.people_is_ahed));
                        }
                    }

                }
            }


        }


        showProgressBar(false);
    }


    public void help(View v) {

        getRoom();

        if (mRoomNumber.equalsIgnoreCase("")) {


            PinDialog cdd = new PinDialog(CustomListActivity.this);
            cdd.show();
        } else {
            createHelpNewObject();


            String curr_text = btnHelp.getText().toString();


            if (curr_text.equals(getString(R.string.help))) {

                if (status.equalsIgnoreCase(Help.READY_PRESS)) {

                    getToast("You must press the NEED MORE TIME button before requesting help", Toast.LENGTH_SHORT).show();


                } else {

                    //Press Help

                    status = Help.HELP_PRESS;
                    help.setHelp_press_time(System.currentTimeMillis());
                    help.setReady_press_time(0l);
                    help.setReady_cancel_time(0l);
                    help.setDone_press_time(0l);
                    help.setHelp_cancel_time(0l);


                    btnHelp.setText(R.string.cancel_help);
                    btnHelp.setTextColor(Color.YELLOW);

                    addHelpInFireBase(help, true);


                }
            } else {

                if (status.equalsIgnoreCase(Help.HELP_PRESS) || status.equalsIgnoreCase(Help.READY_CANCEL)) {

                    status = Help.HELP_CANCEL;
                    help.setHelp_cancel_time(System.currentTimeMillis());

                    helpLayout.setVisibility(View.GONE);


                    btnHelp.setText(R.string.help);
                    btnHelp.setTextColor(Color.WHITE);
                    addHelpInFireBase(help, true);


                }/* else {

                    getToast(getString(R.string.must_cancel_other_request), Toast.LENGTH_SHORT).show();
                }*/

            }

        }
    }

    private Toast getToast(String string, int lengthShort) {
        return Toast.makeText(this, string, lengthShort);
    }


    public void ready(View v) {
        createHelpNewObject();


        String curr_text = btnReady.getText().toString();
        if (curr_text.equals(getString(R.string.ready))) {

            if (status.equalsIgnoreCase(Help.HELP_PRESS)) {


                getToast(getString(R.string.you_must_press_cancel_help), Toast.LENGTH_SHORT).show();


            } else {

                btnReady.setText(R.string.cancel_ready);
                btnReady.setTextColor(Color.YELLOW);
                help.setReady_press_time(System.currentTimeMillis());
                status = Help.READY_PRESS;

                help.setReady_cancel_time(0l);
                //  tvStatusOfWorker.setText(getString(R.string.worker_on_the_way));
                tvStatusOfWorker.setText("Someone is coming");

                addHelpInFireBase(help, false);

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

    private void createHelpNewObject() {
        if (help == null) {
            help = new Help();

        } else {
            if (!help.getRoom_key().equalsIgnoreCase(mRoomNumber)) {
                help = new Help();
            }
        }

        help.setUuid(mUUID);
        help.setRoom_key(mRoomNumber);
    }

    public void done(final View v) {

        createHelpNewObject();


        if (btnDone.getText().toString().equals(getString(R.string.done))) {

            tvStatusOfWorker.setText(getString(R.string.room_ready));

            ratingDialog = new RatingDialog(CustomListActivity.this, new RatingDialog.OnButtonClickListener() {
                @Override
                public void onOkClick(View view) {
                    if (view.getId() == R.id.llImprovement) {
                        ratingStatus = getString(R.string.could_use_imrove).toLowerCase();
                        openRatingDialog();
                    } else if (view.getId() == R.id.llExcellent) {
                        ratingStatus = getString(R.string.excellent).toUpperCase();
                        openRatingDialog();
                    } else {
                        ratingStatus = getString(R.string.satisfactory).toUpperCase();

                    }


                }
            });
            ratingDialog.setCancelable(true);
            ratingDialog.show();


            // helpLayout.setVisibility(View.GONE);


        }

       /* if (status.equalsIgnoreCase(Help.READY_PRESS) || status.equalsIgnoreCase(Help.HELP_PRESS)) {
            getToast("Must press ready request first.", Toast.LENGTH_SHORT).show();
        } else {



        }*/
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
        help.setHelp_count_number(nextRoomHelp.getHelp_count_number());
        addHelpInFireBase(help, false);
    }


    private void addHelpInFireBase(final Help help, final boolean isAhead) {
        showProgressBar(true);
        try {


            help.setStatus(status);
            dbHelp.child(mRoomNumber).setValue(help).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    if (isAhead)
                        callHelp();
                    if (status.equalsIgnoreCase(Help.DONE)) {
                        String helpKey = dbHelpHistory.push().getKey();

                        dbHelpHistory.child(helpKey).setValue(help).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //  dbHelp.getRef().child(mRoomNumber).removeValue();
                            }
                        });
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
    private void openRatingDialog() {

        CommonDialog commonDialog = new CommonDialog(CustomListActivity.this, getString(R.string.your_experience), getString(R.string.str_thank_you_for_comment), getString(R.string.submit), getString(R.string.str_skip), true, new CommonDialog.OnButtonClickListenerWithEdit() {


            @Override
            public void onOkClick(View view, String value, CommonDialog commonDialog) {

                if (view.getId() == R.id.tvYes) {
                    Feedback feedback = new Feedback();
                    feedback.setRoom_key(mRoomNumber);
                    feedback.setFeedback(value);
                    feedback.setCurrantDate(System.currentTimeMillis());

                    feedback.setExperience_status(ratingStatus);
                    addFeedbackInFireBase(feedback);


                    commonDialog.dismiss();


                    CommonDialog dialog = new CommonDialog(CustomListActivity.this, getString(R.string.str_thank_you), getString(R.string.str_thank_you_leave), getString(R.string.okay), "", new CommonDialog.OnButtonClickListener() {
                        @Override
                        public void onOkClick(View view, CommonDialog dialog) {


                            dialog.dismiss();
                            recreate();


                        }
                    });

                    dialog.show();
                } else {
                    Feedback feedback = new Feedback();
                    feedback.setRoom_key(mRoomNumber);
                    feedback.setFeedback(value);
                    feedback.setCurrantDate(System.currentTimeMillis());

                    feedback.setExperience_status(ratingStatus);
                    addFeedbackInFireBase(feedback);
                    commonDialog.dismiss();
                    recreate();


                }

                if (ratingDialog.isShowing()) {
                    ratingDialog.dismiss();
                }

                btnDone.setText(R.string.done_mess);
                btnDone.setTextColor(Color.YELLOW);


                status = Help.DONE;
                help.setDone_press_time(System.currentTimeMillis());
                addHelpInFireBase(help, true);

                helpLayout.setVisibility(View.GONE);


                //
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
            TextView text, text1;
            ViewGroup row = (ViewGroup) v;
            View view = row.getChildAt(1);
            View textView = row.getChildAt(0);
            View textView1 = row.getChildAt(2);
            checkBox = (CheckBox) view;
            text = (TextView) textView;
            text1 = (TextView) textView1;


            if (checkBox.isChecked()) {
                text.setTextColor(getResources().getColor(R.color.auto));
                text1.setTextColor(getResources().getColor(R.color.auto));
                checkBox.setChecked(false);
            } else {
                text.setTextColor(getResources().getColor(R.color.disabled));
                text1.setTextColor(getResources().getColor(R.color.disabled));
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


        CommonDialog commonDialog = new CommonDialog(CustomListActivity.this, getString(R.string.alert), getString(R.string.str_donate), getString(R.string.okay), "", new CommonDialog.OnButtonClickListener() {
            @Override
            public void onOkClick(View view, CommonDialog commonDialog) {


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

            float percent = 7.5f;
            int seventyVolume = (int) (percent);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, seventyVolume, AudioManager.FLAG_SHOW_UI);


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

    public static void clearMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    private void populateUsersList() {

        LinearLayout llList = findViewById(R.id.llList);
        llList.removeAllViews();
        final ArrayList<User> arrayOfUsers = User.getUsersOther(this);
        for (int i = 0; i < arrayOfUsers.size(); i++) {

            View view = LayoutInflater.from(this).inflate(R.layout.item_user_without_checkbox, llList, false);
            final TextView tv = view.findViewById(R.id.tvName);
            TextView tvNumber = view.findViewById(R.id.tvNumber);
            tv.setTypeface(null, Typeface.ITALIC);
            ViewGroup.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            view.setLayoutParams(layoutparams);
            tvNumber.setText(arrayOfUsers.get(i).getId() + ".");

            tv.setText(" " + arrayOfUsers.get(i).getName());

            llList.addView(view);
        }


        // Construct the data source
        final ArrayList<User> arrayOfUsers1 = User.getUsers(this);
        LinearLayout llList1 = findViewById(R.id.llList1);
        llList1.removeAllViews();


        for (int i = 0; i < arrayOfUsers1.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_user, llList1, false);
            TextView tv = view.findViewById(R.id.tvName);
            CheckBox checkBox = view.findViewById(R.id.checkbox);
            TextView tvNumber = view.findViewById(R.id.tvNumber);
            checkBox.setChecked(false);

            ViewGroup.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );

            ((LinearLayout.LayoutParams) layoutparams).setMargins((int) this.getResources().getDimension(R.dimen._1sdp), 0,
                    (int) this.getResources().getDimension(R.dimen._1sdp), (int) this.getResources().getDimension(R.dimen._8sdp));
            view.setLayoutParams(layoutparams);

            tv.setText(" " + arrayOfUsers1.get(i).getName());
            tvNumber.setText(arrayOfUsers1.get(i).getId() + ".");


            llList1.addView(view);

            /*final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(CustomListActivity.this, "Click", Toast.LENGTH_SHORT).show();

                    if (arrayOfUsers1.get(finalI).isChecked()) {
                        tvNumber.setTextColor(getResources().getColor(R.color.auto));
                        tv.setTextColor(getResources().getColor(R.color.auto));
                        checkBox.setChecked(false);
                        arrayOfUsers1.get(finalI).setChecked(false);
                    } else {
                        tvNumber.setTextColor(getResources().getColor(R.color.disabled));
                        tv.setTextColor(getResources().getColor(R.color.disabled));
                        checkBox.setChecked(true);
                    }


                }
            });*/
        }


        ArrayList<User> arrayOfUsers2 = User.getUsersOther1(this);
        LinearLayout llList2 = findViewById(R.id.llList2);
        llList2.removeAllViews();

        for (int i = 0; i < arrayOfUsers2.size(); i++) {

            View view = LayoutInflater.from(this).inflate(R.layout.item_user_without_checkbox, llList, false);
            TextView tv = view.findViewById(R.id.tvName);
            TextView tvNumber = view.findViewById(R.id.tvNumber);
            tv.setTypeface(null, Typeface.ITALIC);
            ViewGroup.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            view.setLayoutParams(layoutparams);
            tvNumber.setText(arrayOfUsers2.get(i).getId() + ".");
            tv.setText(" " + arrayOfUsers2.get(i).getName());


            llList2.addView(view);

        }


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