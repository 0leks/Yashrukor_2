package network;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import main.World;

public class Client implements Runnable{
	private Thread thread;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket socket;
	private World world;
	private Frame frame;
	
	public Client() {
		thread = new Thread(this);
		frame = new Frame();
	}
	@Override
	public void run() {
		InetAddress hostIP = null;
		try {

			String ip = JOptionPane.showInputDialog("IP:");
			if(ip==null) {
				ip = "localhost";
			}
			hostIP = InetAddress.getByName(ip);
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
			e.printStackTrace();
		}
	}
	private void read() {
		while(true) {
			try {
				Object o = in.readUnshared();
				System.out.println("Read Object:"+o);
				//this.send("Read Object:"+o);
				if(o instanceof World) {
					world = (World)o;
					System.out.println("Read World!");
					frame.panel.repaint();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	public class Frame extends JFrame {
		JPanel panel;
		public Frame() {
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize(500, 500);
			this.setVisible(true);
			panel = new JPanel() {
				public void paintComponent(Graphics g) {
					System.out.println("repainting");
					if(world!=null) {
						world.drawEverything(g);
					}
				}
			};
			this.add(panel);
		}
	}
	public void start() {
		thread.start();
	}
}
