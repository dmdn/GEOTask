package by.ddv.geotask;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.android.PolyUtil;

import java.util.List;

import by.ddv.geotask.json.Common;
import by.ddv.geotask.json.Helper;
import by.ddv.geotask.json.model_directions.GoogleDirections;


public class ResultActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap map;

    private String status = null;
    private String polyline = null;

    TextView tvResult;
    String latForStr, lngForStr, latToStr, lngToStr;


    private LocationManager locationManager;
    private Location myLocation;

    LatLngBounds.Builder latLngBuilder;
    private Marker myMarker;
    private Marker markerA;
    private Marker markerB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        String coordinates[] = getIntent().getExtras().getStringArray("coordinates");
        latForStr = coordinates[0];
        lngForStr = coordinates[1];
        latToStr = coordinates[2];
        lngToStr = coordinates[3];

        tvResult = (TextView) findViewById(R.id.tvResult);

        mapView = (MapView) findViewById(R.id.mapResult);

        if (hasConnection(this)) {
            if (mapView != null) {
                mapView.onCreate(null);
                mapView.onResume();
                mapView.getMapAsync(this);
            }
            new GetPolyline().execute(Common.apiRequestDirections(latForStr, lngForStr, latToStr, lngToStr));
        } else showAlertDialog(ResultActivity.this, "No internet connection", "You do not have an Internet connection", "no_connection");

    }

    @Override
    protected void onResume() {
        super.onResume();
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


        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            showAlertDialog(ResultActivity.this, "GPS is disabled", "Please enable GPS", "gps_disabled");
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10, locationListener);
        }

    }



    @Override
    protected void onPause() {
        super.onPause();
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
        locationManager.removeUpdates(locationListener);
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }



    private class GetPolyline extends AsyncTask<String,Void,String> {

        ProgressDialog pd = new ProgressDialog(ResultActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Please wait...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String urlString = params[0];

            Helper http = new Helper();
            String stream = http.getHTTPData(urlString);
            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.contains("Not found data")){
                pd.dismiss();
                return;
            }

            GsonBuilder builger = new GsonBuilder();
            Gson gson = builger.create();
            GoogleDirections googleDirections = gson.fromJson(s, GoogleDirections.class);

            status = googleDirections.getStatus();

            if (status.equals("OK")) {

                double distance = (double) googleDirections.getRoutes().get(0).getLegs().get(0).getDistance().getValue();

                String distanceStr;

                if (distance / 1000 < 1) {
                    distanceStr = String.format("%.0f m", distance);
                } else distanceStr = String.format("%.0f km", distance/1000);


                polyline = googleDirections.getRoutes().get(0).getOverview_polyline().getPoints();

                tvResult.setText("Found; distance = " + distanceStr);

                showMyLocation(myLocation);

                pd.dismiss();

                lineDrawer(polyline);

            } else {
                tvResult.setText("Unknown");
                Toast.makeText(ResultActivity.this, "Server error", Toast.LENGTH_LONG).show();
            }


        }
    }


    private void lineDrawer(String codedPolyline) {

        double myLatCamera;
        double myLngCamera;

        if (myLocation == null){
            myLatCamera = 0.0000;
            myLngCamera = 0.0000;
        } else {
            myLatCamera = myLocation.getLatitude();
            myLngCamera = myLocation.getLongitude();
        }


        if (markerA == null && markerB == null){
            List<LatLng> points = PolyUtil.decode(codedPolyline);

            PolylineOptions line = new PolylineOptions();
            line.width(5f).color(Color.BLACK);
            latLngBuilder = new LatLngBounds.Builder();

            for (int i = 0; i < points.size(); i++) {

                if (i == 0) {
                    markerA = map.addMarker(new MarkerOptions().position(points.get(i)).title("A").snippet("Start")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                } else if (i == points.size() - 1) {
                    markerB = map.addMarker(new MarkerOptions().position(points.get(i)).title("B").snippet("End")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                }

                line.add(points.get(i));
                latLngBuilder.include(points.get(i));
            }

            map.addPolyline(line);
        }

        latLngBuilder.include(new LatLng(myLatCamera, myLngCamera));

        int size = getResources().getDisplayMetrics().widthPixels;
        LatLngBounds latLngBounds = latLngBuilder.build();
        CameraUpdate track = CameraUpdateFactory.newLatLngBounds(latLngBounds, size, size, 70);
        map.moveCamera(track);

    }



    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            myLocation = location;

            showMyLocation(myLocation);

            if (polyline != null){
                lineDrawer(polyline);
            }

        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(ResultActivity.this, "GPS is disabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(ResultActivity.this, "GPS is enabled", Toast.LENGTH_SHORT).show();

            if (ActivityCompat.checkSelfPermission(ResultActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ResultActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            myLocation = locationManager.getLastKnownLocation(provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };


    private void showMyLocation(Location location) {
        String locationStr;
        double myLat;
        double myLng;

        if (myMarker != null){
            myMarker.remove();
        }

        if (location == null){
            myLat = 0.0000;
            myLng = 0.0000;

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                locationStr = "GPS is disabled";
            } else locationStr = "Geolocation not defined";
        } else {
            myLat = location.getLatitude();
            myLng = location.getLongitude();
            locationStr = String.format("lat=%.4f; lng=%.4f", myLat, myLng);
        }

        myMarker = map.addMarker(new MarkerOptions().position(new LatLng(myLat, myLng)).title("My Location").snippet(locationStr));

    }


    public void showAlertDialog(Context context, String title, String message, String status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);

        if (status.equals("no_connection")){
            alertDialog.setIcon(R.drawable.ic_no_connection);
        }

        if (status.equals("gps_disabled")){
            alertDialog.setIcon(R.drawable.ic_gps_off);
        }

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
    }


    public static boolean hasConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()){
            return true;
        }
        return false;
    }





}
