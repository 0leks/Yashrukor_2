package main;

import java.io.Serializable;

public class Resource implements Serializable{
	private int gold;
	private int wood;
	private int stone;
	private int food;
	public Resource(){
		gold=50;
		wood=50;
		stone=50;
		food=50;
	}
	public Resource (int g, int w, int s, int f){
		gold=g;
		wood=w;
		stone=s;
		food=f;
	}
	public void addGold(int g){
		gold+=g;
	}
	public void addWood(int w){
		wood+=w;
	}
	public void addStone(int s){
		stone+=s;
	}
	public void addFood(int f){
		food+=f;
	}
	public void add(Resource r){
		gold+=r.gold();
		wood+=r.wood();
		stone+=r.stone();
		food+=r.food();
	}
	public void clear(){
		gold=0;
		wood=0;
		stone=0;
		food=0;
	}
	public int gold(){
		return gold;
	}
	public int wood(){
		return wood;
	}
	public int stone(){
		return stone;
	}
	public int food(){
		return food;
	}
}
