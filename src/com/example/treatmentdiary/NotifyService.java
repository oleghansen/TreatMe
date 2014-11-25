package com.example.treatmentdiary;


import java.util.Calendar;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NotifyService extends Service{

	DbHandlerDiary dbDiary = new DbHandlerDiary(this);
	DbHandlerTreatments db = new DbHandlerTreatments(this);
	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
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
	
	
	@Override
	public int onStartCommand(Intent intent,int flags,	int startId)	{	
		if(!isNoteToday())
		{	
			NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			Intent i = new Intent (this, MainActivity.class);
			PendingIntent pIntent = PendingIntent.getActivity(this, 0, i, 0);
			Notification noti = new Notification.Builder(this)
			.setContentTitle(getString(R.string.app_name))
			.setContentText(getString(R.string.today))
			.setSmallIcon(R.drawable.ic_treatmentdiary_small)
			.setContentIntent(pIntent).build();
			noti.flags |= Notification.FLAG_AUTO_CANCEL;
			notificationManager.notify(0,noti);
		}	
	return super.onStartCommand(intent,	flags, startId);	
	}
}
