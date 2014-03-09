package main;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

public class Unit extends Thing  implements Serializable{
	public final static int WARRIOR = 2;
	public final static int ARCHER = 3;
	public final static int KNIGHT = 4;
	public final static int CROSSBOW = 5;
	public final static int MEDIC = 6;
	public final static int SHAMAN = 7;
	private boolean isRanged;
	private int damage;
	private int speed; 
	Player myPlayer;
	private int range;
	static int width = 50;
	static int height = 50;
	private ArrayList<Command> commandList = new ArrayList<Command>();
	private int unitType;
	
	public Unit(int unitType, int x, int y, Player p){
		super(x,y, width, height, 0);
		this.x = x;
		this.y = y;
		myPlayer = p;
		this.unitType = unitType; 
		if(unitType == WARRIOR){
			hp = 100;
			MAXHEALTH = 50;
			damage = 20;
			isRanged = false;
			range = 0;
			speed = 6;
		}
		if(unitType == ARCHER){
			hp = 60;
			MAXHEALTH = 60;
			damage = 10;
			isRanged = true;
			range = 4;
			speed = 6;
		}
		if(unitType == KNIGHT){
			hp = 200;
			MAXHEALTH = 200;
			damage = 40;
			isRanged = false;
			range = 0;
			speed = 10;
		}
		if(unitType == CROSSBOW){
			hp = 100;
			MAXHEALTH = 100;
			damage = 30;
			isRanged = true;
			range = 5;
			speed = 5;
		}
		if(unitType == MEDIC){
			hp = 80;
			MAXHEALTH = 80;
			damage = 0; //heals 5 for closest ally
			isRanged = true;
			range = 4;
			speed = 4;
		}
		if(unitType == SHAMAN){
			hp = 80;
			damage = 0; //heals 5 for allies in range
			isRanged = true;
			range = 4;
			speed = 4;
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
		if (commandList.size() > 0) {
			Command todo = commandList.remove(0);// gets and does the next command
												// that the unit needs to do
			
			if (todo.command == Command.ATTACKMOVE) {
				Command attacktheloser = null;
				ArrayList<Thing> inRange = myWorld.thingsInRange(new Point(this.x, this.y), myWorld.FOGOFWAR);
				System.out.println(inRange);
				for(Thing t : inRange)
				{
					//System.out.println("Runs this");
					if(t instanceof Unit)
					{
						if(!(((Unit)t).myPlayer.equals(myPlayer)))
						{
							attacktheloser = new Command(Command.ATTACK);
							attacktheloser.setTarget(t);
							this.addCommand(attacktheloser);
							break;
						}
					}
					if( t instanceof Building )
					{
						if(!(((Building)t).myPlayer.equals(myPlayer)))
						{
							attacktheloser = new Command(Command.ATTACK);
							attacktheloser.setTarget(t);
							this.addCommand(attacktheloser);
							break;
						}
					}
					
				}
				if(attacktheloser == null)
				{
					System.out.println("AttackMove Moving Toward " + x + ", " + y);
					moveToward(todo.x, todo.y);
					commandList.add(0,todo);
				}
			} else if (todo.command == Command.ATTACK) {
				if (todo.target != null) {
					if(this.euclidianDistanceFrom(new Point(todo.target.x, todo.target.y)) > range+todo.target.width)
					{
						moveToward(todo.target.x, todo.target.y);
						commandList.add(0,todo);
					}
					attack(todo.target);
				}
			} 
			else if (todo.command == Command.MOVE) {
				moveToward(todo.x, todo.y);
				if (Math.abs(this.x-todo.x) >speed || Math.abs(this.y-todo.y) >speed) {
					System.out.println("Command x: " + todo.x + " y: " + todo.y);
					commandList.add(0, todo);
				}
//				System.out.println(this.x+" "+this.y);
			}
//			commandList.remove(0);
		}
	}
	ArrayList<ArrayList> Paths;
	ArrayList<Point> somePointList;
	int pathCounter = 0;
	public ArrayList<PathPoint> findPaths(int x, int y)
	{
		Point toGet = new PathPoint(x,y);
		ArrayList<PathPoint> closedSet = new ArrayList<PathPoint>();
		ArrayList<PathPoint> openSet = new ArrayList<PathPoint>();
		openSet.add(new PathPoint(this.x,this.y,0));
		//ArrayList<Point> cameFrom = new ArrayList<Point>();
		while(openSet.size() > 0)
		{
			PathPoint current = null;
			double lowestCost = Double.MAX_VALUE;
			for(PathPoint P : openSet)
			{
				if(P.cost <= lowestCost)
				{
					System.out.println("get here #1");
					current = P;
					lowestCost = P.cost; 
					if(P.equals(toGet))
					{
						return openSet;
					}
				}else{
					System.out.println("Got here #2");
				}
				
			}
			openSet.remove(current);
			ArrayList<PathPoint> adjSquares = walkableSquares(current);
			//System.out.println(adjSquares);
			System.out.println("OpenSet: " + openSet);
			for( PathPoint po : adjSquares)
			{
				System.out.println("Current " + current);
				po.parent = current;
				po.cost = po.parent.cost + 1;
				po.heuristic = distanceBetween(po,toGet);
				po.estCost = po.cost + po.heuristic;
				//System.out.println(current.successors);
				current.successors.add(po);
				boolean cont = false;
				for(PathPoint pp : openSet)
				{
					if(pp.successors.contains(po) && pp.successors.get(pp.successors.indexOf(po)).estCost < po.estCost)
					{
						cont = true;
						break;
					}
				}
				if(cont = true)
				{
					continue;
				}
				for(PathPoint pp : closedSet)
				{
					if(pp.successors.contains(po) && pp.successors.get(pp.successors.indexOf(po)).estCost < po.estCost)
					{
						cont = true;
						break; 
					}
				}
				if(cont = true)
				{
					continue;
				}
				System.out.println("awwh yiss");
				openSet.add(po);
			}
			closedSet.add(current);
		}
		return null;
	}
	public ArrayList<PathPoint> walkableSquares(Point p)
	{
		ArrayList<PathPoint> surrPoints = new ArrayList<PathPoint>();
		for(int i = -1; i < 2; i++)
		{
			for(int j = -1; i<2; i++)
			{
				if(!(myWorld.pointOccupied(new Point(p.x+i,p.y+j))))
				{
					surrPoints.add(new PathPoint(p.x+i,p.y+j));
				}
			}
		}
		surrPoints.remove(p);
		return surrPoints;
	}
	Point getNextPointInAngle(Point p,int angle)
	{
		int x = (int) (speed*Math.cos(angle));
		int y = (int) (speed*Math.sin(angle));
		return new Point(p.x+x,p.y+y);
	}
	double directionToward(Point ip, Point fp)
	{
		int dy = fp.y - ip.y;
		int dx = fp.x - ip.x;
		return Math.atan2(dy, dx);
	}
//	ArrayList<ArrayList> Paths;
//	public ArrayList<Point> findPath(int x, int y)
//	{
//		
//	}
	public void addCommand(Command c) {
		commandList.add(c);
	}
	
	public void moveToward(int x, int y){ //Need to implement, moves the Unit, one unit in the direction 
		boolean colli=false;
		for(Thing t:myWorld.getAllThings()){
			if(this.collides(t)){
				colli=true;
			}
		}
		if(colli=false){
			int dx = x-this.x;
			int dy = y-this.y;
			if(dy == 0)
			{
				if(dx < 0)
					this.setPosition(this.x - speed , this.y);
				else if( dx == 0)
				{
					return;
				}
				else
					this.setPosition(this.x + speed, this.y);
			}
			else if(dx == 0)
			{
				if(dy < 0)
					this.setPosition(this.x, this.y - speed);
				else if(dy == 0)
					return;
				else
					this.setPosition(this.x, this.y + speed);
			}
			else
			{
				double ang = directionToward((new Point(this.x,this.y)), (new Point (x,y)));
				int changex = (int) (Math.cos(ang)*getSpeed());
				int changey = (int) (Math.sin(ang)*getSpeed());
				System.out.println(getSpeed());
				this.setPosition(this.x+changex, this.y+changey);
	//			System.out.println(ang+" "+changex+" "+changey);
			}	
		}
	}
	public int getSpeed()
	{
		return speed;
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
	public double distanceBetween(Point ip, Point fp)
	{
		return Math.sqrt((ip.y-fp.y)*(ip.y-fp.y) + (ip.x-fp.x)*(ip.x-fp.x));
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
