package com.example.amap.service;

import java.util.ArrayList;
import java.util.List;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.p;
import com.example.amap.domain.People;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;


public class ContactsService {
	private Context context = null;
	private GeoService geoService = null;
	
	
	public ContactsService(Context context) {
		super();
		this.context = context;
		this.geoService = new GeoService(context);
	}


	public List<People> getContactsAddress() {
    	List<People> peoples = new ArrayList<People>();
    	
    	Uri uri = Uri.parse("content://com.android.contacts/contacts");
    	ContentResolver resolver = context.getContentResolver();
    	Cursor cursor = resolver.query(uri, new String[]{"_id"}, null, null, null);
    	
    	while (cursor.moveToNext()) {
			People people = new People(context);//????????????????????????????????????????
			int contactId = cursor.getInt(0);
			uri = Uri.parse("content://com.android.contacts/contacts/" + contactId + "/data");
			Cursor dataCursor = resolver.query(uri, new String[]{"mimetype", "data1", "data2"}, null, null, null);
			
			while (dataCursor.moveToNext()) {
				String data = dataCursor.getString(dataCursor.getColumnIndex("data1"));
				String type = dataCursor.getString(dataCursor.getColumnIndex("mimetype"));
				if ("vnd.android.cursor.item/name".equals(type)) {
					people.setName(data);
				}else if ("vnd.android.cursor.item/postal-address_v2".equals(type)) {
					people.setAddress(data);
					people.setGeoPoint();
//					GeoPoint temPoint = people.getGeoFromAddress(data);
//					System.out.println("contactsservice--->" + data + "," + temPoint);
//					people.setGeoPoint(temPoint);
				}else if ("vnd.android.cursor.item/phone_v2".equals(type)) {
					people.setPhone(data);
				}else if ("vnd.android.cursor.item/email_v2".equals(type)) {
					people.setEmail(data);
				}
			}
			if (people.getAddress() == null) {
				continue;
			}
			peoples.add(people);
    	}
    	
    	return peoples;
	}
}
