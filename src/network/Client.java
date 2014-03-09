package network;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;

import main.Player;
import main.World;

public class Client implements Runnable{
	private Thread thread;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Socket socket;
	private World world;
	private Frame frame;
	private PlayerSelectionFrame frame1;
	private Player me;
	private Point lookingat;
	private boolean movecameraright;
	private boolean movecameraleft;
	private boolean movecameraup;
	private boolean movecameradown;
	private int cameraspeed = 20;
	private Timer timer; 
	private Point mousepress;
	public Client() {
		thread = new Thread(this);
		frame1 = new PlayerSelectionFrame();
		lookingat = new Point(0, 0);
		timer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(movecameraup) {
					lookingat.y-=cameraspeed;
					if(lookingat.y<0){
						lookingat.y=0;
					}
				}
				if(movecameradown) {
					lookingat.y+=cameraspeed;
					if(lookingat.y+frame.getHeight()>4800){
						lookingat.y=4800;
					}
				}
				if(movecameraleft) {
					lookingat.x-=cameraspeed;
					if(lookingat.x<0){
						lookingat.x=0;
					}
				}
				if(movecameraright) {
					lookingat.x+=cameraspeed;
					if(lookingat.x+frame.getWidth()>4800){
						lookingat.x=4800;
					}
				}
				if(frame!=null)  {
					frame.repaint();
				}
			}
		});
		timer.start();
	}
	public void connect(String ip) {

		InetAddress hostIP = null;
		try {
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
			send(me);
			frame1.setVisible(false);
			frame = new Frame();
			lookingat.x = frame.getWidth()/2;
			lookingat.y = frame.getHeight()/2;
			thread.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		read();
	}
	public static void main(String[] args) {
		Client c = new Client();
//		c.start();
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
//				System.out.println("Read Object:"+o);
				//this.send("Read Object:"+o);
				if(o instanceof World) {
					world = (World)o;
//					System.out.println("Read World!");
					frame.panel.repaint();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	public class PlayerSelectionFrame extends JFrame {
		JPanel panel;
		JSlider red, green, blue;
		JTextField ipaddress;
		FlowLayout fl;
		JButton connect;
		Timer colortimer;
		public PlayerSelectionFrame() {
			red = new JSlider(JSlider.HORIZONTAL, 0, 255, (int)(Math.random()*255));
			red.setMinorTickSpacing(10);
			green = new JSlider(JSlider.HORIZONTAL, 0, 255, (int)(Math.random()*255));
			green.setMinorTickSpacing(10);
			blue = new JSlider(JSlider.HORIZONTAL, 0, 255, (int)(Math.random()*255));
			blue.setMinorTickSpacing(10);
			fl = new FlowLayout();
			panel = new JPanel() {
				@Override
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(new Color(red.getValue(), green.getValue(), blue.getValue()));
					g.fillRect(1, 1, 20, 20);
				}
			};
			panel.setLayout(fl);
			panel.add(red);
			panel.add(green);
			panel.add(blue);
			ipaddress = new JTextField("localhost");
			ipaddress.setMinimumSize(new Dimension(200, 20));
			ipaddress.setPreferredSize(new Dimension(200, 20));
			panel.add(ipaddress);
			connect = new JButton("Connect");
			connect.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Color c = new Color(red.getValue(), green.getValue(), blue.getValue());
					me = new Player(c);
					connect(ipaddress.getText());
				}
			});
			panel.add(connect);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			//this.setSize(500, 500);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			this.add(panel);
			this.setVisible(true);
			colortimer = new Timer(300, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					panel.repaint();
				}
			});
			colortimer.start();
		}
		@Override
		public void setVisible(boolean vis) {
			super.setVisible(vis);
			if(!vis) {
				colortimer.stop();
			}
		}
	}
	public class Frame extends JFrame {
		JPanel panel;
		public Frame() {
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			//this.setSize(500, 500);
			this.setUndecorated(true);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			this.setVisible(true);
			
			panel = new JPanel() {
				public void paintComponent(Graphics g) {
					if(world!=null) {
						world.drawEverything(g, panel, lookingat);
					}
					g.setColor(Color.white);
					g.drawString("lookingat:"+lookingat, 50, 50);
				}
			};
			this.add(panel);
			this.addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
				}
				@Override
				public void mouseEntered(MouseEvent arg0) {
				}
				@Override
				public void mouseExited(MouseEvent arg0) {
				}
				@Override
				public void mousePressed(MouseEvent e) {
					mousepress = e.getPoint();
				}
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
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
//					System.out.println(lookingat);
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
	public void start() {
		thread.start();
	}
}
