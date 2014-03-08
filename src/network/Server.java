package network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable{
	public ServerSocket server;
	public Thread thread;
	public ArrayList<Connection> connections;
	public Server() {
		connections = new ArrayList<Connection>();
		thread = new Thread(this);
	}
	public void start() {
		thread.start();
	}
	@Override
	public void run() {
		try {
			System.out.println("Creating Server");
			server = new ServerSocket(34555);
			while(true) {
				System.out.println("waiting for connection");
				Socket socket = server.accept();
				System.out.println("Creating ObjectOutputStream");
				ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
				out.flush();
				System.out.println("Creating ObjectInputStream");
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				Connection c = new Connection(in, out);
				c.start();
			}
		} catch (IOException e) {
			System.out.println("Error Creating Server");
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		Server s = new Server();
		s.start();
		s.testConnection();
	}
	public void testConnection() {
		while(true) {
			for(int a=0; a<connections.size(); a++) {
				connections.get(a).send("asdf");
			}
		}
	}
}
