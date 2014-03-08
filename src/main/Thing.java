package main;

public class Thing {
	protected int x;
	protected int y;
	int MAXHEALTH;
	int hp;
	int lasth;
	private boolean isBeingAttacked;
	private boolean destroyed;
	public static World myWorld;
	public Thing()
	{
		lasth = hp;
		destroyed = false; 
	}
	public Thing(int x, int y)
	{
		destroyed = false;
		this.x = this.y;
	}
	public void tic()
	{
		if(lasth < hp)
		{
			isBeingAttacked = true;
		}
	}
	
}
