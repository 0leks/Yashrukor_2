package main;

public class Node {
	int x, y;
	Node prev, next;
	public Node(int sx, int sy) {
		x = sx;
		y = sy;
	}
	public String toString() {
		return "("+x+","+y+")";
	}
	boolean entrance;
	boolean exit;
	public boolean both() {
		return entrance && exit;
	}
}
