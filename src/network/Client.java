package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable{
	private Thread thread;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket socket;
	public Client() {
		thread = new Thread(this);
	}
	@Override
	public void run() {
		InetAddress hostIP = null;
		try {
			hostIP = InetAddress.getByName("localhost");
			if (hostIP == null) {
				return;
			}
			System.out.println("Creating Socket");
			socket = new Socket(hostIP, 34555);
			System.out.println("Creating ObjectOutputStream");
			out = new ObjectOutputStream(socket.getOutputStream());
			out.flush();
			System.out.println("Creating ObjectInputStream");
			in = new ObjectInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		read();
	}
	public static void main(String[] args) {
		Client c = new Client();
		c.start();
	}
	public void send(Object o) {
		try {
			out.writeUnshared(o);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void read() {
		while(true) {
			try {
				Object o = in.readUnshared();
				System.out.println("Read Object:"+o);
				this.send("Read Object:"+o);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	public void start() {
		thread.start();
	}
}
