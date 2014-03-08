package main;

import java.io.Serializable;

public class Building extends Thing  implements Serializable{
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
	private int productiontic=0;
	public Building(int x, int y, int type){
		this.x = x;
		this.y = y;
		this.type=type;
	}
	public void tic(){
		if(type==BASE){
			productiontic++;
			if(productiontic==20){
				myPlayer.resource().addGold(2);
				productiontic=0;
			}
		}
		else if(type==FARM){
			productiontic++;
			if(productiontic==20){
				myPlayer.resource().addFood(4);
				productiontic=0;
			}
		}
		else if(type==QUARRY){
			productiontic++;
			if(productiontic==20){
				myPlayer.resource().addStone(5);
				productiontic=0;
			}
		}
		else if(type==LUMBERMILL){
			productiontic++;
			if(productiontic==20){
				myPlayer.resource().addWood(5);
				productiontic=0;
			}
		}
		else if(type==TOWER){
			
		}
		else if(type==BARRACKS){
			
		}
		else if(type==RANGE){
			
		}
		else if(type==HOSPITAL){
			
		}
	}
	public Player getPlayer()
	{
		return myPlayer;
	}
}
