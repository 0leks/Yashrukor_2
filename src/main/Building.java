package main;

public class Building extends Thing {
	Player myPlayer;
	public final static int BASE = 0;
	public final static int FARM = 1;
	public final static int QUARRY = 2;
	public final static int LUMBERMILL = 3;
	public final static int TOWER = 4;
	public final static int BARRACKS = 5;
	public final static int RANGE = 6;
	public final static int HOSPITAL = 7;
	private int type;
	private int timetic=0;
	public int unittic=0;
	public int unitreq;
	private int buildtic;
	private int buildTime;
	private int unittomake;
	private boolean underConstruction;
	
	public boolean creatingunits=false;
	public Building(int x, int y, int type, Player p) {
		super(x, y, 100, 100 ,300);
		super.MAXHEALTH=300;
		this.type=type;
		myPlayer=p;
		if(type == BASE){
			buildTime = 0;
			super.hp=1200;
			super.MAXHEALTH=1200;
		}
		else if(type == FARM)
			buildTime = 200;
		else if(type == QUARRY)
			buildTime = 200;
		else if(type == LUMBERMILL)
			buildTime = 200;
		else if(type == TOWER){
			buildTime = 600;
			super.hp=600;
			super.MAXHEALTH=600;
		}
		else if(type == BARRACKS){
			buildTime = 200;
		 	super.hp=400;
		 	super.MAXHEALTH=400;
		}
		else if(type == RANGE){
			buildTime = 100;
			super.hp=400;
			super.MAXHEALTH=400;
		}
		else if(type == HOSPITAL){
			buildTime = 200;
			super.hp=400;
			super.MAXHEALTH=400;
		}
	}
	public static Resource getResource(int type) {
		if(type==Building.FARM) {
			return new Resource(-10,-40,-10,0);
		} else if(type==Building.QUARRY) {
			return new Resource(-10,-50,0,0);
		} else if(type==Building.LUMBERMILL) {
			return new Resource(-50,-0,-0,0);
		} else if(type==Building.TOWER) {
			return new Resource(-100,-40,-80,0);
		} else if(type==Building.BARRACKS) {
			return new Resource(-30,-30,-50,0);
		} else if(type==Building.RANGE) {
			return new Resource(-30,-50,-30,0);
		} else if(type==Building.HOSPITAL) {
			return new Resource(-40,-50,-50,0);
		} else {
			return new Resource(0, 0, 0, 0);
		}
	}
	public void makeUnit() {
		
	}
	public void tic(){
		if(hp <= 0)
		{
			myWorld.allThings.remove(this);
			super.destroyed = true;
		}
		if(underConstruction == false){
			if(type==BASE){
				timetic++;
				if(timetic==20){
					myPlayer.resource().addGold(2);
					timetic=0;
				}
			}
			else if(type==FARM){
				timetic++;
				if(timetic==20){
					myPlayer.resource().addFood(4);
					timetic=0;
				}
			}
			else if(type==QUARRY){
				timetic++;
				if(timetic==20){
					myPlayer.resource().addStone(5);
					timetic=0;
				}
			}
			else if(type==LUMBERMILL){
				timetic++;
				if(timetic==20){
					myPlayer.resource().addWood(5);
					timetic=0;
				}
			}
			else if(type==TOWER){
				timetic++;
				if(timetic==20){
					//	ATTACK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					timetic=0;
				}
			}
		}
		if(type==BARRACKS||type==RANGE||type==HOSPITAL){
			//System.out.println("Creatingunits " + creatingunits);
			if(creatingunits){
				unittic++;
				System.out.println("Unitereq: " + unitreq+"  Unittic:"+unittic);
				if(unittic>=unitreq){
					System.out.println("Creating Unit:"+unittomake);
					myWorld.getAllThings().add(new Unit(unittomake,x+width,y+height,myPlayer));
					System.out.println("MADE");
					unittic=0;
					creatingunits = false;
				}
			}	
		}
		else
		{
			buildtic--;
			if(buildtic <=0)
			{
				underConstruction = false; 
			}
			
		}
	}
	public int getBuildTime()
	{
		return buildTime;
	}
	public void setBuildTic(int x)
	{
		buildtic = x;
	}
	public void setConstruction(boolean b)
	{
		underConstruction = b;
	}
	public boolean getConstruction()
	{
		return underConstruction;
	}
	public Player getPlayer(){
		return myPlayer;
	}
	public int getType(){
		return type;
	}
	public void createUnit(int unit){
		if(myPlayer.resource().check(Unit.getResource(unit))) {
			myPlayer.resource().add(Unit.getResource(unit));
			if(unit ==Unit.WARRIOR){
				unitreq=20*10;
				unittomake=unit;
				creatingunits=true;
			}
			else if(unit ==Unit.KNIGHT){
				unitreq=20*30;
				unittomake=unit;
				creatingunits=true;
			}
			if(unit ==Unit.ARCHER){
				unitreq=20*10;
				unittomake=unit;
				creatingunits=true;
			}
			else if(unit ==Unit.CROSSBOW){
				unitreq=20*40;
				unittomake=unit;
				creatingunits=true;
			}
			if(unit ==Unit.MEDIC){
				unitreq=20*20;
				unittomake=unit;
				creatingunits=true;
			}
			else if(unit ==Unit.SHAMAN){
				unitreq=20*120;
				unittomake=unit;
				creatingunits=true;
			}
		}
	}
	public String toString() {
		return "Building("+super.toString()+")";
	}
}
