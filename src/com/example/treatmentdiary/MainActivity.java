package com.example.treatmentdiary;




import java.util.List;
import java.util.Random;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;


public class MainActivity extends Activity {
	static final double LatitudeOSLO=59.87894;
	static final double LangitudeOSLO=10.78142;
	
	public final static int CURRENTTREATMENTSRESULT = 1, USEDTREATMENTSRESULT = 2, AVSLUTTAPPRESULT = 99;
	public static int old;
	private ImageButton barBackButton, usedTreatmentsButton, treatmentsButton, findPharmacyButton, prefsButton;
	private DbHandlerTreatments db;
	private DbHandlerDiary dbDiary;
	private Typeface customFont;
	private TextView todayText;
	private AlertDialog.Builder dialogBuilder;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		db = new DbHandlerTreatments(this);
		dbDiary = new DbHandlerDiary(this);
		customFont = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeueLTW1G-Lt.otf");
		
		if(db.findAllTreatments().isEmpty())
		{
			emptyListDialog();
		}
		
		treatmentsButton = (ImageButton)findViewById(R.id.currentTreatmentsButton);
		usedTreatmentsButton = (ImageButton)findViewById(R.id.usedTreatmentsButton);
		findPharmacyButton = (ImageButton)findViewById(R.id.findPharmacyButton);
		prefsButton = (ImageButton)findViewById(R.id.settingsButton);
		
		prefsButton.setOnClickListener(onClickListener);
		treatmentsButton.setOnClickListener(onClickListener);
		usedTreatmentsButton.setOnClickListener(onClickListener);
		findPharmacyButton.setOnClickListener(onClickListener);
		todayText = (TextView)findViewById(R.id.todayTV);
		todayText.setVisibility(View.INVISIBLE);
		getCustomActionBar();
		
		if(!isNoteToday())
		{
			todayText.setVisibility(View.VISIBLE);
			todayText.setTypeface(customFont);
		}
	}
	
	private void emptyListDialog()
	{
			dialogBuilder = new AlertDialog.Builder(this);
			dialogBuilder.setTitle(getString(R.string.empty));
			dialogBuilder.setMessage(getString(R.string.emptyliststring));
			dialogBuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					db.addTreatment(new Treatment("Basiron (test)", "Acne", "01-01-2014", "2 Week(s)", 0, 0));
					Treatment hei = db.findTreatment(1);

					if(dbDiary.findDiaryNotes(hei).isEmpty())
					{
						String[] genArray = {"Dette ser ut til å fungere. Jeg har fått litt tørt ansikt av kremen, men kompenserer med fuktighetskrem. Får se hvordan det går videre.",
											 "Ingen store forandringer i dag. Føler meg OK",
											 "Har merket en liten bedring siden i går, men huden er litt irritert og rød.",
											 "Det har faktisk blitt værre nå. Kan skylde mye stress, jeg fortsette behandlingen på vanlig måte",
											 "Bedre enn på lenge. Virker som om kremen fungerer bedre hvis man smører den veldig godt inn i huden"};
						
						Random rn = new Random();
						int min = 1; int max = 5;
						for(int i = 1; i < 30; i++)
						{
							dbDiary.addDiary(new Diary("Dag " + i,i + "-1-2014", genArray[rn.nextInt(genArray.length)], "Noon", String.valueOf(rn.nextInt(max-min +1) + min)), hei);
						}
					}
				}
			});
			
			dialogBuilder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					
					dialog.dismiss();
				}
			});
			AlertDialog alert = dialogBuilder.create();
			alert.show();
		}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == AVSLUTTAPPRESULT)
		{
			finish();
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!isNoteToday())
		{
			todayText.setVisibility(View.VISIBLE);
			todayText.setTypeface(customFont);
		}
		else
		{
			todayText.setVisibility(View.INVISIBLE);
		}
		barBackButton.setVisibility(View.INVISIBLE);
	}
	
	private boolean isNoteToday()
	{
		
		List<Treatment> allTreatments = db.findAllTreatments();
		for(Treatment item : allTreatments)
		{
			if(dbDiary.isToday(item))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		return false;
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
	            	 finish();
	            	 Intent gpsactivity = new Intent("com.example.treatmentdiary.GPSACTIVITY");
	            	  startActivity(gpsactivity);
	            	 	
	             break;
	             case R.id.settingsButton:
	            	 Intent prefs = new Intent("com.example.treatmentdiary.PREFS");
	            	 startActivity(prefs);
	         }
	     }
	};
	

}
