public class Proximity {

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
}

