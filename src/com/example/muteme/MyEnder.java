package com.example.muteme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class MyEnder extends BroadcastReceiver {

	SQLiteDbAdaptor sqlDbAdaptor;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		Intent service1 = new Intent(context, MyEndService.class);
		context.startService(service1);
				
		Uri u1 = intent.getData();
						
		MessageText.messageText(context, "END Event completed "+u1.toString());			
				
		
	}

}