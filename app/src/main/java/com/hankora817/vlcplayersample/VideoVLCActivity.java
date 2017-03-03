package com.hankora817.vlcplayersample;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.videolan.libvlc.IVLCVout;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import android.app.Activity;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

public class VideoVLCActivity extends Activity implements IVLCVout.Callback
{
	private LibVLC libvlc;
	private MediaPlayer mMediaPlayer = null;
	
	private SurfaceView mSurfaceView;
	private FrameLayout mSurfaceFrame;
	private SurfaceHolder mSurfaceHolder;
	private Surface mSurface = null;
	
	private int mVideoWidth;
	private int mVideoHeight;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		
		mSurfaceView = (SurfaceView) findViewById(R.id.surface);
		mSurfaceHolder = mSurfaceView.getHolder();
		
		mSurfaceFrame = (FrameLayout) findViewById(R.id.player_surface_frame);
		
		createPlayer(getIntent().getStringExtra("videoUrl"));
	}
	
	
	private void createPlayer(String videoPath)
	{
		releasePlayer();
		try
		{
			ArrayList<String> options = new ArrayList<>();
			options.add("--aout=opensles");
			options.add("--audio-time-stretch");
			options.add("-vvv");
			libvlc = new LibVLC(this, options);
			
			mSurface = mSurfaceHolder.getSurface();
			mMediaPlayer = new MediaPlayer(libvlc);
			mMediaPlayer.setEventListener(mPlayerListener);
			
			IVLCVout vlcVout = mMediaPlayer.getVLCVout();
			vlcVout.setVideoView(mSurfaceView);
			vlcVout.addCallback(this);
			vlcVout.attachViews();
			
			Media media = new Media(libvlc, Uri.parse(videoPath));
			mMediaPlayer.setMedia(media);
			mMediaPlayer.play();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Toast.makeText(this, "Error creating player!", Toast.LENGTH_LONG).show();
		}
	}
	
	
	private void releasePlayer()
	{
		if (libvlc == null)
			return;
		mMediaPlayer.stop();
		final IVLCVout vout = mMediaPlayer.getVLCVout();
		vout.removeCallback(this);
		vout.detachViews();
		libvlc.release();
		libvlc = null;
		
		mVideoWidth = 0;
		mVideoHeight = 0;
	}
	
	private MediaPlayer.EventListener mPlayerListener = new MyPlayerListener(this);
	
	private static class MyPlayerListener implements MediaPlayer.EventListener
	{
		private WeakReference<VideoVLCActivity> mOwner;
		
		
		public MyPlayerListener(VideoVLCActivity owner)
		{
			mOwner = new WeakReference<>(owner);
		}
		
		
		@Override
		public void onEvent(MediaPlayer.Event event)
		{
			VideoVLCActivity player = mOwner.get();
			
			switch (event.type)
			{
				case MediaPlayer.Event.EndReached:
					player.releasePlayer();
					break;
				case MediaPlayer.Event.Playing:
					break;
				case MediaPlayer.Event.Paused:
					break;
				case MediaPlayer.Event.Stopped:
					break;
				case MediaPlayer.Event.PositionChanged:
					break;
				default:
					break;
			}
		}
	}
	
	
	@Override
	public void onNewLayout(IVLCVout vlcVout, int width, int height, int visibleWidth, int visibleHeight, int sarNum, int sarDen)
	{
		if (width * height == 0)
			return;
		setSize(width, height);
	}
	
	
	private void setSize(int width, int height)
	{
		mVideoWidth = width;
		mVideoHeight = height;
		if (mVideoWidth * mVideoHeight <= 1)
			return;
		
		int w = getWindow().getDecorView().getWidth();
		int h = getWindow().getDecorView().getHeight();
		
		boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
		if (w > h && isPortrait || w < h && !isPortrait)
		{
			int i = w;
			w = h;
			h = i;
		}
		
		float videoAR = (float) mVideoWidth / (float) mVideoHeight;
		float screenAR = (float) w / (float) h;
		
		if (screenAR < videoAR)
			h = (int) (w / videoAR);
		else
			w = (int) (h * videoAR);
		
		mSurfaceView.getHolder().setFixedSize(mVideoWidth, mVideoHeight);
		
		ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
		lp.width = w;
		lp.height = h;
		mSurfaceView.setLayoutParams(lp);
		mSurfaceView.invalidate();
	}
	
	
	@Override
	public void onSurfacesCreated(IVLCVout vlcVout)
	{
		
	}
	
	
	@Override
	public void onSurfacesDestroyed(IVLCVout vlcVout)
	{
		
	}
	
	
	@Override
	public void onHardwareAccelerationError(IVLCVout vlcVout)
	{
		releasePlayer();
		Toast.makeText(VideoVLCActivity.this, "Error with hardware acceleration", Toast.LENGTH_LONG).show();
	}
	
	
	@Override
	protected void onPause()
	{
		super.onPause();
		releasePlayer();
	}
	
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		releasePlayer();
	}
}
