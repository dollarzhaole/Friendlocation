package com.example.amap.test;

import java.util.ArrayList;
import java.util.List;

import com.amap.mapapi.core.GeoPoint;
import com.example.amap.domain.People;
import com.example.amap.service.AddressService;
import com.example.amap.service.ContactsService;


import android.test.AndroidTestCase;
import android.util.Log;

public class AMapTest extends AndroidTestCase {
	private static final String TAG = "FriendMapTest";

	public void getAddressFromContactsTest() {

	}

	public void getGeoFromAddressTest() {
		People people = new People(getContext());
		ContactsService contactsService = new ContactsService(getContext());
		
		List<People> peoples = new ArrayList<People>();
		peoples = contactsService.getContactsAddress();
		
		for (int i = 0; i < peoples.size(); i++) {
			people = peoples.get(i);
			GeoPoint geoPoint = people.getGeoFromAddress(people.getAddress());
//			System.out.println();
			System.out.println("Test--->" + geoPoint.getLatitudeE6() + "," + geoPoint.getLongitudeE6());
		}
		
/*		
		Log.i(TAG,
				geoPoint.getLatitudeE6() + "," + geoPoint.getlongLatitudeE6());
		System.out.println(geoPoint.getLatitudeE6() + ","
				+ geoPoint.getlongLatitudeE6());*/
	}

	public void getAddFromSoapResultTest() {
		// String resultString = AddressService
		// .getAddFromSoapResultMoblie("15803387469：河北 衡水 河北移动全球通卡");
		// System.out.println(resultString);
		// Log.i(TAG,
		// AddressService.getAddFromSoapResult("15803387469：河北 衡水 河北移动全球通卡"));
	}

	public void getTest() {
		Log.i(TAG, "f;adjf;afja;sdfj;afj");
	}

	public void getAddFromTel() throws Exception {
		String resultString = AddressService
				.getAddressFromTelephoneNo("01051681454");
		System.out.println(resultString);
		// Log.i(TAG,
		// AddressService.getAddFromSoapResult("15803387469：河北 衡水 河北移动全球通卡"));
	}
}
