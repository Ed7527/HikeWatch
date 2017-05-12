package com.edwardtorpy.hikewatch;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                String address = "";

                try {
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                    if (addressList != null && addressList.size() > 0) {

                        if (addressList.get(0).getAddressLine(0) != null) {
                            address += "\n" + addressList.get(0).getAddressLine(0);
                        }
                        if (addressList.get(0).getAddressLine(1) != null) {
                            address += "\n" + addressList.get(0).getAddressLine(1);
                        }
                        if (addressList.get(0).getAddressLine(2) != null) {
                            address += "\n" + addressList.get(0).getAddressLine(2);
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                TextView longitudeText = (TextView) findViewById(R.id.longitudeTextView);
                TextView latitudeText = (TextView) findViewById(R.id.latitudeTextView);
                TextView accuracyText = (TextView) findViewById(R.id.accuracyTextView);
                TextView altitudeText = (TextView) findViewById(R.id.altitudeTextView);
                TextView addressText = (TextView) findViewById(R.id.addresstextView);

                String longitudeString = String.format("Longitude: %.4f", location.getLongitude());
                String latitudeString = String.format("Latitude: %.4f", location.getLatitude());
                String accuracyString = String.format("Accuracy: %.2f", location.getAccuracy());
                String altitudeString = String.format("Altitude: %.0f", location.getAltitude());
                String addressString = "Address: \n" + address;

                longitudeText.setText(longitudeString);
                latitudeText.setText(latitudeString);
                accuracyText.setText(accuracyString);
                altitudeText.setText(altitudeString);
                addressText.setText(addressString);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,locationListener);

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }
        }
    }
}
