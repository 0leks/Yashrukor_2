package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import network.Server;

public class World implements Serializable {
	public static final int FOGOFWAR = 1000;
	int worldx=4800;
	int worldy=4800;
	int screenw=(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	int screenh=(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	transient ArrayList<Thing> allThings;
	transient BufferedImage ii;
	transient BufferedImage arch;
	transient BufferedImage med;
	TempFrameForTestingOnly asdf;
	transient Server server;
	Timer worldtimer;
	public World(Server serv) {
		allThings = new ArrayList<Thing>();
		Thing.myWorld = this;
		initializeTerrain();
		if(serv!=null) {
			server = serv;
			if(server.connections.size()>0){
				allThings.add(new Building(150, 150, Building.BASE,server.connections.get(0).getPlayer()));
				allThings.add(new Unit(Unit.WARRIOR,100,400,server.connections.get(0).getPlayer()));
			}
			if(server.connections.size()>1){
				allThings.add(new Building(4700, 4700, Building.BASE,server.connections.get(1).getPlayer()));
				allThings.add(new Unit(Unit.WARRIOR,4500,4500,server.connections.get(1).getPlayer()));
			}
			if(server.connections.size()>2){
				allThings.add(new Building(50, 4700, Building.BASE,server.connections.get(2).getPlayer()));
				allThings.add(new Unit(Unit.WARRIOR,100,4500,server.connections.get(2).getPlayer()));
			}
			if(server.connections.size()>3){
				allThings.add(new Building(4700, 50, Building.BASE,server.connections.get(3).getPlayer()));
				allThings.add(new Unit(Unit.WARRIOR,4500,100,server.connections.get(3).getPlayer()));
			}
		}
		//TempFrameForTestingOnly asdf = new TempFrameForTestingOnly();
		worldtimer = new Timer(50, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				System.out.println(allThings.size());
				for(int a=0; a<allThings.size(); a++) {
					allThings.get(a).tic();
				}
//				System.out.println(allThings.size());
			}
		});
		worldtimer.start();
	}
	public void addBuilding(Building b) {
		allThings.add(b);
	}
	/**
	 * ONLY FOR USE BY CLIENT
	 */
	public void initializeAllThings() {
		allThings = new ArrayList<Thing>();
		try {
			ii = ImageIO.read(new File("Warrior.gif"));
			arch = ImageIO.read(new File("Archer.gif"));
			med=ImageIO.read(new File("Medic.gif"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Thing getThing(int id) {
		for(int a=0; a<allThings.size(); a++) {
			if(allThings.get(a).id==id) {
				return allThings.get(a);
			}
		}
		return null;
	}

	public boolean spotCloseEnough(Point p, Player player) {
		for(int a=0; a<allThings.size(); a++) {
			Thing t = allThings.get(a);
			if(t instanceof Building) {
				Building b = (Building)t;
				if(b.getPlayer().equals(player)) {
					int distance = b.euclidianDistanceFrom(p);
					if(distance<FOGOFWAR) {
						return true;
					}
				}
			}
		}
		return false;
	}
//	bselected: 0 = none (creation UI)
//	1=barracks
//	2=range
//	3=hospital
	public void drawEverything(Graphics g, JPanel drawingon, Point lookingat, Player player, boolean fowon, int bselected) {
		g.setColor(new Color(0,128,0));
		g.fillRect(drawingon.getX(), drawingon.getY(), drawingon.getWidth(), drawingon.getHeight());
		if(allThings!=null) {
			for(int a=0; a<allThings.size(); a++) {
				Thing thing = allThings.get(a);
				if(thing instanceof Building){
					Building building = (Building)thing;
					g.setColor(building.myPlayer.getColor());
					g.fillRect(building.x-lookingat.x, building.y-lookingat.y, building.width, building.height);
					
					g.setColor(Color.green);
					g.fillRect(building.x-lookingat.x, building.y-lookingat.y-10, (building.width)*(building.hp/building.MAXHEALTH), 10);
					g.setColor(Color.black);
					g.drawRect(building.x-lookingat.x, building.y-lookingat.y-10, building.width, 10);
					g.setColor(Color.white);
					
					g.drawString("Building #"+a, thing.x-lookingat.x, thing.y-lookingat.y+20);
					if(building.creatingunits) {
						g.drawString(building.unittic+"/"+building.unitreq, thing.x-lookingat.x, thing.y-lookingat.y+40);
					}
				} else if(thing instanceof Unit) {
					Unit unit = (Unit)thing;
					
						g.setColor(unit.myPlayer.getColor());
						if(unit.unitType()==2||unit.unitType()==4){
							//g.fillRect(unit.x-lookingat.x, unit.y-lookingat.y, unit.width, unit.height);
							
							g.drawImage(ii, unit.x-lookingat.x, unit.y-lookingat.y, unit.width,unit.height,null);
							
							g.setColor(unit.myPlayer.getColor());
							g.fillRect(unit.x-lookingat.x, unit.y-lookingat.y-10, (unit.width)*(unit.hp/unit.MAXHEALTH), 10);
							g.setColor(Color.black);
							g.drawRect(unit.x-lookingat.x, unit.y-lookingat.y-10, unit.width, 10);
							
							g.setColor(Color.white);
							g.drawString("Unit #"+a, thing.x-lookingat.x, thing.y-lookingat.y+20);
						}
						else if(unit.unitType()==3||unit.unitType()==5){
							//g.fillOval(unit.x-lookingat.x, unit.y-lookingat.y, unit.width, unit.height);
							g.drawImage(arch, unit.x-lookingat.x, unit.y-lookingat.y, (unit.width)*(unit.hp/unit.MAXHEALTH),unit.height,null);
							g.setColor(unit.myPlayer.getColor());
							g.fillRect(unit.x-lookingat.x, unit.y-lookingat.y-10, unit.width, 10);
							g.setColor(Color.black);
							g.drawRect(unit.x-lookingat.x, unit.y-lookingat.y-10, unit.width, 10);
							
							g.setColor(Color.white);
							g.drawString("Unit #"+a, thing.x-lookingat.x, thing.y-lookingat.y+20);
						}
						else if(unit.unitType()==6||unit.unitType()==7){
							//g.drawOval(unit.x-lookingat.x, unit.y-lookingat.y, unit.width, unit.height);
							g.drawImage(med, unit.x-lookingat.x, unit.y-lookingat.y, (unit.width)*(unit.hp/unit.MAXHEALTH),unit.height,null);
							g.setColor(unit.myPlayer.getColor());
							g.fillRect(unit.x-lookingat.x, unit.y-lookingat.y-10, unit.width, 10);
							g.setColor(Color.black);
							g.drawRect(unit.x-lookingat.x, unit.y-lookingat.y-10, unit.width, 10);
							g.setColor(Color.white);
							g.drawString("Unit #"+a, thing.x-lookingat.x, thing.y-lookingat.y+20);
						}
				} 
				else if(thing instanceof Terrain) {
					Terrain t = (Terrain)thing;
					g.setColor(new Color (1,50,32));
					g.fillRect(t.x-lookingat.x, t.y-lookingat.y,t.width(), t.height());
				} 
				else {
					//this is temporary drawing code for representing Things on the screen
					g.setColor(Color.black);
					g.fillRect(thing.x-lookingat.x, thing.y-lookingat.y, 50, 50);
					g.setColor(Color.white);
					g.drawString("Thing #"+a, thing.x-lookingat.x, thing.y-lookingat.y+20);
				}
			}
			
			//fog of war
			if(player!=null){
				ArrayList<Fog> foglist=new ArrayList<Fog>();
				if(fowon==true){
					for(int i=0;i<4800;i+=100){
						for(int j=0;j<4800;j+=100){
							foglist.add(new Fog(i,j));
						}
					}
					for(int a=0; a<allThings.size(); a++){
						Thing t = allThings.get(a);
						if(t instanceof Unit){
							Unit u=(Unit)t;
							if(u.myPlayer.equals(player)){
								for(int i=0;i<foglist.size();i+=0){
									Fog f=foglist.get(i);
									int d=(int) Math.sqrt(Math.pow(u.x-f.x,2)+Math.pow(u.y-f.y,2));
									if(d<FOGOFWAR){
										foglist.remove(f);
									}
									else{
										i++;
									}
								}
							}
						}
						if(t instanceof Building){
							Building u=(Building)t;
							if(u.myPlayer.equals(player)){
								for(int i=0;i<foglist.size();i+=0){
									Fog f=foglist.get(i);
									int d=(int) Math.sqrt(Math.pow(u.x-f.x,2)+Math.pow(u.y-f.y,2));
									if(d<FOGOFWAR){
										foglist.remove(f);
									}
									else{
										i++;
									}
								}
							}
						}
					}
					g.setColor(new Color(192,192,192));
					for(Fog f:foglist){
						g.fillRect(f.x-lookingat.x, f.y-lookingat.y, 100, 100);
					}
					
				}	
				g.setColor(Color.black);
				g.drawRect(0-lookingat.x, 0-lookingat.y, 4800, 4800);
				//UI
				g.setColor(new Color(52,52,52));
				g.fillRect(0, 0, drawingon.getWidth(), 20);
				g.fillRect(0, drawingon.getHeight()-120, drawingon.getWidth(), 120);
				g.setColor(Color.white);
				g.drawString("Gold: "+player.resource().gold()+"  Wood: "+player.resource().wood()+"  Stone: "+player.resource().stone()+"  Food: "+player.resource().food(),10,12);
				int w=(drawingon.getWidth()-360)/7;
				g.setColor(Color.white);
				if(bselected==0){
					g.fillRect(10, drawingon.getHeight()-110, w, 100);
					g.fillRect(10+1*(w+10), drawingon.getHeight()-110, w, 100);
					g.fillRect(10+2*(w+10), drawingon.getHeight()-110, w, 100);
					g.fillRect(10+3*(w+10), drawingon.getHeight()-110, w, 100);
					g.fillRect(10+4*(w+10), drawingon.getHeight()-110, w, 100);
					g.fillRect(10+5*(w+10), drawingon.getHeight()-110, w, 100);
					g.fillRect(10+6*(w+10), drawingon.getHeight()-110, w, 100);
					
					g.setColor(Color.black);
					g.drawString("1: Farm", 10, drawingon.getHeight()-100);
					g.drawString("Produces food", 10, drawingon.getHeight()-90);
					g.drawString("10 gold", 10, drawingon.getHeight()-60);
					g.drawString("40 wood", 10, drawingon.getHeight()-50);
					g.drawString("10 stone", 10, drawingon.getHeight()-40);
					
					g.drawString("2: Quarry", 10+1*(w+10), drawingon.getHeight()-100);
					g.drawString("Produces stone", 10+1*(w+10), drawingon.getHeight()-90);
					g.drawString("10 gold", 10+1*(w+10), drawingon.getHeight()-60);
					g.drawString("50 wood", 10+1*(w+10), drawingon.getHeight()-50);
					g.drawString("0 stone", 10+1*(w+10), drawingon.getHeight()-40);
					
					g.drawString("3: Lumbermill", 10+2*(w+10), drawingon.getHeight()-100);
					g.drawString("Produces stone", 10+2*(w+10), drawingon.getHeight()-90);
					g.drawString("50 gold", 10+2*(w+10), drawingon.getHeight()-60);
					g.drawString("0 wood", 10+2*(w+10), drawingon.getHeight()-50);
					g.drawString("0 stone", 10+2*(w+10), drawingon.getHeight()-40);
					
					g.drawString("4: Tower", 10+3*(w+10), drawingon.getHeight()-100);
					g.drawString("Attacks nearby enemies", 10+3*(w+10), drawingon.getHeight()-90);
					g.drawString("100 gold", 10+3*(w+10), drawingon.getHeight()-60);
					g.drawString("40 wood", 10+3*(w+10), drawingon.getHeight()-50);
					g.drawString("80 stone", 10+3*(w+10), drawingon.getHeight()-40);
					
					g.drawString("5: Barracks", 10+4*(w+10), drawingon.getHeight()-100);
					g.drawString("Produces warriors", 10+4*(w+10), drawingon.getHeight()-90);
					g.drawString("and knights", 10+4*(w+10), drawingon.getHeight()-80);
					g.drawString("30 gold", 10+4*(w+10), drawingon.getHeight()-60);
					g.drawString("30 wood", 10+4*(w+10), drawingon.getHeight()-50);
					g.drawString("50 stone", 10+4*(w+10), drawingon.getHeight()-40);
					
					g.drawString("6: Range", 10+5*(w+10), drawingon.getHeight()-100);
					g.drawString("Produces archers", 10+5*(w+10), drawingon.getHeight()-90);
					g.drawString("and crossbowmen", 10+5*(w+10), drawingon.getHeight()-80);
					g.drawString("30 gold", 10+5*(w+10), drawingon.getHeight()-60);
					g.drawString("50 wood", 10+5*(w+10), drawingon.getHeight()-50);
					g.drawString("30 stone", 10+5*(w+10), drawingon.getHeight()-40);
					
					g.drawString("7: Hospital", 10+6*(w+10), drawingon.getHeight()-100);
					g.drawString("Produces medics", 10+6*(w+10), drawingon.getHeight()-90);
					g.drawString("and shamans", 10+6*(w+10), drawingon.getHeight()-80);
					g.drawString("40 gold", 10+6*(w+10), drawingon.getHeight()-60);
					g.drawString("50 wood", 10+6*(w+10), drawingon.getHeight()-50);
					g.drawString("50 stone", 10+6*(w+10), drawingon.getHeight()-40);
				}
				else if(bselected==1){
					g.fillRect(10, drawingon.getHeight()-110, w, 100);
					g.fillRect(10+1*(w+10), drawingon.getHeight()-110, w, 100);
					g.setColor(Color.black);
					g.drawString("1: Warrior", 10, drawingon.getHeight()-100);
					g.drawString("100 health", 10, drawingon.getHeight()-90);
					g.drawString("20 damage", 10, drawingon.getHeight()-80);
					g.drawString("20 gold", 10, drawingon.getHeight()-60);
					g.drawString("20 wood", 10, drawingon.getHeight()-50);
					g.drawString("20 stone", 10, drawingon.getHeight()-40);
					g.drawString("20 food", 10, drawingon.getHeight()-30);
					
					g.drawString("2: Knight", 10+1*(w+10), drawingon.getHeight()-100);
					g.drawString("200 health", 10+1*(w+10), drawingon.getHeight()-90);
					g.drawString("40 damage", 10+1*(w+10), drawingon.getHeight()-80);
					g.drawString("40 gold", 10+1*(w+10), drawingon.getHeight()-60);
					g.drawString("30 wood", 10+1*(w+10), drawingon.getHeight()-50);
					g.drawString("30 stone", 10+1*(w+10), drawingon.getHeight()-40);
					g.drawString("60 food", 10+1*(w+10), drawingon.getHeight()-30);
				}
				else if(bselected==2){
					g.fillRect(10, drawingon.getHeight()-110, w, 100);
					g.fillRect(10+1*(w+10), drawingon.getHeight()-110, w, 100);
					g.setColor(Color.black);
					g.drawString("1: Archer", 10, drawingon.getHeight()-100);
					g.drawString("60 health", 10, drawingon.getHeight()-90);
					g.drawString("10 damage", 10, drawingon.getHeight()-80);
					g.drawString("30 gold", 10, drawingon.getHeight()-60);
					g.drawString("20 wood", 10, drawingon.getHeight()-50);
					g.drawString("10 stone", 10, drawingon.getHeight()-40);
					g.drawString("20 food", 10, drawingon.getHeight()-30);
					
					g.drawString("2: Crossbowman", 10+1*(w+10), drawingon.getHeight()-100);
					g.drawString("100 health", 10+1*(w+10), drawingon.getHeight()-90);
					g.drawString("30 damage", 10+1*(w+10), drawingon.getHeight()-80);
					g.drawString("60 gold", 10+1*(w+10), drawingon.getHeight()-60);
					g.drawString("50 wood", 10+1*(w+10), drawingon.getHeight()-50);
					g.drawString("30 stone", 10+1*(w+10), drawingon.getHeight()-40);
					g.drawString("40 food", 10+1*(w+10), drawingon.getHeight()-30);
				}
				else if(bselected==3){
					g.fillRect(10, drawingon.getHeight()-110, w, 100);
					g.fillRect(10+1*(w+10), drawingon.getHeight()-110, w, 100);
					g.setColor(Color.black);
					g.drawString("1: Medic", 10, drawingon.getHeight()-100);
					g.drawString("80 health", 10, drawingon.getHeight()-90);
					g.drawString("5 heal", 10, drawingon.getHeight()-80);
					g.drawString("30 gold", 10, drawingon.getHeight()-60);
					g.drawString("20 wood", 10, drawingon.getHeight()-50);
					g.drawString("10 stone", 10, drawingon.getHeight()-40);
					g.drawString("20 food", 10, drawingon.getHeight()-30);
					
					g.drawString("2: Shaman", 10+1*(w+10), drawingon.getHeight()-100);
					g.drawString("80 health", 10+1*(w+10), drawingon.getHeight()-90);
					g.drawString("5 heal", 10+1*(w+10), drawingon.getHeight()-80);
					g.drawString("100 gold", 10+1*(w+10), drawingon.getHeight()-60);
					g.drawString("50 wood", 10+1*(w+10), drawingon.getHeight()-50);
					g.drawString("50 stone", 10+1*(w+10), drawingon.getHeight()-40);
					g.drawString("100 food", 10+1*(w+10), drawingon.getHeight()-30);
				}
				drawMinimap(g, drawingon,lookingat,foglist,fowon);
			}
		}
		
		
	}

	public void drawMinimap(Graphics g, JPanel drawingon, Point lookingat, ArrayList<Fog> fog, boolean fowon) {
		g.setColor(new Color(107,68,35));
		g.fillRect(drawingon.getWidth()-248, drawingon.getHeight()-248, 248, 248);
		g.setColor(new Color(0,128,0));
		g.fillRect(drawingon.getWidth()-244, drawingon.getHeight()-244, 240, 240);
		for(int a=0; a<allThings.size(); a++) {
			Thing thing = allThings.get(a);
			if(thing instanceof Building){
				Building building = (Building)thing;
				g.setColor(building.myPlayer.getColor());
				g.fillRect(drawingon.getWidth()-244+building.x/20, drawingon.getHeight()-244+building.y/20, 5,5);
			} 
			else if(thing instanceof Unit) {
				Unit unit = (Unit)thing;
				g.setColor(unit.myPlayer.getColor());
				g.fillRect(drawingon.getWidth()-244+unit.x/20, drawingon.getHeight()-244+unit.y/20, 5, 5);
			} 
			else if(thing instanceof Terrain) {
				Terrain t = (Terrain)thing;
				g.setColor(new Color (1,50,32));
				g.fillRect(drawingon.getWidth()-244+t.x/20, drawingon.getHeight()-244+t.y/20, t.width()/20, t.height()/20);
			} 
			else {
				//this is temporary drawing code for representing Things on the screen
				g.setColor(Color.black);
				g.fillRect(drawingon.getWidth()-244+thing.x/20, drawingon.getHeight()-244+thing.y/20, 5, 5);
				g.setColor(Color.white);
			}
		}
		if(fowon==true){
			g.setColor(new Color(192,192,192));
			for(Fog f:fog){
				g.fillRect(drawingon.getWidth()-244+f.x/20, drawingon.getHeight()-244+f.y/20, 5, 5);
			}
		}
		g.setColor(new Color(107,68,35));
		g.drawRect(drawingon.getWidth()-244+lookingat.x/20, drawingon.getHeight()-244+lookingat.y/20, screenw/20, screenh/20);
	}
	public ArrayList<Thing> thingsInRange(Point p, int range)
	{
		ArrayList<Thing> inRange = new ArrayList<Thing>();
		for(Thing t : allThings)
		{
			if(Math.abs(t.euclidianDistanceFrom(p)) < range)
			{
				inRange.add(t);
			}
		}
		return inRange;
	}
	public Thing thingInPoint(Point p)
	{
		for(Thing t : allThings)
		{
			if((p.x > t.x-t.width/2 && p.x < t.x+t.width) && (p.y > t.y-t.height/2 && p.y < t.y+t.height))
			{
				return t;
			}
		}
		return null;
	}
	public boolean pointOccupied(Point p)
	{
		for(Thing t : allThings)
		{
			if((p.x > t.x-t.width/2 && p.x < t.x+t.width) && (p.y > t.y-t.height/2 && p.y < t.y+t.height))
			{
				return true;
			}
		}
		return false;
	}
	public void initializeTerrain(){
		allThings.add(new Terrain(0,2300,200,1600));
		allThings.add(new Terrain(3200,2300,200,1600));
		allThings.add(new Terrain(2300,0,1600,200));
		allThings.add(new Terrain(2300,3200,1600,200));
		
		allThings.add(new Terrain(1600,1600,200,200));
		allThings.add(new Terrain(3000,3000,200,200));
		allThings.add(new Terrain(1600,3000,200,200));
		allThings.add(new Terrain(3000,1600,200,200));
		
		allThings.add(new Terrain(2350,2350,100,100));
	}
	public ArrayList<Thing> getAllThings() {
		return allThings;
	}
	public class TempFrameForTestingOnly extends JFrame {
		JPanel panel;
		public TempFrameForTestingOnly() {
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			//this.setSize(xsize,ysize);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			this.setVisible(true);
			panel = new JPanel() {
				public void paintComponent(Graphics g) {
					drawEverything(g, panel, new Point(0, 0),null,true,0);
				}
			};
			this.add(panel);
		}
	}
	public void testDraw() {
		asdf = new TempFrameForTestingOnly();
	}
	public static void main(String[] args) {
		World w = new World(null);
		w.testDraw();
	}
	public Thing findcollision(Unit unit, Rectangle rectangle) {
		for(int a=0; a<allThings.size(); a++) {
			if(allThings.get(a).collides(rectangle) && allThings.get(a)!=unit) {
				return allThings.get(a);
			}
		}
		if(rectangle.x<-2400 || rectangle.x+rectangle.width>2400 || rectangle.y<-2400 || rectangle.y+rectangle.height>2400) {
			return new Thing(true);
		}
		return null;
	}
}
