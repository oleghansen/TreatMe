package com.example.treatmentdiary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class TreatMeBroadcastReceiver extends BroadcastReceiver
{	
	@Override
	public void onReceive(Context context,	Intent intent)	{	
		 	Intent i= new Intent("com.example.birthdaysms.PeriodicService");
		 	i.setClass(context, PeriodicService.class);
		 	context.startService(i);
	}
}