package network;

import java.awt.Point;
import java.io.Serializable;

public class AttackMoveCommand implements Serializable{
	Point target;
	int id;
}
