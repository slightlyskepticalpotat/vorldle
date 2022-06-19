/*
* This Proximity class calculates how the squares should be coloured
* based on how close the guess was to the actual location. It uses public 
* variables to display the number of squares so other classes can access it.
*
* @author  Anthony Chen, Tony Zhang
* @version 1.0
* @since   2022-06-19
*/

public class Proximity {
	public static final double MAX_DISTANCE_ON_EARTH = 20000; // in meters
	public static int greenSquares;
	public static int yellowSquares;
	public static int whiteSquares;

	public static double PercentProximity(double distance) {
		return Math.round((MAX_DISTANCE_ON_EARTH - distance) / MAX_DISTANCE_ON_EARTH * 100);
	}

	public static void SquareProgress(double percent) {
		greenSquares = 0;
		yellowSquares = 0;
		whiteSquares = 0;
		greenSquares = (int) percent / 20;

		if (percent % 20 > 10) {
			yellowSquares = 1;
		} else {
			yellowSquares = 0;
		}
		whiteSquares = 5 - yellowSquares - greenSquares;
	}
}
