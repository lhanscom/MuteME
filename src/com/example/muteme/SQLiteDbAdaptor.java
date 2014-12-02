package com.example.muteme;

import java.sql.Date;
import java.text.SimpleDateFormat;

import com.example.muteme.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

public class SQLiteDbAdaptor {

	SQLiteMain sqlMain;

	public SQLiteDbAdaptor(Context context) {

		sqlMain = new SQLiteMain(context);

	}

	public long insertData(String s_event, String s_indate, String s_intime,
			String s_outtime, int i_mode) {

		SQLiteDatabase db = sqlMain.getWritableDatabase();

		ContentValues conVal = new ContentValues();

		conVal.put(sqlMain.NAME, s_event);
		conVal.put(sqlMain.INDATE, s_indate);
		conVal.put(sqlMain.INTIME, s_intime);
		conVal.put(sqlMain.OUTTIME, s_outtime);
		conVal.put(sqlMain.MODE, i_mode);
		conVal.put(sqlMain.DELFLAG, 0);
		conVal.put(sqlMain.ALARM, 0);

		long id = db.insert(sqlMain.TABLE_NAME, null, conVal);

		return id;

	}

	public void updateAlarm(int i_num) {

		SQLiteDatabase db = sqlMain.getWritableDatabase();

		ContentValues conVal = new ContentValues();

		conVal.put(sqlMain.ALARM, 1);

		db.update(sqlMain.TABLE_NAME, conVal, sqlMain.UID + " = " + i_num, null);

	}

	public void deleteRecord(int i_num) {

		SQLiteDatabase db = sqlMain.getWritableDatabase();

		ContentValues conVal = new ContentValues();

		conVal.put(sqlMain.DELFLAG, 1);

		db.update(sqlMain.TABLE_NAME, conVal, sqlMain.UID + " = " + i_num, null);

	}

	public void deleteAll() {

		SQLiteDatabase db = sqlMain.getWritableDatabase();

		ContentValues conVal = new ContentValues();

		conVal.put(sqlMain.ALARM, 1);
		conVal.put(sqlMain.DELFLAG, 1);

		db.update(sqlMain.TABLE_NAME, conVal, null, null);

	}

	public int getCount() {

		int i_rowCount;

		SQLiteDatabase db = sqlMain.getWritableDatabase();

		Cursor c = db.rawQuery("SELECT * FROM " + sqlMain.TABLE_NAME, null);

		i_rowCount = c.getCount();

		c.close();

		return i_rowCount;
	}

	public String getDates(String temp) {

		SQLiteDatabase db = sqlMain.getWritableDatabase();

		int i_cid;
		int i_alarm;
		int i_del;
		int i_mode;

		String s_name;
		String s_indate;
		String s_intime;
		String s_outtime;

		String[] columns = { sqlMain.UID, sqlMain.NAME, sqlMain.INDATE,
				sqlMain.INTIME, sqlMain.OUTTIME, sqlMain.MODE, sqlMain.DELFLAG,
				sqlMain.ALARM };
		/*
		 * Cursor cur = db.query(sqlMain.TABLE_NAME, columns,
		 * sqlMain.INDATE+" = " + temp, null, null, null, null );
		 */
		Cursor cur = db.rawQuery("SELECT * FROM " + sqlMain.TABLE_NAME
				+ " WHERE " + sqlMain.INDATE + " IN ( " + temp + " ) "
				+ " AND " + sqlMain.DELFLAG + " = 0 ", null);

		StringBuffer buffer = new StringBuffer();

		while (cur.moveToNext()) {

			i_cid = cur.getInt(0);

			s_name = cur.getString(1);
			s_indate = cur.getString(2);
			s_intime = cur.getString(3);
			s_outtime = cur.getString(4);

			i_mode = cur.getInt(5);
			i_del = cur.getInt(6);
			i_alarm = cur.getInt(7);

			buffer.append(i_cid + "-" + s_name + "-" + s_indate + "-"
					+ s_intime + "-" + s_outtime + "-" + i_mode + "-" + i_del
					+ "-" + i_alarm + "#");

		}

		return buffer.toString();

	}

	public String getRow(String temp) {

		SQLiteDatabase db = sqlMain.getWritableDatabase();

		int i_num = Integer.parseInt(temp);

		int i_cid;
		int i_alarm;
		int i_del;
		int i_mode;

		String s_name;
		String s_indate;
		String s_intime;
		String s_outtime;

		String[] columns = { sqlMain.UID, sqlMain.NAME, sqlMain.INDATE,
				sqlMain.INTIME, sqlMain.OUTTIME, sqlMain.MODE, sqlMain.DELFLAG,
				sqlMain.ALARM };

		Cursor cur = db.query(sqlMain.TABLE_NAME, columns, sqlMain.UID + " = "
				+ i_num, null, null, null, null);

		StringBuffer buffer = new StringBuffer();

		while (cur.moveToNext()) {

			i_cid = cur.getInt(0);

			s_name = cur.getString(1);
			s_indate = cur.getString(2);
			s_intime = cur.getString(3);
			s_outtime = cur.getString(4);

			i_mode = cur.getInt(5);
			i_del = cur.getInt(6);
			i_alarm = cur.getInt(7);

			buffer.append(i_cid + "-" + s_name + "-" + s_indate + "-"
					+ s_intime + "-" + s_outtime + "-" + i_mode + "-" + i_del
					+ "-" + i_alarm + "#");

		}

		return buffer.toString();

	}

	public String getAllData() {

		SQLiteDatabase db = sqlMain.getWritableDatabase();
		/*
		 * String[] columns = { sqlMain.UID, sqlMain.NAME, sqlMain.INDATE,
		 * sqlMain.INTIME,
		 * sqlMain.OUTTIME,sqlMain.MODE,sqlMain.DELFLAG,sqlMain.ALARM };
		 * 
		 * String[] orderBY = { sqlMain.INDATE,sqlMain.INTIME};
		 */
		int i_flag = 0;
		int i_cid;
		int i_mode;
		int i_alarm;
		int i_del;

		String s_name;
		String s_indate;
		String s_intime;
		String s_outtime;

		Cursor cur = null;

		try {

			cur = db.rawQuery(
					"SELECT * FROM EVENTDB WHERE alarm = 0 ORDER BY in_date,in_time ",
					null);

		} catch (Exception e) {

			i_flag = 1;

		}

		StringBuffer buffer = new StringBuffer();

		if (i_flag == 0) {

			while (cur.moveToNext()) {

				i_cid = cur.getInt(0);

				s_name = cur.getString(1);
				s_indate = cur.getString(2);
				s_intime = cur.getString(3);
				s_outtime = cur.getString(4);

				i_mode = cur.getInt(5);
				i_del = cur.getInt(6);
				i_alarm = cur.getInt(7);

				buffer.append(i_cid + "-" + s_name + "-" + s_indate + "-"
						+ s_intime + "-" + s_outtime + "-" + i_mode + "-"
						+ i_del + "-" + i_alarm + "#");

			}
		}

		return buffer.toString();

	}

	static class SQLiteMain extends SQLiteOpenHelper {

		private static final String DATABASE_NAME = "mutedb";
		private static final String TABLE_NAME = "EVENTDB";
		private static final String UID = "event_id";
		private static final String NAME = "event_name";
		private static final String INDATE = "in_date";
		private static final String INTIME = "in_time";
		private static final String OUTTIME = "out_time";
		private static final String MODE = "mode";
		private static final String DELFLAG = "del";
		private static final String ALARM = "alarm";
		private static final String TEM = "tem_num";
		private static final int DATABASE_VERSION = 45;

		private Context context;

		private static final String query = "CREATE TABLE " + TABLE_NAME + "("
				+ UID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + NAME
				+ " VARCHAR(20), " + INDATE + " VARCHAR(20), " + INTIME
				+ " VARCHAR(20), " + OUTTIME + " VARCHAR(20), " + MODE
				+ " INTEGER ," + DELFLAG + " INTEGER ," + ALARM + " INTEGER , "
				+ TEM + " INTEGER  ); ";

		// , " + TEM +" INTEGER

		public SQLiteMain(Context context) {

			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			this.context = context;

			// MessageText.MessageText(context, "DB constructor");

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub

			try {

				db.execSQL(query);

			} catch (SQLException e) {

				MessageText.messageText(context, "On Create DB");

			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			try {

				db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + ";");
				onCreate(db);

			} catch (SQLException e) {

				MessageText.messageText(context, "On Update DB");

			}

		}

	}
}