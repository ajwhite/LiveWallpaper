package com.atticuswhite.livewallpaper;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.service.wallpaper.WallpaperService;
import android.service.wallpaper.WallpaperService.Engine;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class MainActivity extends WallpaperService {
	private final Handler mHandler = new Handler();

	@Override
	public Engine onCreateEngine() {
		return new WallpaperEngine(new FollowerInstance());
	}
    
	class WallpaperEngine extends Engine{
		WallpaperInterface instance;
		boolean mVisible;
		
		private final Runnable mDrawFrame = new Runnable(){
			public void run(){
				drawFrame();
			}
		};
		
		
		WallpaperEngine(WallpaperInterface instance){
			this.instance = instance;
		}
		

		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			setTouchEventsEnabled(false);
		}
		
		public void onDestroy(){
			super.onDestroy();
			mHandler.removeCallbacks(mDrawFrame);
		}
		

		public void onVisibilityChanged(boolean visible){
			mVisible = visible;
			if (visible){
				drawFrame();
			}else{
				mHandler.removeCallbacks(mDrawFrame);
			}
		}
		
		public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height){
			super.onSurfaceChanged(holder, format, width, height);
			instance.surfaceChanged(width/2.0f, height/2.0f);
			drawFrame();
		}
		
		public void onSurfaceCreatd(SurfaceHolder holder){
			super.onSurfaceCreated(holder);
		}
		
		public void onSurfaceDesroyed(SurfaceHolder holder){
			super.onSurfaceDestroyed(holder);
			mVisible = false;
			mHandler.removeCallbacks(mDrawFrame);
		}
		
		public void onOffsetsChanged(float xOffset, float yOffset, float xStep, float yStep, int xPixels, int yPixels) {
			instance.offsetChanged(xOffset);
			drawFrame();
		}
		
		public void onTouchEvent(MotionEvent event){
			super.onTouchEvent(event);
			instance.onTouch(event.getX(), event.getY());
		}
		
		private void drawFrame(){
			final SurfaceHolder holder = getSurfaceHolder();
			
			Canvas c = null;
			try{
				c = holder.lockCanvas();
				if (c != null){
					instance.drawFrame(c);
				}
			} finally{
				if (c != null){
					holder.unlockCanvasAndPost(c);
				}
			}
			
			
			// schedule render
			mHandler.removeCallbacks(mDrawFrame);
			if (mVisible){
				mHandler.postDelayed(mDrawFrame, 1000/25);
			}
		}
	}
	
}
