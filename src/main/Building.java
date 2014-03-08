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
		else if(type==HOSPITAL){
			
		}
	}
	public Player getPlayer(){
		return myPlayer;
	}
	public void createUnit(int unit){
		if (type==1){
			if(unit ==)
		}
	}
}
