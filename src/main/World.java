package main;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import network.Server;

public class World implements Serializable {
	ArrayList<Building> allBuildigs = new ArrayList<Building>();
	ArrayList<Unit> allUnits = new ArrayList<Unit>();
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
		//allThings.add(new Unit(1, 50, 50, new Player(Color.orange)));
		allThings.add(new Building(150, 50, 1));
		allThings.add(new Thing(150, 150));
		//TempFrameForTestingOnly asdf = new TempFrameForTestingOnly();
	}
	public void drawEverything(Graphics g) {
		for(int a=0; a<allThings.size(); a++) {
			Thing thing = allThings.get(a);
			if(thing instanceof Building) {
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
			} else {
				//this is temporary drawing code for representing Things on the screen
				g.setColor(Color.black);
				g.fillRect(thing.x, thing.y, 50, 50);
				g.setColor(Color.white);
				g.drawString("Thing #"+a, thing.x, thing.y+20);
			}
		}
	}
	public class TempFrameForTestingOnly extends JFrame {
		public TempFrameForTestingOnly() {
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setSize(500, 500);
			this.setVisible(true);
			this.add(new JPanel() {
				public void paintComponent(Graphics g) {
					drawEverything(g);
				}
			});
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
