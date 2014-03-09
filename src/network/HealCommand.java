package network;

import java.io.Serializable;

import main.Thing;

public class HealCommand implements Serializable{
	int id;
	Thing target;
}
