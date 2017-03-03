package com.hankora817.vlcplayersample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button b = (Button) findViewById(R.id.play_video);
		final EditText e = (EditText) findViewById(R.id.video_url);
		e.setText("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
		
		b.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				String url = e.getText().toString();
				if (!TextUtils.isEmpty(url))
				{
					Intent intent = new Intent(getApplicationContext(), com.hankora817.vlcplayersample.VideoVLCActivity.class);
					intent.putExtra("videoUrl", url);
					startActivity(intent);
				}
			}
		});
	}
}
