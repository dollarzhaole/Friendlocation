package com.example.amap.service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.example.amap.service.AddressService;
import com.example.amap.utils.StreamTool;

/**
 * 把电话号码转换成地址
 * @author dollar
 *
 */
public class AddressService {
	/**
	 * 查询电话号码归属地
	 * @param mobile	电话号码
	 * @return	电话号码归属地
	 * @throws Exception
	 */
	public static String getAddressFromPhoneNo(String mobile) throws Exception{
		String soap = readSoap("mobile_soap.xml");
		soap = soap.replace("\\$mobile", mobile);
		byte[] entity = soap.getBytes();
		
		String path = "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx";
		HttpURLConnection connection = (HttpURLConnection) ((new URL(path)).openConnection());
		connection.setConnectTimeout(5000);
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
		connection.setRequestProperty("Content-Length", String.valueOf(entity.length));
		connection.getOutputStream().write(entity);
		if (connection.getResponseCode() == 200) {
			String soapResult = parseSOAPMoblie(connection.getInputStream());
			return getAddFromSoapResult(soapResult);
		}
		
		return null;
	}
	

	//15803387469：河北 衡水 河北移动全球通卡
	private static String getAddFromSoapResult(String soapResult) {
		String[] result = soapResult.split(" ");
		String addressTemp1 = result[0];
		String[] addressTemp = addressTemp1.split("：");
		return result[1] + addressTemp[1];
	}

	private static String parseSOAPMoblie(InputStream inputStream) throws Exception {
		XmlPullParser xmlPullParser = Xml.newPullParser();
		xmlPullParser.setInput(inputStream, "UTF-8");
		
		int event = xmlPullParser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_TAG:
				if ("getMobileCodeInfoResult".equals(xmlPullParser.getName())) {
					return xmlPullParser.nextText();
				}
				break;
			}
			event = xmlPullParser.next();
		}
		return null;
	}

	private static String readSoap(String xmlName) throws Exception {
		InputStream inStream = AddressService.class.getClassLoader().getResourceAsStream(xmlName);
		byte[] data = StreamTool.readInputStream(inStream);
		return new String(data);
	}
	
	
	
	
	
	
	public static String getAddressFromTelephoneNo (String telephone) throws Exception {
		String soap = readSoap("tele_soap.xml");
		soap = soap.replace("\\$mobile", telephone);
		byte[] entity = soap.getBytes();
		
		String path = "http://gz.wufish.com/webservice/phone.asmx";
		HttpURLConnection connection = (HttpURLConnection) ((new URL(path)).openConnection());
		connection.setConnectTimeout(5000);
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
		connection.setRequestProperty("Content-Length", String.valueOf(entity.length));
		connection.getOutputStream().write(entity);
		if (connection.getResponseCode() == 200) {
			String soapResult = parseSOAPTel(connection.getInputStream());
			return soapResult;
		}
		
		return null;
		
	}
	
	private static String parseSOAPTel(InputStream inputStream) throws Exception{
		XmlPullParser xmlPullParser = Xml.newPullParser();
		xmlPullParser.setInput(inputStream, "UTF-8");
		
		int event = xmlPullParser.getEventType();
		while (event != XmlPullParser.END_DOCUMENT) {
			switch (event) {
			case XmlPullParser.START_TAG:
				if ("GetCompanyResult".equals(xmlPullParser.getName())) {
					return xmlPullParser.nextText();
				}
				break;
			}
			event = xmlPullParser.next();
		}
		return null;
	}
}
