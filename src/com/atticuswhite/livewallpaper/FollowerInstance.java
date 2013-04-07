package com.atticuswhite.livewallpaper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService.Engine;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class FollowerInstance extends WallpaperInstance{
	private final Paint mPaint = new Paint();
	private Coordinate target = new Coordinate();
	private Coordinate dot = new Coordinate(10, 10);
	
	
	FollowerInstance(){
		//this.mHandler = mHandler;
		final Paint paint = mPaint;
		paint.setColor(0xffffffff);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(2);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStyle(Paint.Style.STROKE);
	}
	
	
	public void drawFrame(Canvas c){
		drawDot(c);
		followPoint(c);
	}
	
	private void drawDot(Canvas c){
		c.save();
		c.drawColor(0xff000000);
		if (target != null){
			c.drawCircle(target.getX(), target.getY(), 30, mPaint);
		}
		c.restore();
	}
	
	private void followPoint(Canvas c){
		if (target == null) return;
		float newX = dot.getX();
		float newY = dot.getY();
		
		if (target.getX() > dot.getX()){
			newX = dot.getX() + 1;
		} else if (target.getX() < dot.getX()) {
			newX = dot.getX() - 1;
		}
		
		if (target.getY() > dot.getY()){
			newY = dot.getY() + 1;
		} else if (target.getY() < dot.getY()){
			newY = dot.getY() - 1;
		}
		
		dot.setX(newX);
		dot.setY(newY);
		c.drawCircle(dot.getX(), dot.getY(), 10, mPaint);
	}


	@Override
	public void onTouch(MotionEvent event) {
		if (target == null){
			target = new Coordinate();
		}
		target.setX(event.getX());
		target.setY(event.getY());
	}
}