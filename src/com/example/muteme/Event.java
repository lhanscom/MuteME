package com.example.muteme;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Event extends Activity {

	DatePicker myDatePicker;
	TextView tvDateFrom;
	TextView tvDateTo;

	int flag_from = 0;
	int flag_to = 0;

	SQLiteDbAdaptor sqlDbAdaptor;

	@Override
	public void onBackPressed() {

	}

	public void ClearDates(View v) {

		Intent openMainActivity = new Intent("com.example.muteme.EVENT");
		startActivity(openMainActivity);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.event);

		Button bBack = (Button) findViewById(R.id.bBack);

		bBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent openMainActivity = new Intent(
						"com.example.muteme.SCREEN");
				startActivity(openMainActivity);

			}
		});

		Button bDateFrom = (Button) findViewById(R.id.bDateFrom);
		Button bDateTo = (Button) findViewById(R.id.bDateTo);

		// Date Code

		bDateFrom.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.date_picker, null, false);

				myDatePicker = (DatePicker) view
						.findViewById(R.id.myDatePicker);

				myDatePicker.setCalendarViewShown(false);

				new AlertDialog.Builder(Event.this)
						.setView(view)
						.setTitle("Set Date")
						.setPositiveButton("Go",
								new DialogInterface.OnClickListener() {
									@TargetApi(11)
									public void onClick(DialogInterface dialog,
											int id) {

										int month = myDatePicker.getMonth() + 1;
										int day = myDatePicker.getDayOfMonth();
										int year = myDatePicker.getYear();

										showToast(month + "/" + day + "/"
												+ year);

										flag_from = 1;

										dialog.cancel();

									}

									private void showToast(String string) {
										// TODO Auto-generated method stub

										Toast.makeText(getBaseContext(),
												"Date Selected : " + string,
												Toast.LENGTH_LONG).show();

										tvDateFrom = (TextView) findViewById(R.id.tvDateFrom);
										tvDateFrom.setText(string);
										tvDateFrom.setTextColor(Color.BLUE);

									}

								}).show();

			}
		});

		// Date Code To

		bDateTo.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.date_picker, null, false);

				myDatePicker = (DatePicker) view
						.findViewById(R.id.myDatePicker);

				myDatePicker.setCalendarViewShown(false);

				new AlertDialog.Builder(Event.this)
						.setView(view)
						.setTitle("Set Date")
						.setPositiveButton("Go",
								new DialogInterface.OnClickListener() {
									@TargetApi(11)
									public void onClick(DialogInterface dialog,
											int id) {

										int month = myDatePicker.getMonth() + 1;
										int day = myDatePicker.getDayOfMonth();
										int year = myDatePicker.getYear();

										showToast(month + "/" + day + "/"
												+ year);

										flag_to = 1;

										dialog.cancel();

									}

									private void showToast(String string) {
										// TODO Auto-generated method stub

										Toast.makeText(getBaseContext(),
												"Date Selected : " + string,
												Toast.LENGTH_LONG).show();

										tvDateTo = (TextView) findViewById(R.id.tvDateTo);

										tvDateTo.setText(string);
										tvDateTo.setTextColor(Color.BLUE);

									}

								}).show();

			}
		});

	}

	public void ShowDate(View v) {

		if (flag_from == 1 && flag_to == 0) {

			String s_inDate = tvDateFrom.getText().toString();

			s_inDate = '"' + s_inDate + '"';

			CalculateView(s_inDate);

		} else if (flag_from == 1 && flag_to == 1) {

			int i_alert = 0;

			String s_inDate = tvDateFrom.getText().toString();
			String s_outDate = tvDateTo.getText().toString();

			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

			Calendar start = Calendar.getInstance();
			Calendar end = Calendar.getInstance();

			try {

				start.setTime(sdf.parse(s_inDate));
				end.setTime(sdf.parse(s_outDate));

			} catch (Exception e) {

				i_alert = 1;

			}

			if (i_alert == 0) {

				if (s_inDate.equals(s_outDate)) {

					s_inDate = tvDateFrom.getText().toString();

					s_inDate = '"' + s_inDate + '"';

					CalculateView(s_inDate);

				} else {
					String s_final = "";

					while (start.before(end)) {

						s_inDate = null;

						int month = start.get(Calendar.MONTH) + 1;

						s_inDate = "" + month + "/" + start.get(Calendar.DATE)
								+ "/" + start.get(Calendar.YEAR);

						s_final = s_final + '"' + s_inDate + '"' + ',';

						start.add(start.DATE, 1);

					}

					s_final = s_final.substring(0, s_final.length() - 1);
					CalculateView(s_final);
				}
			}

		}

	}

	public void CalculateView(String s_inDate) {

		sqlDbAdaptor = new SQLiteDbAdaptor(this);

		String id = null;

		id = sqlDbAdaptor.getDates(s_inDate);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		params.addRule(RelativeLayout.BELOW, R.id.RR2);

		TableLayout tbLayout = (TableLayout) findViewById(R.id.TL1);
		tbLayout.setStretchAllColumns(true);
		tbLayout.bringToFront();
		tbLayout.setLayoutParams(params);
		tbLayout.removeAllViews();

		// MessageText.messageText(this, "message" + id);

		if (id.length() > 1) {

			String s_content[] = id.split("#");

			final TextView[][] myTextViews = new TextView[s_content.length][7]; // Array

			// ScrollView sv = new ScrollView(this);
			// sv.setSmoothScrollingEnabled(true);

			for (int i = 0; i < s_content.length; i++) {

				String tempo = "" + 0; // initialize

				TableRow tr = new TableRow(this);

				tr.setPadding(1, 1, 1, 1);

				String out = s_content[i];

				String temp[] = out.split("-");

				for (int j = 0; j < 7; j++) {

					String s_from_hour;
					String s_from_minute;
					final TextView rowTextView = new TextView(this);

					// set some properties of rowTextView or something
					rowTextView.setText(temp[j].toString());

					switch (j) {

					case 0:
						rowTextView.setMaxWidth(1);
						rowTextView.setTypeface(null, Typeface.BOLD);
						tempo = temp[j].toString();
						rowTextView.setVisibility(View.INVISIBLE);
						break;

					case 1:

						rowTextView.setTypeface(null, Typeface.BOLD);
						rowTextView.setTextSize(17);

						final int iNum = Integer.parseInt(tempo.toString());

						int iMode = Integer.parseInt(temp[7].toString());

						if (iMode == 0) {
							rowTextView.setTextColor(Color.BLUE);
							rowTextView
									.setOnClickListener(new View.OnClickListener() {

										// @Override
										public void onClick(View v) {

											Intent openMainActivity = new Intent(
													"com.example.muteme.MAINACTIVITY");

											openMainActivity.putExtra(
													"UPDATE_EVENT", iNum);

											startActivity(openMainActivity);

										}
									});
						} else {
							rowTextView.setTextColor(Color.RED);
						}
						break;

					case 3:

						String tmp1 = temp[j].toString();

						s_from_hour = tmp1.substring(0, tmp1.indexOf(":"));
						s_from_minute = tmp1.substring(tmp1.indexOf(":") + 1,
								tmp1.length());

						int i_fMi1 = Integer.parseInt(s_from_minute);
						int i_fHr1 = Integer.parseInt(s_from_hour);
						int i1 = 0;
						if (i_fHr1 > 12) {
							i_fHr1 -= 12;
							i1 = 1;
						}
						if (i1 == 0) {
							rowTextView.setText("" + i_fHr1 + ":" + i_fMi1
									+ "AM");

						}
						if (i1 == 1) {
							rowTextView.setText("" + i_fHr1 + ":" + i_fMi1
									+ "PM");
						}
						break;

					case 4:

						String tmp2 = temp[j].toString();

						s_from_hour = tmp2.substring(0, tmp2.indexOf(":"));
						s_from_minute = tmp2.substring(tmp2.indexOf(":") + 1,
								tmp2.length());

						int i_fMi2 = Integer.parseInt(s_from_minute);
						int i_fHr2 = Integer.parseInt(s_from_hour);
						int i2 = 0;
						if (i_fHr2 > 12) {
							i_fHr2 -= 12;
							i2 = 1;
						}
						if (i2 == 0) {
							rowTextView.setText("" + i_fHr2 + ":" + i_fMi2
									+ "AM");

						}
						if (i2 == 1) {
							rowTextView.setText("" + i_fHr2 + ":" + i_fMi2
									+ "PM");
						}
						break;

					case 5:

						int iModeSet = Integer.parseInt(temp[j].toString());
						if (iModeSet == 1) {
							rowTextView.setText("V".toString());
						} else {
							rowTextView.setText("S".toString());
						}
						rowTextView.setTextColor(Color.CYAN);
						rowTextView.setTypeface(null, Typeface.BOLD);
						rowTextView.setTextSize(17);
						break;

					case 6:

						rowTextView.setTextColor(Color.RED);
						rowTextView.setText("X".toString());
						rowTextView.setTypeface(null, Typeface.BOLD);
						rowTextView.setTextSize(17);

						final int iNumDel = Integer.parseInt(tempo.toString());

						rowTextView
								.setOnClickListener(new View.OnClickListener() {

									// @Override
									public void onClick(View v) {
										// TODO Auto-generated method stub

										LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

										View view = inflater.inflate(
												R.layout.delete, null, false);

										new AlertDialog.Builder(Event.this)
												.setView(view)
												.setCancelable(false)
												.setTitle("Confirm delete ?")
												.setPositiveButton(
														"Yes",
														new DialogInterface.OnClickListener() {

															@TargetApi(11)
															public void onClick(
																	DialogInterface dialog,
																	int id) {

																sqlDbAdaptor
																		.deleteRecord(iNumDel);
																sqlDbAdaptor
																		.updateAlarm(iNumDel);

																stopAlarm(iNumDel);

																Intent openMainActivity = new Intent(
																		"com.example.muteme.EVENT");
																startActivity(openMainActivity);

															}
														})
												.setNegativeButton(
														"No",
														new DialogInterface.OnClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int id) {

																dialog.cancel();

															}
														}).show();
									}
								});

						break;

					default:
						break;

					}

					// save a reference to the text view for later
					myTextViews[i][j] = rowTextView;

					tr.addView(rowTextView);

				}

				tbLayout.addView(tr);

			}

		} else {
			MessageText.messageText(this, "No events for the given selection");
		}

	}

	public void stopAlarm(int iNum) {

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

	}

}