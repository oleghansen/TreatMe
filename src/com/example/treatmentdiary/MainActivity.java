package com.example.treatmentdiary;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	public final static int CURRENTTREATMENTSRESULT = 1, USEDTREATMENTSRESULT = 2;
	private Button treatmentsButton, usedTreatmentsButton;
	private DbHandlerTreatments db;
	private DbHandlerDiary dbDiary;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		db = new DbHandlerTreatments(this);
		dbDiary = new DbHandlerDiary(this);
		
		if(db.findAllTreatments().isEmpty())
		{
			Treatment hei = new Treatment("Hodepine", "Paracet", "30-12-1992", "To uker");
			db.addTreatment(hei);
			dbDiary.addDiary(new Diary("Dag1", "Paracet", "30-12-1992", "To uker", "hei", 5), hei);
			dbDiary.addDiary(new Diary("Dag2", "Paracet", "30-12-1992", "To uker", "hei", 5), hei);
			dbDiary.addDiary(new Diary("Dag3", "Paracet", "30-12-1992", "To uker", "hei", 5), hei);
			dbDiary.addDiary(new Diary("Dag4", "Paracet", "30-12-1992", "To uker", "hei", 5), hei);
			dbDiary.addDiary(new Diary("Dag5", "Paracet", "30-12-1992", "To uker", "hei", 5), hei);
			db.addTreatment(new Treatment("HodeVondt", "Paracet", "30-12-1992", "To uker"));
			db.addTreatment(new Treatment("Hodedritt", "Paracet", "30-12-1992", "To uker"));
			db.addTreatment(new Treatment("Hodepikk", "Paracet", "30-12-1992", "To uker"));
			db.addTreatment(new Treatment("HodeMorn", "Paracet", "30-12-1992", "To uker"));
		}

		
		treatmentsButton = (Button)findViewById(R.id.currentTreatmentsButton);
		usedTreatmentsButton = (Button)findViewById(R.id.usedTreatmentsButton);
		
		treatmentsButton.setOnClickListener(onClickListener);
		usedTreatmentsButton.setOnClickListener(onClickListener);
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		 @Override
	     public void onClick(View v) 
	     {
	         switch(v.getId()){
	             case R.id.currentTreatmentsButton:
						Intent showCurrentTreatments = new Intent("com.example.treatmentdiary.TREATMENTLIST");
						startActivityForResult(showCurrentTreatments, CURRENTTREATMENTSRESULT);
	             break;
	             case R.id.usedTreatmentsButton:
	            	 	Intent showUsedTreatments = new Intent("com.example.treatmentdiary.USEDTREATMENTLIST");
	            	 	startActivityForResult(showUsedTreatments, USEDTREATMENTSRESULT);
	             break;
	         }
	     }
	};
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
