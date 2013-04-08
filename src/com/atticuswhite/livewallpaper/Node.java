package com.atticuswhite.livewallpaper;

import java.util.ArrayList;

public class Node {
	private Coordinate dPoint;
	private Coordinate cPoint;
	private float speed = 1;
	private boolean gather = true;
	private int size = 1;
	private ArrayList<Node> children = new ArrayList<Node>();
	private Node parent;

	Node(float x, float y, float cX, float cY, float speed){
		dPoint = new Coordinate(x, y);
		cPoint = new Coordinate(cX, cY);
		this.speed = speed; // + (size * 2); //15.0f; //speed;
		this.size = 1;
	}
	
	Node(float x, float y, float cX, float cY, float speed, int size){
		dPoint = new Coordinate(x, y);
		cPoint = new Coordinate(cX, cY);
		this.speed = speed; // + (size * 2); //15.0f; //speed;
		this.size = size;
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
	
	public Coordinate getDPoint(){
		return dPoint;
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
	
	public int getSize(){
		return size;
	}
	
	public Node getParent(){
		return parent;
	}
	
	public void setParent(Node node){
		parent = node;
	}
	
	public ArrayList<Node> getChildren(){
		return children;
	}
	
	public void addChild(Node node){
		children.add(node);
	}
	
	public void removeChild(Node node){
		children.remove(node);
	}
}

