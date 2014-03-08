package main;

import java.io.Serializable;

public class Player implements Serializable{
	private Resource resource;
	public Player(){
		resource=new Resource();
	}
	public Resource resource(){
		return resource;
	}
}
