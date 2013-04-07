package com.atticuswhite.livewallpaper;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface WallpaperInterface {
	public void drawFrame(Canvas c);
	public void surfaceChanged(float x, float y, float width, float height);
	public void offsetChanged(float offset);
	public void onTouch(MotionEvent event);
}
