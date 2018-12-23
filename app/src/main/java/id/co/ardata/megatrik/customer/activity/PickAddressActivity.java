package id.co.ardata.megatrik.customer.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.ardata.megatrik.customer.R;
import id.co.ardata.megatrik.customer.utils.Tools;

public class PickAddressActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.inputAddress)
    EditText inputAddress;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.btnGps)
    FloatingActionButton btnGps;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    Location location;
    CameraUpdate cameraUpdate;
    Marker marker;
    Geocoder geocoder;
    List<Address> addresses;
    double latitude, longitude;
    String city_name;

    private static final int PLACE_AUTO_COMPLETE_REQUEST_CODE = 1;
    private static final int FINE_LOCATION_PERMISSION_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_address);
        ButterKnife.bind(this);

        geocoder = new Geocoder(this, Locale.getDefault());

        toolbar.setTitle("Pilih Tujuan Alamat");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        latitude = 0.0;
        longitude = 0.0;

        inputAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    inputAddress.setEnabled(false);
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(PickAddressActivity.this);
                    startActivityForResult(intent, PLACE_AUTO_COMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesNotAvailableException e) {
                    inputAddress.setEnabled(true);
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    inputAddress.setEnabled(true);
                    e.printStackTrace();
                }
            }
        });

        btnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocationNow();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String alamat = inputAddress.getText().toString();
                boolean valid = true;

                if (alamat.isEmpty()){
                    valid = false;
                    inputAddress.setError("Alamat tidak valid");
                }else{
                    inputAddress.setError(null);
                }

                if (latitude == 0.0 || longitude == 0.0){
                    valid = false;
                    Toast.makeText(PickAddressActivity.this, "Latitude longitude tidak valid", Toast.LENGTH_SHORT).show();
                }

                if (city_name == null){
                    valid = false;
                    Tools.Tshort(PickAddressActivity.this, "Nama kota tidak terdeteksi");
                }

                if (valid){
                    Intent toBack = new Intent();
                    toBack.putExtra("alamat", alamat);
                    toBack.putExtra("city_name", city_name);
                    toBack.putExtra("lat", latitude);
                    toBack.putExtra("lng", longitude);
                    setResult(RESULT_OK, toBack);
                    finish();
                }
            }
        });

        buildGoogleApiClient();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = Tools.configActivityMaps(googleMap);
                map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        PickAddressActivity.this.latitude = marker.getPosition().latitude;
                        PickAddressActivity.this.longitude = marker.getPosition().longitude;
                        try{
                            addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            if (addresses.size() > 0){
                                city_name = addresses.get(0).getSubAdminArea();
                                inputAddress.setText(addresses.get(0).getAddressLine(0));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private void buildGoogleApiClient() {
        if (googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void getLocationNow() {
        if (marker != null){
            marker.remove();
        }
        if (location != null){
            marker = map.addMarker(new MarkerOptions().position(new LatLng(this.latitude, this.longitude)));
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(this.latitude, this.longitude), 15);
            map.moveCamera(cameraUpdate);

            try {
                addresses = geocoder.getFromLocation(this.latitude, this.longitude, 1);
                city_name = addresses.get(0).getSubAdminArea();
                inputAddress.setText(addresses.get(0).getAddressLine(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStart() {
        if (googleApiClient != null)
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (googleApiClient != null)
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        findLatLngNow();
    }

    private void findLatLngNow() {
        if (android.support.v4.app.ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && android.support.v4.app.ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, FINE_LOCATION_PERMISSION_REQUEST);
        }else{
            try {
                location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                if (location != null){
                    this.latitude = location.getLatitude();
                    this.longitude = location.getLongitude();
                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        if (addresses != null && addresses.size() > 0){
                            city_name = addresses.get(0).getSubAdminArea();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setMarker();
                }
            }catch (SecurityException ex){
                ex.printStackTrace();
            }
        }
    }

    private void setMarker() {
        if (marker != null){
            marker.remove();
        }
        if (location != null) {
            marker = map.addMarker(new MarkerOptions().position(new LatLng(this.latitude, this.longitude)).draggable(true));
            cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(this.latitude, this.longitude), 15);
            map.moveCamera(cameraUpdate);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PLACE_AUTO_COMPLETE_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                Place place = PlaceAutocomplete.getPlace(this, data);
                inputAddress.setText(place.getAddress());
                this.latitude = place.getLatLng().latitude;
                this.longitude = place.getLatLng().longitude;
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (addresses != null && addresses.size() > 0){
                        city_name = addresses.get(0).getSubAdminArea();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setMarker();
            }else if (resultCode == PlaceAutocomplete.RESULT_ERROR){
                Status status = PlaceAutocomplete.getStatus(this, data);
                Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
            inputAddress.setEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case FINE_LOCATION_PERMISSION_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    findLatLngNow();
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
