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
	
	public static final double MAX_DISTANCE_ON_EARTH = 20000; // earth half-circumference in km
	public static int greenSquares; // other classes can access these to draw the squares
	public static int yellowSquares;
	public static int whiteSquares;

	public static double PercentProximity(double distance) {
		return Math.round((MAX_DISTANCE_ON_EARTH - distance) / MAX_DISTANCE_ON_EARTH * 100); // percent of the way there
	}

	public static void SquareProgress(double percent) {
		greenSquares = (int) percent / 20; // display green square for each 20%

		if (percent % 20 > 10) { // display yellow square if we have >10% left over
			yellowSquares = 1;
		} else {
			yellowSquares = 0;
		}
		whiteSquares = 5 - yellowSquares - greenSquares; // the other squares are white (actually grey)
	}
}
