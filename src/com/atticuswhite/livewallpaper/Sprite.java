package com.atticuswhite.livewallpaper;

import android.graphics.Bitmap;

public class Sprite {
	private Bitmap bitmap;
	private Coordinate cPoint;
	private Coordinate dPoint;
	private float speed = 1;
	
	Sprite(float x, float y, float dX, float dY, float speed){
		this.cPoint = new Coordinate(x, y);
		this.dPoint = new Coordinate(dX, dY);
		this.speed = speed;
	}
	
	Sprite(float x, float y){
		this.cPoint = new Coordinate(x, y);
		this.dPoint = new Coordinate();
		this.speed = 1;
	}
	
	Sprite(float x, float y, float speed){
		this.cPoint = new Coordinate(x, y);
		this.dPoint = new Coordinate();
		this.speed = speed;
	}

	public void step(){
		float cX = cPoint.getX() + ((dPoint.getX() - cPoint.getX()) / speed);
		float cY = cPoint.getY() + ((dPoint.getY() - cPoint.getY()) / speed);
		cPoint.setX(cX);
		cPoint.setY(cY);
	}
	
	public boolean doneStepping(){
		return ((getCPoint().getX() >= getDPoint().getX()-1) &&
				(getCPoint().getX() <= getDPoint().getX()+1) &&
				(getCPoint().getY() >= getDPoint().getY()-1) &&
				(getCPoint().getY() <= getDPoint().getY()+1));
	}
	
	
	public Coordinate getCPoint(){
		return cPoint;
	}
	
	public Coordinate getDPoint(){
		return dPoint;
	}
	
	public float getSpeed(){
		return speed;
	}
	
	public void setBitmap(Bitmap bitmap){
		this.bitmap = bitmap;
	}
	
	public Bitmap getBitmap(){
		return bitmap;
	}
}
