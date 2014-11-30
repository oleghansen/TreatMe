package com.example.treatmentdiary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;




public class TreatmentList extends Activity
{
	private AlertDialog.Builder dialogBuilder;
	private ListView list, listDiary;
	private List<Treatment> treatments;
	private List<Diary> notes;
	private DbHandlerTreatments db;
	private DbHandlerDiary dbDiary;
	private Treatment selectedTreatment;
	private Diary selectedNote;
	static final int dialog_id=1;
	private int yr, day, month;
	private boolean inOld;
	
	private int layout_position; 

	private Typeface customFont;
	private TextView titleTV;
	private ImageButton deleteButton, manageButton, addButton, manageButtonTreatments, addButtonTreatments;
	private Button buttonRate;
	
	/* Note details */
	private TextView todTv, dateTv, dateTreatmentTV, durationTreatmentTV, optionalTreatmentTV, textRateHelp, textRateName;
	private EditText dateET, titleET, textET, durationNumberTreatmentET, nameET, methodET;
	private Spinner todSpinner, durSpinner;
	private RatingBar ratingBar, ratingBarRate;
	private ImageButton barBackButton, barExitButton;
	
	private boolean nyDiary, nyTreatment, update;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		customFont = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeueLTW1G-Lt.otf");
		
		db = new DbHandlerTreatments(this);
		dbDiary = new DbHandlerDiary(this);
		
		if(MainActivity.old == 1)
		{
			loadTreatmentList();
		}
		else if(MainActivity.old == 2)
		{
			loadOldTreatmentList();
		}
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

	private void registerClickCallback() {
		list = (ListView)findViewById(R.id.treatmentsListView);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id)
			{
				Treatment clickedTreatment = treatments.get(position);
				selectedTreatment = clickedTreatment;
				filldiaryList(clickedTreatment);
				loadDiaryList();
			}
		});
		
	}
	
	private void registerDiaryClickCallback() {
		listDiary = (ListView)findViewById(R.id.diaryListView);
		listDiary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id)
			{
				Diary note = notes.get(position);
				selectedNote = note;
				loadNoteDetails(note);
			}
		});
		
	}
	private void treatmentLongClickDialog(final Treatment treatment)
	{
		if(!inOld)
		{
			dialogBuilder = new AlertDialog.Builder(this);
			dialogBuilder.setTitle(treatment.getName());
			
			dialogBuilder.setPositiveButton(getString(R.string.edit), new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					loadTreatmentDetails(treatment);
				}
			});
			
			dialogBuilder.setNegativeButton(getString(R.string.delete), new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					
					deleteDialog(treatment);
				}
			});
			
			dialogBuilder.setNeutralButton(getString(R.string.endtreatment), new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					loadTreatmentRating(treatment);
				}
			});
		}
		else
		{
			dialogBuilder = new AlertDialog.Builder(this);
			dialogBuilder.setTitle(treatment.getName());
			
			dialogBuilder.setNegativeButton(getString(R.string.delete), new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					
					deleteTreatment(treatment);
					loadOldTreatmentList();
				}
			});
			
			dialogBuilder.setNeutralButton(getString(R.string.reopentreatment), new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					makeOpen(treatment);
					loadOldTreatmentList();
				}
			});
		}
		
		
		AlertDialog alert = dialogBuilder.create();
		alert.show();
		
	}
	
	private void deleteDialog(final Treatment treatment)
	{
		dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle(getString(R.string.delete) + " " + treatment.getName());
		dialogBuilder.setMessage(R.string.dialogDeleteText);
		
		dialogBuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				deleteTreatment(treatment);
				loadTreatmentList();
			}
		});
		
		dialogBuilder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				
				loadTreatmentList();
			}
		});
		
		dialogBuilder.setNeutralButton(getString(R.string.save), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				makeOld(treatment);
				loadTreatmentList();
			}
		});
		
		AlertDialog alert = dialogBuilder.create();
		alert.show();
		
	}
	
	private void fillTreatmentList()
	{
		treatments = db.findAllTreatments();
	}
	
	private void filldiaryList(Treatment treatment)
	{
		notes = dbDiary.findDiaryNotes(treatment);
	}

	private void fillOldTreatmentList()
	{
		treatments = db.findOldTreatments();
	}
	
	private void loadTreatmentList()
	{
		layout_position = 1;
		inOld = false;
		update = false;
		setContentView(R.layout.treatments);
		titleTV = (TextView)findViewById(R.id.textTitleTreatments);
		titleTV.setTypeface(customFont);
		manageButtonTreatments = (ImageButton)findViewById(R.id.menuManageButtonTreatments);
		addButtonTreatments = (ImageButton)findViewById(R.id.menuAddButtonTreatments);
		manageButtonTreatments.setOnClickListener(onClickListener);
		addButtonTreatments.setOnClickListener(onClickListener);

		
		fillTreatmentList();
		fillListView();
		registerClickCallback();
	}
	
	private void loadOldTreatmentList()
	{
		layout_position = 2;
		inOld = true;
		update = false;
		setContentView(R.layout.treatments_old);
		titleTV = (TextView)findViewById(R.id.textTitleTreatments);
		titleTV.setTypeface(customFont);
		fillOldTreatmentList();
		fillListView();
		registerClickCallback();
	}
	
	private void makeOld(Treatment treatment)
	{
		db.makeTreatmentOld(treatment);
	}
	private void makeOpen(Treatment treatment)
	{
		db.makeTreatmentOpen(treatment);
		Toast openToast = Toast.makeText(getApplicationContext(), "'"+ treatment.getName() + "'" + " " + getString(R.string.reopened), Toast.LENGTH_SHORT);
		openToast.show();
	}
	
	private void loadTreatmentDetails(Treatment treatment)
	{
			if(layout_position == 1)
			{

				layout_position = 11;
			}
			else if(layout_position == 2)
			{
;
				layout_position = 22;
			}
			
			nyTreatment = false;
			setContentView(R.layout.treatment_details);
			dateET = (EditText)findViewById(R.id.etDateTreatment);
			durationNumberTreatmentET = (EditText)findViewById(R.id.etDurationNumberTreatment);
			durSpinner = (Spinner)findViewById(R.id.spinnerDuration);
			
			addButton = (ImageButton)findViewById(R.id.menuAddButtonNewTreatment);
			addButton.setOnClickListener(onClickListener);
			
			titleTV = (TextView)findViewById(R.id.textTitle);
			dateTreatmentTV = (TextView)findViewById(R.id.tvDateTreatment);
			durationTreatmentTV = (TextView)findViewById(R.id.tvDuration);
			optionalTreatmentTV = (TextView)findViewById(R.id.tvOptional);
			nameET = (EditText)findViewById(R.id.etName);
			methodET = (EditText)findViewById(R.id.etMethod);
			
			ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.duration, android.R.layout.simple_spinner_item);
			durSpinner.setAdapter(adapter);
			durSpinner.setPopupBackgroundResource(R.drawable.spinner);
			
			/*Date handling*/
			
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			try {
				c.setTime(sdf.parse(treatment.getStarted()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			yr = c.get(Calendar.HOUR_OF_DAY);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
			
			/* -------------------------------*/

			titleTV.setTypeface(customFont);
			dateTreatmentTV.setTypeface(customFont);
			durationTreatmentTV.setTypeface(customFont);
			optionalTreatmentTV.setTypeface(customFont);
			nameET.setTypeface(customFont);
			methodET.setTypeface(customFont);
			dateET.setTypeface(customFont);
			
			titleTV.setText(selectedTreatment.getName());
			nameET.setText(selectedTreatment.getName());
			methodET.setText(selectedTreatment.getMethod());
			dateET.setText(selectedTreatment.getStarted());
			if(!(selectedTreatment.getExpectedTime() == null || selectedTreatment.getExpectedTime().isEmpty()))
			{
				int res = 0;
				for (int i=0; i < selectedTreatment.getExpectedTime().length(); i++) {
				    char ch = selectedTreatment.getExpectedTime().charAt(i);
				    if (ch < '0' || ch > '9') continue;
				    res = res * 10 + ch - '0';
				}
				
				if(selectedTreatment.getExpectedTime().contains(getString(R.string.days)))
				{
					durSpinner.setSelection(0);
				}
				else if(selectedTreatment.getExpectedTime().contains(getString(R.string.week)))
				{
					durSpinner.setSelection(1);
				}
				else if(selectedTreatment.getExpectedTime().contains(getString(R.string.month)))
				{
					durSpinner.setSelection(2);
				}
				else if(selectedTreatment.getExpectedTime().contains(getString(R.string.year)))
				{
					durSpinner.setSelection(3);
				}
				
				durationNumberTreatmentET.setText(String.valueOf(res));
			}
	}
	
	private void loadTreatmentRating(Treatment treatment)
	{
		layout_position = 111;

		setContentView(R.layout.treatment_rating);
		textRateHelp = (TextView)findViewById(R.id.textRateHelpText);
		textRateName = (TextView)findViewById(R.id.textRateTreatmentName);
		ratingBarRate = (RatingBar)findViewById(R.id.rateTreatment);
		buttonRate = (Button)findViewById(R.id.rateButton);
		buttonRate.setOnClickListener(onClickListener);
		
		textRateName.setText(treatment.getName());
		
		textRateHelp.setTypeface(customFont);
		textRateName.setTypeface(customFont);
		buttonRate.setTypeface(customFont);
	}
	
	private void rateTreatment()
	{
		db.rateTreatment(selectedTreatment, (int)ratingBarRate.getRating());
		makeOld(selectedTreatment);
		Toast rateToast = Toast.makeText(getApplicationContext(), "'"+ selectedTreatment.getName()+ "'" + " " + getString(R.string.movedToHistory), Toast.LENGTH_SHORT);
		rateToast.show();
		
	}
	
	private void loadAddTreatment()
	{
		layout_position = 11;
		nyTreatment = true;
		setContentView(R.layout.treatment_details);
		/*Date handling*/
		
		Calendar c = Calendar.getInstance();
		yr = c.get(Calendar.HOUR_OF_DAY);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		
		/* -------------------------------*/
		addButton = (ImageButton)findViewById(R.id.menuAddButtonNewTreatment);
		addButton.setOnClickListener(onClickListener);
		dateET = (EditText)findViewById(R.id.etDateTreatment);
		dateET.setOnClickListener(onClickListener);
		durationNumberTreatmentET = (EditText)findViewById(R.id.etDurationNumberTreatment);
		durSpinner = (Spinner)findViewById(R.id.spinnerDuration);
		
		titleTV = (TextView)findViewById(R.id.textTitle);
		titleTV.setText(getString(R.string.newtreatment));
		dateTreatmentTV = (TextView)findViewById(R.id.tvDateTreatment);
		durationTreatmentTV = (TextView)findViewById(R.id.tvDuration);
		optionalTreatmentTV = (TextView)findViewById(R.id.tvOptional);
		nameET = (EditText)findViewById(R.id.etName);
		methodET = (EditText)findViewById(R.id.etMethod);
		
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.duration, android.R.layout.simple_spinner_item);
		durSpinner.setAdapter(adapter);
		durSpinner.setPopupBackgroundResource(R.drawable.spinner);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String now = sdf.format(c.getTime());
		
		dateET.setText(now);
		
		titleTV.setTypeface(customFont);
		dateTreatmentTV.setTypeface(customFont);
		durationTreatmentTV.setTypeface(customFont);
		optionalTreatmentTV.setTypeface(customFont);
		nameET.setTypeface(customFont);
		methodET.setTypeface(customFont);
		dateET.setTypeface(customFont);
	}
	
	private void quitAppDialog()
	{
		dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setCancelable(false);
		dialogBuilder.setTitle(getString(R.string.quit) + "?");
		dialogBuilder.setMessage(getString(R.string.dialogQuitText));
		
		dialogBuilder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
 				setResult(99);
 				finish();
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
	private boolean addTreatmentValidation()
	{
		int validatedFields = 0;
		
		//Validate form//
		//***************************
		//Treatment/Medicine-field//
				if(2 < methodET.getText().toString().length() &&  methodET.getText().toString().length() < 20
						){
					methodET.setBackgroundColor(Color.parseColor("#ffffff"));
					validatedFields++;
				}
				else
				{
					Toast.makeText(getApplicationContext(), getString(R.string.validationfailed), Toast.LENGTH_LONG).show();
					methodET.setBackgroundColor(Color.parseColor("#ffa9a9"));
				}
			
			//***************************
			//Condition-field//
			if(2 < nameET.getText().toString().length() &&  nameET.getText().toString().length() < 20)
				{
					nameET.setBackgroundColor(Color.parseColor("#ffffff"));
					validatedFields++;
				}
				else
				{
					Toast.makeText(getApplicationContext(),  getString(R.string.validationfailed), Toast.LENGTH_LONG).show();
					nameET.setBackgroundColor(Color.parseColor("#ffa9a9"));
				}
			
			//***************************
			//Date-field//
			if(!dateET.getText().toString().equals(""))
			{
				validatedFields++;
			}
			else
			{
				Toast.makeText(getApplicationContext(),  getString(R.string.validationfailed) , Toast.LENGTH_SHORT).show();
			}
			
			//************************
			//Field summary and result//
			
			if(validatedFields==3)
			{
				return true;
			}
			else
			{
				return false;
			}
	}
	private void addTreatment()
	{
		if(addTreatmentValidation())
		{
			Toast.makeText(getApplicationContext(), nameET.getText().toString() + " " + getString(R.string.added) , Toast.LENGTH_SHORT).show();
			db.addTreatment(new Treatment(methodET.getText().toString(), nameET.getText().toString(), dateET.getText().toString(), (durationNumberTreatmentET.getText() + " " + durSpinner.getSelectedItem().toString()),0,0));
			loadTreatmentList();
		}
	}

	private boolean updateTreatmentValidation()
	{
     int validatedFields = 0;
		
		//Validate form//
		//***************************
		//Treatment/Medicine-field//
				if(2 < methodET.getText().toString().length() &&  methodET.getText().toString().length() < 20
						){
					methodET.setBackgroundColor(Color.parseColor("#ffffff"));
					validatedFields++;
				}
				else
				{
					Toast.makeText(getApplicationContext(),  getString(R.string.validationfailed), Toast.LENGTH_LONG).show();
					methodET.setBackgroundColor(Color.parseColor("#ffa9a9"));
				}
			
			//***************************
			//Condition-field//
			if(2 < nameET.getText().toString().length() &&  nameET.getText().toString().length() < 20)
				{
					nameET.setBackgroundColor(Color.parseColor("#ffffff"));
					validatedFields++;
				}
				else
				{
					Toast.makeText(getApplicationContext(),  getString(R.string.validationfailed), Toast.LENGTH_LONG).show();
					nameET.setBackgroundColor(Color.parseColor("#ffa9a9"));
				}
			
			//***************************
			//Date-field//
			if(!dateET.getText().toString().equals(""))
			{
				validatedFields++;
			}
			else
			{
				Toast.makeText(getApplicationContext(),  getString(R.string.validationfailed) , Toast.LENGTH_SHORT).show();
			}
			
			//************************
			//Field summary and result//
			
			if(validatedFields==3)
			{
				return true;
			}
			else
			{
				return false;
			}
	}
	
	private void updateTreatment(Treatment treatment)
	{
		if(updateTreatmentValidation())
		{
			Toast updateToast = Toast.makeText(getApplicationContext(), "'"+ nameET.getText() + "'" + " " + getString(R.string.updated), Toast.LENGTH_SHORT);
			updateToast.show();
			Treatment updated = new Treatment(nameET.getText().toString(), methodET.getText().toString(), dateET.getText().toString(), (durationNumberTreatmentET.getText() + " " + durSpinner.getSelectedItem().toString()), 0);
			db.updateTreatment(treatment, updated);
			loadTreatmentList();
		}
	}
	
	private void deleteTreatment(Treatment treatment)
	{
		db.deleteTreatment(treatment);
	}
	
	private void loadDiaryList()
	{
		nyDiary = false;
		layout_position = 3;
		
		update = false;
		if(inOld)
		{
			
			setContentView(R.layout.diaries_old);
			titleTV = (TextView)findViewById(R.id.textTitle);
			titleTV.setText(selectedTreatment.getName());
			titleTV.setTypeface(customFont);
		}
		else
		{
			setContentView(R.layout.diaries);
			titleTV = (TextView)findViewById(R.id.textTitle);
			titleTV.setText(selectedTreatment.getName());
			titleTV.setTypeface(customFont);
			manageButton = (ImageButton)findViewById(R.id.menuManageButton);
			addButton = (ImageButton)findViewById(R.id.menuAddButto);
			manageButton.setOnClickListener(onClickListener);
			addButton.setOnClickListener(onClickListener);
		}
		
		fillDiaryListView();
		registerDiaryClickCallback();
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		 @Override
	     public void onClick(View v) 
	     {
	         switch(v.getId()){
	             case R.id.menuManageButton:
	            	 loadTreatmentDetails(selectedTreatment);
	             break;
	             case R.id.menuDeleteButton:
	            	 deleteNote(selectedNote);
	            	 filldiaryList(selectedTreatment);
	            	 loadDiaryList();
	             break;
	             case R.id.menuAddButtonTreatments:
	            	 loadAddTreatment();
	             break;
	             case R.id.rateButton:
	            	 rateTreatment();
	            	 loadOldTreatmentList();
	             break;
	             case R.id.menuAddButtonNewTreatment:
	            	 if(nyTreatment)
	            	 {
	            		 addTreatment();
	            	 }
	            	 else
	            	 {
	            		 updateTreatment(selectedTreatment);
	            	 }
	             break;
	             case R.id.menuManageButtonTreatments:
	            	 Intent prefs = new Intent("com.example.treatmentdiary.PREFS");
	            	 startActivity(prefs);
	             break;
	             case R.id.menuAddButto:
	            	 if(nyDiary)
	            	 {
		 	     			addNote();
	            	 }
	            	 else
	            	 {
	            		    if(update)
	            		    {
	            		    	updateNote(selectedNote);
	            		    }
	            		    else
	            		    {
		            		    loadAddNote();
	            		    }
	            	 }
	             break;
	             case R.id.actionBackButton:
	            	  switch(layout_position){
	            	  case 1:
	            	  case 2:
	            		  finish();
	            	  break;
	            	  case 11:
	            	  case 3:
	            		  if(inOld)
	            		  {
	            			  loadOldTreatmentList();
	            		  }	  
	            		  else
	            		  {
	            			  loadTreatmentList();
	            		  }
	            	  break;
	            	  case 111:
	            		  loadTreatmentDetails(selectedTreatment);
	            	  break;
	            	  case 22:
	            		  loadOldTreatmentList();
	            	  break;
	            	  case 33:
	            		  loadDiaryList();
	            	  break;
	            	  default:
	            		  finish();
	            	  }
	             break;
	             case R.id.actionExitButton:
	            	if(layout_position == 11 || layout_position == 22 || layout_position == 33 )
	            	{
	     				quitAppDialog();
	            	}
	            	else
	            	{
	     				setResult(99);
	     				finish();
	            	}
	             break;
	             
		         case R.id.etDate:
		        	 showDialog(dialog_id);
		        break;
		         case R.id.etDateTreatment:
			 			showDialog(dialog_id);
			 	 break;
	         }
	     }
	};
	
	private void fillListView()
	{
		ArrayAdapter<Treatment> adapter = new MyListAdapter();
		list = (ListView) findViewById(R.id.treatmentsListView);
		list.setAdapter(adapter);
		

			list.setOnItemLongClickListener(new OnItemLongClickListener()
			{
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id)
				{
					Treatment clickedTreatment = treatments.get(pos);
					selectedTreatment = clickedTreatment;
					treatmentLongClickDialog(selectedTreatment);
					return true;
				}
			});
	}

	
	@Override
	public void onBackPressed() {
		barBackButton.performClick();
	}

	private void fillDiaryListView()
	{
		ArrayAdapter<Diary> diaryAdapter = new MyDiaryAdapter();
		listDiary = (ListView)findViewById(R.id.diaryListView);
		listDiary.setAdapter(diaryAdapter);
		
	}
	
	
	/* Note details */
	private void loadNoteDetails(Diary note)
	{
		layout_position = 33;
		
		update = true;
		nyDiary = false;
		setContentView(R.layout.diary_note);
		deleteButton = (ImageButton)findViewById(R.id.menuDeleteButton);
		deleteButton.setOnClickListener(onClickListener);
		addButton = (ImageButton)findViewById(R.id.menuAddButto);
		addButton.setOnClickListener(onClickListener);
		dateET = (EditText)findViewById(R.id.etDate);
		dateET.setOnClickListener(onClickListener);
		
		
		/*Date handling*/
		
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		try {
			c.setTime(sdf.parse(note.getDate()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		yr = c.get(Calendar.HOUR_OF_DAY);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		
		/* -------------------------------*/
		
		titleET = (EditText)findViewById(R.id.etTitle);
		titleTV = (TextView)findViewById(R.id.textTitle);
		todTv = (TextView)findViewById(R.id.tvTod);
		dateTv = (TextView)findViewById(R.id.tvDate);
		textET = (EditText)findViewById(R.id.etText);
		todSpinner = (Spinner)findViewById(R.id.spinnerTod);
		ratingBar = (RatingBar)findViewById(R.id.ratingBar);
		
		titleTV.setTypeface(customFont);
		todTv.setTypeface(customFont);
		dateTv.setTypeface(customFont);
		titleET.setTypeface(customFont);
		textET.setTypeface(customFont);
		dateET.setTypeface(customFont);
		
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.timesofday, android.R.layout.simple_spinner_item);
		todSpinner.setAdapter(adapter);
		todSpinner.setPopupBackgroundResource(R.drawable.spinner);
		

		dateET.setText("" + note.getDate());
		titleTV.setText("" + note.getTitle());
		titleET.setText("" + note.getTitle());
		textET.setText(""+note.getDescription());
		if(note.getRate() == null || note.getRate().isEmpty())
		{
			ratingBar.setRating(0);
		}
		else
		{
			ratingBar.setRating(Float.valueOf(note.getRate()));
		}
		
		if(note.getTimeOfDay()==null || note.getTimeOfDay().isEmpty())
		{
			todSpinner.setSelection(0);
		}
		else
		{
			switch(note.getTimeOfDay())
			{
			case "Morning":
				todSpinner.setSelection(0);
				break;
			case "Noon":
				todSpinner.setSelection(1);
				break;
			case "Evening":
				todSpinner.setSelection(2);
				break;
			case "Night":
				todSpinner.setSelection(3);
				break;
			case "Morgen":
				todSpinner.setSelection(0);
				break;
			case "Formiddag":
				todSpinner.setSelection(1);
				break;
			case "Ettermiddag":
				todSpinner.setSelection(2);
				break;
			case "Kveld":
				todSpinner.setSelection(3);
			}
		}
		

	}
	/* Note add */
	private void loadAddNote()
	{
		layout_position = 33;
		
		nyDiary = true;
		
		setContentView(R.layout.diary_note);
		
		deleteButton = (ImageButton)findViewById(R.id.menuDeleteButton);
		deleteButton.setOnClickListener(onClickListener);
		
		/*Date handling*/
		
		Calendar c = Calendar.getInstance();
		yr = c.get(Calendar.HOUR_OF_DAY);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		
		/* -------------------------------*/
		dateET = (EditText)findViewById(R.id.etDate);
		dateET.setOnClickListener(onClickListener);
		titleET = (EditText)findViewById(R.id.etTitle);
		titleTV = (TextView)findViewById(R.id.textTitle);
		textET = (EditText)findViewById(R.id.etText);
		todTv = (TextView)findViewById(R.id.tvTod);
		dateTv = (TextView)findViewById(R.id.tvDate);
		
		titleTV.setTypeface(customFont);
		titleET.setTypeface(customFont);
		textET.setTypeface(customFont);
		dateET.setTypeface(customFont);
		todTv.setTypeface(customFont);
		dateTv.setTypeface(customFont);
		
		
		todSpinner = (Spinner)findViewById(R.id.spinnerTod);
		ratingBar = (RatingBar)findViewById(R.id.ratingBar);
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.timesofday, android.R.layout.simple_spinner_item);
		todSpinner.setAdapter(adapter);
		todSpinner.setPopupBackgroundResource(R.drawable.spinner);
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String now = sdf.format(c.getTime());
		
		dateET.setText(now);
		titleTV.setText("");
		titleET.setText("");
		textET.setText("");
		
		dateET.setHint(getString(R.string.hintDate));
		titleET.setHint(getString(R.string.hintTitle));
		textET.setHint(getString(R.string.hintNote));
		ratingBar.setRating(0);
		addButton = (ImageButton)findViewById(R.id.menuAddButto);
		addButton.setOnClickListener(onClickListener);
	}
	
	private boolean addNoteValidation()

	{
	int validatedFields = 0;
		
		//Validate form//
		//***************************
		//Title-field//
				if(1 < titleET.getText().toString().length() &&  titleET.getText().toString().length() < 20)
				{
					titleET.setBackgroundColor(Color.parseColor("#ffffff"));
					validatedFields++;
				}
				else
				{
					Toast.makeText(getApplicationContext(), getString(R.string.validationfailed), Toast.LENGTH_LONG).show();
					titleET.setBackgroundColor(Color.parseColor("#ffa9a9"));
				}
			
			//***************************
			//Text-field//
			if(textET.getText().toString().length() > 1 && textET.getText().toString().length() < 220)
				{
					textET.setBackgroundColor(Color.parseColor("#ffffff"));
					validatedFields++;
				}
				else
				{
					Toast.makeText(getApplicationContext(), getString(R.string.validationfailed), Toast.LENGTH_LONG).show();
					textET.setBackgroundColor(Color.parseColor("#ffa9a9"));
				}
			
			//***************************
			//Date-field//
			if(!dateET.getText().toString().equals(""))
			{
				validatedFields++;
			}
			else
			{
				Toast.makeText(getApplicationContext(), getString(R.string.validationfailed) , Toast.LENGTH_SHORT).show();
			}
			
			//************************
			//Field summary and result//
			
			if(validatedFields==3)
			{
				return true;
			}
			else
			{
				return false;
			}
	}
	
	/* Diary: Add, delete, update */
	private void addNote()
	{
		if(addNoteValidation())
		{
			Toast.makeText(getApplicationContext(), titleET.getText().toString() + " " + getString(R.string.added), Toast.LENGTH_SHORT).show();
			dbDiary.addDiary(new Diary(titleET.getText().toString(),dateET.getText().toString(), textET.getText().toString(), todSpinner.getSelectedItem().toString(), Float.toString(ratingBar.getRating())), selectedTreatment);
  			filldiaryList(selectedTreatment);
			loadDiaryList();
		}
		
	}
	
	private void deleteNote(Diary note)
	{
		Toast deleteToast = Toast.makeText(getApplicationContext(), "'"+note.getTitle() + "'" + " " + getString(R.string.deleted), Toast.LENGTH_SHORT);
		deleteToast.show();
		dbDiary.deleteNote(note);
	}
	
	
	private boolean updateNoteValidation()
	{
	int validatedFields = 0;
		
		//Validate form//
		//***************************
		//Title-field//
				if(1 < titleET.getText().toString().length() &&  titleET.getText().toString().length() < 20)
				{
					titleET.setBackgroundColor(Color.parseColor("#ffffff"));
					validatedFields++;
				}
				else
				{
					Toast.makeText(getApplicationContext(), getString(R.string.validationfailed), Toast.LENGTH_LONG).show();
					titleET.setBackgroundColor(Color.parseColor("#ffa9a9"));
				}
			
			//***************************
			//Text-field//
			if(textET.getText().toString().length() > 1 && textET.getText().toString().length() < 220)
				{
					textET.setBackgroundColor(Color.parseColor("#ffffff"));
					validatedFields++;
				}
				else
				{
					Toast.makeText(getApplicationContext(), getString(R.string.validationfailed), Toast.LENGTH_LONG).show();
					textET.setBackgroundColor(Color.parseColor("#ffa9a9"));
				}
			
			//***************************
			//Date-field//
			if(!dateET.getText().toString().equals(""))
			{
				validatedFields++;
			}
			else
			{
				Toast.makeText(getApplicationContext(), getString(R.string.validationfailed) , Toast.LENGTH_SHORT).show();
			}
			
			//************************
			//Field summary and result//
			
			if(validatedFields==3)
			{
				return true;
			}
			else
			{
				return false;
			}
	}
	
private void updateNote(Diary note)
	{
		if(updateNoteValidation())
		{
			Toast updateToast = Toast.makeText(getApplicationContext(), "'"+note.getTitle() + "'" + " updated.", Toast.LENGTH_SHORT);
			updateToast.show();
			Diary updated = new Diary(titleET.getText().toString(),dateET.getText().toString(), textET.getText().toString(), todSpinner.getSelectedItem().toString(), Float.toString(ratingBar.getRating()));
			dbDiary.updateNote(note, updated);
	    	filldiaryList(selectedTreatment);
	    	loadDiaryList();
		}

	}
	
	
	
	private class MyListAdapter extends ArrayAdapter<Treatment>
	{
		public MyListAdapter()
		{
			super(TreatmentList.this, R.layout.treatment_view, treatments);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		
			View itemView = convertView;
			if(itemView == null)
			{
				itemView = getLayoutInflater().inflate(R.layout.treatment_view, parent, false);
			}
			Treatment currentTreatment = treatments.get(position);
			TextView textMake = (TextView)itemView.findViewById(R.id.txtMake);
			textMake.setTypeface(customFont);
			RatingBar ratingTreatment = (RatingBar)itemView.findViewById(R.id.ratingBarTreatments);
			if(inOld)
			{
				ratingTreatment.setVisibility(View.VISIBLE);
				ratingTreatment.setRating(Float.valueOf(currentTreatment.getRating()));
			}
			else
			{
				ratingTreatment.setVisibility(View.INVISIBLE);
			}
			
			if(currentTreatment.getName() == null || currentTreatment.getName().isEmpty())
			{
				textMake.setText("(Empty)");
			}
			else
			{
				textMake.setText(currentTreatment.getName());
			}
			
			TextView textStarted = (TextView)itemView.findViewById(R.id.textStarted);
			textStarted.setText(getString(R.string.started) + " " + currentTreatment.getStarted());
			textStarted.setTypeface(customFont);
			TextView textMethod = (TextView)itemView.findViewById(R.id.textMethod);
			textMethod.setText(currentTreatment.getMethod());
			textMethod.setTypeface(customFont);
			return itemView;
		}
	}
	private class MyDiaryAdapter extends ArrayAdapter<Diary>
	{
		
		public MyDiaryAdapter()
		{
			super(TreatmentList.this, R.layout.diary_view, notes);
		}


		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			if(itemView == null)
			{
				itemView = getLayoutInflater().inflate(R.layout.diary_view, parent, false);
			}
				Diary currentNote = notes.get(position);
				TextView textMake = (TextView)itemView.findViewById(R.id.txtMake);
				TextView textDesc = (TextView)itemView.findViewById(R.id.textListDesc);
				TextView textDate = (TextView)itemView.findViewById(R.id.textListDate);
				
				textMake.setTypeface(customFont);
				textDesc.setTypeface(customFont);
				textDate.setTypeface(customFont);
				
				RatingBar ratingBarList = (RatingBar)itemView.findViewById(R.id.ratingBar1);
				ratingBarList.setRating(Float.valueOf(currentNote.getRate()));
				textDesc.setText(currentNote.getDescription());
				textDate.setText(currentNote.getDate());
				
				if(currentNote.getTitle() == null || currentNote.getTitle().isEmpty())
				{
					textMake.setText("(Empty)");
				}
				else
				{
					textMake.setText(currentNote.getTitle());
				}
				
				return itemView;
			}
		}

	
	/*TimePicker dialog*/
	protected Dialog onCreateDialog(int id)
	{
		switch(id)
		{
		case dialog_id:
			return new DatePickerDialog(this,mDateSetListener, 2014, month, day);
		}
		return null;
	}
	
	private DatePickerDialog.OnDateSetListener mDateSetListener = 
			new	DatePickerDialog.OnDateSetListener() {
				
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear,
						int dayOfMonth) {

					yr = year;
					month = monthOfYear;
					day = dayOfMonth;
					dateET.setText(day + "-" + (month+1) + "-" + (year));
					
				}
			};

}