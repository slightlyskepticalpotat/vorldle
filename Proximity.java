import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Proximity extends JPanel {

	public static final double MAX_DISTANCE_ON_EARTH = 20000000; // in meters
	public static int green;
	public static int yellow;
	public static int white;

	public static double PercentProximity(double distance) {

		double percentproximity = Math.round((MAX_DISTANCE_ON_EARTH - distance) / MAX_DISTANCE_ON_EARTH * 100);

		return (percentproximity);
	}

	public static void SquareProgress(int percent) {
		green = percent % 20;

		if (percent % 20 > 10) {
			yellow = 1;
		} else {
			yellow = 0;
		}

		white = 5 - yellow - green;
	}

	public void draw(Graphics g) {
		for (int i = 0; i < 512; i += 32) { // draw central line of squares
			if (green != 0) {
				green -= 1;
				g.setColor(Color.green);
			} else if (yellow != 0) {
				yellow -= 1;
				g.setColor(Color.yellow);
			} else if (white != 0) {
				white -= 1;
				g.setColor(Color.white);
			} else {
				return;
			}
			g.fillRect(256, i, 8, 8);
		}
	}
}

