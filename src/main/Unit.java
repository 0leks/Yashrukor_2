package main;

import java.io.Serializable;
import java.util.ArrayList;

public class Unit extends Thing  implements Serializable{
	public final static int WORKER = 1;
	public final static int WARRIOR = 2;
	public final static int ARCHER = 3;
	public final static int KNIGHT = 4;
	public final static int CROSSBOW = 5;
	public final static int MEDIC = 6;
	public final static int SHAMAN = 7;
	private boolean isRanged;
	private int damage;
	Player myPlayer;
	private int range;
	private ArrayList<Command> commandList = new ArrayList<Command>();
	private int unitType;
	
	public Unit(int unitType, int x, int y, Player p){
		this.x = x;
		this.y = y;
		myPlayer = p;
		this.unitType = unitType; 
		if(unitType == WORKER){
			hp = 50;
			MAXHEALTH = 50;
			damage =0;
			isRanged = false;
			range = 0;
		}
		if(unitType == WARRIOR){
			hp = 100;
			MAXHEALTH = 50;
			damage = 20;
			isRanged = false;
			range = 0;
		}
		if(unitType == ARCHER){
			hp = 60;
			MAXHEALTH = 60;
			damage = 10;
			isRanged = true;
			range = 4;
		}
		if(unitType == KNIGHT){
			hp = 200;
			MAXHEALTH = 200;
			damage = 40;
			isRanged = false;
			range = 0;
		}
		if(unitType == CROSSBOW){
			hp = 100;
			MAXHEALTH = 100;
			damage = 30;
			isRanged = true;
			range = 5;
		}
		if(unitType == MEDIC){
			hp = 80;
			MAXHEALTH = 80;
			damage = 0; //heals 5 for closest ally
			isRanged = true;
			range = 4;
		}
		if(unitType == SHAMAN){
			hp = 80;
			damage = 0; //heals 5 for allies in range
			isRanged = true;
			range = 4;
		}
		if(isRanged)
		{
			range = 400;
		}
		else
		{
			range = 50; 
		}
	}
	public void tic(){
		Command todo = commandList.get(0);//gets and does the next command that the unit needs to do
		if(todo.command == Command.ATTACKMOVE){
			
		}
		else if(todo.command == Command.ATTACK)
		{
			if(todo.target != null)
			{
				attack(todo.target);
			}
		}
		else if(todo.command == Command.BUILD){
			if(unitType == WORKER && todo.target != null)
			{
				if(distanceTo(todo.target) > 40)
				{
					moveToward(todo.target.x, todo.target.y);
					commandList.set(0,todo);
				}
				else
				{
					buildBuilding((Building)todo.target);
				}
			}
		}
		else if(todo.command == Command.MOVE){
			
		}
		commandList.remove(0);
	}
	public void moveToward(int x, int y) //Need to implement, moves the Unit, one unit in the direction 
	{
		
	}
	public void buildBuilding(Building b)
	{
		b.setBuildTic(b.getBuildTime());
		b.setConstruction(true);
	}
	public void attack(Thing target)
	{
		if(distanceTo(target) < range)
		{
			target.getAttacked(damage);
		}
	}
	public int distanceTo(Thing target)
	{
		return (int) Math.sqrt((target.y-this.y)*(target.y-this.y) + (target.x-this.x)*(target.x-this.x));
	}
	public boolean isRanged(){
		return isRanged;
	}
	public int unitType(){
		return unitType;
	}
	public void move(){
		
	}
}
