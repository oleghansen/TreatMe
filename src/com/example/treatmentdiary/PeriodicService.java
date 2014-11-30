package com.example.treatmentdiary;


import java.util.Calendar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;



public class PeriodicService extends Service{
	public static AlarmManager alarm;
	public static PendingIntent pintent;
	public static final String MY_SERVICE = "com.example.treatmentdiary.PeriodicService";
	public static final int hour = 21, minute = 0, second=0;
	
	@Override
	public int onStartCommand(Intent intent, int flags,	int startId)	{	
	 		 Calendar calendar = Calendar.getInstance();
		 	 Intent	i = new Intent(this, NotifyService.class);
		 	 pintent =	PendingIntent.getService(this,	0,	i,	0);	
		 	 alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);	
		 	 calendar.setTimeInMillis(System.currentTimeMillis());
		 	 calendar.set(Calendar.HOUR_OF_DAY, hour);
		 	 calendar.set(Calendar.MINUTE,minute);
		 	 calendar.set(Calendar.SECOND, second);

			 alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000*60*60*24, pintent);

		 	 return super.onStartCommand(intent, flags, startId);	
	}
	
	public static void stopService()
	{
		alarm.cancel(pintent);
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stubo
		return null;
	}
}
