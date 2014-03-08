package main;

public class Unit extends Thing {
	public final int WORKER = 1;
	public final int WARRIOR = 2;
	public final int ARCHER = 3;
	public final int KNIGHT = 4;
	public final int CROSSBOW = 5;
	public final int MEDIC = 6;
	private boolean isRanged;
	private int damage;
	Player myPlayer;
	private int unitType;
	public Unit(int unitType, int x, int y, Player p)
	{	
		this.x = x;
		this.y = y;
		myPlayer = p;
		this.unitType = unitType; 
		if(unitType == WORKER)
		{
			hp = 50;
			MAXHEALTH = 50;
			damage =0;
			isRanged = false;
		}
		if(unitType == WARRIOR)
		{
			hp = 100;
			MAXHEALTH = 50;
			damage = 20;
			isRanged = false;
		}
		if(unitType == ARCHER)
		{
			hp = 60;
			MAXHEALTH = 60;
			damage = 10;
			isRanged = true;
		}
		if(unitType == KNIGHT)
		{
			hp = 200;
			MAXHEALTH = 200;
			damage = 40;
			isRanged = false;
		}
		if(unitType == CROSSBOW)
		{
			hp = 100;
			MAXHEALTH = 100;
			damage = 30;
			isRanged = true;
		}
		if(unitType == MEDIC)
		{
			hp = 80;
			MAXHEALTH = 80;
			damage = 0; //heals 5 for closest ally
			isRanged = true; 
			
		}
		
	}
	
	public boolean isRanged()
	{
		return isRanged;
	}
	public int unitType()
	{
		return unitType;
	}
	
}
