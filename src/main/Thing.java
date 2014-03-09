package main;

import java.io.Serializable;

public class Thing implements Serializable{
	protected int x;
	protected int y;
	int MAXHEALTH;
	int hp;
	private boolean isBeingAttacked;
	private boolean destroyed;
	public static World myWorld;
	public Thing()
	{
		destroyed = false; 
	}
	public Thing(int x, int y)
	{
		destroyed = false;
		this.x = x;
		this.y = y;
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
