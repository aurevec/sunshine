package com.aurevec.sunshine;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private final String SCREEN_NAME = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Mixpanel
        String projectToken = BuildConfig.MIXPANEL_API_KEY;
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);

        try {
            JSONObject props = new JSONObject();
            props.put("Gender", "Male");
            props.put("Logged in", false);
            mixpanel.track("MainActivity - onCreate called", props);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Unable to add properties to JSONObject", e);
        }

        //Google Analytics
        Tracker mTracker;
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        Log.i(LOG_TAG, "Setting screen name: " + SCREEN_NAME);
        mTracker.setScreenName("Image~" + SCREEN_NAME);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Display")
                .setAction("View")
                .build());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}
