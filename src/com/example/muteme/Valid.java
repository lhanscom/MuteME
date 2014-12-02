package com.example.muteme;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

public class Valid extends Activity {

	final Calendar cal = Calendar.getInstance();

	public void defaultValidation(int fromTime, int month, int day, int year,
			int toTime, int minute, TextView myOutDate, TextView myOutTime,
			TextView myInTime, TextView myInTime2, TextView myOutTime2) {

		final String s_outDate;
		final String s_inTime;
		final String s_outTime;
		final String s_inTime2;
		final String s_outTime2;
		int fromTime2= fromTime;
		int toTime2=toTime;

		if (fromTime >= 24) {

			fromTime = 00;
			toTime = fromTime + 1;

			if ((month == 2 && day == 28)) {

				day = 1;
				month = 3;

			} else {
				
				switch (day) {
				case 30:
					if (month == 4 || month == 6 || month == 9 || month == 11) {
						month += 1;
						day = 1;
					}
					break;
					
				case 31:
					if (month != 12) {
						month += 1;
						day = 1;
					} else if (month == 12) {
						month = 1;
						day = 1;
						year += 1;
					}
					break;
					
				default:
					if (!(month == 2 && day == 28)) {
						day++;
					}
					break;
				}
			}
		}
		if (toTime == 24) {
			toTime = 00;
		}

		if(fromTime>12)
		{
			myInTime.setTextColor(Color.BLUE);
			fromTime2 =fromTime-12;
			s_inTime = ("" + fromTime2 + ":" + minute +" PM");
		}
		else{
			s_inTime = ("" + fromTime2 + ":" + minute + " AM");
		}
		if(toTime>12)
		{
			toTime2=toTime-12;
			s_outTime = ("" + toTime2 + ":" + minute+" PM");
			
		}
		else
			s_outTime = ("" + toTime2 + ":" + minute+ " AM");
		

		// change date

		// default date

		// default date
		s_outDate = ("" + month + "/" + day + "/" + year);

		myOutDate.setText(s_outDate);
		myOutDate.setTextColor(Color.BLUE);
		// default date end
		s_inTime2 =("" + fromTime + ":" + minute);
		s_outTime2 =("" + toTime + ":" + minute);
		// default time. both from and to	
		
		myInTime.setText(s_inTime);
		myInTime.setTextColor(Color.BLUE);

		//s_outTime = ("" + i_toTime + ":" + i_minute);
		
		myOutTime.setText(s_outTime);
		myOutTime.setTextColor(Color.BLUE);
		

		myInTime2.setText(s_inTime2);
		myInTime2.setTextColor(Color.WHITE);
		myOutTime2.setText(s_outTime2);
		myOutTime2.setTextColor(Color.WHITE);

		// default time end

		// default time end
	}

	public int dateValidation(Context context, int month, int day, int year) {

		if ((year < cal.get(Calendar.YEAR))
				|| (month < cal.get(Calendar.MONTH) + 1 && year == cal
						.get(Calendar.YEAR))
				|| (month == cal.get(Calendar.MONTH) + 1
						&& day < cal.get(Calendar.DAY_OF_MONTH) && year == cal
						.get(Calendar.YEAR)))

		{
			Toast.makeText(context, "only choose future dates",
					Toast.LENGTH_LONG).show();
			return 0;
		} else {
			return 1;
		}

	}

	public int fromTimeValidate(int selectedHour, int selectedMinute,
			String currDate, Context context, TextView myOutDate) {
		if (currDate.equals(myOutDate.getText().toString())) {

			if (selectedHour < cal.get(Calendar.HOUR_OF_DAY)
					|| selectedHour == cal.get(Calendar.HOUR_OF_DAY)
					&& selectedMinute < cal.get(Calendar.MINUTE)) {
				Toast.makeText(context,
						"selected time has to be in the future",
						Toast.LENGTH_LONG).show();
				return 0;
			} else {
				return 1;
			}

		}
		return 1;
	}

	// code when the confirm button is clicked
	public int confirmValidation(Context context, String E_Name, String E_date,
			String E_from_time, String E_to_time) {
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(E_Name);
		boolean b = m.find();
		// to check if all main fields are filled or not
		if ((E_Name.isEmpty() || E_date.isEmpty() || E_from_time.isEmpty() || E_to_time
				.isEmpty())) {
			Toast.makeText(context, "Required fields cannot be left empty",
					Toast.LENGTH_LONG).show();
			return -1;
		}
		if(E_Name.length()>10){
			Toast.makeText(context, "Event Name format: Max length 10 characters long",
					Toast.LENGTH_LONG).show();
			return -1;
			
		}
		// bcz we don't want special characters on the event name
		if (b) {
			Toast.makeText(context, "Event Name format: A-Z, 0-9 only",
					Toast.LENGTH_LONG).show();
			// myOutDate.setText("");
			return -1;
		}
		return 1;

	}
	// end for code -- confirm button click

}