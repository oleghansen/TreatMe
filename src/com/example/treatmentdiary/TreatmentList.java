package com.example.treatmentdiary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
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
	private ListView list, listDiary;
	private List<Treatment> treatments;
	private List<Diary> notes;
	private DbHandlerTreatments db;
	private DbHandlerDiary dbDiary;
	private Treatment selectedTreatment;
	private Diary selectedNote;
	private MenuItem item, item2;
	static final int dialog_id=1;
	int yr, day, month;
	
	private Typeface customFont;
	private TextView titleTV;
	private ImageButton deleteButton, manageButton, addButton, manageButtonTreatments, addButtonTreatments;
	
	/* Note details */
	private TextView tv, todTv, dateTv, dateTreatmentTV, durationTreatmentTV, optionalTreatmentTV;
	private EditText dateET, titleET, textET, dateTreatmentET, durationNumberTreatmentET, nameET, methodET;
	private Spinner todSpinner, durSpinner;
	private RatingBar ratingBar;
	private ImageButton barBackButton;
	
	private boolean nyDiary, nyTreatment, update;
	


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		customFont = Typeface.createFromAsset(getAssets(), "fonts/HelveticaNeueLTW1G-Lt.otf");
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		db = new DbHandlerTreatments(this);
		dbDiary = new DbHandlerDiary(this);
		
		loadTreatmentList();
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
    
	private void fillTreatmentList()
	{
		treatments = db.findAllTreatments();
	}
	
	private void filldiaryList(Treatment treatment)
	{
		notes = dbDiary.findDiaryNotes(treatment);
	}

	
	private void loadTreatmentList()
	{
		update = false;
		setContentView(R.layout.treatments);
		manageButtonTreatments = (ImageButton)findViewById(R.id.menuManageButtonTreatments);
		addButtonTreatments = (ImageButton)findViewById(R.id.menuAddButtonTreatments);
		manageButtonTreatments.setOnClickListener(onClickListener);
		addButtonTreatments.setOnClickListener(onClickListener);
		
		
		fillTreatmentList();
		fillListView();
		registerClickCallback();
	}
	
	private void loadTreatmentDetails(Treatment treatment)
	{
			setContentView(R.layout.treatment_details);
			dateTreatmentET = (EditText)findViewById(R.id.etDateTreatment);
			durationNumberTreatmentET = (EditText)findViewById(R.id.etDurationNumberTreatment);
			durSpinner = (Spinner)findViewById(R.id.spinnerDuration);
			
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
			dateTreatmentET.setTypeface(customFont);
			
			titleTV.setText(selectedTreatment.getName());
			nameET.setText(selectedTreatment.getName());
			methodET.setText(selectedTreatment.getMethod());
			dateTreatmentET.setText(selectedTreatment.getStarted());
			if(!(selectedTreatment.getExpectedTime() == null || selectedTreatment.getExpectedTime().isEmpty()))
			{
				int res = 0;
				for (int i=0; i < selectedTreatment.getExpectedTime().length(); i++) {
				    char ch = selectedTreatment.getExpectedTime().charAt(i);
				    if (ch < '0' || ch > '9') continue;
				    res = res * 10 + ch - '0';
				}
				
				if(selectedTreatment.getExpectedTime().contains("Days"))
				{
					durSpinner.setSelection(0);
				}
				else if(selectedTreatment.getExpectedTime().contains("Week"))
				{
					durSpinner.setSelection(1);
				}
				else if(selectedTreatment.getExpectedTime().contains("Month"))
				{
					durSpinner.setSelection(2);
				}
				else if(selectedTreatment.getExpectedTime().contains("Year"))
				{
					durSpinner.setSelection(3);
				}
				
				durationNumberTreatmentET.setText(String.valueOf(res));
			}
	}
	
	private void loadAddTreatment()
	{
		
	}
	
	private void loadDiaryList()
	{
		update = false;
		setContentView(R.layout.diaries);
		titleTV = (TextView)findViewById(R.id.textTitle);
		titleTV.setText(selectedTreatment.getName());
		titleTV.setTypeface(customFont);
		manageButton = (ImageButton)findViewById(R.id.menuManageButton);
		addButton = (ImageButton)findViewById(R.id.menuAddButto);
		manageButton.setOnClickListener(onClickListener);
		addButton.setOnClickListener(onClickListener);
		
		fillDiaryListView();
		registerDiaryClickCallback();
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		 @Override
	     public void onClick(View v) 
	     {
	         switch(v.getId()){
	             case R.id.menuManageButton:
	            	 // Innstillinger
	             break;
	             case R.id.menuDeleteButton:
	            	 deleteNote(selectedNote);
	            	 filldiaryList(selectedTreatment);
	            	 loadDiaryList();
	             break;
	             case R.id.menuAddButtonTreatments:
	            	 // legg til ny
	             break;
	             case R.id.menuManageButtonTreatments:
	            	 //innstillinger
	             break;
	             case R.id.menuAddButto:
	            	 if(nyDiary)
	            	 {
		 	     			addNote();
		 	     			filldiaryList(selectedTreatment);
			    			loadDiaryList();
	            	 }
	            	 else
	            	 {
	            		    if(update)
	            		    {
	            		    	updateNote(selectedNote);
	            		    	filldiaryList(selectedTreatment);
	            		    	loadDiaryList();
	            		    }
	            		    else
	            		    {
		            		    loadAddNote();
	            		    }

	            	 }

	             break;
	             case R.id.actionBackButton:
	            	 finish();
	             break;
	             
		         case R.id.etDate:
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
				loadTreatmentDetails(selectedTreatment);
				return true;
			}
		});
		
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
			switch(note.getTimeOfDay()) // LAG EN SMIDIGERE MÅTE Å GJØRE DETTE PÅ. GETSTRING(R.ID.. osv? )
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
			}
		}
		

	}
	/* Note add */
	private void loadAddNote()
	{
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
		
		titleTV.setTypeface(customFont);
		titleET.setTypeface(customFont);
		textET.setTypeface(customFont);
		dateET.setTypeface(customFont);
		
		
		todSpinner = (Spinner)findViewById(R.id.spinnerTod);
		ratingBar = (RatingBar)findViewById(R.id.ratingBar);
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.timesofday, android.R.layout.simple_spinner_item);
		todSpinner.setAdapter(adapter);
		todSpinner.setPopupBackgroundResource(R.drawable.spinner);
		dateET.setText("");
		titleTV.setText("");
		titleET.setText("New note");
		
		dateET.setHint("Date");
		titleET.setHint("Title");
		textET.setHint("Write your note here");
		ratingBar.setRating(0);
		addButton = (ImageButton)findViewById(R.id.menuAddButto);
		addButton.setOnClickListener(onClickListener);
	}
	
	/* Diary: Add, delete, update */
	private void addNote()
	{
		dbDiary.addDiary(new Diary(dateET.getText().toString(),titleET.getText().toString(), textET.getText().toString(), todSpinner.getSelectedItem().toString(), Float.toString(ratingBar.getRating())), selectedTreatment);
	}
	
	private void deleteNote(Diary note)
	{
		Toast deleteToast = Toast.makeText(getApplicationContext(), "'"+note.getTitle() + "'" + " deleted.", Toast.LENGTH_SHORT);
		deleteToast.show();
		dbDiary.deleteNote(note);
	}
	
	private void updateNote(Diary note)
	{
		Toast updateToast = Toast.makeText(getApplicationContext(), "'"+note.getTitle() + "'" + " updated.", Toast.LENGTH_SHORT);
		updateToast.show();
		Diary updated = new Diary(dateET.getText().toString(),titleET.getText().toString(), textET.getText().toString(), todSpinner.getSelectedItem().toString(), Float.toString(ratingBar.getRating()));
		dbDiary.updateNote(note, updated);
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
			
			if(currentTreatment.getName() == null || currentTreatment.getName().isEmpty())
			{
				textMake.setText("(Tom)");
			}
			else
			{
				textMake.setText(currentTreatment.getName());
			}
			
			TextView textStarted = (TextView)itemView.findViewById(R.id.textStarted);
			textStarted.setText("Startet  " + currentTreatment.getStarted());
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
					textMake.setText("(Tom)");
				}
				else
				{
					textMake.setText(currentNote.getTitle());
				}
				
				
				return itemView;
			}
			else
			{
				itemView = getLayoutInflater().inflate(R.layout.diary_view, parent, false);
				Diary currentNote = notes.get(position);
				TextView textMake = (TextView)itemView.findViewById(R.id.txtMake);
				textMake.setText(currentNote.getTitle());
				textMake.setTypeface(customFont);
				return itemView;
			}
		}
	}
	
	/*TimePicker dialog*/
	protected Dialog onCreateDialog(int id)
	{
		switch(id)
		{
		case dialog_id:
			System.out.println(day + " " + month + " " + yr);
			return new DatePickerDialog(this,mDateSetListener, 2013, day, month);
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
					dateET.setText(day + "/" + (month+1) + "/" + (year));
					
				}
			};

}