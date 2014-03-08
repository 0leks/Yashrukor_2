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
				
			}
		}
	}
}
