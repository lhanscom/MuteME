package com.example.muteme;

import android.content.Context;
import android.widget.Toast;

public class MessageText {
	
	public static void messageText(Context context, String message){
	
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		
	}

}
