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
	public Building(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

}
