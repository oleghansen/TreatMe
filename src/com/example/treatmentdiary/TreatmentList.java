package com.example.treatmentdiary;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;



public class TreatmentList extends Activity
{
	private ListView list, listDiary;
	private List<Treatment> treatments;
	private List<Diary> notes;
	private DbHandlerTreatments db;
	private DbHandlerDiary dbDiary;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		db = new DbHandlerTreatments(this);
		dbDiary = new DbHandlerDiary(this);
		
		loadTreatmentList();
    }

	/*
	protected Dialog onCreateDialog(int id)
	{
		switch(id)
		{
		case dialog_id:
			return new DatePickerDialog(this,mDateSetListener,1980, day, month);
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
	*/
	private void registerClickCallback() {
		list = (ListView)findViewById(R.id.treatmentsListView);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id)
			{
				Treatment clickedTreatment = treatments.get(position);
				loadDiaryList(clickedTreatment);
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
	/*
	
	private boolean isValidName(String name) 
	{
		String NAME_PATTERN = "^[a-zA-Z\\s.]+";

		Pattern pattern = Pattern.compile(NAME_PATTERN);
		Matcher matcher = pattern.matcher(name);
		return matcher.matches();
	}
	
	private boolean isValidPhone(String phone) 
	{
		String PHONE_PATTERN = "[0-9]+";
		Pattern pattern = Pattern.compile(PHONE_PATTERN);
		Matcher matcher = pattern.matcher(phone);
		return matcher.matches();
	}
	
	
	
	private void addFriend()
	{
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		@SuppressWarnings("deprecation")
		String formatedDate = sdf.format(new Date(yr-1900, month, day));

		int validatedFields = 0; // should be 4 by the end of validation
		
		// Validation
		if(isValidName(nameET.getText().toString()))
		{
			if(2 < nameET.getText().toString().length() && nameET.getText().toString().length() < 20)
			{
				nameET.setBackgroundColor(Color.parseColor("#ffffff"));
				validatedFields++;
			}
			else
			{
				Toast.makeText(getApplicationContext(), getString(R.string.toastInvName), Toast.LENGTH_LONG).show();
				nameET.setBackgroundColor(Color.parseColor("#ffa9a9"));
			}
		}
		else
		{
			nameET.setBackgroundColor(Color.parseColor("#ffa9a9"));
			Toast.makeText(getApplicationContext(), getString(R.string.toastInvNameBc) , Toast.LENGTH_SHORT).show();
		}
		
		if(isValidPhone(phoneET.getText().toString()))
		{
			if((phoneET.getText().toString().length()==8))
			{
				validatedFields++;
				phoneET.setBackgroundColor(Color.parseColor("#ffffff"));
			}
			else
			{
				phoneET.setBackgroundColor(Color.parseColor("#ffa9a9"));
				Toast.makeText(getApplicationContext(), getString(R.string.toastInvPhone), Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			phoneET.setBackgroundColor(Color.parseColor("#ffa9a9"));
			Toast.makeText(getApplicationContext(),getString(R.string.toastInvPhoneDig) , Toast.LENGTH_SHORT).show();
		}
		
		if(!dateET.getText().toString().equals(""))
		{
			validatedFields++;
			dateET.setBackgroundColor(Color.parseColor("#ffffff"));
		}
		else
		{
			dateET.setBackgroundColor(Color.parseColor("#ffa9a9"));
			Toast.makeText(getApplicationContext(), getString(R.string.toastInvDate), Toast.LENGTH_SHORT).show();
		}
		
		if(!smsEText.getText().toString().equals(""))
		{
			validatedFields++;
		}
		else
		{
			smsEText.setBackgroundColor(Color.parseColor("#ffa9a9"));
			Toast.makeText(getApplicationContext(), getString(R.string.toastInvSms), Toast.LENGTH_SHORT).show();
		}
		// end of validation
		
		if(validatedFields==4)
		{
			smsEText.setBackgroundColor(Color.parseColor("#ffffff"));
			db.leggTilKontakt(new Friend(nameET.getText().toString(), Integer.parseInt(phoneET.getText().toString()), formatedDate, smsEText.getText().toString()));
			Toast.makeText(getApplicationContext(), nameET.getText().toString() + " added successfully!", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
	
	private void updateFriend(Friend friend)
	{
		String formatedDate = dateET.getText().toString();
		int validatedFields = 0; // should be 4 by the end of validation
		
		// Validation
		if(isValidName(nameText.getText().toString()))
		{
			if(2 < nameText.getText().toString().length() && nameText.getText().toString().length() < 20)
			{
				nameText.setBackgroundColor(Color.parseColor("#ffffff"));
				validatedFields++;
			}
			else
			{
				Toast.makeText(getApplicationContext(), getString(R.string.toastInvName), Toast.LENGTH_LONG).show();
				nameText.setBackgroundColor(Color.parseColor("#ffa9a9"));
			}
		}
		else
		{
			nameText.setBackgroundColor(Color.parseColor("#ffa9a9"));
			Toast.makeText(getApplicationContext(), getString(R.string.toastInvNameBc), Toast.LENGTH_SHORT).show();
		}
		
		if(isValidPhone(phoneText.getText().toString()))
		{
			if((phoneText.getText().toString().length()==8))
			{
				validatedFields++;
				phoneText.setBackgroundColor(Color.parseColor("#ffffff"));
			}
			else
			{
				phoneText.setBackgroundColor(Color.parseColor("#ffa9a9"));
				Toast.makeText(getApplicationContext(), getString(R.string.toastInvPhone), Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			phoneText.setBackgroundColor(Color.parseColor("#ffa9a9"));
			Toast.makeText(getApplicationContext(),getString(R.string.toastInvPhoneDig), Toast.LENGTH_SHORT).show();
		}
		
		if(!dateET.getText().toString().equals(""))
		{
			validatedFields++;
			dateET.setBackgroundColor(Color.parseColor("#ffffff"));
		}
		else
		{
			dateET.setBackgroundColor(Color.parseColor("#ffa9a9"));
			Toast.makeText(getApplicationContext(), getString(R.string.toastInvDate), Toast.LENGTH_SHORT).show();
		}
		
		if(!smsText.getText().toString().equals(""))
		{
			validatedFields++;
		}
		else
		{
			smsText.setBackgroundColor(Color.parseColor("#ffa9a9"));
			Toast.makeText(getApplicationContext(), getString(R.string.toastInvSms), Toast.LENGTH_SHORT).show();
		}
		// end of validation
		
		if(validatedFields==4)
		{
			smsText.setBackgroundColor(Color.parseColor("#ffffff"));
			db.deleteFriend(friend);
			db.leggTilKontakt(new Friend(nameText.getText().toString(), Integer.parseInt(phoneText.getText().toString()), formatedDate, smsText.getText().toString()));
			Toast.makeText(getApplicationContext(), nameText.getText().toString() + getString(R.string.updateOk) , Toast.LENGTH_SHORT).show();
			finish();
		}

	}
	
	*/
	
	private void loadTreatmentList()
	{
		setContentView(R.layout.treatments);
		fillTreatmentList();
		fillListView();
		registerClickCallback();
	}
	
	private void loadDiaryList(Treatment treatment)
	{
		setContentView(R.layout.diaries);
		filldiaryList(treatment);
		fillDiaryListView();
		registerDiaryClickCallback();
	}
	
	/*
	
	private void loadFriendSettings(Friend friend)
	{
		setContentView(R.layout.friendedit);
		help = friend;
		nameText = (EditText)findViewById(R.id.textName);
		nameText.setText(friend.getName());
		
		phoneText = (EditText)findViewById(R.id.textPhone);
		phoneText.setText("" + friend.getPhone());
		
		smsText = (EditText)findViewById(R.id.textSms);
		smsText.setText(friend.getSms());
		
		dateET = (EditText)findViewById(R.id.dateET);
		dateET.setOnClickListener(onClickListener);
		dateET.setText(friend.getBirthday());
		
		smsSwitch = (Switch)findViewById(R.id.smsSwitch);
		if(friend.getSms()!=globalSMS)
		{
			smsSwitch.setChecked(false);
			smsText.setEnabled(false);
		}
		else
		{
			smsSwitch.setChecked(true);
			smsText.setEnabled(true);
			smsText.setText("");
		}
		smsSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			 
			   @Override
			   public void onCheckedChanged(CompoundButton buttonView,
			     boolean isChecked) {
			    if(isChecked){
			     smsText.setText("");
			     smsText.setEnabled(true);
			     smsText.setBackgroundColor(Color.parseColor("#ffffff"));
			    }else{
			     smsText.setText(globalSMS);
			     smsText.setBackgroundColor(Color.parseColor("#ffffff"));
			     smsText.setEnabled(false);
			    }
			 
			   }
			  });
		
		updateButton = (Button)findViewById(R.id.updateButton);
		updateButton.setOnClickListener(new OnClickListener(){
				public void onClick(View v) 
				{
					updateFriend(help);
				}
			});
	}
	
	private void friendSelectDialog(final Friend friend)
	{
		String clickedString = getString(R.string.name) + friend.getName() + "\n" + getString(R.string.phone) + friend.getPhone() + "\n" + getString(R.string.birthday) + friend.getBirthday() + "\n" + getString(R.string.sms) + friend.getSms();
		
		dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setCancelable(true);
		dialogBuilder.setTitle(friend.getName());
		dialogBuilder.setMessage(clickedString);
		
		dialogBuilder.setNegativeButton((getString(R.string.action_settings)), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				loadFriendSettings(friend);
			}
		});
		
		dialogBuilder.setPositiveButton((getString(R.string.delete)), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				db.deleteFriend(friend);
				loadFriendList();
				Toast.makeText(getBaseContext(), getString(R.string.deleteOk), Toast.LENGTH_SHORT).show();
			}
		});
		
		AlertDialog alert = dialogBuilder.create();
		alert.show();

	}
	
	private void fillListWithTestDataDialog()
	{
		dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setCancelable(true);
		dialogBuilder.setTitle("Empty list");
		dialogBuilder.setMessage("Your friend list is empty! Do you wish to load some test data into it?");
		
		dialogBuilder.setNegativeButton((getString(R.string.no)), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				//
			}
		});
		
		dialogBuilder.setPositiveButton((getString(R.string.yes)), new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				loadTestData();
				loadFriendList();
			}
		});
		
		AlertDialog alert = dialogBuilder.create();
		alert.show();

	}
	
	private void loadTestData()
	{
		db.leggTilKontakt(new Friend("Dave Grohl", 99995555, "10-10-1992", globalSMS));
		db.leggTilKontakt(new Friend("Nate Mendel", 22225555, "01-05-1922", globalSMS));
		db.leggTilKontakt(new Friend("Taylor Hawkins", 12345467, "30-12-1966", globalSMS));
		db.leggTilKontakt(new Friend("Chris Shiflett", 22222222, "02-03-1945", globalSMS));
		db.leggTilKontakt(new Friend("Pat Smear", 99295555, "04-04-1956", "Gratulerer"));
		db.leggTilKontakt(new Friend("William Goldsmith", 19995555, "09-04-1911", globalSMS));
		db.leggTilKontakt(new Friend("Franz Stahl ", 19995555, "14-06-1922", globalSMS));
	}
	
	*/
	private void fillListView()
	{
		ArrayAdapter<Treatment> adapter = new MyListAdapter();
		list = (ListView) findViewById(R.id.treatmentsListView);
		list.setAdapter(adapter);
		
	}
	
	private void fillDiaryListView()
	{
		ArrayAdapter<Diary> diaryAdapter = new MyDiaryAdapter();
		listDiary = (ListView) findViewById(R.id.diaryListView);
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
			textMake.setText(currentTreatment.getName());
			TextView textStarted = (TextView)itemView.findViewById(R.id.textStarted);
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
			}
			Diary currentNote = notes.get(position);
			TextView textMake = (TextView)itemView.findViewById(R.id.txtMake);
			textMake.setText(currentNote.getTitle());
			return itemView;
			

		}
	}
	
	/*private void loadAddFriendScreen()
	{
		setContentView(R.layout.addfriend);
		nameET = (EditText)findViewById(R.id.textName);
		phoneET = (EditText)findViewById(R.id.phoneBox);
		dateET = (EditText)findViewById(R.id.dateET);
		smsEText = (EditText)findViewById(R.id.smsET);
		smsSwitch = (Switch)findViewById(R.id.smsSwitch);
		smsSwitch.setChecked(false);
		
		smsSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			 
			   @Override
			   public void onCheckedChanged(CompoundButton buttonView,
			     boolean isChecked) {
			    if(isChecked){
			     smsEText.setText("");
			     smsEText.setEnabled(true);
			     smsEText.setBackgroundColor(Color.parseColor("#ffffff"));
			    }else{
			     smsEText.setText(globalSMS);
			     smsEText.setEnabled(false);
			     smsEText.setBackgroundColor(Color.parseColor("#ffffff"));
			    }
			 
			   }
			  });
			   
			
			  if(smsSwitch.isChecked()){
				     smsEText.setText("");
				     smsEText.setEnabled(true);
			  }
			  else {
				  smsEText.setText(globalSMS);
				  smsEText.setEnabled(false);
			  }
			 
		dateET.setOnClickListener(onClickListener);
		addButton = (Button)findViewById(R.id.addButton);
		addButton.setOnClickListener(onClickListener);
		
	} */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		
		switch(item.getItemId())
		{
		case android.R.id.home:
			finish();
			return true;
			
		}
		return super.onOptionsItemSelected(item);
	}
}