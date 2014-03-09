package network;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import main.World;

public class Server implements Runnable{
	public ServerSocket server;
	public Thread thread;
	public ArrayList<Connection> connections;
	private World world;
	public CreationFrame asdf;
	boolean drawworld;
	public Timer timer;
	public Server() {
		connections = new ArrayList<Connection>();
		asdf = new CreationFrame();
		thread = new Thread(this);
	}
	public void start() {
		thread.start();
	}
	public void sendtoall(Object o) {
		for(int a=0; a<connections.size(); a++) {
			connections.get(a).send(o);
		}
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
				c.send("asdf");
				c.start();
				connections.add(c);
			}
		} catch (IOException e) {
			System.out.println("Error Creating Server");
			e.printStackTrace();
		}
	}
	public void startGame() {
		System.out.println("GAME STARTING");
		sendtoall("GAME STARTING");
		world = new World(this);
		drawworld = true;
		timer = new Timer(150, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendtoall(world);
				asdf.panel.repaint();
			}
		});
		timer.start();
	}
	
	public class CreationFrame extends JFrame {
		JButton start;
		FlowLayout experimentLayout = new FlowLayout();
		JPanel panel;
		public CreationFrame() {
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize(500, 500);
			this.setTitle("Yashrukor Server Settings");
			this.setVisible(true);
			
			panel = new JPanel() {
				public void paintComponent(Graphics g) {
					if(drawworld) {
						world.drawEverything(g);
					}
				}
			};
			this.add(panel);
			panel.setLayout(experimentLayout);
			start = new JButton("Start");
			start.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					panel.remove(start);
					startGame();
					panel.repaint();
				}
			});
			panel.add(start);
		}
	}
	public static void main(String[] args) {
		Server s = new Server();
		s.start();
		//s.testConnection();
	}
	public void testConnection() {
		while(true) {
			for(int a=0; a<connections.size(); a++) {
				connections.get(a).send("asdf");
			}
		}
	}
}
