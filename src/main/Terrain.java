package main;

public class Terrain extends Thing{
	public Terrain(int x, int y, int h, int w){
		super(x,y,w,h, 1);
	}
	public int height(){
		return height;
	}
	public int width(){
		return width;
	}
}
