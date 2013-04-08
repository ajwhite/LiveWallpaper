package com.atticuswhite.livewallpaper;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.view.MotionEvent;

public abstract class WallpaperInstance implements WallpaperInterface {
	private Coordinate center;
	protected float mWidth = 500;
	protected float mHeight = 700;
	protected ScreenOffset mOffset;
	protected long mStartTime;
	
	WallpaperInstance(){
		center = new Coordinate();
		mStartTime = SystemClock.elapsedRealtime();
		mOffset = new ScreenOffset();
	}
	
	@Override
	public abstract void drawFrame(Canvas c);
	public abstract void onTouch(MotionEvent event);
	
	@Override
	public void surfaceChanged(float x, float y, float width, float height) {
		center.setX(x);
		center.setY(y);
		mWidth = width;
		mHeight = height;
	}

	@Override
	public void offsetChanged(float xOffset, float yOffset, float xStep, float yStep, float xPixels, float yPixels) {
		mOffset.xOffset = xOffset;
		mOffset.yOffset = yOffset;
		mOffset.xStep = xStep;
		mOffset.yStep = yStep;
		mOffset.xPixels = xPixels;
		mOffset.yPixels = yPixels;
	}
	
	protected Coordinate getCenter(){
		return center;
	}
	
	protected ScreenOffset getOffset(){
		return mOffset;
	}
	
	
	class ScreenOffset {
		public float xOffset;
		public float yOffset;
		public float xStep;
		public float yStep;
		public float xPixels;
		public float yPixels;
	}
	
	
}
