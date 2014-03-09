package main;

import java.awt.Color;
import java.awt.Rectangle;

public class Util {
	public static Rectangle normalizeRectangle(int x1, int y1, int x2, int y2) {
		int xf = x1;
		int width = x2-x1;
		if(width<0) {
			xf = x2;
			width = x1-x2;
		}
		int yf = y1;
		int height = y2-y1;
		if(height<0) {
			yf = y2;
			height = y1-y2;
		}
		return new Rectangle(xf, yf, width, height);
	}
	public static Color inverseColor(Color c) {
		return new Color(255-c.getRed(), 255-c.getGreen(), 255-c.getBlue());
	}
}
