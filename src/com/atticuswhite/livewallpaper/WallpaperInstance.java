package com.atticuswhite.livewallpaper;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.view.MotionEvent;

public abstract class WallpaperInstance implements WallpaperInterface {
	private Coordinate center;
	protected float mWidth = 500;
	protected float mHeight = 700;
	private float mOffset;
	protected long mStartTime;
	
	WallpaperInstance(){
		center = new Coordinate();
		mStartTime = SystemClock.elapsedRealtime();
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
	public void offsetChanged(float offset) {
		mOffset = offset;
		
	}
	
	protected Coordinate getCenter(){
		return center;
	}
	
	protected float getOffset(){
		return mOffset;
	}
	
	
	
	
}
