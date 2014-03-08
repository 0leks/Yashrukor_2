package main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class World {
	ArrayList<Building> allBuildigs = new ArrayList<Building>();
	ArrayList<Unit> allUnits = new ArrayList<Unit>();
	ArrayList<Thing> allThings = new ArrayList<Thing>();
	public World() {
		Thing.myWorld = this;
		allThings.add(new Unit(1, 50, 50));
		//TempFrameForTestingOnly asdf = new TempFrameForTestingOnly();
	}
	
	public void drawEverything(Graphics g) {
		for(int a=0; a<allThings.size(); a++) {
			Thing thing = allThings.get(a);
			
			//this is temporary drawing code for representing Things on the screen
			g.setColor(Color.black);
			g.fillRect(thing.x, thing.y, 50, 50);
			g.setColor(Color.white);
			g.drawString(a+"", thing.x, thing.y+20);
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
	public static void main(String[] args) {
		new World();
	}
}
