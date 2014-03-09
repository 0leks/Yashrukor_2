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
	public Thing(int x, int y, int width, int height)
	{
		id = idcounter++;
		destroyed = false;
		this.x = x;
		this.y = y;
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
	
}
