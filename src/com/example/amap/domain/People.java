package com.example.amap.domain;

import java.util.List;

import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.geocoder.Geocoder;

import android.content.Context;
import android.location.Address;



public class People {
	private String name = null;
	private String phone = null;
	private String email = null;
	private String address = null;
	private GeoPoint geoPoint = null;
	private Context context;

	public People(Context context) {
		super();
		this.context = context;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public GeoPoint getGeoPoint() {
		return geoPoint;
	}

	public void setGeoPoint() {
		if (address != null) {
			this.geoPoint = this.getGeoFromAddress(this.address);
//			System.out.println("People-Geopoint--_>" + this.geoPoint);
		} else {
			return;
		}
	}

	
	@Override
	public String toString() {
		if (phone == null || email == null || address == null || geoPoint == null) {
			return null;
		}
		return "People [name=" + name + ", phone=" + phone + ", email=" + email
				+ ", address=" + address + ", geoPoint=" + geoPoint.getLatitudeE6() + "," + geoPoint.getLongitudeE6() + "]";
	}

	public GeoPoint getGeoFromAddress(String address) {
		GeoPoint geoPoint = new GeoPoint();
		Geocoder geocoder = new Geocoder(context);

		try {
			List<Address> addresses = geocoder.getFromLocationName(address, 1);
			geoPoint = new GeoPoint((int) (addresses.get(0).getLatitude() * 1E6),
					(int) (addresses.get(0).getLongitude() * 1E6));
			
/*			if (addresses != null && addresses.size()>0) {
				Address addres = addresses.get(0);
				String addressName = addres.getLatitude()
						+ "," + addres.getLongitude();
				System.out.println("getgeofromaddress--->" + addressName);
			}*/
		} catch (AMapException e) {
			e.printStackTrace();
		}

		return geoPoint;
	}

}