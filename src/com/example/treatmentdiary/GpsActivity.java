package com.example.treatmentdiary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class GpsActivity extends Activity implements LocationListener{
		LocationManager locationManager;
		Location location;
		String provider;
		double lo, lat;
		private Typeface customFont;
		private ImageButton barBackButton, barExitButton;
		WebView webView;
		@Override
		protected void onCreate(Bundle savedInstanceState) 
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.webview);
			customFont = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeueLTW1G-Lt.otf");
			 
			webView = (WebView) findViewById(R.id.webView1);
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
	                setContentView(R.layout.activity_main);
	                Toast.makeText(getApplicationContext(), "Opening browser..", Toast.LENGTH_SHORT).show();
	                openBrowserMap();
	                super.onReceivedError(view, errorCode, description, failingUrl);
	            }
	        });
			getCustomActionBar();
			
		}
		
		private void getCustomActionBar()
		{
			getActionBar().setCustomView(R.layout.custom_actionbar);
			getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			
			TextView actionTitle = (TextView)findViewById(R.id.actionBarTitle);
			actionTitle.setTypeface(customFont);
			
			barBackButton = (ImageButton)findViewById(R.id.actionBackButton);
			barBackButton.setVisibility(View.VISIBLE);
			barBackButton.setOnClickListener(onClickListener);
			
			barExitButton = (ImageButton)findViewById(R.id.actionExitButton);
			barExitButton.setVisibility(View.VISIBLE);
			barExitButton.setOnClickListener(onClickListener);
		}
		
		private OnClickListener onClickListener = new OnClickListener() {
			 @Override
		     public void onClick(View v) 
		     {
		         switch(v.getId()){
		             case R.id.actionBackButton:
		            		  finish();
		             break;
		             case R.id.actionExitButton:
		     				setResult(99);
		     				finish();
		     		 break;

		         }
		     }
		};

		
		private void openBrowserMap()
		{
			if(lo!=0.0 && lat!=0.0)
			{
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.no/maps/search/Pharmacy/@" + lo + "," + lat + ",15z?hl=no"));
				startActivity(browserIntent);
			}
			else
			{
				 Toast.makeText(getApplicationContext(), "This device does not support location providers. Showing Pharmacies closest to Pilestredet 35", Toast.LENGTH_LONG).show();
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.no/maps/search/Pharmacy/@59.919537,10.735061,15z?hl=no"));
				startActivity(browserIntent);
			}

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
			String latlongtekst="Plassering motatt! Longitude: " + lo + "\n" + "Latitude:" + lat;
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