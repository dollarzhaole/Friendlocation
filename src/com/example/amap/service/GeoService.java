package com.example.amap.service;


import java.util.List;

import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.geocoder.Geocoder;
import com.amap.mapapi.map.MapActivity;

import android.content.Context;
import android.location.Address;



public class GeoService {
	private Context context;

	public GeoService(Context context) {
		super();
		this.context = context;
	}
	/**
	 * �ѵ�ַת��������
	 * @param address ��ַ
	 * @return
	 */
	public GeoPoint getGeoFromAddress(String address) {
		GeoPoint geoPoint = new GeoPoint();
		Geocoder geocoder = new Geocoder((MapActivity) context);
		
		try {
			List<Address> addresses = geocoder.getFromLocationName(address, 1);
			geoPoint = new GeoPoint((int)(addresses.get(0).getLatitude()), (int)(addresses.get(0).getLongitude()));
		} catch (AMapException e) {
			e.printStackTrace();
		}
		
		return geoPoint;
	}
}
