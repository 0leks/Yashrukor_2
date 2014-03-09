package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import main.Command;
import main.Player;
import main.Thing;
import main.Unit;

public class Connection implements Runnable{
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Thread thread;
	private Player me;
	private Server server;
	public Connection(ObjectInputStream i, ObjectOutputStream o, Server serv) {
		server = serv;
		in = i;
		out = o;
		thread = new Thread(this);
	}
	public void send(Object o) {
		try {
			out.writeUnshared(o);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void start() {
		thread.start();
	}
	public Player getPlayer() {
		return me;
	}
	public void run() {
		while(true) {
			try {
				Object read = in.readUnshared();
				System.out.println("Read Object:"+read);
				if(read instanceof Player) {
					me = (Player)read;
				}
//				if(read instanceof MoveCommand) {
//					MoveCommand mv = (MoveCommand)read;
//					Unit thing = (Unit)server.world.getThing(mv.id);
//					Command c = new Command(Command.MOVE);
//					c.setX(mv.target.x);
//					c.setY(mv.target.y);
//					thing.addCommand(c);
//				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
