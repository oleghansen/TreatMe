package com.example.treatmentdiary;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {
	static final double LatitudeOSLO=59.87894;
	static final double LangitudeOSLO=10.78142;
	
	public final static int CURRENTTREATMENTSRESULT = 1, USEDTREATMENTSRESULT = 2;
	public static int old;
	private ImageButton barBackButton, usedTreatmentsButton, treatmentsButton, findPharmacyButton;
	private DbHandlerTreatments db;
	private DbHandlerDiary dbDiary;
	private Typeface customFont;
	private double latitude, longitude;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		db = new DbHandlerTreatments(this);
		dbDiary = new DbHandlerDiary(this);
		customFont = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeueLTW1G-Lt.otf");
		
		if(db.findAllTreatments().isEmpty())
		{
			
			db.addTreatment(new Treatment("Basiron (eksempel)", "Acne", "01-01-2014", "2 Week(s)", 0, 0));
			db.addTreatment(new Treatment("Paracet", "Hodepine", "30-12-2012", "33 Days", 0, 0));
			db.addTreatment(new Treatment("Grønn te", "Humør", "30-10-2010", "1 Year(s)", 1, 4));
		}
		
		Treatment hei = db.findTreatment(1);

		if(dbDiary.findDiaryNotes(hei).isEmpty())
		{
			System.out.println("TOM DIARY. LEGGER INN...");
			Random rn = new Random();
			int min = 0; int max = 5;
			for(int i = 1; i < 30; i++)
			{
				dbDiary.addDiary(new Diary("Dag " + i, i + "-1-2014", "Dette ser ut til å fungere. Jeg har fått litt tørt ansikt av kremen, men kompenserer med fuktighetskrem. Får se hvordan det går videre.", "Noon", String.valueOf(rn.nextInt(max-min +1) + min)), hei);
			}
		}

		treatmentsButton = (ImageButton)findViewById(R.id.currentTreatmentsButton);
		usedTreatmentsButton = (ImageButton)findViewById(R.id.usedTreatmentsButton);
		findPharmacyButton = (ImageButton)findViewById(R.id.findPharmacyButton);
		
		treatmentsButton.setOnClickListener(onClickListener);
		usedTreatmentsButton.setOnClickListener(onClickListener);
		findPharmacyButton.setOnClickListener(onClickListener);
		getCustomActionBar();
		 
		
	}
	
	private void getLatitudeLongitude()
	{
		    LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE); 
		    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		    
	
		        showMyAddress(location);
		
		    
		    final LocationListener locationListener = new LocationListener() {
		    	@Override
		        public void onLocationChanged(Location location) {
		        	showMyAddress(location);
		        }

		        public void onProviderDisabled(String arg0) {
		            // TODO Auto-generated method stub

		        }

		        public void onProviderEnabled(String arg0) {
		            // TODO Auto-generated method stub

		        }

		        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		            // TODO Auto-generated method stub

		        }
		    };

		    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

	}
	
	private void showMyAddress(Location location) {
	    double latitude = location.getLatitude();
	    double longitude = location.getLongitude();
	    Geocoder myLocation = new Geocoder(getApplicationContext(), Locale.getDefault());   
	    
	    List<Address> latlongList = new ArrayList<Address>();
	    try {
	        latlongList = myLocation.getFromLocation(latitude, longitude, 1);
	        System.out.println(latlongList.get(0).toString());

	    } catch (IOException e1) {
	        // TODO Auto-generated catch block
	    	System.out.println("IOException");
	        e1.printStackTrace();
	    }
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		barBackButton.setVisibility(View.INVISIBLE);
	}
	
	private void findPharmacy()
	{
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.no/maps?q=Pharmacy&hl=no"));
		startActivity(browserIntent);
	}

	private void getCustomActionBar()
	{
		getActionBar().setCustomView(R.layout.custom_actionbar);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		
		TextView actionTitle = (TextView)findViewById(R.id.actionBarTitle);
		actionTitle.setTypeface(customFont);
		barBackButton = (ImageButton)findViewById(R.id.actionBackButton);
		barBackButton.setVisibility(View.INVISIBLE);
		
	}
	 	
	private OnClickListener onClickListener = new OnClickListener() {
		 @Override
	     public void onClick(View v) 
	     {
	         switch(v.getId()){
	             case R.id.currentTreatmentsButton:
	            	 	old = 1;
						Intent showTreatments = new Intent("com.example.treatmentdiary.TREATMENTLIST");
						startActivityForResult(showTreatments, CURRENTTREATMENTSRESULT);
						
	             break;
	             case R.id.usedTreatmentsButton:
	            	 	old = 2;
	            	 	Intent showOldTreatments = new Intent("com.example.treatmentdiary.TREATMENTLIST");
	            	 	startActivityForResult(showOldTreatments, USEDTREATMENTSRESULT);
	            	 	
	             break;
	             case R.id.findPharmacyButton:
	            	 getLatitudeLongitude();
	             break;
	         }
	     }
	};
	

}
