package com.atticuswhite.livewallpaper;

import android.graphics.Canvas;
import android.os.SystemClock;

public abstract class WallpaperInstance implements WallpaperInterface {
	private Coordinate center;
	private float mOffset;
	protected long mStartTime;
	
	WallpaperInstance(){
		center = new Coordinate();
		mStartTime = SystemClock.elapsedRealtime();
	}
	
	@Override
	public abstract void drawFrame(Canvas c);
	public abstract void onTouch(float x, float y);
	
	@Override
	public void surfaceChanged(float x, float y) {
		center.setX(x);
		center.setY(y);
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
