package main;

public class Terrain extends Thing{
	private int height;
	private int width;
	public Terrain(int x, int y, int h, int w){
		
		super(x,y,w,h);
		height=h;
		width=w;
		this.x=x;
		this.y=y;
	}
	public int height(){
		return height;
	}
	public int width(){
		return width;
	}
}
