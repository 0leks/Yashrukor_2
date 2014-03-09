package main;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;

public class Thing implements Serializable{
	protected int x;
	protected int y;
	int MAXHEALTH;
	int width;
	int height; 
	int hp;
	private boolean destroyed;
	public static World myWorld;
	private static int idcounter = 0;
	public final int id;
	public Thing(int x, int y, int width, int height, int health){
		hp = health;
		id = idcounter++;
		destroyed = false;
		this.x = x;
		this.y = y;
		this.width=width;
		this.height=height;
		myWorld.allThings.add(this);
	}	
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	public Point getLocation() {
		return new Point(x, y);
	}
	public void tic()
	{
		if(hp <= 0)
		{
			myWorld.allThings.remove(this);
			destroyed = true;
		}
	}
	public void getAttacked(int damage)
	{
		hp -= damage; 
	}
	public World myWorld()
	{
		return myWorld;
	}
	public void setPosition(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public Point[] getCorners()
	{
		Point[] arrayPoint = new Point[4];
		arrayPoint[0] = new Point(x-width, y-height);
		arrayPoint[1] = new Point(x+width, y-height);
		arrayPoint[2] = new Point(x-width, y+height);
		arrayPoint[3] = new Point(x+width, y+height);
		return arrayPoint; 
	}
	public int euclidianDistanceFrom(Point other) {
		int dx = other.x-x;
		int dy = other.y-y;
		return (int)(Math.sqrt(dx*dx+dy*dy));

	}
	public boolean collides (Rectangle t){
		return this.getBounds().intersects(t);
//		if(((t.x>x)&&(t.x<(x+width)))||(((t.x+t.width)>x)&&((t.x+t.width)<(x+width)))){
//			if(((t.y>y)&&(t.y<(y+height)))||(((t.y+t.height)>y)&&((t.y+t.height)<(y+height)))){
//				return true;
//			}
//		}
//		return false;
	}
}
