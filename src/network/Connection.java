package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import main.Player;

public class Connection implements Runnable{
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Thread thread;
	private Player me;
	public Connection(ObjectInputStream i, ObjectOutputStream o) {
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
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
