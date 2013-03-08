package org.gbc.phonestats;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class OrientationHandler implements MeasurementHandler, SensorEventListener{
  private String orientationData = "";
  private float[] gravity = new float[3];
  private static OrientationHandler instance;
  
  private OrientationHandler() { }
  
  @Override
  public void start(Activity activity){
    SensorManager sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
    Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
  }
  
  @Override
  public String getMostRecentMeasurement(){
    return orientationData;
  }
  
  @Override
  public void onSensorChanged(SensorEvent event) {
    // alpha is calculated as t / (t + dT)
    // with t, the low-pass filter's time-constant
    // and dT, the event delivery rate

    final float alpha = 0.8f;

    gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
    gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
    gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
    
    orientationData = gravity[0] + ":" + gravity[1] + ":" + gravity[2];
  }
  
  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) { }
  
  public static OrientationHandler getInstance(){
    if(instance == null){
      instance = new OrientationHandler();
    }
    return instance;
  }
}
