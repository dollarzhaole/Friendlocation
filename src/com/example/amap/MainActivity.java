package com.example.amap;

import java.util.ArrayList;
import java.util.List;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.MyLocationOverlay;
import com.example.amap.domain.People;
import com.example.amap.marker.MarkerOverlay;
import com.example.amap.service.ContactsService;

import android.os.Bundle;
import android.os.StrictMode;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends MapActivity {
	public MapView mMapView;
	private MapController mMapController;
	private List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
	private List<People> peoples = new ArrayList<People>();
	public View popView;

	public void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// 4.0���������Ƹ��ϣ���Ҫ������´���
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath()
				.build());

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.setMapMode(MAP_MODE_VECTOR);

		mMapView = (MapView) findViewById(R.id.mapView);
		mMapView.setBuiltInZoomControls(true);
		mMapController = mMapView.getController();
		
		// ��λOverlay
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(
				MainActivity.this, mMapView);
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.enableCompass();
		mMapView.getOverlays().add(myLocationOverlay);
		
		//��õ�ǰλ������
		mMapController.setCenter(myLocationOverlay.getMyLocation());
		mMapController.setZoom(10);
//		point = new GeoPoint((int) (39.982378 * 1E6), (int) (116.304923 * 1E6));
//		mMapController.setCenter(point);
//		mMapController.setZoom(8);

		
		
		// ��ӵ�ͼ���
		Context context = (Context) this;

		ContactsService contactsService = new com.example.amap.service.ContactsService(
				(context));

		peoples = contactsService.getContactsAddress();
		for (People people : peoples) {
			System.out.println(people.getName());
			if (people.getGeoPoint() != null) {
				geoPoints.add(people.getGeoPoint());
			}
		}

		// ���Overlay
		Drawable marker = getResources().getDrawable(R.drawable.da_marker_red); // �õ���Ҫ���ڵ�ͼ�ϵ���Դ
		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight()); // Ϊmaker����λ�úͱ߽�
		mMapView.getOverlays().add(new MarkerOverlay(this, marker, peoples));

		// ����View
		popView = super.getLayoutInflater().inflate(R.layout.overlay_pop, null);
		mMapView.addView(popView, new MapView.LayoutParams(
				MapView.LayoutParams.WRAP_CONTENT,
				MapView.LayoutParams.WRAP_CONTENT, null,
				MapView.LayoutParams.BOTTOM_CENTER));
		// �������ݵ�β�������±߾��е�,���Ҫ���ó�MapView.LayoutParams.BOTTOM_CENTER.
		// ����û�и�GeoPoint,��onFocusChangeListener������
		// views.add(popView);
		popView.setVisibility(View.GONE);

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
