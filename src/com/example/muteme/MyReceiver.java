package com.example.muteme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.net.Uri;

public class MyReceiver extends BroadcastReceiver {
	
	SQLiteDbAdaptor sqlDbAdaptor;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Intent service1 = new Intent(context, MyAlarmService.class);
		context.startService(service1);

		Uri u1 = intent.getData();

		String s_temp = u1.toString();

		s_temp = s_temp.substring(1);
		
		int iNum = Integer.parseInt(s_temp);

		Intent service2 = new Intent(context, MainActivity.class);
		
		service2.putExtra("ALARM_EVENT", iNum);
		service2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		service2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		context.startActivity(service2);
		
		MessageText.messageText(context, "Alarm Event  started " + s_temp);

	}

}