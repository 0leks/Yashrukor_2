package main;

import java.awt.Point;
import java.util.ArrayList;

public class PathPoint extends Point {
	
	int cost;
	PathPoint parent;
	double heuristic;
	double estCost;
	ArrayList<PathPoint> successors;
	public PathPoint(int x, int y)
	{
		super(x,y);
		successors = new ArrayList<PathPoint>();
	}
	public PathPoint(int x, int y, int cost)
	{
		super(x,y);
		this.cost = cost;
		successors = new ArrayList<PathPoint>();
	}

}
