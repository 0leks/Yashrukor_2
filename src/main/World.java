package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class World implements Serializable {
	ArrayList<Building> allBuildigs = new ArrayList<Building>();
	ArrayList<Unit> allUnits = new ArrayList<Unit>();
	Player temp = new Player();
	Toolkit tk = Toolkit.getDefaultToolkit();
	int xsize=(int) tk.getScreenSize().getWidth();
	int ysize=(int) tk.getScreenSize().getHeight();
	ArrayList<Thing> allThings = new ArrayList<Thing>();
	TempFrameForTestingOnly asdf;
	public World() {
		Thing.myWorld = this;
		allThings.add(new Unit(1, 50, 50, temp));
		allThings.add(new Building(150, 50, 1));
		allThings.add(new Thing(150, 150));
		//TempFrameForTestingOnly asdf = new TempFrameForTestingOnly();
	}
	
	public void drawEverything(Graphics g) {
		g.setColor(new Color(0,128,0));
		g.fillRect(0, 0, xsize, ysize);
		for(int a=0; a<allThings.size(); a++) {
			Thing thing = allThings.get(a);
			if(thing instanceof Building){
				g.setColor(Color.blue);
				g.fillRect(thing.x, thing.y, 50, 50);
				g.setColor(Color.white);
				g.drawString("Building #"+a, thing.x, thing.y+20);
			} 
			else if(thing instanceof Unit) {
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
		public TempFrameForTestingOnly() {
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			//this.setSize(xsize,ysize);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
		World w = new World();
		w.testDraw();
	}
}
