public class Bearing {

	public static double getBearing(double lat1, double lon1, double lat2, double lon2){

		double x,y,bearingrad,bearingdeg;

		lat1 = Math.toRadians(lat1);
		lon1 = Math.toRadians(lon1);
		lat2 = Math.toRadians(lat2);
		lon2 = Math.toRadians(lon2);


		x = Math.cos(lat2)*Math.sin(lon2-lon1);

		y = Math.cos(lat1)*Math.sin(lat2)-Math.sin(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1);

		bearingrad = Math.atan2(x, y);
		bearingdeg = Math.toDegrees(bearingrad);

		return bearingdeg;
	}
  }
