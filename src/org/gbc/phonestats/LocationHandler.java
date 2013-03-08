package org.gbc.phonestats;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Handles listening and returning Location
 * 
 * @author yuhao93
 */
public class LocationHandler implements MeasurementHandler, LocationListener{
  private String locationData = "";
  private static LocationHandler instance;
  
  private LocationHandler(){ }
  
  @Override
  public void start(Activity activity){
    // Acquire a reference to the system Location Manager
    LocationManager locationManager = (LocationManager) activity.getSystemService(Activity.LOCATION_SERVICE);
    
    // Register the listener with the Location Manager to receive location updates
    Location l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    locationData = l.getLatitude() + ":" + l.getLongitude();
    
    // Start Listening
    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, this);
    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 0, this);
  }
  
  @Override
  public String getMostRecentMeasurement(){
    return locationData;
  }

  @Override
  public void onLocationChanged(Location location) {
    locationData = location.getLatitude() + ":" + location.getLongitude();
  }

  @Override
  public void onProviderDisabled(String provider) { }
  @Override
  public void onProviderEnabled(String provider) { }
  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) { }
  
  public static LocationHandler getInstance(){
    if(instance == null){
      instance = new LocationHandler();
    }
    return instance;
  }
}
