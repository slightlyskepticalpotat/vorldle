public class Proximity {

	public static final double MAX_DISTANCE_ON_EARTH = 20000; // in meters
	public static int greenSquares;
	public static int yellowSquares;
	public static int whiteSquares;

	public static double PercentProximity(double distance) {

		double percentproximity = Math.round((MAX_DISTANCE_ON_EARTH - distance) / MAX_DISTANCE_ON_EARTH * 100);
		return (percentproximity);
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
