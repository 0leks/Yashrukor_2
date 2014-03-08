package main;

public class Command {
	public final static int ATTACKMOVE = 0;
	public final static int ATTACK = 1;
	public final static int MOVE = 2;
	public final static int BUILD = 3;
	int command;
	
	public Command (int command){
		this.command = command;
	}
	public int getCommand(){
		return command;
	}
}
