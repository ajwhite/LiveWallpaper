package com.atticuswhite.livewallpaper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService.Engine;
import android.util.Log;
import android.view.MotionEvent;

public class NeighborInstance extends WallpaperInstance {
	private final Paint mPaint = new Paint();
	private int totalNodes = 30;
	private float min_dist = 100;
	private float min_speed = 10;
	private float node_speed = 7;
	private float circle_radius = 100;
	private boolean gather = false;
	private Coordinate clickCoord;
	private Node[] nodes;
	
	NeighborInstance(){
		super();

		final Paint paint = mPaint;
		paint.setColor(0xffffffff);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(1);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStyle(Paint.Style.STROKE);
		
		
		nodes = new Node[totalNodes];
		for (int i=0; i<totalNodes; i++){
			nodes[i] = new Node((float)(Math.random() * mWidth), 
							(float)Math.random() * mHeight, 
							(float) Math.random() * mWidth, 
							(float) Math.random() * mHeight,
							(float) (min_speed + Math.random() * node_speed));
		}
	}

	@Override
	public void drawFrame(Canvas c) {
		c.drawColor(0xff000000);
		updateNodes(c);
		if (!gather){
			webNodes(c);
		}
	}

	@Override
	public void onTouch(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN){
			clickCoord = new Coordinate(event.getX(), event.getY());
			gather = !gather;
			if (gather){
				gatherNodes();
			} else {
				releaseNodes();
			}
		}
	}
	
	
	private void gatherNodes(){
		for (Node node : nodes){
			positionRadialNode(node);
		}
	}
	
	private void releaseNodes(){
		for (Node node : nodes){
			positionLinearNode(node);
		}
	}
	
	private void updateNodes(Canvas c){
		for (Node node : nodes){
			Coordinate cPoint = node.getCPoint();
			Coordinate dPpoint = node.getDPoint();
			float cX = node.getCPoint().getX() + (dPpoint.getX() - cPoint.getX()) / node.getSpeed();
			float cY = node.getCPoint().getY() + (dPpoint.getY() - cPoint.getY()) / node.getSpeed();
			//Log.i("New Point", "CX: " + cX + ", CY: " + cY);
			node.getCPoint().setX(cX);
			node.getCPoint().setY(cY);
			

			c.drawCircle(node.getCPoint().getX(), node.getCPoint().getY(), 2, mPaint);
			
			if ((node.getCPoint().getX() >= node.getDPoint().getX()-1) &&
				(node.getCPoint().getX() <= node.getDPoint().getX()+1) &&
				(node.getCPoint().getY() >= node.getDPoint().getY()-1) &&
				(node.getCPoint().getY() <= node.getDPoint().getY()+1)){
				
				if (!gather){
					positionLinearNode(node);
				} else {
					positionRadialNode(node);
				}
			}
			if (!gather){
				//webNodes(c);
			}
		}
	}
	
	private void positionRadialNode(Node node){
		float radian = (float) (((Math.random() * 360) / 180) * Math.PI);
		//node.getDPoint().setX((float) ((mWidth / 2) + Math.cos(radian) * circle_radius));
		//node.getDPoint().setY((float) ((mHeight / 2) + Math.sin(radian) * circle_radius));
		node.getDPoint().setX(clickCoord.getX() + (float) (Math.cos(radian) * circle_radius));
		node.getDPoint().setY(clickCoord.getY() + (float) (Math.sin(radian) * circle_radius));
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

	
	class Node {
		private Coordinate point;
		private Coordinate cPoint;
		private float speed = 1;
		private boolean gather = true;
		
		Node(float x, float y, float cX, float cY, float speed){
			point = new Coordinate(x, y);
			cPoint = new Coordinate(cX, cY);
			this.speed = 15.0f; //speed;
		}
		
		public Coordinate getDPoint(){
			return point;
		}
		
		public Coordinate getCPoint(){
			return cPoint;
		}
		
		public float getSpeed(){
			return speed;
		}
		
		public boolean isGather(){
			return gather;
		}
	}

}
