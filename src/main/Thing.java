package main;

import java.io.Serializable;

public class Thing implements Serializable{
	protected int x;
	protected int y;
	int MAXHEALTH;
	int hp;
	private boolean destroyed;
	public static World myWorld;
	public Thing()
	{
		destroyed = false; 
	}
	public Thing(int x, int y)
	{
		destroyed = false;
		this.x = this.y;
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
