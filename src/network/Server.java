package network;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

import main.Thing;
import main.Unit;
import main.World;

public class Server implements Runnable{
	public ServerSocket server;
	public Thread thread;
	public ArrayList<Connection> connections;
	public World theworld;
	public CreationFrame asdf;
	boolean drawworld;
	public Timer timer;
	public Point lookingat;
	private boolean movecameraright;
	private boolean movecameraleft;
	private boolean movecameraup;
	private boolean movecameradown;
	private int cameraspeed = 20;
	public Server() {
		connections = new ArrayList<Connection>();
		asdf = new CreationFrame();
		lookingat = new Point(asdf.getWidth()/2, asdf.getHeight()/2);
		thread = new Thread(this);
		timer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(movecameraup) {
					lookingat.y-=cameraspeed;
				}
				if(movecameradown) {
					lookingat.y+=cameraspeed;
				}
				if(movecameraleft) {
					lookingat.x-=cameraspeed;
				}
				if(movecameraright) {
					lookingat.x+=cameraspeed;
				}
				if(theworld!=null) {
					sendtoall(theworld);
					for(int a=0; a<theworld.getAllThings().size(); a++) {
						if(theworld.getAllThings().get(a) instanceof Thing) {
							sendtoall(theworld.getAllThings().get(a));
						}
					}
				}
				asdf.repaint();
			}
		});
		timer.start();
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
				Connection c = new Connection(in, out, this);
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
		theworld = new World(this);
		drawworld = true;
		timer.start();
	}
	
	public class CreationFrame extends JFrame {
		JButton start;
		FlowLayout experimentLayout = new FlowLayout();
		JPanel panel;
		public CreationFrame() {
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			//this.setSize(500, 500);
			this.setUndecorated(true);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			this.setTitle("Yashrukor Server Settings");
			this.setVisible(true);

			
			panel = new JPanel() {
				public void paintComponent(Graphics g) {
					if(drawworld) {
						theworld.drawEverything(g, panel, lookingat, null,true,0);
						g.setColor(Color.white);
						g.drawString("lookingat: "+lookingat, 50, 50);
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
//					panel.repaint();
				}
			});
			panel.add(start);
			this.addKeyListener(new KeyListener() {
				@Override
				public void keyPressed(KeyEvent e) {
					if(e.getKeyCode()==KeyEvent.VK_UP) {
						movecameraup = true;
					}
					if(e.getKeyCode()==KeyEvent.VK_DOWN) {
						movecameradown = true;
					}
					if(e.getKeyCode()==KeyEvent.VK_LEFT) {
						movecameraleft = true;
					}
					if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
						movecameraright = true;
					}
				}
				@Override
				public void keyReleased(KeyEvent e) {
					if(e.getKeyCode()==KeyEvent.VK_UP) {
						movecameraup = false;
					}
					if(e.getKeyCode()==KeyEvent.VK_DOWN) {
						movecameradown = false;
					}
					if(e.getKeyCode()==KeyEvent.VK_LEFT) {
						movecameraleft = false;
					}
					if(e.getKeyCode()==KeyEvent.VK_RIGHT) {
						movecameraright = false;
					}
				}
				@Override
				public void keyTyped(KeyEvent e) {
				}
			});
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
