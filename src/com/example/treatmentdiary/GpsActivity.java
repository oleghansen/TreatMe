package com.example.treatmentdiary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class GpsActivity extends Activity implements LocationListener{
		LocationManager locationManager;
		Location location;
		String provider;
		double lo, lat;
		WebView webView;
		@Override
		protected void onCreate(Bundle savedInstanceState) 
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.webview);
			 
			webView = (WebView) findViewById(R.id.webView1);
			webView.getSettings().setJavaScriptEnabled(true);
			webView.getSettings().setDomStorageEnabled(true);
			
			
			locationManager= (LocationManager)getSystemService(Context.LOCATION_SERVICE);
			location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, this);
			oppdaterLokasjon(location);
			

			webView.loadUrl("https://www.google.no/maps/search/Pharmacy/@" + lo + "," + lat + ",15z?hl=no/");
			webView.setWebViewClient(new WebViewClient() {
	            @Override
	            public void onReceivedError(WebView view, int errorCode,
	                String description, String failingUrl) {
	                view.loadUrl("about:blank");
	                Toast.makeText(getApplicationContext(), "Opening browser..", Toast.LENGTH_SHORT).show();
	                openBrowserMap();
	                super.onReceivedError(view, errorCode, description, failingUrl);
	            }
	        });
			
		}
		
		private void openBrowserMap()
		{
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.no/maps/search/Pharmacy/@" + lo + "," + lat + ",15z?hl=no"));
			startActivity(browserIntent);
		}
		
		@Override
		public void onPause(){
		super.onPause();
		locationManager.removeUpdates(this);
		}
		
		@Override
		public void onResume(){
		super.onResume();
		}
		
		public void oppdaterLokasjon(Location location){
			if (location != null){
			lo=location.getLongitude();
			lat=location.getLatitude();
			String latlongtekst="Longitude: " + lo + "\n" + "Latitude:" + lat;
			System.out.println(latlongtekst);
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