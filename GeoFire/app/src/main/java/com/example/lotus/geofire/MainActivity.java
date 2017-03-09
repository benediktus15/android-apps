package com.example.lotus.geofire;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference();

    private GoogleMap googleMap;
    LocationManager locationManager;

    List<Double> arrayLatitude = new ArrayList<>();
    List<Double> arrayLongitude = new ArrayList<>();
    List<String> arrayName = new ArrayList<>();

    public Double latitudeUser = 0.0;
    public Double longitudeUser = 0.0;
    public Double latitudeSekitar = 0.0;
    public Double longitudeSekitar = 0.0;
    public Double jarakTempuh = 0.0;
    public String namaAccount = "Alvin";
    Location locationUser = new Location ("User");
    Location locationSekitar = new Location ("Sekitar");

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Query mQuery;
    private ArrayList<User> mAdapterItems;
    private ArrayList<String> mAdapterKeys;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_recview);

        //writeNewUser("Orange", 24, 12.32, 13.20);
        checkLocationPermission();

        myRef.child("user").child(namaAccount).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User akun = dataSnapshot.getValue(User.class);
                //locationUser.setLatitude(akun.getLatitude());
                //locationUser.setLongitude(akun.getLongitude());
                latitudeUser = akun.getLatitude();
                longitudeUser = akun.getLongitude();
                //Log.d("TAG", "Lokasi: " + user.getLatitude() + " ; " + user.getLongitude());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.child("user").orderByChild("latitude").addChildEventListener(new ChildEventListener(){
        //myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                //String value = dataSnapshot.getValue(User.class);
                User user = dataSnapshot.getValue(User.class);
                //locationSekitar.setLatitude(user.getLatitude());
                //locationSekitar.setLongitude(user.getLongitude());
                //Log.d("TAG", "Lokasi User: " + latitudeUser + " ; " + longitudeUser);
                //Log.d("TAG", "Lokasi Database: " + locationSekitar);
                Log.d("TAG", "Masuk childhood");

                jarakTempuh = distance(latitudeUser, longitudeUser, user.getLatitude(), user.getLongitude());

                Log.d("TAG", "Status: " + user.getStatus());

                if (jarakTempuh < 50 && jarakTempuh != 0.0 && user.getStatus().equals("help")) {
                    latitudeSekitar = user.getLatitude();
                    longitudeSekitar = user.getLongitude();
                    //Log.d("TAG", "Lokasi User: " + latitudeUser + " ; " + longitudeUser);
                    //Log.d("TAG", "Lokasi Database: " + user.getLatitude() + " ; " + user.getLongitude());
                    Log.d("TAG", user.getName() + " ada di dekatmu! minta bantuan dari dia");
                    Log.d("TAGI", "Latitude:" + latitudeUser + " Longitude:" + longitudeUser);
                    Log.d("TAGI", "LatitudeSekitar:" + latitudeSekitar + " LongitudeSekitar:" + longitudeSekitar);
                    Log.d("TAG", "Distance: " + jarakTempuh);

                    arrayLatitude.add(latitudeSekitar);
                    arrayLongitude.add(longitudeSekitar);
                    arrayName.add(user.getName());

                    //Log.d("TAG", arrayLatitude.toString());
                    //Log.d("TAG", arrayLongitude.toString());

                    //userList.addAll(User.class);

                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            Log.d("TAGI", "masuk");

                            googleMap.getUiSettings().setZoomControlsEnabled(true); // true to enable
                            googleMap.getUiSettings().setZoomGesturesEnabled(true);

                            for(int i=0;i<arrayLatitude.size();i++)
                            {
                                Log.d("TAG", "marker: " + arrayLatitude.get(i).toString() + " " + arrayLongitude.get(i).toString());
                                //Log.d("TAG", arrayLongitude.get(i).toString());

                                MarkerOptions marker = new MarkerOptions().position(new LatLng(arrayLatitude.get(i), arrayLongitude.get(i))).title(arrayName.get(i));
                                googleMap.addMarker(marker);

                            }


                            /*CameraPosition cameraPosition = new CameraPosition.Builder().target(
                                    new LatLng(arrayLatitude.get(0), arrayLongitude.get(0))).zoom(8).build();

                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

                    /*locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, mLocationListener);
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (locationManager != null) {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    Log.i("TAGI", "GPS dinyalakan");*/
                        }
                    });
                    // check if map is created successfully or not
                    if (googleMap == null) {
                        Toast.makeText(getApplicationContext(),
                                "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                                .show();
                    }
                }


/*                try {
                    initializeMap();

                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}

            /*@Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }*/
        });
    }


    private void initializeMap() {
        if (googleMap == null) {
            /*SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    Log.d("TAGI", "masuk");
                    Log.d("TAGI", "Latitude:" + latitudeUser + " Longitude:" + longitudeUser);
                    Log.d("TAGI", "LatitudeSekitar:" + latitudeSekitar + " LongitudeSekitar:" + longitudeSekitar);

                    googleMap.getUiSettings().setZoomControlsEnabled(true); // true to enable
                    googleMap.getUiSettings().setZoomGesturesEnabled(true);

                    MarkerOptions marker = new MarkerOptions().position(new LatLng(latitudeSekitar, longitudeSekitar)).title("Kamu Disini");

                    // adding marker
                    googleMap.addMarker(marker);

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(
                            new LatLng(latitudeSekitar, longitudeSekitar)).zoom(8).build();

                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, mLocationListener);
                    locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (locationManager != null) {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    Log.i("TAGI", "GPS dinyalakan");
                }
            });
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }*/

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, mLocationListener);
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (locationManager != null) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            Log.i("TAGI", "GPS dinyalakan");
        }
        if (googleMap == null) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("TAGI", "masuk");
            Log.d("TAGI", "Latitude:" + location.getLatitude() + " Longitude:" + location.getLongitude());
            //textView.setText("Latitude:" + location.getLatitude() + " Longitude:" + location.getLongitude());
            //Toast.makeText(MainActivity.this, "Latitude:" + location.getLatitude() + "\nLongitude:" + location.getLongitude(), Toast.LENGTH_LONG).show();

            //MarkerOptions marker = new MarkerOptions().position(new LatLng(latitudeUser, longitudeUser)).title("Kamu Disini");
            /*MarkerOptions marker = new MarkerOptions().position(new LatLng(latitudeUser, longitudeUser)).title("Kamu Disini");

            // adding marker
            googleMap.addMarker(marker);*/

/*
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(latitudeUser, longitudeUser)).zoom(11).build();

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
*/
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("Latitude", "disable");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("Latitude", "enable");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("Latitude", "status");
        }
    };

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeMap();
    }

    private void writeNewUser(String name, int age, double latitude, double longitude) {
        User user = new User(age, latitude, longitude);

        myRef.child("user").child(name).setValue(user);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist*1.60934);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}