package com.ps2.petsfinder.petsfinder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker currentLocationMaker;
    private LatLng currentLocationLatLong;
    private DatabaseReference mDatabase;
    private FloatingActionButton fab;
    private Location location;
    private Marker marker;
    private double latitude;
    private double longitude;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference registroAnimalBase = databaseReference.child("registroAnimal");
    private List<String> listLocation;
    private List<String> listUsuarios;
    LocationData lData;
    private long childrenCount;
    private InicialLogadoActivity iniLogAct = new InicialLogadoActivity();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        boolean exiteReg = false;
        mDatabase = FirebaseDatabase.getInstance().getReference().child("location");

        getMarkersAnimais();


        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MapsActivity.this, RegistroAnimalActivity.class);
                Bundle bundle = new Bundle();

                latitude = getLatitude(getLocation());
                longitude = getLongitude(getLocation());

                String lat = new Double(latitude).toString();
                String lng = new Double(longitude).toString();

                bundle.putString("lat", lat);
                bundle.putString("lng", lng);
                intent.putExtras(bundle);

                startActivity(intent);

                //startActivity(new Intent(MapsActivity.this, RegistroAnimalActivity.class));
            }
        });



        latitude = getLatitude(getLocation());
        longitude = getLongitude(getLocation());

        currentLocationLatLong = new LatLng(latitude, longitude);


    }

    public void custonAddMarker(LatLng latlng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlng);
        markerOptions.title("teste");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        marker = mMap.addMarker(markerOptions);

        CameraPosition cameraPosition = new CameraPosition.Builder().zoom(15).target(currentLocationLatLong).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        custonAddMarker(currentLocationLatLong);
        mDatabase.child("23").child("Latitude").setValue("12");
        mDatabase.child("23").child("Longitude").setValue("12");


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

        } else {
            // Show rationale and request permission.
        }
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                String lat = "";
                String lng = "";
                for(int i = 0; i < listLocation.size(); i++){
                    lat = listLocation.get(i).substring(0, listLocation.get(i).indexOf("#") - 1);
                    lng = listLocation.get(i).substring(listLocation.get(i).indexOf("#") + 1, listLocation.get(i).length());
                    final int j = i;
                    if(lat.equals(String.valueOf(marker.getPosition().latitude))){
                        if(lng.equals(String.valueOf(marker.getPosition().longitude))){
                            getInfoReg(j);
                        }
                    }
                }
                return true;
            }
        });
    }

    public void getInfoReg(final int j){
        registroAnimalBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               // RegistroAnimal ra = new RegistroAnimal();
                //ra.setDono(dataSnapshot.child(String.valueOf(j)).child("usuario").getValue().toString());
                //iniLogAct.getRegistrosAnimais(ra);
                Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(intent);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void addGreenMarker(LatLng latLng) {
        //SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        //markerOptions.title(dt.format(newDate));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mMap.addMarker(markerOptions);
    }

    private String getLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        return String.valueOf(latitude) + "#" + String.valueOf(longitude);
    }

    private double getLatitude(String location) {
        String latStr = location.substring(0, location.indexOf("#") - 1);
        double latitude = Double.parseDouble(latStr);
        return latitude;
    }

    private double getLongitude(String location) {
        String longStr = location.substring(location.indexOf("#") + 1, location.length());
        double longitude = Double.parseDouble(longStr);
        return longitude;
    }

    public void getUsuariosReg(){
        registroAnimalBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listUsuarios = new ArrayList<>();
                for(int i = 1; i < dataSnapshot.getChildrenCount() + 1; i++){
                    listUsuarios.add(String.valueOf(registroAnimalBase.child(dataSnapshot.getValue().toString())));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void getMarkersAnimais(){
        registroAnimalBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                childrenCount = dataSnapshot.getChildrenCount();
                String strCoord = "";
                listLocation = new ArrayList<>();
                for(int i = 1; i < dataSnapshot.getChildrenCount() + 1; i++){
                    strCoord = dataSnapshot.child(String.valueOf(i)).child("latitude").getValue().toString() + "#" +
                               dataSnapshot.child(String.valueOf(i)).child("longitude").getValue().toString();
                    listLocation.add(strCoord);
                    replaceLatLng(strCoord);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void pintaMarcadorOutros(){
        if(lData != null){
            LatLng latLng = new LatLng(lData.getLatitude(), lData.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            mMap.addMarker(markerOptions);
        }
    }

    public void replaceLatLng(String strLatLng){
        lData = new LocationData();

        lData.setLatitude(Double.parseDouble(strLatLng.substring(0, strLatLng.indexOf("#") - 1)));
        lData.setLongitude(Double.parseDouble(strLatLng.substring(strLatLng.indexOf("#") + 1, strLatLng.length())));

        LatLng latLng = new LatLng(lData.getLatitude(), lData.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
        mMap.addMarker(markerOptions);

    }


}
