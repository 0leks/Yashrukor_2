package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import network.Server;

public class World implements Serializable {
	ArrayList<Building> allBuildigs = new ArrayList<Building>();
	ArrayList<Unit> allUnits = new ArrayList<Unit>();
	int worldx=4800;
	int worldy=4800;
	int screenw=(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	int screenh=(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	ArrayList<Thing> allThings = new ArrayList<Thing>();
	TempFrameForTestingOnly asdf;
	transient Server server;
	public World(Server serv) {
		Thing.myWorld = this;
		initializeTerrain();
		if(serv!=null) {
			server = serv;
			if(server.connections.size()>0){
				allThings.add(new Unit(1, 0, 50, server.connections.get(0).getPlayer()));
				allThings.add(new Building(50, 50, 1,server.connections.get(0).getPlayer()));
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
	}
	public void drawEverything(Graphics g, JPanel drawingon, Point lookingat) {
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
				g.setColor(new Color (1,50,32));
				g.fillRect(t.x-lookingat.x, t.y-lookingat.y, t.width(), t.height());
			} 
			else {
				//this is temporary drawing code for representing Things on the screen
				g.setColor(Color.black);
				g.fillRect(thing.x-lookingat.x, thing.y-lookingat.y, 50, 50);
				g.setColor(Color.white);
				g.drawString("Thing #"+a, thing.x-lookingat.x, thing.y-lookingat.y+20);
			}
		}
		drawMinimap(g,drawingon,lookingat);
	}
	public void drawMinimap(Graphics g, JPanel drawingon, Point lookingat) {
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
		g.setColor(new Color(107,68,35));
		g.drawRect(drawingon.getWidth()-244+lookingat.x/20, drawingon.getHeight()-244+lookingat.y/20, screenw/20, screenh/20);
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
	public class TempFrameForTestingOnly extends JFrame {
		JPanel panel;
		public TempFrameForTestingOnly() {
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			//this.setSize(xsize,ysize);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
			this.setVisible(true);
			panel = new JPanel() {
				public void paintComponent(Graphics g) {
					drawEverything(g, panel, new Point(0, 0));
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
