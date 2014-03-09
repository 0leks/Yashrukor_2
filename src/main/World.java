package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.Serializable;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import network.Server;

public class World implements Serializable {
	
	int worldx=4800;
	int worldy=4800;
	int screenw=(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	int screenh=(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	transient ArrayList<Thing> allThings;
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
				allThings.add(new Unit(1, 50, 50, server.connections.get(0).getPlayer()));
				allThings.add(new Building(100, 50, 1,server.connections.get(0).getPlayer()));
			}
			if(server.connections.size()>1){
				allThings.add(new Unit(1, 4650, 4700, server.connections.get(1).getPlayer()));
				allThings.add(new Building(4700, 4700, 1,server.connections.get(1).getPlayer()));
			}
			if(server.connections.size()>2){
				allThings.add(new Unit(1, 0, 4700, server.connections.get(2).getPlayer()));
				allThings.add(new Building(50, 4700, 1,server.connections.get(2).getPlayer()));
			}
			if(server.connections.size()>3){
				allThings.add(new Unit(1, 4700, 0, server.connections.get(3).getPlayer()));
				allThings.add(new Building(4700, 50, 1,server.connections.get(3).getPlayer()));
			}
		}
		//TempFrameForTestingOnly asdf = new TempFrameForTestingOnly();
		worldtimer = new Timer(50, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(allThings.size());
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
	}
	public Thing getThing(int id) {
		for(int a=0; a<allThings.size(); a++) {
			if(allThings.get(a).id==id) {
				return allThings.get(a);
			}
		}
		return null;
	}
	public void drawEverything(Graphics g, JPanel drawingon, Point lookingat, Player player, boolean fowon) {
		g.setColor(new Color(0,128,0));
		g.fillRect(drawingon.getX(), drawingon.getY(), drawingon.getWidth(), drawingon.getHeight());
		for(int a=0; a<allThings.size(); a++) {
			Thing thing = allThings.get(a);
			if(thing instanceof Building){
				Building building = (Building)thing;
				g.setColor(building.myPlayer.getColor());
				g.fillRect(building.x-lookingat.x, building.y-lookingat.y, 50, 50);
				g.setColor(Color.white);
				g.drawString("Building #"+a, thing.x-lookingat.x, thing.y-lookingat.y+20);
			} else if(thing instanceof Unit) {
				Unit unit = (Unit)thing;
				g.setColor(unit.myPlayer.getColor());
				g.fillRect(unit.x-lookingat.x, unit.y-lookingat.y, 50, 50);
				g.setColor(Color.white);
				g.drawString("Unit #"+a, thing.x-lookingat.x, thing.y-lookingat.y+20);
			} 
			else if(thing instanceof Terrain) {
				Terrain t = (Terrain)thing;
				System.out.println("ter");
				g.setColor(new Color (1,50,32));
				g.fillRect(t.x-lookingat.x, t.y-lookingat.y,t.width(), t.height());
				System.out.println((t.x-lookingat.x)+" "+(t.y-lookingat.y)+" "+t.width()+" "+t.height());
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
								if(d<1000){
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
								if(d<1000){
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
			//UI
			g.setColor(new Color(52,52,52));
			g.fillRect(0, 0, drawingon.getWidth(), 20);
			g.fillRect(0, drawingon.getHeight()-120, drawingon.getWidth(), 120);
			g.setColor(Color.white);
			g.drawString("Gold: "+player.resource().gold()+"  Wood: "+player.resource().wood()+"  Stone: "+player.resource().stone()+"  Food: "+player.resource().food(),10,12);
			int w=(drawingon.getWidth()-360)/7;
			g.setColor(Color.white);
			g.fillRect(10, drawingon.getHeight()-110, w, 100);
			g.fillRect(10+1*(w+10), drawingon.getHeight()-110, w, 100);
			g.fillRect(10+2*(w+10), drawingon.getHeight()-110, w, 100);
			g.fillRect(10+3*(w+10), drawingon.getHeight()-110, w, 100);
			g.fillRect(10+4*(w+10), drawingon.getHeight()-110, w, 100);
			g.fillRect(10+5*(w+10), drawingon.getHeight()-110, w, 100);
			g.fillRect(10+6*(w+10), drawingon.getHeight()-110, w, 100);
			drawMinimap(g, drawingon,lookingat,foglist,fowon);
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
					drawEverything(g, panel, new Point(0, 0),null,true);
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
}
