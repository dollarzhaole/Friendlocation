package com.example.amap.marker;


import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.OverlayItem;
import com.example.amap.domain.People;

public class MyOverlayItem extends OverlayItem{
	private People people = null;
	
	public MyOverlayItem(GeoPoint geoPoint, People people, String title, String snippet) {
		super(geoPoint, title, snippet);
		this.people = people;
	}

	
}
