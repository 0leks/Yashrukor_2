package network;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Target;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Timer;

import main.Building;
import main.Player;
import main.Resource;
import main.Thing;
import main.Unit;
import main.Util;
import main.World;

public class Client implements Runnable {
	private ArrayList<Thing> selected;
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
	private boolean smovecameraright;
	private boolean smovecameraleft;
	private boolean smovecameraup;
	private boolean smovecameradown;
	private int cameraspeed = 20;
	private Timer timer;
	private Point mousepress;
	private Point currentmouse;
	private int bselected = 0;
	private String errormessage;
	private int errortic;
	private String winner;
	public Client() {
		thread = new Thread(this);
		frame1 = new PlayerSelectionFrame();
		lookingat = new Point(0, 0);
		timer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//mouseOnSide(currentmouse);
				if (movecameraup) {
					lookingat.y -= cameraspeed;
					if (lookingat.y < 0) {
						lookingat.y = 0;
					}
				}
				if (movecameradown) {
					lookingat.y += cameraspeed;
					if (lookingat.y + frame.getHeight() > 4800) {
						lookingat.y = 4800;
					}
				}
				if (movecameraleft) {
					lookingat.x -= cameraspeed;
					if (lookingat.x < 0) {
						lookingat.x = 0;
					}
				}
				if (movecameraright) {
					lookingat.x += cameraspeed;
					if (lookingat.x + frame.getWidth() > 4800) {
						lookingat.x = 4800;
					}
				}
				if (smovecameraup) {
					lookingat.y -= 4*cameraspeed;
					if (lookingat.y < 0-frame.getHeight()/2) {
						lookingat.y = 0-frame.getHeight()/2;
					}
				}
				if (smovecameradown) {
					lookingat.y += 4*cameraspeed;
					if (lookingat.y - frame.getHeight()/2 > 4800) {
						lookingat.y = 4800+frame.getHeight()/2;
					}
				}
				if (smovecameraleft) {
					lookingat.x -=4*cameraspeed;
					if (lookingat.x < 0-frame.getWidth()/2) {
						lookingat.x = 0-frame.getWidth()/2;
					}
				}
				if (smovecameraright) {
					lookingat.x += 4*cameraspeed;
					if (lookingat.x - frame.getWidth()/2 > 4800) {
						lookingat.x = 4800+frame.getWidth()/2;
					}
				}
				if (errortic-- < 0) {
					errormessage = null;
				}
			}
		});
		timer.start();
	}
	public void mouseOnSide(Point mouse)
	{
		if(mouse.x >= frame.getWidth()-5)
			smovecameraright = true;
		else
			smovecameraright = false;
		if(mouse.x <= 5)
			smovecameraleft = true;
		else
			smovecameraleft = false;
		if(mouse.y <= 5)
			smovecameraup = true;
		else
			smovecameraup = false;
		if(mouse.y >= frame.getHeight()-5)
			smovecameradown = true;
		else
			smovecameradown = false;
		
		
	}
	public void connect(String ip) {
		JLabel cinfo = new JLabel();;
		InetAddress hostIP = null;
		try {
			if (ip == null) {
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
			lookingat.x = 2400-frame.getWidth() / 2;
			lookingat.y = 2400-frame.getHeight() / 2;
			thread.start();
		}catch(SocketException e){
			e.printStackTrace();
			main(null);
		}catch (UnknownHostException e) {
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
		// c.start();
	}
	
	public void send(Object o) {
		try {
			out.writeUnshared(o);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void read() {
		while (true) {
			try {
				Object o = in.readUnshared();
				if (o instanceof World) {
					world = null;
					world = (World) o;
					world.initializeAllThings();
				}
				if(o instanceof String) {
					winner = (String)o;
					
				}
				if (o instanceof Thing) {
//					if (world.getAllThings() == null) {
//						world.initializeAllThings();
//					}
					world.getAllThings().add((Thing) o);
					if (frame != null) {
						frame.repaint();
					}
				}
				if(o instanceof Resource) {
					Resource p = (Resource)o;
					me.setResource(p);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					in.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		}
	}

	public class PlayerSelectionFrame extends JFrame {
		JPanel panel;
		JSlider red, green, blue;
		JTextField ipaddress;
		JLabel connectioninfo;
		GroupLayout fl;
		JButton connect;
		Timer colortimer;

		public PlayerSelectionFrame() {
			super("Yashrukor II");
			red = new JSlider(JSlider.HORIZONTAL, 0, 255,
					(int) (Math.random() * 255));
			red.setMinorTickSpacing(10);
			green = new JSlider(JSlider.HORIZONTAL, 0, 255,
					(int) (Math.random() * 255));
			green.setMinorTickSpacing(10);
			blue = new JSlider(JSlider.HORIZONTAL, 0, 255,
					(int) (Math.random() * 255));
			blue.setMinorTickSpacing(10);
			ipaddress = new JTextField("localhost");
			ipaddress.setMinimumSize(new Dimension(200, 20));
			ipaddress.setPreferredSize(new Dimension(200, 20));
			panel = new JPanel() { 
				@Override
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(new Color(red.getValue(), green.getValue(), blue.getValue()));
					g.fillRect(225, 10, 80, 80);
				}
			};
			panel.setLayout(null);
			red.setLocation(10, 10);
			red.setSize(200, 20);
			panel.add(red);
			green.setLocation(10, 40);
			green.setSize(200, 20);
			panel.add(green);
			blue.setLocation(10, 70);
			blue.setSize(200, 20);
			panel.add(blue);
			ipaddress.setLocation(10, 100);
			ipaddress.setSize(200, 20);
			panel.add(ipaddress);
			connect = new JButton("Connect");
			connect.setLocation(10, 130);
			connect.setSize(200, 20);
			connect.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Color c = new Color(red.getValue(), green.getValue(), blue
							.getValue());
					me = new Player(c);
					connect(ipaddress.getText());
				}
			});
			panel.add(connect);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize(325, 200);
//			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			this.add(panel);
			this.setVisible(true);
			this.setLocationRelativeTo(null);
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
			if (!vis) {
				colortimer.stop();
			}
		}
	}

	public class Frame extends JFrame {
		JPanel panel;

		public Frame() {
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			// this.setSize(500, 500);
			this.setUndecorated(true);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			this.setVisible(true);

			panel = new JPanel() {
				public void paintComponent(Graphics g) {
					if (world != null) {
						
						world.drawEverything(g, panel, lookingat, me, true, bselected, selected);
					} else {
					}
					g.setColor(Color.white);
					g.drawString("lookingat:" + lookingat, 50, 50);
					if (errormessage != null) {
						g.setColor(Color.blue);
						g.setFont(new Font("Nyala", Font.PLAIN, 50));
						g.drawString(errormessage, 100, getHeight() / 2);
					}
					if(mousepress!=null) {
						g.setColor(Color.green);
						Rectangle sele = Util.normalizeRectangle(mousepress.x, mousepress.y, currentmouse.x, currentmouse.y);
						g.drawRect(sele.x, sele.y, sele.width, sele.height);
					}
					if(winner!=null) {
						g.setColor(Color.red);
						g.setFont(new Font("Nyala", Font.PLAIN, 50));
						g.drawString(winner, 50, getHeight()/2);
					}
				}
			};
			this.add(panel);
			this.addMouseMotionListener(new MouseMotionListener() {
				@Override
				public void mouseDragged(MouseEvent e) {
					mouseMoved(e);
				}

				@Override
				public void mouseMoved(MouseEvent e) {
					currentmouse = e.getPoint();
					mouseOnSide(currentmouse);
					
				}
			});
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
					if (e.getButton() == MouseEvent.BUTTON1) {
						if ((e.getX() > panel.getWidth() - 244)
								&& (e.getX() < panel.getWidth())
								&& (e.getY() > panel.getHeight() - 244)
								&& (e.getY() < panel.getHeight())) {
							mousepress = e.getPoint();
							lookingat.x = 20
									* (mousepress.x - (panel.getWidth() - 244))
									- panel.getWidth() / 2;
							lookingat.y = 20
									* (mousepress.y - (panel.getHeight() - 244))
									- panel.getHeight() / 2;
							if (lookingat.y < 0-frame.getHeight()/2) {
								lookingat.y = 0-frame.getHeight()/2;
							} else if (lookingat.y + frame.getHeight()/2 > 4800+frame.getHeight()/2) {
								lookingat.y = 4800 + frame.getHeight()/2;
							}
							if (lookingat.x < 0-frame.getWidth()/2) {
								lookingat.x = 0-frame.getWidth()/2;
							} else if (lookingat.x + frame.getWidth()/2 > 4800+frame.getHeight()/2) {
								lookingat.x = 4800+ frame.getWidth()/2;
							}
							mousepress = null;
						} else {
							mousepress = e.getPoint();
						}
					}
					if (e.getButton() == MouseEvent.BUTTON3) {
						System.out.println("Pressed Button 3");
						if (selected != null) {
							System.out.println("Selected not null size:"+ selected.size());
							for (int a = 0; a < selected.size(); a++) {
								if (selected.get(a) instanceof Unit) {
									Unit un = (Unit)(selected.get(a));
									if(un.unitType()==Unit.MEDIC) {
										Point onmap = new Point(e.getX()+lookingat.x, e.getY()+lookingat.y);
										Thing t = world.thingInPoint(onmap);
										if(t!=null) {
											HealCommand hc = new HealCommand();
											hc.id = un.id;
											hc.target = t;
											send(hc);
										} else {
											MoveCommand temp = new MoveCommand();
											temp.id = selected.get(a).id;
											int targetx = e.getX() + lookingat.x;
											int targety = e.getY() + lookingat.y;
											if ((e.getX() > panel.getWidth() - 244)
													&& (e.getX() < panel.getWidth())
													&& (e.getY() > panel.getHeight() - 244)
													&& (e.getY() < panel.getHeight())) {
												targetx = 20 * (e.getX() - (panel
														.getWidth() - 244));
												targety = 20 * (e.getY() - (panel
														.getHeight() - 244));
											}
											temp.target = new Point(targetx, targety);
											send(temp);
											System.out.println("Sending Move Command");
										}
									} else {
										MoveCommand temp = new MoveCommand();
										temp.id = selected.get(a).id;
										int targetx = e.getX() + lookingat.x;
										int targety = e.getY() + lookingat.y;
										if ((e.getX() > panel.getWidth() - 244)
												&& (e.getX() < panel.getWidth())
												&& (e.getY() > panel.getHeight() - 244)
												&& (e.getY() < panel.getHeight())) {
											targetx = 20 * (e.getX() - (panel
													.getWidth() - 244));
											targety = 20 * (e.getY() - (panel
													.getHeight() - 244));
										}
										temp.target = new Point(targetx, targety);
										send(temp);
										System.out.println("Sending Move Command");
									}
								}
							}
						}
					}
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON1) {
						if(mousepress!=null) {
							if ((mousepress.x > panel.getWidth() - 244)
									&& (mousepress.x < panel.getWidth())
									&& (mousepress.y > panel.getHeight() - 244)
									&& (mousepress.y < panel.getHeight())) {
	
							} else {
								
								Rectangle selection = Util.normalizeRectangle(mousepress.x+lookingat.x, mousepress.y+lookingat.y, e.getX()+lookingat.x,e.getY()+lookingat.y);
								ArrayList<Thing> possibleselect = new ArrayList<Thing>();
								if(world!=null)
								for (int a = 0; a < world.getAllThings().size(); a++) {
									Thing t = world.getAllThings().get(a);
	//								if (t.getLocation().x > selection.x
	//										&& t.getLocation().x < selection.x
	//												+ selection.width) {
	//									if (t.getLocation().y > selection.y
	//											&& t.getLocation().y < selection.y
	//													+ selection.height) {
	//										possibleselect.add(world.getAllThings()
	//												.get(a));
	//										selected = possibleselect;
	//									}
	//								}
									if (selection.intersects(t.getBounds())) {
										possibleselect.add(t);
									}
								}
								mousepress = null;
								selected = possibleselect;
								updateUI(selected);
								System.out.println("selected "+selected.size());
						}
						}
					}
				}
			});
			this.addKeyListener(new KeyListener() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_UP) {
						movecameraup = true;
					}
					if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						movecameradown = true;
					}
					if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						movecameraleft = true;
					}
					if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						movecameraright = true;
					}
				}

				@Override
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_UP) {
						movecameraup = false;
					}
					if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						movecameradown = false;
					}
					if (e.getKeyCode() == KeyEvent.VK_LEFT) {
						movecameraleft = false;
					}
					if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
						movecameraright = false;
					}
					if(e.getKeyCode() == KeyEvent.VK_A){
						Point p;
						if ((currentmouse.x > panel.getWidth() - 244)
								&& (currentmouse.x < panel.getWidth())
								&& (currentmouse.y > panel.getHeight() - 244)
								&& (currentmouse.y < panel.getHeight())) {
							p = new Point((20 * (currentmouse.x - (panel
									.getWidth() - 244))),(20 * (currentmouse.y - (panel
									.getHeight() - 244))));
						}
						else{
							p = new Point(currentmouse.x + lookingat.x,currentmouse.y + lookingat.y);
						}
						for(int a=0; a<selected.size();a++) {
							if(selected.get(a) instanceof Unit) {
								Unit u = (Unit)selected.get(a);
								AttackMoveCommand ac = new AttackMoveCommand();
								ac.id = u.id;
								ac.target = p;
								send(ac);
							}
							
						}
						
					}
					if(bselected==0){
						if (e.getKeyCode() >= 49 && e.getKeyCode() <= 56) {
							int buildingtype = e.getKeyCode() - 48;
							BuildCommand bc = new BuildCommand();
							bc.type = buildingtype;
							bc.location = new Point(currentmouse.x + lookingat.x,currentmouse.y + lookingat.y);
							boolean cansend = false;
							if (world.spotCloseEnough(bc.location, me)) {
								if (me.resource().check(Building.getResource(bc.type))) {
									me.resource().add(Building.getResource(bc.type));
									send(bc);
								} else {
									errormessage = "Not enough Resources";
									errortic = 20;
								}
							} 
							else {
								errormessage = "Too far from your buildings";
								errortic = 20;
							}
						}
					} else {
						if(e.getKeyCode()==49) {
							for(int a=0; a<selected.size(); a++) {
								if(selected.get(a) instanceof Building) {
									Building b = (Building)(selected.get(a));
									CreateCommand cc = new CreateCommand();
									cc.id = b.id;
									if(b.getType()==Building.BARRACKS) {
										cc.type = Unit.WARRIOR;
									}
									if(b.getType()==Building.RANGE) {
										cc.type = Unit.ARCHER;
									}
									if(b.getType()==Building.HOSPITAL) {
										cc.type = Unit.MEDIC;
									}
									if(cc.type!=0) {
										send(cc);
									}
								}
							}
						}
						if(e.getKeyCode()==50) {
							for(int a=0; a<selected.size(); a++) {
								if(selected.get(a) instanceof Building) {
									Building b = (Building)(selected.get(a));
									CreateCommand cc = new CreateCommand();
									cc.id = b.id;
									if(b.getType()==Building.BARRACKS) {
										cc.type = Unit.KNIGHT;
									}
									if(b.getType()==Building.RANGE) {
										cc.type = Unit.CROSSBOW;
									}
									if(b.getType()==Building.HOSPITAL) {
										cc.type = Unit.SHAMAN;
									}
									if(cc.type!=0) {
										send(cc);
									}
								}
							}
						}
					}
//					else if(bselected==1){
//						if(e.getKeyCode()==49){
//							for(Thing t:selected){
//								CreateCommand cc = new CreateCommand();
//								
//								((Building)t).createUnit(Unit.WARRIOR);
//							}
//						}
//						if(e.getKeyCode()==50){
//							for(Thing t:selected){
//								((Building)t).createUnit(Unit.KNIGHT);
//							}
//						}
//					}
//					else if(bselected==2){
//						if(e.getKeyCode()==49){
//							for(Thing t:selected){
//								((Building)t).createUnit(Unit.ARCHER);
//							}
//						}
//						if(e.getKeyCode()==50){
//							for(Thing t:selected){
//								((Building)t).createUnit(Unit.CROSSBOW);
//							}
//						}
//					}
//					else if(bselected==3){
//						if(e.getKeyCode()==49){
//							for(Thing t:selected){
//								((Building)t).createUnit(Unit.MEDIC);
//							}
//						}
//						if(e.getKeyCode()==50){
//							for(Thing t:selected){
//								((Building)t).createUnit(Unit.SHAMAN);
//							}
//						}
//					}
				}

				@Override
				public void keyTyped(KeyEvent e) {
				}
			});
		}
	}
	public int updateUI(ArrayList<Thing> selected) {
		boolean all = true;
		ArrayList<Building> bs = new ArrayList<Building>();
		for (Thing t : selected) {
			if (t instanceof Building) {
				bs.add((Building) t);
			}
		}
		int type = 4;
		if (bs.size() > 0) {
			type = bs.get(0).getType();
			for (int a = 1; a < bs.size(); a++) {
				if (type != bs.get(a).getType() || type < 5 || type > 7) {
					all = false;
				}
			}
		}
		if (!all)
			type = 4;
		bselected = type - 4;
		return bselected;
	}

	public void start() {
		thread.start();
	}
}
