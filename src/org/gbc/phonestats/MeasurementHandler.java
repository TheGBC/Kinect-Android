package org.gbc.phonestats;

import android.app.Activity;

public interface MeasurementHandler {
  
  /**
   * Starts reading measurements
   * 
   * @param activity activity to get services from
   */
  public void start(Activity activity);
  
  /**
   * Returns the most recent measurement
   */
  public String getMostRecentMeasurement();
}
