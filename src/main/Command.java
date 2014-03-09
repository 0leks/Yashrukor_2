package main;

public class Command {
	public final static int ATTACKMOVE = 0;
	public final static int ATTACK = 1;
	public final static int MOVE = 2;
	public final static int BUILD = 3;
	public final static int HEALSINGLE = 4;
	public final static int HEALAOE = 5;
	int command;
	Thing target;
	int x;
	int y;
	
	public Command (int command){
		this.command = command;
	}
	public int getCommand(){
		return command;
	}
	
	public void setTarget(Thing t)
	{
		target = t; 
	}
	public void setX(int tx)
	{
		x = tx;
	}
	public void setY(int ty)
	{
		y = ty;
	}
	
}
