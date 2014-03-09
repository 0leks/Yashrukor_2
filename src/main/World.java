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
	Player t4 = new Player();
	Player t1=new Player();
	Player t2=new Player();
	Player t3=new Player();
	ArrayList<Player> playerlist=new ArrayList<Player>();
	Toolkit tk = Toolkit.getDefaultToolkit();
	int xsize=(int) tk.getScreenSize().getWidth();
	int ysize=(int) tk.getScreenSize().getHeight();
	int worldx=4800;
	int worldy=4800;
	ArrayList<Thing> allThings = new ArrayList<Thing>();
	TempFrameForTestingOnly asdf;
	public World() {
		Thing.myWorld = this;
		playerlist.add(t1);
		playerlist.add(t2);
		playerlist.add(t3);
		playerlist.add(t4);
		if(playerlist.size()>0){
			allThings.add(new Unit(1, 0, 50, playerlist.get(0)));
			allThings.add(new Building(50, 50, 1,playerlist.get(0)));
		}
		if(playerlist.size()>1){
			allThings.add(new Unit(1, 4650, 4700, playerlist.get(1)));
			allThings.add(new Building(4700, 4700, 1,playerlist.get(1)));
		}
		if(playerlist.size()>2){
			allThings.add(new Unit(1, 0, 4700, playerlist.get(2)));
			allThings.add(new Building(50, 4700, 1,playerlist.get(2)));
		}
		if(playerlist.size()>3){
			allThings.add(new Unit(1, 4700, 0, playerlist.get(3)));
			allThings.add(new Building(4700, 50, 1,playerlist.get(3)));
		}
		
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
