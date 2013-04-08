package com.atticuswhite.livewallpaper;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public class TailInstance extends WallpaperInstance {
	private final Paint mPaint = new Paint();
	private Node parentNode;
	private float min_speed = 10;
	private float node_speed = 7;
	
	
	TailInstance(){
		super();

		final Paint paint = mPaint;
		paint.setColor(0xffffffff);
		paint.setAntiAlias(true);
		paint.setStrokeWidth(1);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStyle(Paint.Style.STROKE);
		
		parentNode = new Node((float)(Math.random() * mWidth), 
				(float) Math.random() * mHeight, 
				(float) Math.random() * mWidth, 
				(float) Math.random() * mHeight,
				(float) (min_speed + Math.random() * node_speed),
				(int) 20);
	}
	
	@Override
	public void drawFrame(Canvas c) {
		c.drawColor(0xff000000);
		updateNodes(c);
	}

	private void updateNodes(Canvas c){
		
		parentNode.step();
		c.drawCircle(parentNode.getCPoint().getX(), parentNode.getCPoint().getY(), parentNode.getSize(), mPaint);
		
		Node parent = parentNode;
		while (!parent.getChildren().isEmpty()){
			for (Node node : parent.getChildren()){
				moveChildNode(c, node);
			}
			parent = parent.getChildren().get(0);
		}
		if (parentNode.doneStepping()){
			float dX = (float) Math.random() * mWidth;
			float dY = (float) Math.random() * mHeight;
			parentNode.getDPoint().setX(dX);
			parentNode.getDPoint().setY(dY);
		}
	}
	
	private void moveChildNode(Canvas c, Node node){
		node.step();
		c.drawCircle(node.getCPoint().getX(), node.getCPoint().getY(), node.getSize(), mPaint);

		node.getDPoint().setX(node.getParent().getCPoint().getX());
		node.getDPoint().setY(node.getParent().getCPoint().getY());
		
	}
	
	@Override
	public void onTouch(MotionEvent event) {
		Node parent = parentNode;
		while(!parent.getChildren().isEmpty()){
			parent = parent.getChildren().get(0);
		}
		int size = parent.getSize();
		if (size > 1){
			size -= 1;
		}
		
		
		
		Node node = new Node(parent.getCPoint().getX(),
				parent.getCPoint().getY(),
							event.getX(),
							event.getY(),
							10,
							size);
		parent.addChild(node);
		node.setParent(parent);
		
	}
	
}
