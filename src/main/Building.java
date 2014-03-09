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
	private int unittic=0;
	private int unitreq;
	private int buildtic;
	private int buildTime;
	private int unittomake;
	private boolean underConstruction;
	
	private Resource warrior=new Resource(-20,-20,-20,-20);
	private Resource archer=new Resource(-30,-20,-10,-20);
	private Resource knight=new Resource(-40,-30,-30,-60);
	private Resource crossbow=new Resource(-60,-50,-30,-40);
	private Resource medic = new Resource(-30,-20,-10,-20);
	private Resource shaman=new Resource(-100,-50,-50,-100);
	
	private boolean creatingunits=false;
	public Building(int x, int y, int type, Player p) {
		super(x, y, 100, 100 ,0);
		myPlayer=p;
		this.type=type;
		if(type == BASE)
			buildTime = 0;
		else if(type == FARM)
			buildTime = 200;
		else if(type == QUARRY)
			buildTime = 200;
		else if(type == LUMBERMILL)
			buildTime = 200;
		else if(type == TOWER)
			buildTime = 600;
		else if(type == BARRACKS)
			buildTime = 200;
		else if(type == RANGE)
			buildTime = 100;
		else if(type == HOSPITAL)
			buildTime = 200;
		
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
	public void tic(){
		System.out.println("Under Construction: " + underConstruction);
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
				System.out.println("Unitereq: " + unitreq);
				if(unitreq>0){
					System.out.println(unitreq);
				}
				if(unittic==unitreq){
					if(unittomake==Unit.WARRIOR){
						myWorld.getAllThings().add(new Unit(1,x+width,y+height,myPlayer));
						System.out.println("MADE");
						myPlayer.resource().add(warrior);
					}
					else if(unittomake==Unit.ARCHER){
						myWorld.getAllThings().add(new Unit(2,x+width,y+height,myPlayer));
						myPlayer.resource().add(archer);
					}
					else if(unittomake==Unit.KNIGHT){
						myWorld.getAllThings().add(new Unit(3,x+width,y+height,myPlayer));
						myPlayer.resource().add(knight);
					}
					else if(unittomake==Unit.CROSSBOW){
						myWorld.getAllThings().add(new Unit(4,x+width,y+height,myPlayer));
						myPlayer.resource().add(crossbow);
					}
					else if(unittomake==Unit.MEDIC){
						myWorld.getAllThings().add(new Unit(5,x+width,y+height,myPlayer));
						myPlayer.resource().add(medic);
					}
					else if(unittomake==Unit.SHAMAN){
						myWorld.getAllThings().add(new Unit(6,x+width,y+height,myPlayer));
						myPlayer.resource().add(shaman);
					}
					unittic=0;
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
		if (type==5){
			if(unit ==Unit.WARRIOR&&myPlayer.resource().check(warrior)){
				unitreq=20*10;
				unittomake=unit;
				creatingunits=true;
				System.out.println(creatingunits);
			}
			else if(unit ==Unit.KNIGHT&&myPlayer.resource().check(knight)){
				unitreq=20*30;
				unittomake=unit;
				creatingunits=true;
			}
		}
		if(type==6){
			if(unit ==Unit.ARCHER&&myPlayer.resource().check(archer)){
				unitreq=20*10;
				unittomake=unit;
				creatingunits=true;
			}
			else if(unit ==Unit.CROSSBOW&&myPlayer.resource().check(crossbow)){
				unitreq=20*40;
				unittomake=unit;
				creatingunits=true;
			}
		}
		if(type==7){
			if(unit ==Unit.MEDIC&&myPlayer.resource().check(medic)){
				unitreq=20*20;
				unittomake=unit;
				creatingunits=true;
			}
			else if(unit ==Unit.SHAMAN&&myPlayer.resource().check(shaman)){
				unitreq=20*120;
				unittomake=unit;
				creatingunits=true;
			}
		}
	}
}
