package com.atticuswhite.livewallpaper;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public class CloudInstance extends WallpaperInstance {
	private final Paint mPaint = new Paint();
	private final Bitmap background;
	private Context context;
	private ArrayList<Sprite> clouds = new ArrayList<Sprite>();
	
	private boolean NIGHT = false;
	
	private int total_nodes = 100;
	private float node_speed = 7;
	private float min_node_speed = 30;
	private int circle_radius = 300;
	private int min_dist = 25;
	
	private int min_speed = 1;
	private int speed = 4;
	
	private Node[] nodes;
	
	private int[] cloud_assets = new int[]{
			R.drawable.cloud01, R.drawable.cloud02,
			R.drawable.cloud03, R.drawable.cloud04,
			R.drawable.cloud05, R.drawable.cloud06,
			R.drawable.cloud07, R.drawable.cloud08,
			R.drawable.cloud09
	};

	CloudInstance(Context context){
		super();
		this.context = context;
		final Paint paint = mPaint;
		paint.setColor(0xffffffff);
		paint.setAntiAlias(true);
		paint.setStrokeWidth((float) 0.5);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStyle(Paint.Style.STROKE);
		background = BitmapFactory.decodeResource(context.getResources(), R.drawable.castlemountain);
		
		
		for (int i=0; i<8; i++){
			Sprite cloud = new CloudSprite(
					(float) (canvasWidth() + (i * 50)),
					(float) (200 + i * 50),
					(float) -200,
					(float) (200 + i * 50),
					(float) (Math.random()/10000));
			cloud.setBitmap(BitmapFactory.decodeResource(context.getResources(), cloud_assets[i]));
			clouds.add(cloud);
		}
		
		nodes = new Node[total_nodes];
		for (int i=0; i<total_nodes; i++){
			nodes[i] = new Node((float)(Math.random() * canvasWidth()), 
					(float) Math.random() * canvasHeight(), 
					(float) Math.random() * canvasWidth(), 
					(float) Math.random() * canvasHeight(),
					(float) (min_node_speed + Math.random() * node_speed),
					1);
			positionRadialNode(nodes[i]);
		}
	}
	
	
	@Override
	public void drawFrame(Canvas c) {
		drawSky(c);
		if (!NIGHT){
			drawClouds(c);
		} else {
			drawStars(c);
			webNodes(c);
		}
		drawForeground(c);
	}
	
	private void drawSky(Canvas c){
		// draw sky
		if (NIGHT){
			c.drawColor(Color.rgb(58, 101, 131));
		} else {
			c.drawColor(Color.rgb(88, 153, 200));
		}
	}
	
	private void drawForeground(Canvas c){

		int y = context.getResources().getDisplayMetrics().heightPixels - background.getHeight();
		c.drawBitmap(background, -Math.abs(mOffset.xPixels), y, null);
		if (NIGHT){
			c.drawColor(Color.argb(75, 0, 0, 0));
		}
	}
	
	private void drawClouds(Canvas c){
		for(Sprite cloud : clouds){
			cloud.step();
			c.drawBitmap(cloud.getBitmap(), cloud.getCPoint().getX(), cloud.getCPoint().getY(), null);
			
			if (cloud.doneStepping()){
				cloud.getCPoint().setX(canvasWidth() + 300);
			}
		}
	}
	
	private void drawStars(Canvas c){
		for (Node node : nodes){
			node.step();
			c.drawCircle(node.getCPoint().getX(), node.getCPoint().getY(), node.getSize(), mPaint);
			
			if (node.doneStepping()){
				positionRadialNode(node);
			}
		}
	}
	
	private void positionRadialNode(Node node){
		float radian = (float) (((Math.random() * 360) / 180) * Math.PI);
		float dX = mWidth/2;
		float dY = mHeight/2;
		float radius = (float) (100 + Math.random() * circle_radius);
		node.getDPoint().setX(dX + (float) (Math.cos(radian) * radius));
		node.getDPoint().setY(dY + (float) (Math.sin(radian) * radius));
	}

	private void positionLinearNode(Node node){
		float dX = (float) Math.random() * mWidth;
		float dY = (float) Math.random() * mHeight;
		node.getDPoint().setX(dX);
		node.getDPoint().setY(dY);
	}
	
	private void webNodes(Canvas c){
		for (int i=0; i<nodes.length; i++){
			for (int j=i+1; j<nodes.length; j++){
				float x_dist = nodes[i].getCPoint().getX() - nodes[j].getCPoint().getX();
				float y_dist = nodes[i].getCPoint().getY() - nodes[j].getCPoint().getY();
				float dist = (float) Math.sqrt(Math.pow(x_dist, 2) + Math.pow(y_dist, 2));
				
				if (dist <= min_dist){
					c.drawLine(nodes[i].getCPoint().getX() + 1, 
							nodes[i].getCPoint().getY() + 1,
							nodes[j].getCPoint().getX() + 1,
							nodes[j].getCPoint().getY() + 1,
							mPaint);
				}
			}
		}
	}
	
	
	
	
	@Override
	public void onTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN){
			NIGHT = !NIGHT;
		}
		
	}
	
	public int canvasHeight(){
		return context.getResources().getDisplayMetrics().heightPixels;
	}
	
	public int canvasWidth(){
		return context.getResources().getDisplayMetrics().widthPixels;
	}
	
	class CloudSprite extends Sprite{

		CloudSprite(float x, float y, float dX, float dY, float speed) {
			super(x, y, dX, dY, speed);
		}
		
		public void step(){
			if (getDPoint().getX() > getCPoint().getX()){
				getCPoint().setX(getCPoint().getX() + speed);
			} else if (getDPoint().getX() < getCPoint().getX()) {
				getCPoint().setX(getCPoint().getX() - speed);
			}
			
			if (getDPoint().getY() > getCPoint().getY()){
				getCPoint().setY(getCPoint().getY() + speed);
			} else if (getDPoint().getY() < getCPoint().getY()){
				getCPoint().setY(getCPoint().getY() - speed);
			}
		}
		
		public boolean doneStepping(){
			return ((getCPoint().getX() >= getDPoint().getX()-speed) &&
					(getCPoint().getX() <= getDPoint().getX()+speed) &&
					(getCPoint().getY() >= getDPoint().getY()-speed) &&
					(getCPoint().getY() <= getDPoint().getY()+speed));
		}
	}
}
