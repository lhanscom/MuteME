package com.example.muteme;

import com.example.muteme.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

public class Screen extends Activity {

	SQLiteDbAdaptor sqlDbAdaptor;
	MainActivity ma;

	TextView tvEId;
	TextView tvEname;
	TextView tvEintime;
	TextView tvEoutTime;
	TextView tvEdate;

	public void DeleteDb(View v) {

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View view = inflater.inflate(R.layout.delete, null, false);

		sqlDbAdaptor = new SQLiteDbAdaptor(this);

		new AlertDialog.Builder(Screen.this)
				.setView(view)
				.setCancelable(false)
				.setMessage("Do you want to delete all events ?")
				.setTitle("Confirm delete?")
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@TargetApi(11)
							public void onClick(DialogInterface dialog, int id) {

								String s = null;

								s = sqlDbAdaptor.getAllData();

								if (s.length() > 1) {

									String s_content[] = s.split("#");

									for (int i = 0; i < s_content.length; i++) {

										String tempo = "" + 0; // initialize

										String out = s_content[i];

										String temp[] = out.split("-");

										for (int j = 0; j < 7; j++) {

											switch (j) {

											case 0:

												tempo = temp[j].toString();

												int iNum = Integer
														.parseInt(tempo
																.toString());
												sleepAlarm(iNum);

												break;
											}
										}
									}
									
									sqlDbAdaptor.deleteAll();

									Intent openMainActivity = new Intent(
											"com.example.muteme.SCREEN");
									startActivity(openMainActivity);
									
								}
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						dialog.cancel();

					}
				}).show();
	}

	@Override
	public void onBackPressed() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		Button bAddEvent = (Button) findViewById(R.id.ibAdd);

		bAddEvent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent openMainActivity = new Intent(
						"com.example.muteme.MAINACTIVITY");
				startActivity(openMainActivity);

			}
		});

		Button bAddCal = (Button) findViewById(R.id.ibCalender);

		bAddCal.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent openMainActivity = new Intent("com.example.muteme.EVENT");
				startActivity(openMainActivity);

			}
		});

        Button bMyWifi = (Button) findViewById(R.id.ibWifi);

        bMyWifi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                ConnectivityManager cm =
                        (ConnectivityManager)v.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                boolean isWifi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;



            }
        });


        sqlDbAdaptor = new SQLiteDbAdaptor(this);

		String id = null;

		id = sqlDbAdaptor.getAllData();

		if (id.length() > 1) {

			String s_content[] = id.split("#");

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);

			params.addRule(RelativeLayout.BELOW, R.id.tvMain);

			final TextView[][] myTextViews = new TextView[s_content.length][7]; // Array

			TableLayout tbLayout = (TableLayout) findViewById(R.id.tableLayout);
			tbLayout.setStretchAllColumns(true);
			tbLayout.bringToFront();
			tbLayout.setLayoutParams(params);

			for (int i = 0; i < s_content.length; i++) {

				String tempo = "" + 0; // initialize
				String event_name = null;

				TableRow tr = new TableRow(this);

				tr.setPadding(2,2,2,2);

				String out = s_content[i];

				String temp[] = out.split("-");

				for (int j = 0; j < 7; j++) {

					final TextView rowTextView = new TextView(this);

					// set some properties of rowTextView or something
					rowTextView.setText(temp[j].toString());

					String s_from_hour;
					String s_from_minute;

					rowTextView.setTextSize(15);

					switch (j) {

					case 0:
						rowTextView.setMaxWidth(1);
						rowTextView.setTypeface(null, Typeface.BOLD);
						tempo = temp[j].toString();
						rowTextView.setVisibility(View.INVISIBLE);
						break;

					case 1:

						rowTextView.setTextColor(Color.BLUE);
						rowTextView.setTypeface(null, Typeface.BOLD);
						// rowTextView.setTextSize(17);
						event_name = rowTextView.getText().toString();

						final int iNum = Integer.parseInt(tempo.toString());

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
						// rowTextView.setTextSize(17);
						break;

					case 6:

						rowTextView.setTextColor(Color.RED);
						rowTextView.setText("X".toString());
						rowTextView.setTypeface(null, Typeface.BOLD);
						// rowTextView.setTextSize(17);

						final String event = event_name;

						final int iNumDel = Integer.parseInt(tempo.toString());

						rowTextView
								.setOnClickListener(new View.OnClickListener() {

									// @Override
									public void onClick(View v) {
										// TODO Auto-generated method stub

										LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

										View view = inflater.inflate(
												R.layout.delete, null, false);

										new AlertDialog.Builder(Screen.this)
												.setView(view)
												.setCancelable(false)
												.setMessage(
														"Do you want to delete event "
																+ event)
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

																sleepAlarm(iNumDel);

																Intent openMainActivity = new Intent(
																		"com.example.muteme.SCREEN");
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

		}

	}

	public void sleepAlarm(int iNum) {

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
