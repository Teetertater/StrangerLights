package com.repleno.strangerlights;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private LinkedList<LatLng> locations = new LinkedList<>();
    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = MainActivity.class.getSimpleName();
    private LocationRequest mLocationRequest;
    private Location currentLocation;

    private int buttonState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locations.clear();
                TextView tv = (TextView) findViewById(R.id.textView2);
                tv.setText("Reset");
            }
        });

        final Button button = (Button) findViewById(R.id.toggle_btn);
        View.OnClickListener myOCL = new View.OnClickListener(){
            public void onClick(View v) {
                handleNewLocation(currentLocation);
                buttonState++;
                switch (buttonState % 3){
                    case 0:
                        TaskManager.getInstance().lightUpdate = "Auto";
                        button.setText("PROXIMITY LIGHTS");
                        break;
                    case 1:
                        TaskManager.getInstance().lightUpdate = "OFF";
                        button.setText("LIGHTS OFF");
                        break;
                    case 2:
                        TaskManager.getInstance().lightUpdate = "ON";
                        button.setText("LIGHTS ON");
                        break;
                }
            }
        };
        button.setOnClickListener(myOCL);

        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)        // 5 seconds, in milliseconds
                .setFastestInterval(750); // 1 second, in milliseconds

    }

    @Override
    protected void onResume(){
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            Log.i(TAG, "Location services connected.");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            handleNewLocation(currentLocation);
        }catch (SecurityException e){}
    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        locations.add(newLatLng);
        if (locations.size() > 10)
            locations.remove(locations.size()-1);
        this.currentLocation = location;
        TaskManager.getInstance().publishLocation(newLatLng);

        TextView tv = (TextView)findViewById(R.id.textView2);
        tv.setText(locations.toString().replaceAll("lat/lng:", ""));
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Location services failed!");
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("StrangerLights") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        AppIndex.AppIndexApi.start(mGoogleApiClient, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mGoogleApiClient, getIndexApiAction());
        mGoogleApiClient.disconnect();
    }
}
