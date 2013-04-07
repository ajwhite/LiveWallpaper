package com.atticuswhite.livewallpaper;

import android.graphics.Canvas;

public interface WallpaperInterface {
	public void drawFrame(Canvas c);
	public void surfaceChanged(float x, float y);
	public void offsetChanged(float offset);
	public void onTouch(float x, float y);
}
