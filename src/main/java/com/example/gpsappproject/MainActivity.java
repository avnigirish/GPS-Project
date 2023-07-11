package com.example.gpsappproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView display;
    TextView address;
    TextView distance;
    TextView time;
    Geocoder geocoder;
    List<Address> addresses;
    boolean isGPSEnabled;
    double latitude1;
    double longitude1;
    double latitude2;
    double longitude2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display = findViewById(R.id.display);
        address = findViewById(R.id.address);
        distance = findViewById(R.id.distance);
        time = findViewById(R.id.time);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
        latitude2 = 40.37421236948826;
        longitude2 = -74.56404355982876;
        Location loc = gpsTracker.getLocation();
        gpsTracker.onLocationChanged(loc);
    }

    public class GPSTracker implements LocationListener{
        Context context;
        public GPSTracker(Context context){
            super();
            this.context = context;
        }
        @Override
        public void onLocationChanged(@NonNull Location location) {
            Log.d("TAG", "onLocationChanged");
            latitude1 = location.getLatitude();
            longitude1 = location.getLongitude();
            display.setText("Latitude: " + latitude1+ "\nLongitude: " + longitude1);
            geocoder = new Geocoder(context, Locale.US);
            /*try {
                addresses = geocoder.getFromLocation(latitude1, longitude1, 1);
                address.setText("Address: "+addresses.get(0).getAddressLine(0));
            } catch (IOException e) {
                e.printStackTrace();
            }*/

                /*float[] results = new float[1];
                location.distanceBetween(latitude1, longitude1,latitude2, longitude2, results);
                float meters = results[0];
                double miles = meters*0.00062137119224;
                DecimalFormat mi = new DecimalFormat("#.##");
                distance.setText("Distance from my location to SBHS(miles): " + mi.format(miles) + " mi");*/
            }
        public Location getLocation(){
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                Log.d("TAG", "Denied");
                return null;
            }
            try{
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if(isGPSEnabled){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000,10, this);
                    Location loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    isGPSEnabled=false;
                    return loc;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.d("TAG", "permission granted");
        }
        else{
            Log.d("TAG", "permission denied");
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }
    }
}
