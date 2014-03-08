package main;

public class Command {
	public final int ATTACKMOVE = 0;
	public final int ATTACK = 1;
	public final int MOVE = 2;
	public final int BUILD = 3;
	int command;
	
	public Command (int command){
		this.command = command;
	}
	public int getCommand(){
		return command;
	}
}
