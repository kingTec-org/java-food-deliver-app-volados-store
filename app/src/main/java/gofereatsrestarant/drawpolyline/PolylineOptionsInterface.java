package gofereatsrestarant.drawpolyline;

/**
 * @package com.trioangle.gofereatsrestarant
 * @subpackage drawpolyline
 * @category PolylineOptions Interface
 * @author Trioangle Product Team
 * @version 1.5
 */

import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/*************************************************************
 Interface for draw route
 *************************************************************** */
public interface PolylineOptionsInterface {
    void getPolylineOptions(PolylineOptions output, ArrayList points);
}
