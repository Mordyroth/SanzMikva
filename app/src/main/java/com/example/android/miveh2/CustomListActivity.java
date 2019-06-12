package com.example.android.miveh2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static android.support.constraint.Constraints.TAG;

public class CustomListActivity extends BaseActivity {
    MediaPlayer mediaPlayer = new MediaPlayer();
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // If the Android version is lower than Jellybean, use this call to hide
        // the status bar.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_custom_list);
        populateUsersList();

        spinner = findViewById(R.id.spinner1);
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String music = sharedPref.getString("current_music", "hey");
        System.out.println(music);
        // check if media player is playing
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.song_arrays, R.layout.music_spinner);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        Object item = parent.getItemAtPosition(pos);
                        if (pos != 0) {
                            play(item.toString());
                        } else {
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.stop();
                                mediaPlayer.release();
                            }
                        }
                        System.out.println(item.toString());     //prints the text in spinner item.
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
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


    public void help(View v) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hey!");
        ConstraintLayout helpLayout = findViewById(R.id.arrival);
        Button p1_button = (Button) findViewById(R.id.button);
        String curr_text = p1_button.getText().toString();
        if (curr_text.equals(getString(R.string.help))) {
            helpLayout.setVisibility(View.VISIBLE);
            p1_button.setText(R.string.cancel_help);
            p1_button.setTextSize(20);
            p1_button.setTextColor(Color.YELLOW);
            //Some url endpoint that you may have
            String myUrl = "http://54.218.240.133/mikva/input.php?id=" + getString(R.string.app_id) + "&status=help";
            //String to place our result in
            String result = null;
            //Instantiate new instance of our class
            HttpGetRequest getRequest = new HttpGetRequest();
            //Perform the doInBackground method, passing in our url
            try {
                result = getRequest.execute(myUrl).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ((TextView) findViewById(R.id.number)).setText(result);
        } else {
            helpLayout.setVisibility(View.GONE);
            p1_button.setText(R.string.help);
            p1_button.setTextSize(30);
            p1_button.setTextColor(Color.WHITE);
            //Some url endpoint that you may have
            String myUrl = "http://54.218.240.133/mikva/input.php?id=" + getString(R.string.app_id) + "&status=cancel-help";
            //String to place our result in
            String result = null;
            //Instantiate new instance of our class
            HttpGetRequest getRequest = new HttpGetRequest();
            //Perform the doInBackground method, passing in our url
            try {
                result = getRequest.execute(myUrl).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    public void done(View v) {
        Button p1_button = (Button) findViewById(R.id.button3);
        p1_button.setText(R.string.done_mess);
        p1_button.setTextSize(15);
        p1_button.setTextColor(Color.YELLOW);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Toast.makeText(getApplicationContext() , value, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void ready(View v) {
        ConstraintLayout helpLayout = findViewById(R.id.arrival);
        Button p1_button = (Button) findViewById(R.id.button2);
        String curr_text = p1_button.getText().toString();
        if (curr_text.equals(getString(R.string.ready))) {
            helpLayout.setVisibility(View.VISIBLE);
            p1_button.setText(R.string.cancel_ready);
            p1_button.setTextSize(20);
            p1_button.setTextColor(Color.YELLOW);
            //Some url endpoint that you may have
            String myUrl = "http://54.218.240.133/mikva/input.php?id=" + getString(R.string.app_id) + "&status=ready";
            //String to place our result in
            String result = null;
            //Instantiate new instance of our class
            HttpGetRequest getRequest = new HttpGetRequest();
            //Perform the doInBackground method, passing in our url
            try {
                result = getRequest.execute(myUrl).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ((TextView) findViewById(R.id.number)).setText(result);
        } else {
            helpLayout.setVisibility(View.GONE);
            p1_button.setText(R.string.ready);
            p1_button.setTextSize(30);
            p1_button.setTextColor(Color.WHITE);
            //Some url endpoint that you may have
            String myUrl = "http://54.218.240.133/mikva/input.php?id=" + getString(R.string.app_id) + "&status=cancel-ready";
            //String to place our result in
            String result = null;
            //Instantiate new instance of our class
            HttpGetRequest getRequest = new HttpGetRequest();
            //Perform the doInBackground method, passing in our url
            try {
                result = getRequest.execute(myUrl).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void play(String song) {
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

        Toast.makeText(this, songName, Toast.LENGTH_LONG).show();
    }

    public void music(View V) {
        spinner.performClick();
    }
    public void checkbox (View v) {
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
}