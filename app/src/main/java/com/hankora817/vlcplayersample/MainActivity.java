package com.hankora817.vlcplayersample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
//		e.setText("https://victony.jp.quickconnect.to/fbdownload/math_1.mp4?_sid=%22GPXRCRUTnhYm.1710O1N559600%22&mode=open&dlink=%222f61636164656d79636b2f6d6174685f312e6d7034%22&stdhtml=true&SynoToken=Gn2IF0lfrNNnA");
		
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
