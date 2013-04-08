package com.atticuswhite.livewallpaper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;


// inspiration: http://jsfiddle.net/esteewhy/5Ht3b/6/
public class RippleInstance extends WallpaperInstance {

	private Context ctx;
	private final Bitmap background;
	private Bitmap writableBackground;
	
	private int[] lastMap;
	private int[] rippleMap;
	private int size;
	private int oldind;
	private int newind;
	private int riprad = 3;
	
	// pixel data
	private int[] ripple;
	private int[] texture;
	
	private int width;
	private int height;
	
	private int half_width;
	private int half_height;
	
	
	RippleInstance(Context ctx){
		this.ctx = ctx;
		background = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.nyc);
		width = background.getWidth();
		height = background.getHeight();

		writableBackground = Bitmap.createBitmap(width, height, background.getConfig());
		half_width = width >> 1;
		half_height = height >> 1;
		
		size = width * (height + 2) * 2;
		
		oldind = width;
		newind = width * (height + 3);
		
		Log.i("WLLPAPER SIZE", "Size: " + size);
		rippleMap = new int[size];
		lastMap = new int[size];
		ripple = new int[size];
		texture = new int[size];
		background.getPixels(ripple, 0, background.getWidth(), 0, 0, background.getWidth(), background.getHeight());
		background.getPixels(texture, 0, background.getWidth(), 0, 0, background.getWidth(), background.getHeight());
		
		
		
		
		
		for (int i=0; i<size; i++){
			rippleMap[i] = 0;
			lastMap[i] = 0;
		}
	}
	
	@Override
	public void drawFrame(Canvas c) {
		c.drawColor(Color.rgb(88, 153, 200));
		generateRipples();
		writableBackground.setPixels(ripple, 0, background.getWidth(), 0, 0, background.getWidth(), background.getHeight());
		c.drawBitmap(writableBackground, 0, 200, null);
	}
	
	private void disturb(int dx, int dy){
		dx = dx << 0;
		dy = dy << 0;
		
		for (int i = dy - riprad; i < dy + riprad; i++){
			for (int j = dx - riprad; j < dx + riprad; j++){
				if (oldind + (i * width) + j < size){ 
					rippleMap[oldind + (i * width) + j] += 128;
				} else {
					Log.i("Wallpapper", "Ignoring out of bounds index: " + (oldind + (i * width) + j));
				}
			}
		}
	}
	
	
	
	private void generateRipples(){
		int idx = 0;
		
		
		
		int old_data, 
			a, b,
			new_pixel,
			cur_pixel;
		
		int temp = oldind;
		oldind = newind;
		newind = temp;
		
		for (int y=0; y<height; y++){
			for (int x=0; x<width; x++){
				int _newind = newind + idx;
				int _mapind = oldind + idx;
				
				int data = (
					rippleMap[_mapind - width] +
					rippleMap[_mapind + width] +
					rippleMap[_mapind - 1] + 
					rippleMap[_mapind + 1]
				) >> 1;
					
				data -= rippleMap[_newind];
				data -= data >> 5;
				
				rippleMap[_newind] = data;
				
				data = 1024 - data;
				
				old_data = lastMap[idx];
				lastMap[idx] = data;
				
				if (old_data != data){
					// offset
					a = ((( x - half_width) * data / 1024) << 0) + half_width;
					b = ((( y - half_height) * data / 1024) << 0) + half_height;
					
					//bounds
					if (a >= width) a = width -1;
					if (a < 0) a = 0;
					if (b >= height) b = height -1;
					if (b < 0) b = 0;
					
					new_pixel = (a + (b * width)) * 4;
					cur_pixel = idx * 4;
					if (new_pixel + 3 >= size || cur_pixel + 3 >= size){
						return;
					}
					ripple[cur_pixel] = texture[new_pixel];
					ripple[cur_pixel + 1] = texture[new_pixel + 1];
					ripple[cur_pixel + 2] = texture[new_pixel + 2];
				}
				++idx;
			}
		}
	}

	@Override
	public void onTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN){
			if (event.getX() <= width && event.getY() <= height){
				disturb((int)event.getX(), (int) event.getY());
			}
		}
	}

}
