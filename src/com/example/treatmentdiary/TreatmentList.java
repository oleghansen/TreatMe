package com.example.treatmentdiary;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;



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
	
	
	private TextView titleTV;
	private ImageButton manageButton, addButton;
	
	/* Note details */
	private TextView tv;
	private EditText dateET, titleET, textET;
	private Spinner todSpinner;
	private RatingBar ratingBar;
	


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		db = new DbHandlerTreatments(this);
		dbDiary = new DbHandlerDiary(this);
		
		loadTreatmentList();

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
				item.setVisible(true);
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
		setContentView(R.layout.treatments);
		fillTreatmentList();
		fillListView();
		registerClickCallback();
	}
	
	private void loadDiaryList()
	{
		setContentView(R.layout.diaries);
		titleTV = (TextView)findViewById(R.id.textTitle);
		titleTV.setText(selectedTreatment.getName());
		
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
	             case R.id.menuAddButto:
	     			dbDiary.addDiary(new Diary(), selectedTreatment);
	    			filldiaryList(selectedTreatment);
	    			fillDiaryListView();
	    			registerDiaryClickCallback();
	             break;
	         }
	     }
	};
	

	private void fillListView()
	{
		ArrayAdapter<Treatment> adapter = new MyListAdapter();
		list = (ListView) findViewById(R.id.treatmentsListView);
		list.setAdapter(adapter);
		
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
		setContentView(R.layout.diary_note);
		dateET = (EditText)findViewById(R.id.etDate);
		titleET = (EditText)findViewById(R.id.etTitle);
		titleTV = (TextView)findViewById(R.id.textTitle);
		textET = (EditText)findViewById(R.id.etText);
		todSpinner = (Spinner)findViewById(R.id.spinnerTod);
		ratingBar = (RatingBar)findViewById(R.id.ratingBar);
		
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.timesofday, android.R.layout.simple_spinner_item);
		todSpinner.setAdapter(adapter);
		todSpinner.setPopupBackgroundResource(R.drawable.spinner);
		

		dateET.setText("" + note.getDate());
		titleTV.setText("" + note.getTitle());
		titleET.setText("" + note.getTitle());
		textET.setText(""+note.getDescription() + "time of day: " + note.getTimeOfDay() + " rating: " + note.getRate() + " tretID: " + note.getTreatmentId());
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
			if(currentTreatment.getName() == null || currentTreatment.getName().isEmpty())
			{
				textMake.setText("(Tom)");
			}
			else
			{
				textMake.setText(currentTreatment.getName());
			}
			
			TextView textStarted = (TextView)itemView.findViewById(R.id.textStarted);
			textStarted.setText("ID: " + currentTreatment.getId());
			TextView textMethod = (TextView)itemView.findViewById(R.id.textMethod);
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
				return itemView;
			}
				

			

		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
		getMenuInflater().inflate(R.menu.mainadd, menu);
		item = menu.findItem(R.id.add);
		item.setVisible(false);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		
		switch(item.getItemId())
		{
		case R.id.add:
			dbDiary.addDiary(new Diary(), selectedTreatment);
			filldiaryList(selectedTreatment);
			fillDiaryListView();
			registerDiaryClickCallback();
		break;
		case android.R.id.home:
			finish();
			return true;
			
		}
		return super.onOptionsItemSelected(item);
	}
}