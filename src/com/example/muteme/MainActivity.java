package com.example.muteme;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.example.muteme.MyReceiver;
import com.example.muteme.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity {

	DatePicker myDatePicker;
	TimePicker myTimePicker;

	TextView tvUnique; // Event Id

	TextView myOutDate; // From date
	TextView myOutDate2; // Till date

	TextView myOutTime; // To time
	TextView myInTime; // From time

	TextView myOutTime2; // To time
	TextView myInTime2;

	EditText myEvent; // Event name

	Button bMyConfirm;
	Button bMyReset;

	CheckBox cbTil;

	SQLiteDbAdaptor sqlDbAdaptor;

	int fetchedVariable = 0; // UID
	int silentMode = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		CheckBox cbSilentMode = (CheckBox) findViewById(R.id.cbSilentMode);
		CheckBox cbVibrateMode = (CheckBox) findViewById(R.id.cbVibrateMode);

		// cbSilentMode.setChecked(true);
		int iAlarmNum = 0, iAlarmFound = 0;

		try {

			Bundle extras = getIntent().getExtras();
			iAlarmNum = extras.getInt("ALARM_EVENT");

		} catch (Exception e) {

			iAlarmFound = 1;

		}

		if (iAlarmFound == 0 & iAlarmNum != 0) {

		//	MessageText.messageText(this, "alarm updated yipeee " + iAlarmNum);

			sqlDbAdaptor = new SQLiteDbAdaptor(this);

			sqlDbAdaptor.updateAlarm(iAlarmNum);

			setSound(iAlarmNum);

		} else {

			setContentView(R.layout.addnewevent);

			int iFound = 0;

			try {

				Bundle extras = getIntent().getExtras();
				fetchedVariable = extras.getInt("UPDATE_EVENT");

			} catch (Exception e) {
				iFound = 1;
			}

			if (iFound == 0 && fetchedVariable != 0) {

				sqlDbAdaptor = new SQLiteDbAdaptor(this);

				myEvent = (EditText) findViewById(R.id.edEventName);
				myInTime = (TextView) findViewById(R.id.tvTimeI);
				myOutDate = (TextView) findViewById(R.id.tvOutDate);
				myOutTime = (TextView) findViewById(R.id.tvTimeTo);
				myInTime2 = (TextView) findViewById(R.id.tvTimeI2);
				myOutTime2 = (TextView) findViewById(R.id.tvTimeTo2);

				tvUnique = (TextView) findViewById(R.id.tvEunique);

				String id = null;

				id = sqlDbAdaptor.getRow("" + fetchedVariable);

				if (id.length() > 1) {

					String s_content[] = id.split("-");

					tvUnique.setText(s_content[0].toString());

					myEvent.setText(s_content[1].toString());

					myOutDate.setText(s_content[2].toString());

					String tmp1 = s_content[3].toString();
					String tmp2 = s_content[4].toString();
					String s_from_hour;
					String s_from_minute;
					String s_to_hour;
					String s_to_minute;

					s_from_hour = tmp1.substring(0, tmp1.indexOf(":"));
					s_from_minute = tmp1.substring(tmp1.indexOf(":") + 1,
							tmp1.length());

					s_to_hour = tmp2.substring(0, tmp2.indexOf(":"));
					s_to_minute = tmp2.substring(tmp2.indexOf(":") + 1,
							tmp2.length());

					int i_tHr1 = Integer.parseInt(s_to_hour);
					int i_fMi1 = Integer.parseInt(s_from_minute);
					int i_fHr1 = Integer.parseInt(s_from_hour);
					int i_tMi1 = Integer.parseInt(s_to_minute);
					int i = 0, j = 0;
					if (i_fHr1 > 12) {
						i_fHr1 -= 12;
						i = 1;
					}
					if (i_tHr1 > 12) {
						i_tHr1 -= 12;
						j = 1;
					}
					if (i == 0) {
						myInTime.setText("" + i_fHr1 + ":" + i_fMi1 + "AM");
					}
					if (j == 0) {
						myOutTime.setText("" + i_tHr1 + ":" + i_tMi1 + "AM");
					}
					if (i == 1) {
						myInTime.setText("" + i_fHr1 + ":" + i_fMi1 + "PM");
					}
					if (j == 1) {
						myOutTime.setText("" + i_tHr1 + ":" + i_tMi1 + "PM");
					}
					myInTime2.setText(s_content[3].toString());
					myOutTime2.setText(s_content[4].toString());

					cbSilentMode = (CheckBox) findViewById(R.id.cbSilentMode);
					cbVibrateMode = (CheckBox) findViewById(R.id.cbVibrateMode);

					int iModeSet = Integer.parseInt(s_content[5].toString());

					if (iModeSet == 1) {
						silentMode = 1;
						cbVibrateMode.setChecked(true);
					} else {
						cbSilentMode.setChecked(true);
					}

					myOutDate.setTextColor(Color.BLUE);
					myInTime.setTextColor(Color.BLUE);
					myOutTime.setTextColor(Color.BLUE);
					myInTime2.setTextColor(Color.WHITE);
					myOutTime2.setTextColor(Color.WHITE);
				}

			} else {
				cbSilentMode = (CheckBox) findViewById(R.id.cbSilentMode);
				cbSilentMode.setChecked(true);
			}

			Button bPickDate = (Button) findViewById(R.id.bPickDate);
			Button bPickTime = (Button) findViewById(R.id.bPickTime);
			Button bPickTimeTo = (Button) findViewById(R.id.bTimeTo);

			final String currDate;
			// Check whether it is update mode

			// System date and time

			// String tempString;

			myInTime = (TextView) findViewById(R.id.tvTimeI);
			myOutDate = (TextView) findViewById(R.id.tvOutDate);
			myOutTime = (TextView) findViewById(R.id.tvTimeTo);
			myInTime2 = (TextView) findViewById(R.id.tvTimeI2);
			myOutTime2 = (TextView) findViewById(R.id.tvTimeTo2);

			final Valid valid = new Valid();
			Calendar cal = Calendar.getInstance();

			// From time

			int minute = cal.get(Calendar.MINUTE);
			// int hour = cal.get(Calendar.HOUR); // 12 hour format
			int hourOfDay = cal.get(Calendar.HOUR_OF_DAY); // 24 hour format

			// Date

			int day = cal.get(Calendar.DAY_OF_MONTH);
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int fromTime = hourOfDay + 1;
			int toTime = fromTime + 1;
			currDate = ("" + month + "/" + day + "/" + year);

			// extra cases. calendar and time validation start -Sahithya
			if (iFound == 1) {

				valid.defaultValidation(fromTime, month, day, year, toTime,
						minute, myOutDate, myOutTime, myInTime, myInTime2,
						myOutTime2);
			}
			// extra cases calendar and time validation end -Sahithya

			// From Time code
			bPickTime.setOnClickListener(new View.OnClickListener() {

				// @Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					/*
					 * Inflate the XML view. activity_main is in
					 * res/layout/time_picker.xml
					 */
					LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View view = inflater.inflate(R.layout.time_picker, null,
							false);

					// the time picker on the alert dialog, this is how to get
					// the
					// value
					myTimePicker = (TimePicker) view
							.findViewById(R.id.myTimePicker);

					/*
					 * To remove option for AM/PM, add the following line:
					 * 
					 * operatingHoursTimePicker.setIs24HourView(true);
					 */

					// the alert dialog
					new AlertDialog.Builder(MainActivity.this)
							.setView(view)
							.setTitle("Set Time")
							.setPositiveButton("Go",
									new DialogInterface.OnClickListener() {
										@TargetApi(11)
										public void onClick(
												DialogInterface dialog, int id) {
											Valid valid = new Valid();
											String currentHourText = myTimePicker
													.getCurrentHour()
													.toString();

											String currentMinuteText = myTimePicker
													.getCurrentMinute()
													.toString();
											int selectedHour = myTimePicker
													.getCurrentHour();
											int selectedMinute = myTimePicker
													.getCurrentMinute();

											// We cannot get AM/PM value since
											// the
											// returning value
											// will always be in 24-hour format.
											int x = valid
													.fromTimeValidate(
															selectedHour,
															selectedMinute,
															currDate,
															getBaseContext(),
															myOutDate);
											if (x == 1) {
												myInTime2 = (TextView) findViewById(R.id.tvTimeI2);
												myInTime2.setText(selectedHour
														+ ":"
														+ currentMinuteText);
												myInTime2
														.setTextColor(Color.WHITE);
												if (selectedHour > 12) {
													selectedHour = selectedHour - 12;

													showToast(selectedHour
															+ ":"
															+ currentMinuteText
															+ " PM");
												} else
													showToast(selectedHour
															+ ":"
															+ currentMinuteText
															+ " AM");

												dialog.cancel();

											}
										}

										private void showToast(String string) {
											// TODO Auto-generated method stub

											Toast.makeText(
													getBaseContext(),
													"Time Selected : " + string,
													Toast.LENGTH_LONG).show();
											String blankText = "";
											myInTime = (TextView) findViewById(R.id.tvTimeI);

											myInTime.setText(string);
											myInTime.setTextColor(Color.BLUE);
											myOutTime.setText(blankText);

										}

									}).show();
				}

			});

			// To Time code
			bPickTimeTo.setOnClickListener(new View.OnClickListener() {

				// @Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					/*
					 * Inflate the XML view. activity_main is in
					 * res/layout/time_picker.xml
					 */
					LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View view = inflater.inflate(R.layout.time_picker, null,
							false);

					// the time picker on the alert dialog, this is how to get
					// the
					// value
					myTimePicker = (TimePicker) view
							.findViewById(R.id.myTimePicker);

					/*
					 * To remove option for AM/PM, add the following line:
					 * 
					 * operatingHoursTimePicker.setIs24HourView(true);
					 */

					// the alert dialog
					new AlertDialog.Builder(MainActivity.this)
							.setView(view)
							.setTitle("Set Time")
							.setPositiveButton("Go",
									new DialogInterface.OnClickListener() {
										@TargetApi(11)
										public void onClick(
												DialogInterface dialog, int id) {

											String currentHourText = myTimePicker
													.getCurrentHour()
													.toString();

											String currentMinuteText = myTimePicker
													.getCurrentMinute()
													.toString();

											int selectedHour = myTimePicker
													.getCurrentHour();
											int selectedMinute = myTimePicker
													.getCurrentMinute();

											// We cannot get AM/PM value since
											// the
											// returning value
											// will always be in 24-hour format.

											int x = valid
													.fromTimeValidate(
															selectedHour,
															selectedMinute,
															currDate,
															getBaseContext(),
															myOutDate);
											if (x == 1) {

												String tmpString;
												String tmpString2;
												String s_from_hour;
												String s_from_minute;
												String s_to_hour;
												String s_to_minute;
												tmpString = myInTime2.getText()
														.toString();

												s_from_hour = tmpString
														.substring(0, tmpString
																.indexOf(":"));
												s_from_minute = tmpString.substring(
														tmpString.indexOf(":") + 1,
														tmpString.length());

												// int i_tHr =
												// Integer.parseInt(s_to_hour);
												int i_tHr = selectedHour;
												int i_fMi = Integer
														.parseInt(s_from_minute);
												int i_fHr = Integer
														.parseInt(s_from_hour);
												// int i_tMi =
												// Integer.parseInt(s_to_minute);
												int i_tMi = Integer
														.parseInt(currentMinuteText);
												int flag = 0;

												if (i_fHr > i_tHr
														|| (i_fHr == i_tHr && i_fMi > i_tMi)
														|| (i_fHr == i_tHr && i_fMi == i_tMi)) {
													Toast.makeText(
															getBaseContext(),
															"To time should be greater than from time",
															Toast.LENGTH_LONG)
															.show();
													flag = 1;
													myOutTime.setText("");
													myOutTime2.setText("");
												}

												if (flag == 0) {
													myOutTime2 = (TextView) findViewById(R.id.tvTimeTo2);
													myOutTime2
															.setText(selectedHour
																	+ ":"
																	+ currentMinuteText);
													myOutTime2
															.setTextColor(Color.WHITE);
													if (selectedHour > 12) {
														selectedHour = selectedHour - 12;

														showToast(selectedHour
																+ ":"
																+ currentMinuteText
																+ " PM");
													} else
														showToast(selectedHour
																+ ":"
																+ currentMinuteText
																+ " AM");

													dialog.cancel();
												}
											} else
												dialog.cancel();

										}

										private void showToast(String string) {
											// TODO Auto-generated method stub

											Toast.makeText(
													getBaseContext(),
													"Time Selected : " + string,
													Toast.LENGTH_LONG).show();

											myOutTime = (TextView) findViewById(R.id.tvTimeTo);

											myOutTime.setText(string);
											myOutTime.setTextColor(Color.BLUE);

										}

									}).show();
				}

			});

			// Date Code

			bPickDate.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub

					LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View view = inflater.inflate(R.layout.date_picker, null,
							false);

					// the time picker on the alert dialog, this is how to get
					// the
					// value
					myDatePicker = (DatePicker) view
							.findViewById(R.id.myDatePicker);

					// so that the calendar view won't appear
					myDatePicker.setCalendarViewShown(false);

					// the alert dialog
					new AlertDialog.Builder(MainActivity.this)
							.setView(view)
							.setTitle("Set Date")
							.setPositiveButton("Go",
									new DialogInterface.OnClickListener() {
										@TargetApi(11)
										public void onClick(
												DialogInterface dialog, int id) {
											Valid vld = new Valid();
											/*
											 * In the docs of the calendar
											 * class, January = 0, so we have to
											 * add 1 for getting correct month.
											 * http://goo.gl/9ywsj
											 */
											int month = myDatePicker.getMonth() + 1;
											int day = myDatePicker
													.getDayOfMonth();
											int year = myDatePicker.getYear();
											int x = vld.dateValidation(
													getBaseContext(), month,
													day, year);
											if (x == 1) {
												showToast(month + "/" + day
														+ "/" + year);

												dialog.cancel();
											}

										}

										private void showToast(String string) {
											// TODO Auto-generated method stub

											Toast.makeText(
													getBaseContext(),
													"Date Selected : " + string,
													Toast.LENGTH_LONG).show();
											String blankText = "";
											myOutDate = (TextView) findViewById(R.id.tvOutDate);

											myOutDate.setText(string);
											myOutDate.setTextColor(Color.BLUE);
											myInTime.setText(blankText);
											myOutTime.setText(blankText);
										}

									}).show();

				}
			});

			CheckBox cbTill = (CheckBox) findViewById(R.id.cbTill);

			// CheckBox Till Code
			cbTill.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					CheckBox cbTill = (CheckBox) findViewById(R.id.cbTill);

					if (cbTill.isChecked()) {

						// TODO Auto-generated method stub
						LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						View view = inflater.inflate(R.layout.date_picker,
								null, false);

						// the time picker on the alert dialog, this is how to
						// get
						// the
						// value
						myDatePicker = (DatePicker) view
								.findViewById(R.id.myDatePicker);

						// so that the calendar view won't appear
						myDatePicker.setCalendarViewShown(false);

						// the alert dialog
						new AlertDialog.Builder(MainActivity.this)
								.setView(view)
								.setTitle("Set Date")
								.setPositiveButton("Go",
										new DialogInterface.OnClickListener() {
											@TargetApi(11)
											public void onClick(
													DialogInterface dialog,
													int id) {
												Valid vld = new Valid();
												/*
												 * In the docs of the calendar
												 * class, January = 0, so we
												 * have to add 1 for getting
												 * correct month.
												 * http://goo.gl/9ywsj
												 */
												int month = myDatePicker
														.getMonth() + 1;
												int day = myDatePicker
														.getDayOfMonth();
												int year = myDatePicker
														.getYear();
												int x = vld.dateValidation(
														getBaseContext(),
														month, day, year);
												if (x == 1) {
													showToast(month + "/" + day
															+ "/" + year);

													dialog.cancel();

												}
											}

											private void showToast(String temp) {
												// TODO Auto-generated method
												// stub

												Toast.makeText(
														getBaseContext(),
														"Date Selected : "
																+ temp,
														Toast.LENGTH_LONG)
														.show();

												myOutDate2 = (TextView) findViewById(R.id.tvTillOutDate);

												myOutDate2.setText(temp);
												myOutDate2
														.setTextColor(Color.BLUE);

											}

										}).show();

					} else {

						myOutDate2.setText("");

					}
				}
			});

		}

	}

	public void cutAlarm(int iNum) {

		String s_temp = "a";
		s_temp = s_temp + iNum;

		Intent intentAlarmIn = new Intent(this, MyReceiver.class);
		intentAlarmIn.setData(Uri.parse(s_temp));

		AlarmManager inAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		inAlarmManager.cancel(PendingIntent.getBroadcast(this, 1,
				intentAlarmIn, 0));

		s_temp = null;
		s_temp = "b";
		s_temp = s_temp + iNum;

		Intent intentAlarmOut = new Intent(this, MyEnder.class);
		intentAlarmOut.setData(Uri.parse(s_temp));

		AlarmManager outAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		outAlarmManager.cancel(PendingIntent.getBroadcast(this, 1,
				intentAlarmOut, 0));

		//MessageText.messageText(this, "Alarm end cancelled.." + s_temp);

	}

    public void listenWifi() {

        String s_temp = "a";

        Intent intentAlarmIn = new Intent(this, MyWifiReceiver.class);
        intentAlarmIn.setData(Uri.parse(s_temp));

        AlarmManager inAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        inAlarmManager.cancel(PendingIntent.getBroadcast(this, 1,
                intentAlarmIn, 0));

    }

	public void setSound(int iNum) {

		String id = sqlDbAdaptor.getRow("" + iNum);
		String s_temp[] = id.split("-");

		//MessageText.messageText(this, "sound..mode...");

		int iMode = Integer.parseInt(s_temp[5]);

		if (iMode == 0) {

			AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			audio.setRingerMode(AudioManager.RINGER_MODE_SILENT);

		} else {

			AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);

		}

	}

	public void clickConfirm(View v) {

		sqlDbAdaptor = new SQLiteDbAdaptor(this);

		myEvent = (EditText) findViewById(R.id.edEventName);

		if (fetchedVariable != 0) {

			sqlDbAdaptor.updateAlarm(fetchedVariable);
			sqlDbAdaptor.deleteRecord(fetchedVariable);

			//MessageText.messageText(this, "Record deleted.. " + fetchedVariable);

			cutAlarm(fetchedVariable);

			fetchedVariable = 0;
		}

		Valid va = new Valid();

		String s_event = myEvent.getText().toString();
		String s_inDate = myOutDate.getText().toString();
		String s_outDate = null;

		try {
			s_outDate = myOutDate2.getText().toString();
		} catch (Exception e) {
			s_outDate = null;
		}

		int i_mode = silentMode;

		String s_inTime = myInTime2.getText().toString();
		String s_outTime = myOutTime2.getText().toString();

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();

		try {

			start.setTime(sdf.parse(s_inDate));

		} catch (Exception e) {

		}
		if (s_outDate == null) {

			start.add(start.DATE, 7);

			int month = start.get(Calendar.MONTH) + 1;

			s_outDate = "" + month + "/" + start.get(Calendar.DATE) + "/"
					+ start.get(Calendar.YEAR);

		}

		try {

			start.setTime(sdf.parse(s_inDate));
			end.setTime(sdf.parse(s_outDate));

		} catch (Exception e) {

		}

		int valid = va.confirmValidation(this, s_event, s_inDate, s_inTime,
				s_outTime);

		if (valid == 1) {
			createEvent(s_event, s_inDate, s_inTime, s_outTime, i_mode);

			CheckBox cb_sun = (CheckBox) findViewById(R.id.cbSunday);
			CheckBox cb_mon = (CheckBox) findViewById(R.id.cbMonday);
			CheckBox cb_tue = (CheckBox) findViewById(R.id.cbTuesday);
			CheckBox cb_wed = (CheckBox) findViewById(R.id.cbWednesday);
			CheckBox cb_thu = (CheckBox) findViewById(R.id.cbThursday);
			CheckBox cb_fri = (CheckBox) findViewById(R.id.cbFriday);
			CheckBox cb_sat = (CheckBox) findViewById(R.id.cbSaturday);

			while (start.before(end)) {

				start.add(start.DATE, 1);

				s_inDate = null;

				int month = start.get(Calendar.MONTH) + 1;

				s_inDate = "" + month + "/" + start.get(Calendar.DATE) + "/"
						+ start.get(Calendar.YEAR);

				switch (start.get(Calendar.DAY_OF_WEEK)) {

				case 1:
					if (cb_sun.isChecked()) {
						createEvent(s_event, s_inDate, s_inTime, s_outTime,
								i_mode);
					}
					break;

				case 2:
					if (cb_mon.isChecked()) {
						createEvent(s_event, s_inDate, s_inTime, s_outTime,
								i_mode);
					}
					break;

				case 3:
					if (cb_tue.isChecked()) {
						createEvent(s_event, s_inDate, s_inTime, s_outTime,
								i_mode);
					}
					break;

				case 4:
					if (cb_wed.isChecked()) {
						createEvent(s_event, s_inDate, s_inTime, s_outTime,
								i_mode);
					}
					break;

				case 5:
					if (cb_thu.isChecked()) {
						createEvent(s_event, s_inDate, s_inTime, s_outTime,
								i_mode);
					}
					break;

				case 6:
					if (cb_fri.isChecked()) {
						createEvent(s_event, s_inDate, s_inTime, s_outTime,
								i_mode);
					}
					break;

				case 7:
					if (cb_sat.isChecked()) {
						createEvent(s_event, s_inDate, s_inTime, s_outTime,
								i_mode);
					}
					break;

				default:
					break;
				}
			}
			Intent openMainActivity = new Intent("com.example.muteme.SCREEN");
			startActivity(openMainActivity);

		} else {
			MessageText.messageText(this, "Validation failed");
		}

	}

	public void createEvent(String s_event, String s_inDate, String s_inTime,
			String s_outTime, int i_mode) {

		Valid va = new Valid();

		int valid = va.confirmValidation(this, s_event, s_inDate, s_inTime,
				s_outTime);

		if (valid == 1) {

			long id = 0;

			try {

				id = sqlDbAdaptor.insertData(s_event, s_inDate, s_inTime,
						s_outTime, i_mode);

			} catch (Exception e) {

				MessageText.messageText(this, s_inDate);
			}

			if (id < 0) {

				MessageText.messageText(this, "Failed to create event" + s_event);

			} else {

				MessageText.messageText(this, s_event + " created");

				int i_rowCount = sqlDbAdaptor.getCount();

				String s_inAlarm = "a" + i_rowCount;
				String s_outAlarm = "b" + i_rowCount;

				// Call Alarm
				setAlarm(s_inDate, s_inTime, s_outTime, s_inAlarm, s_outAlarm);
				/*
				 * Intent openMainActivity = new Intent(
				 * "com.example.muteme.SCREEN");
				 * startActivity(openMainActivity);
				 */
			}

		}

	}

	// For silent Mode
	public void onClickSilent(View v) {

		CheckBox checkBox = (CheckBox) findViewById(R.id.cbVibrateMode);
		CheckBox cbSilentMode = (CheckBox) findViewById(R.id.cbSilentMode);

		boolean check = cbSilentMode.isChecked();

		if (check == true) {
			checkBox.setChecked(false);
			silentMode = 0;
		}

	}

	// For Vibration Mode
	public void onClickVibrate(View v) {

		CheckBox checkBox = (CheckBox) findViewById(R.id.cbSilentMode);
		CheckBox cbVibrateMode = (CheckBox) findViewById(R.id.cbVibrateMode);

		boolean check = cbVibrateMode.isChecked();

		if (check == true) {
			checkBox.setChecked(false);
			silentMode = 1;
		}

	}

	public void onClickAnyCb(View v) {
		CheckBox checkBox = (CheckBox) findViewById(R.id.cbAll);
		CheckBox cb_mon = (CheckBox) findViewById(R.id.cbMonday);
		CheckBox cb_tue = (CheckBox) findViewById(R.id.cbTuesday);
		CheckBox cb_wed = (CheckBox) findViewById(R.id.cbWednesday);
		CheckBox cb_thu = (CheckBox) findViewById(R.id.cbThursday);
		CheckBox cb_fri = (CheckBox) findViewById(R.id.cbFriday);
		CheckBox cb_sat = (CheckBox) findViewById(R.id.cbSaturday);
		CheckBox cb_sun = (CheckBox) findViewById(R.id.cbSunday);

		if (cb_mon.isChecked() == false || cb_tue.isChecked() == false
				|| cb_wed.isChecked() == false || cb_thu.isChecked() == false
				|| cb_fri.isChecked() == false || cb_sat.isChecked() == false
				|| cb_sun.isChecked() == false) {
			checkBox.setChecked(false);
		} else {
			checkBox.setChecked(true);
		}

	}

	public void onClickAll(View v) {

		// is chkIos checked?
		CheckBox checkBox = (CheckBox) findViewById(R.id.cbAll);
		CheckBox cb_mon = (CheckBox) findViewById(R.id.cbMonday);
		CheckBox cb_tue = (CheckBox) findViewById(R.id.cbTuesday);
		CheckBox cb_wed = (CheckBox) findViewById(R.id.cbWednesday);
		CheckBox cb_thu = (CheckBox) findViewById(R.id.cbThursday);
		CheckBox cb_fri = (CheckBox) findViewById(R.id.cbFriday);
		CheckBox cb_sat = (CheckBox) findViewById(R.id.cbSaturday);
		CheckBox cb_sun = (CheckBox) findViewById(R.id.cbSunday);
		boolean check = checkBox.isChecked();

		if (check == true) {

			cb_mon.setChecked(true);
			cb_tue.setChecked(true);
			cb_wed.setChecked(true);
			cb_thu.setChecked(true);
			cb_fri.setChecked(true);
			cb_sat.setChecked(true);
			cb_sun.setChecked(true);
			// case 2
		}

		if (check == false) {

			cb_mon.setChecked(false);
			cb_tue.setChecked(false);
			cb_wed.setChecked(false);
			cb_thu.setChecked(false);
			cb_fri.setChecked(false);
			cb_sat.setChecked(false);
			cb_sun.setChecked(false);
		}

	}

	public void setAlarm(String s_inDate, String s_inTime, String s_outTime,
			String s_inAlarm, String s_outAlarm) {

		// Split IN date
		String s_date_tmp[] = s_inDate.split("/");

		int i_month = Integer.parseInt(s_date_tmp[0]);
		int i_day = Integer.parseInt(s_date_tmp[1]);
		int i_year = Integer.parseInt(s_date_tmp[2]);

		// Split inTime for Alarm 1
		String s_time_tmp[] = s_inTime.split(":");

		int i_hour = Integer.parseInt(s_time_tmp[0]);
		int i_min = Integer.parseInt(s_time_tmp[1]);

		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.MONTH, i_month - 1);
		calendar.set(Calendar.YEAR, i_year);
		calendar.set(Calendar.DAY_OF_MONTH, i_day);
		calendar.set(Calendar.HOUR_OF_DAY, i_hour);
		calendar.set(Calendar.MINUTE, i_min);
		calendar.set(Calendar.SECOND, 0);

		Long l_universalTime = new GregorianCalendar().getTimeInMillis();

		Long l_time = l_universalTime;

		Long l_diff = calendar.getTimeInMillis() - l_time;

		// set IN time intent
		Intent intentAlarmIn = new Intent(this, MyReceiver.class);
		intentAlarmIn.setData(Uri.parse(s_inAlarm));

		// set IN time ALARM
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		alarmManager.set(AlarmManager.RTC_WAKEUP, l_time + l_diff,
				PendingIntent.getBroadcast(this, 1, intentAlarmIn,
						PendingIntent.FLAG_UPDATE_CURRENT));

	//	MessageText.messageText(this, "Alarm Scheduled (IN): " + l_diff);

		// Split outTime for Alarm 2
		s_time_tmp = s_outTime.split(":");

		i_hour = Integer.parseInt(s_time_tmp[0]);
		i_min = Integer.parseInt(s_time_tmp[1]);

		calendar.set(Calendar.MONTH, i_month - 1);
		calendar.set(Calendar.YEAR, i_year);
		calendar.set(Calendar.DAY_OF_MONTH, i_day);
		calendar.set(Calendar.HOUR_OF_DAY, i_hour);
		calendar.set(Calendar.MINUTE, i_min);
		calendar.set(Calendar.SECOND, 0);

		l_diff = calendar.getTimeInMillis() - l_universalTime;

		// set OUT time intent
		Intent intentAlarmOut = new Intent(this, MyEnder.class);
		intentAlarmOut.setData(Uri.parse(s_outAlarm));

		// set OUT time ALARM

		alarmManager.set(AlarmManager.RTC_WAKEUP, l_universalTime + l_diff,
				PendingIntent.getBroadcast(this, 1, intentAlarmOut,
						PendingIntent.FLAG_UPDATE_CURRENT));

	//	MessageText.messageText(this, "Alarm Scheduled (OUT): " + l_diff);

	}

	public void clickCancel(View v) {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		int id = item.getItemId();

		if (id == R.id.action_settings) {

			return true;

		}

		return super.onOptionsItemSelected(item);
	}
}