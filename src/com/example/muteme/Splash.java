package com.example.muteme;

import com.example.muteme.R;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Splash extends Activity {

	int i_progressStatus = 0;

	ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.splash);

		progressBar = (ProgressBar) findViewById(R.id.pbSplash);
		progressBar.setMax(100);

		Thread timer = new Thread() {

			public void run() {

				// String tempString;

				// Progress Bar Code

				while (i_progressStatus < 100) {

					try {

						sleep(200);

						i_progressStatus += 10;
						progressBar.setProgress(i_progressStatus);

					} catch (InterruptedException e) {

						e.printStackTrace();

					}

					finally {

						Intent openMainActivity = new Intent(
								"com.example.muteme.SCREEN");
						startActivity(openMainActivity);

					}
				}

			}
		};

		timer.start();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// finish();
	}

}
