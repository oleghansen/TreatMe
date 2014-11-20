package com.example.treatmentdiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Prefs extends PreferenceActivity {
	private AlertDialog.Builder dialogBuilder;
	private DbHandlerTreatments db;
	private DbHandlerDiary dbDiary;
	private Typeface customFont;
	private ImageButton barBackButton, barExitButton;
	Preference deletePref;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
		db = new DbHandlerTreatments(this);
		dbDiary = new DbHandlerDiary(this);
		customFont = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeueLTW1G-Lt.otf");
		
		getCustomActionBar();
		
		deletePref = (Preference) findPreference("deleteKey");
		deletePref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                deleteDatabaseDialog();
				return true;
            }
        });
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
	
	private void deleteDatabaseDialog()
	{
		String clickedString = ("Are you sure you want to reset your treatment database? All treatments and treatment history will be deleted.");
		
		dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setCancelable(true);
		dialogBuilder.setTitle("Reset database");
		dialogBuilder.setMessage(clickedString);
		
		dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
			     dialog.dismiss();
			}
		});
		
		dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dbDiary.resetDatabase();
				db.resetDatabase();
				Toast.makeText(getBaseContext(), "Database reset.", Toast.LENGTH_LONG).show();
			}
		});
		
		AlertDialog alert = dialogBuilder.create();
		alert.show();

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
}
