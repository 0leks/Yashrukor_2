package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import main.Building;
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
				if(read instanceof MoveCommand) {
					MoveCommand mv = (MoveCommand)read;
					Unit thing = (Unit)server.theworld.getThing(mv.id);
					Command c = new Command(Command.MOVE);
					c.setX(mv.target.x);
					c.setY(mv.target.y);
					thing.addCommand(c);
				}
				if(read instanceof CreateCommand) {
					CreateCommand cc = (CreateCommand)read;
					Building bob = (Building)(server.theworld.getThing(cc.id));
					bob.createUnit(cc.type);
				}
				if(read instanceof BuildCommand) {
					BuildCommand bc = (BuildCommand)read;
					Building bob = new Building(bc.location.x, bc.location.y, bc.type, me);
					me.resource().add(Building.getResource(bc.type));
					server.theworld.addBuilding(bob);
					System.out.println("creating building");
				}
				if(read instanceof AttackMoveCommand) {
					AttackMoveCommand ac = (AttackMoveCommand)read;
					Unit thing = (Unit)server.theworld.getThing(ac.id);
					Command c = new Command(Command.ATTACKMOVE);
					c.setX(ac.target.x);
					c.setY(ac.target.y);
					thing.addCommand(c);
				}
				if(read instanceof HealCommand) {
					HealCommand hc = (HealCommand)read;
					Unit healer = (Unit)(server.theworld.getThing(hc.id));
					Command c = new Command(Command.HEALSINGLE);
					c.setTarget(hc.target);
					healer.addCommand(c);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
//				try {
//					in.close();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
			} catch (IOException e) {
				e.printStackTrace();
//				try {
//					in.close();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
			}
		}
	}
}
