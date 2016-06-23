package org.hehe7xiao.yijitong;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by TianXX on 2016/4/4.
 */
public class Geo {
    private String coordinate;
    private String address;
    private String name;
    private Double longitude;
    private Double latitude;

    public String getCoordinate() {
        return coordinate;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setCoordinate(Double longitude, Double latitude) {
        this.coordinate = floatCoordinate(longitude, latitude);
    }

    private String floatCoordinate(Double longitude, Double latitude) {
        Random r = new Random();
        longitude = longitude + (r.nextInt(20) - 10) * 0.000001; // [0, 19) - 9 = [-9 , 10) = -9, -8 ... 0 ... 8, 9
        latitude = latitude + (r.nextInt(20) - 10) * 0.000001; // [0, 19) - 9 = [-9 , 10) = -9, -8 ... 0 ... 8, 9
        DecimalFormat df = new DecimalFormat("#.000000");
        return df.format(longitude) + ',' + df.format(latitude);
    }

    public Geo(String name, String coordinate, String address) {
        this.name = name;
        this.coordinate = coordinate;
        this.address = address;
    }


    public Geo(String name, Double longitude, Double latitude, String address) {
        this.name = name;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.setCoordinate(longitude, latitude);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
