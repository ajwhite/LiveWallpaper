package com.atticuswhite.livewallpaper;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService.Engine;
import android.util.Log;
import android.view.MotionEvent;

public class NeighborInstance extends WallpaperInstance {
	private final Paint mPaint = new Paint();
	private Coordinate clickCoord;
	private Node[] nodes;
	
	// settings
	private int totalNodes = 300;
	private float min_dist = 75;
	private float min_speed = 10;
	private float node_speed = 7;
	private float circle_radius = 150;

	// states
	private boolean gather = false;
	
	// options
	private boolean webNodes = false;
	private boolean followMouse = true;
	
	
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
							(float) Math.random() * mHeight, 
							(float) Math.random() * mWidth, 
							(float) Math.random() * mHeight,
							(float) (min_speed + Math.random() * node_speed),
							(int) Math.floor((1 + Math.random() * 3))) ;
		}
	}

	@Override
	public void drawFrame(Canvas c) {
		c.drawColor(0xff000000);
		updateNodes(c);
		if (!gather && webNodes){
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
			
			node.step();
			c.drawCircle(node.getCPoint().getX(), node.getCPoint().getY(), node.getSize(), mPaint);
			
			
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
		}
	}
	
	private void positionRadialNode(Node node){
		float radian = (float) (((Math.random() * 360) / 180) * Math.PI);
		float dX = followMouse ? clickCoord.getX() : mWidth/2;
		float dY = followMouse ? clickCoord.getY() : mHeight/2;
		
		node.getDPoint().setX(dX + (float) (Math.cos(radian) * circle_radius));
		node.getDPoint().setY(dY + (float) (Math.sin(radian) * circle_radius));
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

	
}
