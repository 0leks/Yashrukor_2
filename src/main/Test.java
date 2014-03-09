package main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Test {
	Timer tim;
	Unit u;
	Unit t;
	public Test() {

		World w = new World(null);
		 u = new Unit(2, 50, 50, new Player(Color.red));
		 t = new Unit(2,400,400, new Player(Color.blue));
		w.allThings.add(u);
		Command c = new Command(Command.MOVE);
		c.setX(400);
		c.setY(400);
		c.setTarget(t);
		
		u.addCommand(c);
		tim = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				u.tic();
				System.out.println(u.x+" "+u.y);
			}
		});
		tim.start();
		
	}
	public static void main(String[] args) {
		new Test();
	}
}
