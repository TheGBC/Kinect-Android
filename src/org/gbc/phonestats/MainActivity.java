package org.gbc.phonestats;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;


public class MainActivity extends Activity {
  private LocationHandler locationHandler = LocationHandler.getInstance();
  private OrientationHandler orientationHandler = OrientationHandler.getInstance();
	
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
		
    // Start measurment handlers
    locationHandler.start(this);
    orientationHandler.start(this);
		
    // Run the Socket Server on a background thread
    (new Thread(
        new Runnable(){
          @Override
          public void run(){
            try{
					  
              // Create a server socket listening on port 8080
              ServerSocket welcomeSocket = new ServerSocket(8080);
              while(true){
						  
                // Take in one socket at a time
                Socket connectionSocket = welcomeSocket.accept();
							
                // Read in the input from a client
                // Once we've read a line of input, parse it
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                String res = inFromClient.readLine();

                // If we are getting the location
                if(res.trim().equals("get-location")){
                  DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                  outToClient.writeBytes(locationHandler.getMostRecentMeasurement() + '\n');
								
								
                // If we are getting the orientation
                }else if(res.trim().equals("get-orientation") || (res.length() > 0 && res.substring(1).trim().equals("get-orientation"))){
                  DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                  System.out.println(orientationHandler.getMostRecentMeasurement());
                  outToClient.writeBytes(orientationHandler.getMostRecentMeasurement() + '\n');
							  
                // Unknown command returns empty string
                }else{
                  DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                  outToClient.writeBytes("\n");
                }
              }
            }catch(IOException e){
              e.printStackTrace();
            }
          }
        }
    )).start();
  }
	
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
  }
}
