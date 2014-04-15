package main;

import java.awt.Point;
import java.awt.Rectangle;
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
			hp = 1000;
			MAXHEALTH = 1000;
			damage = 5;
			isRanged = false;
			range = 20;
			speed = 6;
		}
		if(unitType == ARCHER){
			hp = 600;
			MAXHEALTH = 600;
			damage = 4;
			isRanged = true;
			range = 160;
			speed = 6;
		}
		if(unitType == KNIGHT){
			hp = 2000;
			MAXHEALTH = 2000;
			damage = 6;
			isRanged = false;
			range = 25;
			speed = 10;
		}
		if(unitType == CROSSBOW){
			hp = 1000;
			MAXHEALTH = 1000;
			damage = 4;
			isRanged = true;
			range = 200;
			speed = 5;
		}
		if(unitType == MEDIC){
			hp = 800;
			MAXHEALTH = 800;
			damage = 0; //heals 5 for closest ally
			isRanged = true;
			range = 160;
			speed = 4;
		}
		if(unitType == SHAMAN){
			hp = 800;
			damage = 0; //heals 5 for allies in range
			isRanged = true;
			range = 160;
			speed = 4;
		}
		if(isRanged)
		{
			range = 200;
		}
		else
		{
			range = 20; 
		}
	}
	public Player getPlayer() {
		return this.myPlayer;
	}
	public void tic(){
		
		super.tic();
		if(unitType == SHAMAN)
		{
			for(Thing t : myWorld.thingsInRange(new Point(this.x, this.y), 80))
			{
				if(t instanceof Unit && ((Unit)t).myPlayer.equals(myPlayer) )
				{
					t.hp += 2; 
 				}
			}
		}
		if (commandList.size() > 0) {
			Command todo = commandList.remove(0);// gets and does the next command
												// that the unit needs to do
			
			if (todo.command == Command.ATTACKMOVE) {
				Command attacktheloser = null;
				ArrayList<Thing> inRange = myWorld.thingsInRange(new Point(this.x, this.y), myWorld.FOGOFWAR);
				System.out.println(inRange);
				//Command attacktheloser = null;
				ArrayList<Thing> enemies = new ArrayList<Thing>();
				for(Thing t : inRange)
				{
					//System.out.println("Runs this");
					if(t instanceof Unit)
					{
						if(!(((Unit)t).myPlayer.equals(myPlayer)))
						{
							enemies.add(t);
						}
					}
					if( t instanceof Building )
					{
						if(!(((Building)t).myPlayer.equals(myPlayer)))
						{
							enemies.add(t);
						}
					}
					
				}
				System.out.println("The size of enemies " + enemies.size());
				int distance = Integer.MAX_VALUE;
				Thing tempo = null;
				if(enemies.size() > 0)
				{
					for(Thing th : enemies)
					{
						if((this.euclidianDistanceFrom(new Point(th.x,th.y)) < distance))
						{
							tempo = th;
							distance = this.euclidianDistanceFrom(new Point(th.x,th.y));
						}
					}
					Command killThis = new Command(Command.ATTACK);
					killThis.setTarget(tempo);
					commandList.add(0,killThis);
				}
				else
				{
//					System.out.println("AttackMove Moving Toward " + x + ", " + y);
					moveToward(todo.x, todo.y);
					commandList.add(0,todo);
				}
			} 
			else if (todo.command == Command.ATTACK) {
				System.out.println("Attack Target: " + todo.target);
				if (todo.target != null) {
					if(this.euclidianDistanceFrom(new Point(todo.target.x, todo.target.y)) > range+todo.target.width)
					{
						moveToward(todo.target.x, todo.target.y);
						commandList.add(0,todo);
					}
					else
						attack(todo.target);
					if(todo.target.hp > 0)
						commandList.add(0,todo);
				}
			}
			else if(todo.command == Command.HEALSINGLE)
			{
				if(this.unitType == MEDIC)
				{
					if(todo.target != null && todo.target instanceof Unit && ((Unit)todo.target).myPlayer.equals(myPlayer) )
					{
						if(this.euclidianDistanceFrom(new Point(todo.target.x, todo.target.y)) < range+todo.target.width)
						{
							todo.target.hp += 2;
						}
						else
						{
							moveToward(todo.target.x, todo.target.y);
							commandList.add(0,todo);
						}
					}
				}
			}
			else if (todo.command == Command.MOVE) {
				moveToward(todo.x, todo.y);
				if (Math.abs(this.x-todo.x) >speed || Math.abs(this.y-todo.y) >speed) {
//					System.out.println("Command x: " + todo.x + " y: " + todo.y);
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
		commandList.clear();
		commandList.add(c);
	}
	public Thing findcollisioninline(int x1, int y1, int x2, int y2) {
		int dx = x2-x1;
		int dy = y2-y1;
		double distance = Math.sqrt(dx*dx+dy*dy);
		double deltax = dx/distance;
		double deltay = dy/distance;
		double xc = x1;
		double yc = y1;
		for(int a=0; a<distance; a++) {
			xc = xc+deltax;
			yc = yc+deltay;
			int x = (int)xc;
			int y = (int)yc;
			Thing coll = myWorld.findcollision(this, new Rectangle(x, y, width, height));
			if(coll==null) {
				
			} else {
//				System.out.println("FOUND COLLISION");
				return coll;
			}
		}
		return null;
	}
	public Thing find(Node n1, Node n2) {
		return findcollisioninline(n1.x, n1.y, n2.x, n2.y);
	}
	public void moveToward(int x, int y){ //Need to implement, moves the Unit, one unit in the direction 
		ArrayList<Node> nodes = new ArrayList<Node>();
		Node beg = new Node(this.x, this.y);
		Node end = new Node(x, y);
		beg.next = end;
		end.prev = beg;
		nodes.add(beg);
		nodes.add(end);
		for(int a=0; a<nodes.size()-1 && nodes.size()<100; a++) {
			System.out.println("Nodes:"+nodes.size());
			Node cur = nodes.get(a);
			if(cur.next==null) {
				
			} else {
				Node next = cur.next;
				Thing coll = findcollisioninline(cur.x, cur.y, next.x, next.y);
				System.out.println("Next:"+next);
				System.out.println("Collision:"+coll);
				if(coll!=null && coll.id==-100) {
					System.out.println("Off the Map");
				} else if(coll!=null) {
					System.out.println(coll);
					Node tl = new Node(coll.x-this.width-20, coll.y-this.height-20);
					Node tr = new Node(coll.x+coll.width+20, coll.y-this.height-20);
					Node bl = new Node(coll.x-this.width-20, coll.y+coll.height+20);
					Node br = new Node(coll.x+coll.width+20, coll.y+coll.height+20);
					tl.next = tr;
					tl.prev = bl;
					tr.next = br;
					tr.prev = tl;
					br.next = bl;
					br.prev = tr;
					bl.next = tl;
					bl.prev = br;
//					ArrayList<Node> temp = new ArrayList<Node>();
					
					if(findcollisioninline(cur.x, cur.y, tl.x, tl.y)==null){
						tl.entrance = true;
						System.out.println("Entrance:"+tl);
					}
					Thing inter = find(tl, next);
					if(inter!=coll) {
						tl.exit = true;
						System.out.println("Exit:"+tl);
					}
					if(findcollisioninline(cur.x, cur.y, tr.x, tr.y)==null){
						tr.entrance = true;
						System.out.println("Entrance:"+tr);
					}
					inter = find(tr, next);
					if(inter!=coll) {
						tr.exit = true;
						System.out.println("Exit:"+tr);
					}
					if(findcollisioninline(cur.x, cur.y, bl.x, bl.y)==null){
						bl.entrance = true;
						System.out.println("Entrance:"+bl);
					}
					inter = find(bl, next);
					if(inter!=coll) {
						bl.exit = true;
						System.out.println("Exit:"+bl);
					}
					if(findcollisioninline(cur.x, cur.y, br.x, br.y)==null){
						br.entrance = true;
						System.out.println("Entrance:"+br);
					}
					inter = find(br, next);
					if(inter!=coll) {
						br.exit = true;
						System.out.println("Exit:"+br);
					}
					Node use = null;
					if(tl.both())
						use = tl;
					if(tr.both())
						use = tr;
					if(bl.both())
						use = bl;
					if(br.both())
						use = br;
					if(use!=null) {
						cur.next = use;
						next.prev = use;
						nodes.add(use);
					} else {
						Node enter = null;
						if(tl.entrance)
							enter = tl;
						if(tr.entrance)
							enter = tr;
						if(bl.entrance)
							enter = bl;
						if(br.entrance)
							enter = br;
						Node exit = null;
						if(enter!=null) {
							if(enter.next.exit)
								exit = enter.next;
							if(enter.prev.exit) 
								exit = enter.prev;
							if(exit!=null) {
								cur.next = enter;
								enter.next = exit;
								next.prev = exit;
								nodes.add(enter);
								nodes.add(exit);
							}
						}
					}
					
					
//					temp.add(tr);
//					temp.add(bl);
//					temp.add(br);
//					for(int b=0; a<temp.size(); b++) {
//						
//					}
//					System.out.println(tl+" "+tr+" "+bl+" "+br);
				}
				
			}
		}
//		System.out.println(coll);
//		
//		if(dy == 0)
//		{
//			if(dx < 0)
//				newx = this.x - speed;
//			else if( dx == 0)
//			{
//				return;
//			}
//			else
//				newx = this.x + speed;
//		}
//		else if(dx == 0)
//		{
//			if(dy < 0)
//				newy = this.y - speed;
//			else if(dy == 0)
//				return;
//			else
//				newy = this.y + speed;
//		}
//		else
//		{
		System.out.println("Nodes:");
		for(Node n : nodes) {
			System.out.println(n);
		}
		System.out.println(":Nodes");
		int newx = this.x;
		int newy = this.y;
		int targetx = beg.next.x;
		int targety = beg.next.y;
		double ang = directionToward((new Point(this.x,this.y)), (new Point (targetx,targety)));
		int changex = (int) (Math.cos(ang)*getSpeed());
		int changey = (int) (Math.sin(ang)*getSpeed());
		System.out.println(getSpeed());
		
		newx = this.x+changex;
		newy = this.y+changey;
//		}	
		Rectangle cur = getBounds();
		cur.x = newx;
		cur.y = newy;
		boolean colli=false;
		for(int a=0; a<myWorld.getAllThings().size(); a++){
			Thing t=myWorld.getAllThings().get(a);
			if(t!=this && t.collides(cur)){
				colli=true;
			}
		}
		if(!colli)
			this.setPosition(newx, newy);
//		double ang = directionToward((new Point(this.x,this.y)), (new Point (x,y)));
//		int changex = (int) (Math.cos(ang)*getSpeed());
//		int changey = (int) (Math.sin(ang)*getSpeed());
//		System.out.println(getSpeed());
//		
//		newx = this.x+changex;
//		newy = this.y+changey;
////		}	
//		Rectangle cur = getBounds();
//		cur.x = newx;
//		cur.y = newy;
//		boolean colli=false;
//		for(Thing t:myWorld.getAllThings()){
//			if(t!=this && t.collides(cur)){
//				colli=true;
//			}
//		}
//		if(!colli)
//			this.setPosition(newx, newy);
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
		target.getAttacked(damage);
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
	public static Resource getResource(int type) {
		if(type==Unit.WARRIOR)
			return new Resource(-20,-20,-20,-20);
		if(type==Unit.ARCHER) 
			return new Resource(-30,-20,-10,-20);
		if(type==Unit.KNIGHT)
			return new Resource(-40,-30,-30,-60);
		if(type==Unit.CROSSBOW)
			return new Resource(-60,-50,-30,-40);
		if(type==Unit.MEDIC)
			return new Resource(-30,-20,-10,-20);
		if(type==Unit.SHAMAN)
			return new Resource(-100,-50,-50,-100);
		return new Resource(0,0,0,0);
	}
	public int unitType(){
		return unitType;
	}
	public void move(){
		
	}
}
