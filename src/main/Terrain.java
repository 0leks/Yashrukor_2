package main;

public class Terrain extends Thing{
	public Terrain(int x, int y, int w, int h){
		
		super(x,y,w,h);
	}
	public int height(){
		return height;
	}
	public int width(){
		return width;
	}
}
