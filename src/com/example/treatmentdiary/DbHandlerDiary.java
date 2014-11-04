package com.example.treatmentdiary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.Contacts;
import android.util.Log;

public class DbHandlerDiary extends SQLiteOpenHelper 
{
	static String TABLE_DIARY="Diary";
	static String KEY_ID="_ID";
	static String KEY_TID="TreatmentId";
	static String KEY_DATE="Date";
	static String KEY_TITLE="Title";
	static String KEY_TEXT="Text";
	static String KEY_TIMEOFDAY="TimeOfDay";
	static String KEY_RATE="Rating";
	static int DATABASE_VERSION=111;
	static String DATABASE_NAME="TreatmentDatabase";
	private List<Diary> diaryList; 
	
	public DbHandlerDiary(Context context)
	{
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		String CREATE_TABLE = "CREATE TABLE " + 
				TABLE_DIARY + "(" + 
				KEY_ID + " INTEGER PRIMARY KEY, " + 
				KEY_TID + " INTEGER, " + 
				KEY_DATE + " DATETIME, " +
				KEY_TITLE + " TEXT, " +
				KEY_TEXT + " TEXT, " +
				KEY_TIMEOFDAY + " TEXT, " +
				KEY_RATE + " TEXT " + ")";
		Log.d("SQL", CREATE_TABLE);
		db.execSQL(CREATE_TABLE);
		
	}
	
	public void resetDatabase()
	{
		//SQLiteDatabase db = this.getWritableDatabase();
		//db.execSQL("DELETE * FROM TABLE_TREATMENT);
	}
	
	public	List<Diary> findDiaryNotes(Treatment treatment){
		diaryList = new ArrayList<Diary>();
		String selectQuery = "SELECT * FROM " + TABLE_DIARY + " WHERE TreatmentId = " + treatment.getId() + " ORDER BY _ID DESC";
		System.out.println("Forsøker å finne note med treatmentId" + treatment.getId());

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery,null);
		
		if (cursor.moveToFirst()) {
				do{
					Diary diary = new Diary();
					diary.setId(Integer.parseInt(cursor.getString(0)));
					diary.setTreatmentId(Integer.parseInt(cursor.getString(1)));
					diary.setTitle(cursor.getString(2));
					diary.setDate(cursor.getString(3));
					diary.setDescription(cursor.getString(4));
					diary.setRate(cursor.getString(5));
					diaryList.add(diary);

				}
			while (cursor.moveToNext());
		}
		db.close();
		return diaryList;
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DIARY);
		onCreate(db);
		
	}


	public void addDiary(Diary diary, Treatment treatment){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TID, treatment.getId());
		values.put(KEY_TITLE, diary.getTitle());
		values.put(KEY_DATE,diary.getDate());
		values.put(KEY_TEXT, diary.getDescription());
		values.put(KEY_RATE, diary.getRate());
		db.insert(TABLE_DIARY, null, values);
		System.out.println("Legger inn note med TreatmentId" + treatment.getId());
		db.close();
	}
	
	public void deleteNote(Diary note)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_DIARY + " WHERE _ID = " + note.getId());
		db.close();
	}
	
	

}
