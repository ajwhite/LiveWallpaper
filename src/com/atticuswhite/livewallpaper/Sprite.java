package com.atticuswhite.livewallpaper;

import android.graphics.Bitmap;

public class Sprite {
	private Bitmap bitmap;
	private Coordinate point;
	
	Sprite(){
		this.point = new Coordinate();
	}
	
	public Coordinate getPoint(){
		return point;
	}
	
	public void setBitmap(Bitmap bitmap){
		this.bitmap = bitmap;
	}
	
	public Bitmap getBitmap(){
		return bitmap;
	}
}
