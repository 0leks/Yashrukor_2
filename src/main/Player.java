package main;

import java.awt.Color;
import java.io.Serializable;

public class Player implements Serializable{
	private Resource resource;
	private final Color color;
	public Player(Color scolor){
		resource=new Resource();
		color = scolor;
	}
	public Color getColor() {
		return color; 
	}
	public Resource resource(){
		return resource;
	}
	public boolean equals(Player p){
		if (color.equals(p.color)){
			return true;
		}
		return false;
	}
	public String toString() {
		return "Player("+color+" "+resource+")";
	}
}
