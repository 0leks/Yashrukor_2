package main;

public class Building extends Thing {
	Player myPlayer;
	public final int BASE = 1;
	public final int FARM = 2;
	public final int QUARRY = 3;
	public final int LUMBERMILL = 4;
	public final int TOWER = 5;
	public final int BARRACKS = 6;
	public final int RANGE = 7;
	public final int HOSPITAL = 8; 
	private int type;
	private int timetic=0;
	private int unittic=0;
	private int unitreq;
	private int unittomake;
	
	private Resource worker=new Resource(-10,0,0,-10);
	private Resource warrior=new Resource(-20,-20,-20,-20);
	private Resource archer=new Resource(-30,-20,-10,-20);
	private Resource knight=new Resource(-40,-30,-30,-60);
	private Resource crossbow=new Resource(-60,-50,-30,-40);
	private Resource medic = new Resource(-30,-20,-10,-20);
	private Resource shaman=new Resource(-100,-50,-50,-100);
	
	private boolean creatingunits=false;
	public Building(int x, int y, int type){
		this.x = x;
		this.y = y;
		this.type=type;
	}
	public void tic(){
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
				//ATTACK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				timetic=0;
			}
		}
		else if(type==BARRACKS){
			
		}
		else if(type==RANGE){
		}
		if(type==BASE||type==BARRACKS||type==RANGE||type==HOSPITAL){
			if(creatingunits==true){
				unittic++;
				if(unittic==unitreq){
					//ADD THE UNIT THE THE WORLD!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					if(unittomake==Unit.WORKER){
						myPlayer.resource().add(worker);
					}
					else if(unittomake==Unit.WARRIOR){
						myPlayer.resource().add(warrior);
					}
					else if(unittomake==Unit.ARCHER){
						myPlayer.resource().add(archer);
					}
					else if(unittomake==Unit.KNIGHT){
						myPlayer.resource().add(knight);
					}
					else if(unittomake==Unit.CROSSBOW){
						myPlayer.resource().add(crossbow);
					}
					else if(unittomake==Unit.MEDIC){
						myPlayer.resource().add(medic);
					}
					else if(unittomake==Unit.SHAMAN){
						myPlayer.resource().add(shaman);
					}
					unittic=0;
					unitreq=0;
					creatingunits=false;
				}
			}
		}
	}
	public Player getPlayer(){
		return myPlayer;
	}
	public void createUnit(int unit){
		if (type==1){
			if(unit ==Unit.WORKER){
				unitreq=20*5;
				unittomake=unit;
				creatingunits=true;
			}
		}
		if (type==6){
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
		}
		if(type==7){
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
		}
		if(type==8){
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
}
