package com.example.treatmentdiary;

import java.util.List;

import android.R.menu;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;



public class TreatmentList extends Activity
{
	private ListView list, listDiary;
	private List<Treatment> treatments;
	private List<Diary> notes;
	private DbHandlerTreatments db;
	private DbHandlerDiary dbDiary;
	private Treatment selectedTreatment;
	private MenuItem item;
	
	
	private TextView titleTV;
	private ImageButton manageButton, addButton;


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
				System.out.println(note.getTitle());
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