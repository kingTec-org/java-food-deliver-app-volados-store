package gofereatsrestarant.views.main;
/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage view.main.menu
 * @category MapsActivity
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import gofereatsrestarant.R;
import gofereatsrestarant.configs.AppController;
import gofereatsrestarant.configs.Constants;
import gofereatsrestarant.configs.RunTimePermission;
import gofereatsrestarant.configs.SessionManager;
import gofereatsrestarant.drawpolyline.DownloadTask;
import gofereatsrestarant.drawpolyline.PolylineOptionsInterface;
import gofereatsrestarant.interfaces.DriverLocation;
import gofereatsrestarant.interfaces.LatLngInterpolator;
import gofereatsrestarant.pushnotification.Config;
import gofereatsrestarant.utils.CommonMethods;
import gofereatsrestarant.utils.MyLocation;
import gofereatsrestarant.views.customize.CustomDialog;

/*****************************************************************
 Track order and driver using google map
 ****************************************************************/
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 1000;
    private static String TAG = "MAP LOCATION";
    private final ArrayList movepoints = new ArrayList();
    public GoogleMap mGoogleMap;
    public @Inject
    SessionManager sessionManager;
    public @Inject
    CommonMethods commonMethods;
    public @Inject
    CustomDialog customDialog;
    public @Inject
    RunTimePermission runTimePermission;
    /**
     * Annotation  using ButterKnife lib to Injection and OnClick
     **/
    public @InjectView(R.id.ivBack)
    ImageView ivBack;
    public @InjectView(R.id.tvTitle)
    TextView tvTitle;
    public @InjectView(R.id.vBottom)
    View vBottom;
    public @InjectView(R.id.mapview)
    MapView mMapView;

    private Polyline polyline;
    private ArrayList markerPoints = new ArrayList();
    private int status = -1;
    private int orderId = 0;
    private Marker carmarker;
    //private float startbear = 0;
    private float endbear = 0;
    private Marker marker;
    private float speed = 13f;
    private boolean isFirst = true;
    private ValueAnimator valueAnimator;
    private DecimalFormat twoDForm;
    private Query query;
    /**
     * Get user current location
     */
    private MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
        @Override
        public void gotLocation(Location location) {
            if (location == null) return;
        }
    };
    private BroadcastReceiver mBroadcastReceiver;
    private LocationManager mLocationManager;
    private Location lastLocation = null;
    // Firebase database variables
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private ValueEventListener mSearchedLocationReferenceListener;
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                Log.d("", String.format("%f, %f", location.getLatitude(), location.getLongitude()));

            } else {
                Log.d("", "Location is null");
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    /**
     * Rotate marker
     **/
    private static float computeRotation(float fraction, float start, float end) {
        float normalizeEnd = end - start; // rotate start to 0
        float normalizedEndAbs = (normalizeEnd + 360) % 360;

        float direction = (normalizedEndAbs > 180) ? -1 : 1; // -1 = anticlockwise, 1 = clockwise
        float rotation;
        if (direction > 0) {
            rotation = normalizedEndAbs;
        } else {
            rotation = normalizedEndAbs - 360;
        }

        float result = fraction * rotation + start;
        return (result + 360) % 360;
    }

    @OnClick(R.id.ivBack)
    public void onBack() {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.inject(this);
        AppController.getAppComponent().inject(this);

        tvTitle.setText(getResources().getString(R.string.track_order));
        vBottom.setVisibility(View.VISIBLE);

        mMapView.onCreate(savedInstanceState);
        commonMethods.rotateArrow(ivBack, this);

        twoDForm = new DecimalFormat("#.##########");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setDecimalSeparator('.');
        twoDForm.setDecimalFormatSymbols(dfs);

        mMapView.getMapAsync(this); //this is important
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'Driver Location' node
        mFirebaseDatabase = mFirebaseInstance.getReference("live_tracking");

        getIntents();
        initMap();
        receivePushNotification();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();


        getPushNotification();
        // clear the notification area when the app is opened
        //NotificationUtils.clearNotifications(getApplicationContext());

        if (orderId > 0 && mSearchedLocationReferenceListener == null) {
            addLatLngChangeListener();
        }

        checkAllPermission(Constants.PERMISSIONS_LOCATION);

        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        if (mSearchedLocationReferenceListener != null) {
            query.removeEventListener(mSearchedLocationReferenceListener);
            mFirebaseDatabase.removeEventListener(mSearchedLocationReferenceListener);
            mSearchedLocationReferenceListener = null;
        } else {
            Log.e(TAG, "Driver Location data removed! Failed");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        //mLocationManager.removeUpdates(mLocationListener);
        //LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    /**
     * To fetch data's using intents
     */

    public void getIntents() {
        markerPoints = (ArrayList) getIntent().getSerializableExtra("PickupDrop");
        /**
         * @TripStatus 'pending' => 0, Driver accept the request
         * 'confirmed' => 1, Driver confirmed the  list and rating for restaurant
         * 'declined' => 2,  Driver declined the order list
         * 'started' => 3,  Driver start the trip (pickup the order)
         * 'delivered'  => 4, Driver drop off or delivered the order and rating for eater
         * 'completed' => 5, Driver complete the trip
         * 'cancelled' => 6, Driver or restaurant cancel the trip
         **/
        status = getIntent().getIntExtra("status", -1);
        orderId = getIntent().getIntExtra("orderId", 0);
    }


    /****************************************************************** */
    /**                  Animate Marker for Live Tracking               */
    /****************************************************************** */

    private void initMap() {
        int googlePlayStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (googlePlayStatus != ConnectionResult.SUCCESS) {
            GooglePlayServicesUtil.getErrorDialog(googlePlayStatus, this, -1).show();
            finish();
        } else {
            if (mGoogleMap != null) {
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
                mGoogleMap.setMyLocationEnabled(false);
                mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
                mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
            }
        }
    }

    private void getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location location = null;
        if (!(isGPSEnabled || isNetworkEnabled))
            Snackbar.make(mMapView, "Unable to get GPS", Snackbar.LENGTH_SHORT).show();
        else {
            if (isNetworkEnabled) {
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
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        if (location != null) {
            Log.d("", String.format("getCurrentLocation(%f, %f)", location.getLatitude(),
                    location.getLongitude()));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        initMap();
        drawMarker();
    }

    /**
     * Live tracking function
     */
    public void liveTrackingFireBase(Double driverlat, Double driverlong) {

        LatLng driverlatlng = new LatLng(driverlat, driverlong);
        sessionManager.setDriverUpdatedLat(String.valueOf(driverlatlng.latitude));
        sessionManager.setDriverUpdatedLng(String.valueOf(driverlatlng.longitude));
        markerPoints.set(2, driverlatlng);
        if (movepoints.size() < 1) {
            movepoints.add(0, driverlatlng);
            movepoints.add(1, driverlatlng);

        } else {
            movepoints.set(1, movepoints.get(0));
            movepoints.set(0, driverlatlng);
        }

        DecimalFormat twoDForm = new DecimalFormat("#.#######");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setDecimalSeparator('.');
        twoDForm.setDecimalFormatSymbols(dfs);
        String zerolat = twoDForm.format(((LatLng) movepoints.get(0)).latitude);
        String zerolng = twoDForm.format(((LatLng) movepoints.get(0)).longitude);

        String onelat = twoDForm.format(((LatLng) movepoints.get(1)).latitude);
        String onelng = twoDForm.format(((LatLng) movepoints.get(1)).longitude);

        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(((LatLng) movepoints.get(1)).latitude);
        location.setLongitude(((LatLng) movepoints.get(1)).longitude);
        location.setTime(System.currentTimeMillis());


        float calculatedSpeed = 0;
        float distance = 0;
        if (lastLocation != null) {
            double elapsedTime = (location.getTime() - lastLocation.getTime()) / 1_000; // Convert milliseconds to seconds
            elapsedTime = Double.parseDouble(twoDForm.format(elapsedTime));

            if (elapsedTime > 0)
                calculatedSpeed = (float) (lastLocation.distanceTo(location) / elapsedTime);
            else
                calculatedSpeed = lastLocation.distanceTo(location) / 1;
            calculatedSpeed = Float.valueOf(twoDForm.format(calculatedSpeed));
            distance = location.distanceTo(lastLocation);

        }


        lastLocation = location;

        if (!Float.isNaN(calculatedSpeed) && !Float.isInfinite(calculatedSpeed)) {
            speed = calculatedSpeed;
        }

        if (speed <= 0)
            speed = 10;


        if ((!zerolat.equals(onelat) || !zerolng.equals(onelng)) && distance < 30) {
            adddefaultMarker((LatLng) movepoints.get(1), (LatLng) movepoints.get(0));
        }

    }

    /**
     * Driver Location change listener
     */
    private void addLatLngChangeListener() {

        // User data change listener
        query = mFirebaseDatabase.child(String.valueOf(orderId));

        mSearchedLocationReferenceListener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (orderId > 0) {
                    DriverLocation driverLocation = dataSnapshot.getValue(DriverLocation.class);

                    // Check for null
                    if (driverLocation == null) {
                        Log.e("Map", "Driver Location data is null!");
                        return;
                    }
                    liveTrackingFireBase(Double.valueOf(driverLocation.lat), Double.valueOf(driverLocation.lng));
                } else {
                    query.removeEventListener(this);
                    mFirebaseDatabase.removeEventListener(this);
                    mFirebaseDatabase.onDisconnect();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    /**
     * Update route while trip (Car moving)
     */
    public void drawRoute(LatLng driverlatlng) {

        LatLng pickupLatlng = (LatLng) markerPoints.get(0);
        LatLng dropLatlng = (LatLng) markerPoints.get(1);


        // Add new marker to the Google Map Android API V2

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        //the include method will calculate the min and max bound.
        builder.include(driverlatlng);
        if (status >= 3) {
            builder.include(dropLatlng);
        } else {
            builder.include(pickupLatlng);
        }

        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels / 2;
        int height = getResources().getDisplayMetrics().heightPixels / 2;
        int padding = (int) (width * 0.08); // offset from edges of the map 10% of screen

        if (isFirst) {
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
            mGoogleMap.moveCamera(cu);
            isFirst = false;
        }

        String url;
        if (status >= 3) {
            // Getting URL to the Google Directions API
            url = getDirectionsUrl(driverlatlng, dropLatlng);
        } else {
            url = getDirectionsUrl(driverlatlng, pickupLatlng);
        }

        DownloadTask downloadTask = new DownloadTask(new PolylineOptionsInterface() {
            @Override
            public void getPolylineOptions(PolylineOptions output, ArrayList points) {

                if (mGoogleMap != null && output != null) {
                    if (polyline != null)
                        polyline.remove();
                    polyline = mGoogleMap.addPolyline(output);
                } /*else {
                    //  Toast.makeText(getApplicationContext(), "Map or route not ready", Toast.LENGTH_LONG).show();
                }*/
            }
        });
        Log.v("Downloadd", "task");
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    /**
     * Update route while trip (Car moving)
     */
    public void drawMarker() {

        if (mGoogleMap == null)
            return;
        mGoogleMap.clear();
        LatLng pickuplatlng = (LatLng) markerPoints.get(0);
        LatLng droplatlng = (LatLng) markerPoints.get(1);
        LatLng driverlatlng = (LatLng) markerPoints.get(2);

        // Creating MarkerOptions
        MarkerOptions pickupOptions = new MarkerOptions();

        // Setting the position of the marker

        if (status >= 3) {
            pickupOptions.position(droplatlng);
            pickupOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ub__ic_pin_dropoff));
        } else {
            pickupOptions.position(pickuplatlng);
            pickupOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ub__ic_pin_pickup));
        }
        // Add new marker to the Google Map Android API V2
        mGoogleMap.addMarker(pickupOptions);

        // Creating MarkerOptions
        MarkerOptions dropOptions = new MarkerOptions();


        // Setting the position of the marker
        dropOptions.position(driverlatlng);
        dropOptions.anchor(0.5f, 0.5f);
        dropOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.gf_moto_bike));
        // Add new marker to the Google Map Android API V2
        carmarker = mGoogleMap.addMarker(dropOptions);

        drawRoute(driverlatlng);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(driverlatlng).zoom(16.5f).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

    /**
     * get direction for given origin, dest
     */
    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&key=" + getResources().getString(R.string.google_maps_key);

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * Move marker for given locations
     **/
    public void adddefaultMarker(LatLng latlng, LatLng latlng1) {
        Location startbearlocation = new Location(LocationManager.GPS_PROVIDER);
        Location endbearlocation = new Location(LocationManager.GPS_PROVIDER);

        startbearlocation.setLatitude(latlng.latitude);
        startbearlocation.setLongitude(latlng.longitude);

        endbearlocation.setLatitude(latlng1.latitude);
        endbearlocation.setLongitude(latlng1.longitude);

        /*if (endbear != 0.0) {
            startbear = endbear;
        }*/

        carmarker.setFlat(true);
        carmarker.setAnchor(0.5f, 0.5f);
        marker = carmarker;
        // Move map while marker gone
        ensureMarkerOnBounds(latlng, "updated");

        endbear = (float) bearing(startbearlocation, endbearlocation);
        endbear = (float) (endbear * (180.0 / 3.14));

        double distance = Double.valueOf(twoDForm.format(startbearlocation.distanceTo(endbearlocation)));

        if (distance > 0 && distance < 30)
            animateMarker(latlng1, marker, speed, endbear);
        twoDForm = new DecimalFormat("#.####");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        dfs.setDecimalSeparator('.');
        twoDForm.setDecimalFormatSymbols(dfs);
        String oldlat = twoDForm.format(latlng.latitude);
        double ola = Double.valueOf(oldlat);
        String oldlong = twoDForm.format(latlng.longitude);
        double olo = Double.valueOf(oldlong);
        String newlat = twoDForm.format(latlng1.latitude);
        double nla = Double.valueOf(newlat);
        String newlong = twoDForm.format(latlng1.longitude);
        double nlo = Double.valueOf(newlong);

        if (ola != nla && olo != nlo) {
            drawRoute((LatLng) movepoints.get(1));
        }
    }

    /**
     * Move marker
     **/
    public void animateMarker(final LatLng destination, final Marker marker, final float speed, final float endbear) {

        final LatLng[] newPosition = new LatLng[1];
        //final LatLng[] oldPosition = new LatLng[1];
        if (marker != null) {
            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(destination.latitude, destination.longitude);
            long duration;
            Location newLoc = new Location(LocationManager.GPS_PROVIDER);
            newLoc.setLatitude(startPosition.latitude);
            newLoc.setLongitude(startPosition.longitude);
            Location prevLoc = new Location(LocationManager.GPS_PROVIDER);
            prevLoc.setLatitude(endPosition.latitude);
            prevLoc.setLongitude(endPosition.longitude);

            //final double distance = Double.valueOf(twoDForm.format(newLoc.distanceTo(prevLoc)));

            //duration = (long) ((distance / speed) * 950);
            duration = 2000;
            final float startRotation = marker.getRotation();

            final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.LinearFixed();
            if (valueAnimator != null) {
                valueAnimator.cancel();
                valueAnimator.end();
            }
            valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(duration);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        newPosition[0] = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition[0]); // Move Marker
                        marker.setRotation(computeRotation(v, startRotation, endbear)); // Rotate Marker
                    } catch (Exception ex) {
                        // I don't care atm..
                    }
                }
            });

            valueAnimator.start();

        }
    }

    /**
     * Find GPS rotate position
     **/
    private double bearing(Location startPoint, Location endPoint) {
        double deltaLongitude = endPoint.getLongitude() - startPoint.getLongitude();
        double deltaLatitude = endPoint.getLatitude() - startPoint.getLatitude();
        double angle = (3.14 * .5f) - Math.atan(deltaLatitude / deltaLongitude);

        if (deltaLongitude > 0) return angle;
        else if (deltaLongitude < 0) return angle + 3.14;
        else if (deltaLatitude < 0) return 3.14;

        return 0.0f;
    }

    /**
     * move map to center position while marker hide
     **/
    private void ensureMarkerOnBounds(LatLng toPosition, String type) {
        if (marker != null) {
            float currentZoomLevel = mGoogleMap.getCameraPosition().zoom;
            float bearing = mGoogleMap.getCameraPosition().bearing;

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(toPosition).zoom(currentZoomLevel).bearing(bearing).build();

            if ("updated".equalsIgnoreCase(type)) {
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } else {
                if (!mGoogleMap.getProjection().getVisibleRegion().latLngBounds.contains(toPosition)) {
                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        }
    }

    /**
     * Check user allow all permission and ask permission to allow
     */
    private void checkAllPermission(String[] permission) {
        ArrayList<String> blockedPermission = runTimePermission.checkHasPermission(MapsActivity.this, permission);
        if (blockedPermission != null && !blockedPermission.isEmpty()) {
            boolean isBlocked = runTimePermission.isPermissionBlocked(MapsActivity.this, blockedPermission.toArray(new String[blockedPermission.size()]));
            if (isBlocked) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        showEnablePermissionDailog(0, getString(R.string.please_enable_permissions));
                    }
                });
            } else {
                ActivityCompat.requestPermissions(MapsActivity.this, permission, 150);
            }
        } else {
            checkGpsEnable();
        }
    }

    /**
     * Get permission result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ArrayList<String> permission = runTimePermission.onRequestPermissionsResult(permissions, grantResults);
        if (permission != null && !permission.isEmpty()) {
            runTimePermission.setFirstTimePermission(true);
            String[] dsf = new String[permission.size()];
            permission.toArray(dsf);
            checkAllPermission(dsf);
        } else {
            checkGpsEnable();
        }
    }

    /**
     * If any one or more permission is deny or block show the enable permission dialog
     */
    private void showEnablePermissionDailog(final int type, String message) {
        if (!customDialog.isVisible()) {
            customDialog = new CustomDialog(message, getString(R.string.okay), new CustomDialog.btnAllowClick() {
                @Override
                public void clicked() {
                    if (type == 0)
                        callPermissionSettings();
                    else
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 101);
                }
            });
            customDialog.show(getSupportFragmentManager(), "");
        }
    }

    /**
     * Open permission dialog
     */
    private void callPermissionSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 300);
    }

    /**
     * Check GPS enable or not
     */
    private void checkGpsEnable() {
        boolean isGpsEnabled = MyLocation.defaultHandler().isLocationAvailable(this);
        if (!isGpsEnabled) {

            showEnablePermissionDailog(1, getString(R.string.please_enable_location));
        } else {
            MyLocation.defaultHandler().getLocation(this, locationResult);
            getCurrentLocation();
        }
    }

    /**
     * Get notification from Firebase broadcast
     */
    public void receivePushNotification() {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // FCM successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    //String message = intent.getStringExtra("message");

                    getPushNotification();
                }
            }
        };
    }

    private void getPushNotification() {
        String JSON_DATA = sessionManager.getPushNotification();

        try {
            JSONObject jsonObject = new JSONObject(JSON_DATA);
            if (jsonObject.getJSONObject("custom").has("type") && (jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("order_delivery_started")
                    || jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("order_delivery_completed"))) {
                Integer orderIds = jsonObject.getJSONObject("custom").getInt("order_id");
                if (orderId == orderIds) {
                    if (jsonObject.getJSONObject("custom").getString("type").equalsIgnoreCase("order_delivery_started"))
                        status = 3;
                    else
                        status = 5;
                    drawMarker();
                }
            }
        } catch (JSONException e) {

        }
    }
}
