package com.example.treatmentdiary;



import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {
	public final static int CURRENTTREATMENTSRESULT = 1, USEDTREATMENTSRESULT = 2;
	private ImageButton barBackButton, usedTreatmentsButton, treatmentsButton;
	private DbHandlerTreatments db;
	private DbHandlerDiary dbDiary;
	private Typeface customFont;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		db = new DbHandlerTreatments(this);
		dbDiary = new DbHandlerDiary(this);
		customFont = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeueLTW1G-Lt.otf");
		
		if(db.findAllTreatments().isEmpty())
		{
			
			db.addTreatment(new Treatment("Basiron (eksempel)", "Acne", "01-01-2014", "2 Week(s)"));
			db.addTreatment(new Treatment("Paracet", "Hodepine", "30-12-2012", "33 Days"));
			db.addTreatment(new Treatment("Grønn te", "Humør", "30-10-2010", "1 Year(s)"));
		}
		
		Treatment hei = db.findTreatment(1);

		if(dbDiary.findDiaryNotes(hei).isEmpty())
		{
			System.out.println("TOM DIARY. LEGGER INN...");
			Random rn = new Random();
			int min = 0; int max = 5;
			for(int i = 1; i < 30; i++)
			{
				dbDiary.addDiary(new Diary(i + "-1-2014","Dag " + i, "Dette ser ut til å fungere. Jeg har fått litt tørt ansikt av kremen, men kompenserer med fuktighetskrem. Får se hvordan det går videre.", "Noon", String.valueOf(rn.nextInt(max-min +1) + min)), hei);
			}
		}

		treatmentsButton = (ImageButton)findViewById(R.id.currentTreatmentsButton);
		usedTreatmentsButton = (ImageButton)findViewById(R.id.usedTreatmentsButton);
		
		treatmentsButton.setOnClickListener(onClickListener);
		usedTreatmentsButton.setOnClickListener(onClickListener);
		getCustomActionBar();
		 
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		barBackButton.setVisibility(View.INVISIBLE);
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
	

}
