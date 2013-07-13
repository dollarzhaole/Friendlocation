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

		// 4.0的网络限制更严，需要添加以下代码
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
		
		// 定位Overlay
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay(
				MainActivity.this, mMapView);
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.enableCompass();
		mMapView.getOverlays().add(myLocationOverlay);
		
		//获得当前位置坐标
		mMapController.setCenter(myLocationOverlay.getMyLocation());
		mMapController.setZoom(10);
//		point = new GeoPoint((int) (39.982378 * 1E6), (int) (116.304923 * 1E6));
//		mMapController.setCenter(point);
//		mMapController.setZoom(8);

		
		
		// 添加地图标记
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

		// 标记Overlay
		Drawable marker = getResources().getDrawable(R.drawable.da_marker_red); // 得到需要标在地图上的资源
		marker.setBounds(0, 0, marker.getIntrinsicWidth(),
				marker.getIntrinsicHeight()); // 为maker定义位置和边界
		mMapView.getOverlays().add(new MarkerOverlay(this, marker, peoples));

		// 气泡View
		popView = super.getLayoutInflater().inflate(R.layout.overlay_pop, null);
		mMapView.addView(popView, new MapView.LayoutParams(
				MapView.LayoutParams.WRAP_CONTENT,
				MapView.LayoutParams.WRAP_CONTENT, null,
				MapView.LayoutParams.BOTTOM_CENTER));
		// 由于气泡的尾巴是在下边居中的,因此要设置成MapView.LayoutParams.BOTTOM_CENTER.
		// 这里没有给GeoPoint,在onFocusChangeListener中设置
		// views.add(popView);
		popView.setVisibility(View.GONE);

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
