package by.ddv.geotask.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import by.ddv.geotask.InputActivity;
import by.ddv.geotask.R;
import by.ddv.geotask.json.Common;
import by.ddv.geotask.json.Helper;
import by.ddv.geotask.json.model_geocoding.GoogleGeocoding;
import by.ddv.geotask.list_view.GeocogingListItem;
import by.ddv.geotask.list_view.RecyclerAdapter;


public class FromFragment extends Fragment implements OnMapReadyCallback {

    private static String stream = null;

    MapView mapView;
    static GoogleMap map;
    private static Marker marker;
    private View v;

    List<GeocogingListItem> listItems = new ArrayList<>();

    private String inputString = null;
    RecyclerAdapter adapter;
    RecyclerView recyclerView;

    private int numberListItem = 0;

    public FromFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_from, container, false);

        final EditText inputData = (EditText) v.findViewById(R.id.inputFrom);

        Button button = (Button) v.findViewById(R.id.btnFrom);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (InputActivity.hasConnection(getActivity())) {

                    inputString = inputData.getText().toString();

                    removePoint();

                    if (inputString.isEmpty()){
                        if (listItems.size() != 0){

                            clearData();

                            //set height=WRAP_CONTENT for RecyclerView
                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            recyclerView.setLayoutParams(lp);
                        }
                        Toast.makeText(getActivity(), "Please enter a value", Toast.LENGTH_LONG).show();

                    } else new GetObject().execute(Common.apiRequestGeocoding(inputString));

                } else {
                    InputActivity.showAlertDialog(getActivity(), "No internet connection", "You do not have an Internet connection", "no_connection");
                }
            }
        });

        return v;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) v.findViewById(R.id.mapViewFrom);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        map = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    public static void addPoint(double lat, double lng, String address) {

        removePoint();

        marker = map.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                .title("A <Start>").snippet(address)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(new LatLng(lat, lng)).zoom(5).bearing(0)
                .tilt(0).build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        InputActivity.latFrom = lat;
        InputActivity.lngFrom = lng;
    }


    private static void removePoint() {
        if (marker != null) marker.remove();
    }


    private class GetObject extends AsyncTask<String,Void,String> {

        ProgressDialog pd = new ProgressDialog(getActivity());

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
            stream = http.getHTTPData(urlString);
            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.contains("Not found data")){
                pd.dismiss();
                return;
            }


            if (listItems.size() != 0){
                clearData();
                setInitialData();
            } else {
                setInitialData();
                recyclerView = (RecyclerView) getActivity().findViewById(R.id.listDataFrom);
                recyclerView.setTag("FromFragment");
                adapter = new RecyclerAdapter(getActivity(), listItems);
            }

            //set height of RecyclerView
            int heightRecyclerView;

            if (numberListItem > 3) {
                heightRecyclerView = 228;
            } else {
                heightRecyclerView = RelativeLayout.LayoutParams.WRAP_CONTENT;
            }

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, heightRecyclerView);
            recyclerView.setLayoutParams(lp);

            recyclerView.setAdapter(adapter);

            pd.dismiss();


        }

    }

    private void clearData() {
        listItems.clear();
        adapter.notifyDataSetChanged();
    }

    private void setInitialData() {

        GsonBuilder builger = new GsonBuilder();
        Gson gson = builger.create();
        GoogleGeocoding googleGeocoding = gson.fromJson(stream, GoogleGeocoding.class);

        String status = googleGeocoding.getStatus();

        if (status.equals("OK")) {

            int countListItem = 7;
            numberListItem = googleGeocoding.getResults().size();


            if (numberListItem < countListItem){
                countListItem = googleGeocoding.getResults().size();
            }

            for (int i = 0; i < countListItem; i++) {
                String address = null;
                String coordinates;
                double lat = 0;
                double lng = 0;
                try {
                    address = googleGeocoding.getResults().get(i).getFormatted_address();
                    lat = googleGeocoding.getResults().get(i).getGeometry().getLocation().getLat();
                    lng = googleGeocoding.getResults().get(i).getGeometry().getLocation().getLng();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (address == null){
                    address = "no data";
                }

                if (lat != 0 && lng != 0) {
                    coordinates = String.format("lat=%.4f; lng=%.4f", lat, lng);
                } else coordinates = "no data";

                listItems.add(new GeocogingListItem (address, coordinates, lat, lng));

            }

        } else Toast.makeText(getActivity(), "Server error", Toast.LENGTH_SHORT).show();


    }


}
