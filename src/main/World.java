package main;

import java.awt.Color;
import java.awt.Graphics;
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
	ArrayList<Thing> allThings = new ArrayList<Thing>();
	TempFrameForTestingOnly asdf;
	transient Server server;
	public World(Server serv) {
		if(serv!=null) {
			server = serv;
			for(int a=0; a<server.connections.size(); a++) {
				allThings.add(new Unit(1, (int)(Math.random()*500), (int)(Math.random()*500), server.connections.get(a).getPlayer()));
			}
		}
		Thing.myWorld = this;
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
		//TempFrameForTestingOnly asdf = new TempFrameForTestingOnly();
	}
	public void drawEverything(Graphics g, JPanel drawingon) {
		g.setColor(new Color(0,128,0));
		g.fillRect(drawingon.getX(), drawingon.getY(), drawingon.getWidth(), drawingon.getHeight());
		for(int a=0; a<allThings.size(); a++) {
			Thing thing = allThings.get(a);
			if(thing instanceof Building){
				g.setColor(Color.blue);
				g.fillRect(thing.x, thing.y, 50, 50);
				g.setColor(Color.white);
				g.drawString("Building #"+a, thing.x, thing.y+20);
			} else if(thing instanceof Unit) {
				Unit unit = (Unit)thing;
				g.setColor(new Color(200, 40, 60));
				g.fillRect(thing.x, thing.y, 50, 50);
				g.setColor(Color.white);
				g.drawString("Unit #"+a, thing.x, thing.y+20);
			} 
			else {
				//this is temporary drawing code for representing Things on the screen
				g.setColor(Color.black);
				g.fillRect(thing.x, thing.y, 50, 50);
				g.setColor(Color.white);
				g.drawString("Thing #"+a, thing.x, thing.y+20);
			}
		}
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
					drawEverything(g, panel);
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
