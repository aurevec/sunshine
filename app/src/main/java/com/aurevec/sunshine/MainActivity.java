package com.aurevec.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.segment.analytics.Analytics;
import com.segment.analytics.Traits;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private final String SCREEN_NAME = MainActivity.class.getSimpleName();
    private final String FORECASTFRAGMENT_TAG = "FFTAG";

    private String mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLocation = Utility.getPreferredLocation(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment(), FORECASTFRAGMENT_TAG)
                    .commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        //Mixpanel
//        String projectToken = BuildConfig.MIXPANEL_API_KEY;
//        MixpanelAPI mixpanel = MixpanelAPI.getInstance(this, projectToken);
//
//        try {
//            JSONObject props = new JSONObject();
//            props.put("Gender", "Male");
//            props.put("Logged in", false);
//            mixpanel.track("MainActivity - onCreate called", props);
//        } catch (JSONException e) {
//            Log.e(LOG_TAG, "Unable to add properties to JSONObject", e);
//        }
//
//        //Google Analytics
//        Tracker mTracker;
//        AnalyticsApplication application = (AnalyticsApplication) getApplication();
//        mTracker = application.getDefaultTracker();
//
//        Log.i(LOG_TAG, "Setting screen name: " + SCREEN_NAME);
//        mTracker.setScreenName("Image~" + SCREEN_NAME);
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
//
//        mTracker.send(new HitBuilders.EventBuilder()
//                .setCategory("Display")
//                .setAction("View")
//                .build());

        Analytics.with(this).identify("000000000001", new Traits().putName("Aurélien Vecchiato").putEmail("aurevec@yahoo.fr"), null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        if (id == R.id.action_map) {
            openPreferredLocationInMap();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openPreferredLocationInMap() {
        String location = Utility.getPreferredLocation(this);

        // Using the URI scheme for showing a location found on a map.  This super-handy
        // intent can is detailed in the "Common Intents" page of Android's developer site:
        // http://developer.android.com/guide/components/intents-common.html#Maps
        Uri geoLocation = Uri.parse("geo:0,0?").buildUpon()
                .appendQueryParameter("q", location)
                .build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d(LOG_TAG, "Couldn't call " + location + ", no receiving apps installed!");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String location = Utility.getPreferredLocation( this );
        // update the location in our second pane using the fragment manager
        if (location != null && !location.equals(mLocation)) {
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentByTag(FORECASTFRAGMENT_TAG);
            if ( null != ff ) {
                ff.onLocationChanged();
            }
            mLocation = location;
        }
    }
}
