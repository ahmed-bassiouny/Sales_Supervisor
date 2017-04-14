package pharmaproject.ahmed.example.packagecom.pharmaproject;

/**
 * Created by ahmed on 26/03/17.
 */

public class LatLong {


    static double lat;
    static double longt;


    public static double getLat(String lat_long) {
        String[] parts = lat_long.split(",");
        lat = Double.parseDouble(parts[0].replace("lat/lng: (", ""));
        return lat;
    }

    public static double getLongt(String lat_long) {
        String[] parts = lat_long.split(",");
        longt = Double.parseDouble(parts[1].replace(")", ""));
        return longt;
    }



}