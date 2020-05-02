package gofereatsrestarant.interfaces;

/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage interface
 * @category DriverLocation
 * @author Trioangle Product Team
 * @version 1.0
 **/


/*****************************************************************
 Interface to get driver location from fire base
 ****************************************************************/
public class DriverLocation {
    public String lat;
    public String lng;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public DriverLocation() {
    }

    public DriverLocation(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
