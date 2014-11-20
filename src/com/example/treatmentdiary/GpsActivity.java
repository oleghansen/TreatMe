package com.example.treatmentdiary;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class GpsActivity extends Activity implements LocationListener{
		LocationManager locationManager;
		Location location;
		String provider;
		@Override
		protected void onCreate(Bundle savedInstanceState) 
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_gps);
			
			locationManager= (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		    Criteria criteria = new Criteria();
		    criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		    String provider = locationManager.getBestProvider(criteria, true);
		    
			location=locationManager.getLastKnownLocation(provider);
			
			locationManager.requestLocationUpdates(provider,0,0, this);
			oppdaterLokasjon(location);
		}
		
		@Override
		public void onPause(){
		super.onPause();
		locationManager.removeUpdates(this);
		}
		
		@Override
		public void onResume(){
		super.onResume();
		locationManager.requestLocationUpdates(provider,2000,10, this);
		}
		
		public void oppdaterLokasjon(Location location){
			if (location != null){
			TextView tw=(TextView)findViewById(R.id.lokasjonstekst);
			double lo=location.getLongitude();
			double lat=location.getLatitude();
			String latlongtekst="Longitude: " + lo + "\n" + "Latitude:" + lat;
			tw.setText(latlongtekst);
			}
		}
		

			@Override
			public void onLocationChanged(Location location) {
				oppdaterLokasjon(location);

				
			}
			@Override
			public void onProviderDisabled(String provider) {
				oppdaterLokasjon(null);

				
			}
			@Override
			public void onProviderEnabled(String provider) {
				oppdaterLokasjon(location);

				
			}
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				oppdaterLokasjon(location);

			}
				
}