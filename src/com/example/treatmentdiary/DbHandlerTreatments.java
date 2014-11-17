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

public class DbHandlerTreatments extends SQLiteOpenHelper 
{
	static String TABLE_TREATMENT="Treatments";
	static String KEY_ID="_ID";
	static String KEY_NAME="Name";
	static String KEY_METHOD="Method";
	static String KEY_STARTED="Started";
	static String KEY_EXPECTEDTIME="ExpectedTime";
	static String KEY_ISOLD="IsOld";
	static String KEY_RATING="Rating";
	static int DATABASE_VERSION=111;
	static String DATABASE_NAME="TreatmentDatabase";
	private List<Treatment> treatmentList; 
	
	public DbHandlerTreatments(Context context)
	{
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		String CREATE_TABLE = "CREATE TABLE " + 
				TABLE_TREATMENT + "(" + 
				KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				KEY_NAME + " TEXT, " +
				KEY_METHOD + " TEXT, " +
				KEY_STARTED + " DATETIME, " +
				KEY_EXPECTEDTIME + " TEXT, " + 
				KEY_ISOLD + " INTEGER, "+ 
				KEY_RATING + " INTEGER " + ")";
		Log.d("SQL", CREATE_TABLE);
		db.execSQL(CREATE_TABLE);
		
	}
	
	public void resetDatabase()
	{
		//SQLiteDatabase db = this.getWritableDatabase();
		//db.execSQL("DELETE * FROM TABLE_TREATMENT);
	}
	
	public	List<Treatment> findAllTreatments(){
		treatmentList = new ArrayList<Treatment>();
		String selectQuery = "SELECT * FROM " + TABLE_TREATMENT + " WHERE IsOld = 0";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery,null);
		
		if (cursor.moveToFirst()) {
				do{
					Treatment treatment = new Treatment();
					treatment.setId(Integer.parseInt(cursor.getString(0)));
					treatment.setName(cursor.getString(1));
					treatment.setMethod(cursor.getString(2));
					treatment.setStarted(cursor.getString(3));
					treatment.setExpectedTime(cursor.getString(4));
					treatment.setIsOld(Integer.parseInt(cursor.getString(5)));
					treatment.setRating(Integer.parseInt(cursor.getString(6)));
					treatmentList.add(treatment);

				}
			while (cursor.moveToNext());
		}
		db.close();
		return treatmentList;
	}
	
	public	List<Treatment> findOldTreatments(){
		treatmentList = new ArrayList<Treatment>();
		String selectQuery = "SELECT * FROM " + TABLE_TREATMENT + " WHERE IsOld = 1";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery,null);
		
		if (cursor.moveToFirst()) {
				do{
					Treatment treatment = new Treatment();
					treatment.setId(Integer.parseInt(cursor.getString(0)));
					treatment.setName(cursor.getString(1));
					treatment.setMethod(cursor.getString(2));
					treatment.setStarted(cursor.getString(3));
					treatment.setExpectedTime(cursor.getString(4));
					treatment.setIsOld(Integer.parseInt(cursor.getString(5)));
					treatment.setRating(Integer.parseInt(cursor.getString(6)));
					treatmentList.add(treatment);
				}
			while (cursor.moveToNext());
		}
		db.close();
		return treatmentList;
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TREATMENT);
		onCreate(db);
		
	}

	public Treatment findTreatment(int id)
	{
		Treatment treatment = new Treatment();
		String selectQuery = "SELECT * FROM " + TABLE_TREATMENT + " WHERE _ID = " + id + " ORDER BY _ID DESC";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery,null);
		
		if (cursor.moveToFirst()) {
				do{
					treatment.setId(Integer.parseInt(cursor.getString(0)));
					treatment.setName(cursor.getString(1));
					treatment.setMethod(cursor.getString(2));
					treatment.setStarted(cursor.getString(3));
					treatment.setExpectedTime(cursor.getString(4));
					treatment.setIsOld(Integer.parseInt(cursor.getString(5)));
					treatment.setRating(Integer.parseInt(cursor.getString(6)));
				}
			while (cursor.moveToNext());
		}
		db.close();
		return treatment;
	}
	
	public void addTreatment(Treatment treatment){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, treatment.getName());
		values.put(KEY_METHOD,treatment.getMethod());
		values.put(KEY_STARTED, treatment.getStarted());
		values.put(KEY_EXPECTEDTIME, treatment.getExpectedTime());
		values.put(KEY_ISOLD, treatment.getIsOld());
		values.put(KEY_RATING, treatment.getRating());
		db.insert(TABLE_TREATMENT, null, values);
		db.close();
	}
	
	public void updateTreatment(Treatment treatment, Treatment updated)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, updated.getName());
		values.put(KEY_METHOD,updated.getMethod());
		values.put(KEY_STARTED, updated.getStarted());
		values.put(KEY_EXPECTEDTIME, updated.getExpectedTime());
		values.put(KEY_ISOLD,  updated.getIsOld());
		db.update(TABLE_TREATMENT, values, "_id=" + treatment.getId(), null);
	}
	
	public void makeTreatmentOld(Treatment treatment)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_ISOLD, 1);
		db.update(TABLE_TREATMENT, values, "_id=" + treatment.getId(), null);
	}
	
	public void makeTreatmentOpen(Treatment treatment)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_ISOLD, 0);
		db.update(TABLE_TREATMENT, values, "_id=" + treatment.getId(), null);
	}
	
	public void rateTreatment(Treatment treatment, int rate)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_RATING, rate);
		db.update(TABLE_TREATMENT, values, "_id=" + treatment.getId(), null);
	}
	public void deleteTreatment(Treatment treatment)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_TREATMENT + " WHERE _ID = " + treatment.getId());
		db.close();
	}
	
	

}
