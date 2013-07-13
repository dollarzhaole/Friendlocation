package com.example.amap.marker;

import java.util.ArrayList;
import java.util.List;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.OverlayItem;
import com.amap.mapapi.map.ItemizedOverlay;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.Projection;
import com.example.amap.MainActivity;
import com.example.amap.R;
import com.example.amap.domain.People;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;



public class MarkerOverlay extends ItemizedOverlay<OverlayItem> {
	private List<OverlayItem> GeoList = new ArrayList<OverlayItem>();
	private Drawable marker;
	private Context context;
	private MainActivity mContext;
	
	private int layout_x = 0; // ��������popview ���ĳ��λ����x��ƫ��  
    private int layout_y = -30; // ��������popview ���ĳ��λ����x��ƫ��  
	
	private TextView textView;
	/**
	 * MarkerOverlay�Ĺ��캯������Geopoint���󴫵ݹ���
	 * @param context
	 * @param marker
	 * @param geoPoints
	 */
	public MarkerOverlay(Context context, Drawable marker, List<People> peoples) {
		super(boundCenterBottom(marker));
        this.marker = marker;
		this.context = context;
		this.mContext = (MainActivity)context;
		int i = 0;
		for (People people : peoples) {
			GeoList.add(new OverlayItem(people.getGeoPoint(), people.getPhone(), people.getName()));
			i++;
		}
		
		layout_x = marker.getBounds().centerX();  
        layout_y = - marker.getBounds().height();
		
		populate();
	}
	
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        // Projection�ӿ�������Ļ���ص�����ϵͳ�͵�����澭γ�ȵ�����ϵͳ֮��ı任
		Projection projection = mapView.getProjection(); 
		for (int index = size() - 1; index >= 0; index--) { // ����GeoList
			OverlayItem overLayItem = getItem(index); // �õ�����������item
			// �Ѿ�γ�ȱ任�������MapView���Ͻǵ���Ļ��������
			Point point = projection.toPixels(overLayItem.getPoint(), null); 
			// ���ڴ˴�������Ļ��ƴ���
//			Paint paintText = new Paint();
//			paintText.setColor(Color.BLACK);
//			paintText.setTextSize(15);
//			canvas.drawText(title, point.x-30, point.y - 25, paintText); // �����ı�
		}
        super.draw(canvas, mapView, shadow);
		//����һ��drawable�߽磬ʹ�ã�0��0�������drawable�ײ����һ�����ĵ�һ������
		boundCenterBottom(marker);
	}

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return GeoList.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return GeoList.size();
	}

	@Override
	// ��������¼�
	protected boolean onTap(int i) {
		
		//�õ���ǰ����Ķ���
        OverlayItem item = GeoList.get(i);
        setFocus(item);
        //���ͼ����ʾ��Ϣ����
        showInfoWindow(item, i);
        //���뷵��true,�ú���һ��onTap����֪�������ͼ��
        return true;
		
		/*
		setFocus(GeoList.get(i));
		Toast.makeText(this.context, GeoList.get(i).getSnippet(),
				Toast.LENGTH_SHORT).show();
		return true;*/
	}

	private void showInfoWindow(OverlayItem item, int i) {
		
		setFocus(GeoList.get(i));
		final String phoneNumber = GeoList.get(i).getTitle();
		MapView.LayoutParams geoLP = (MapView.LayoutParams) mContext.popView.getLayoutParams();
		geoLP.x = layout_x;
		geoLP.y = layout_y;
		geoLP.point = GeoList.get(i).getPoint();
		mContext.mMapView.updateViewLayout(mContext.popView, geoLP);
		mContext.popView.setVisibility(View.VISIBLE);
		textView = (TextView) mContext.findViewById(R.id.map_bubble_name);
		textView.setText(GeoList.get(i).getSnippet());
		
		ImageView imageViewMess = (ImageView) mContext.findViewById(R.id.map_bubble_message);
		ImageView imageViewPhone = (ImageView) mContext.findViewById(R.id.map_bubble_phone);
		imageViewMess.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Uri uri = Uri.parse("smsto:" + phoneNumber);  
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);  
                mContext.startActivity(intent);
			}
		});
		imageViewPhone.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v) {
				Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNumber)); 
				mContext.startActivity(intent);
			}
		});
	}

	@Override
	public boolean onTap(GeoPoint point, MapView mapView) {
		 if (!super.onTap(point, mapView)) {
			 mContext.popView.setVisibility(View.GONE);
         }
         return true;
	}

}