package org.hehe7xiao.yijitong;

/**
 * Created by TianXX on 2016/4/4.
 */
public class Geo {
    private String coordinate;
    private String address;
    private String name;
    private Float longitude;
    private Float latitude;

    public String getCoordinate() {
        return coordinate;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setCoordinate(Float longitude, Float latitude) { // TODO: 2016/4/4
        this.coordinate = longitude.toString() + ',' + latitude.toString();
    }

    public Geo(String name, String coordinate, String address) {
        this.name = name;
        this.coordinate = coordinate;
        this.address = address;
    }


    public Geo(String name, Float longitude, Float latitude, String address) {
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
