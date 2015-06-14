package com.example.flowlayout;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	float scale ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(getWindow().FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		scale= this.getResources().getDisplayMetrics().density;
		PackageManager manager = this.getPackageManager();
		List<ApplicationInfo> list = manager.getInstalledApplications(PackageManager.GET_META_DATA);
		ArrayList<ApplicationBean> bean = new ArrayList<ApplicationBean>();
	
		for(ApplicationInfo a:list){
			
			ApplicationBean  aBean=new ApplicationBean();
			aBean.name=manager.getApplicationLabel(a).toString();
			aBean.icon = manager.getApplicationIcon(a);
			aBean.intent=manager.getLaunchIntentForPackage(a.packageName);
			bean.add(aBean);
		}
	
		initView(this,bean);
	}

	private void initView(Context context,List<ApplicationBean> bean) {

		//��ֹ��С���ֻ�����ʾ��ȫ����Ҫͨ��scrollView����������
		ScrollView mScrollView = new ScrollView(context);
		mScrollView.setFillViewport(true);//���ÿ�����丸����
		//��ʼ�����֣��ò��ֿ����Զ�������Viewλ�ã�����ÿһ�ж��ܶ���
		FolwLayouts mLayout = new FolwLayouts(context);
		//���ü��
		int layoutPadding = (int) (scale*13);
		//�����������µļ��
		mLayout.setPadding(layoutPadding, layoutPadding, layoutPadding, layoutPadding);
		//���ú��ŵļ��
		/*mLayout.setHorizontalSpacing(layoutPadding);
		//�������ŵļ��
		mLayout.setVerticalSpacing(layoutPadding);*/

		int textPaddingV =(int)(scale*4);
		int textPaddingH =(int)(scale*7);
		int backColor = 0xffcecece;
		int radius = (int) (scale*5);
		//���붯̬����һ��ͼƬ
		/**
		 * ��һ��������������ɫ���ڶ�����������ߵ���ɫ��������������Բ��
		 */
		GradientDrawable pressDrawable = DrawableUtils.createDrawable(backColor, backColor, radius);
	
		for (int i = 0; i < bean.size(); i++) {
			TextView tv = new TextView(context);
			// �����ɫ�ķ�Χ0x202020~0xefefef
			int color =DrawableUtils.getRandomColor();
			//����Բ��
			GradientDrawable normalDrawable = DrawableUtils.createDrawable(color, color, radius);
			//��������ͼƬѡ����
			StateListDrawable selector = DrawableUtils.createSelector(normalDrawable, pressDrawable);
			//Ҫ������������ģ���Ϊʹ��setBackground()�����������4.0��ʼ֧�ֵ�
			tv.setBackgroundDrawable(selector);
			
			ApplicationBean app= bean.get(i);
			tv.setText(app.name);
			tv.setTextColor(Color.WHITE);
			//���������ó�dip��ֻ���ݵ�ǰ����Ļ��ʾ����Ĵ�С��ϵͳ���������С����ı�����Ĵ�С
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
			tv.setGravity(Gravity.CENTER);
			tv.setPadding(textPaddingH, textPaddingV, textPaddingH, textPaddingV);
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			tv.setCompoundDrawablesWithIntrinsicBounds(null,app.icon,null, null);
			tv.setClickable(true);
			tv.setFocusable(true);
			tv.setFocusableInTouchMode(true);
			tv.setOnClickListener(this);
			tv.setTag(app.intent);
			mLayout.addView(tv);
		}
		mScrollView.addView(mLayout);
		
		setContentView(mScrollView);
	}

	@Override
	public void onClick(View v) {
		Intent	intent=(Intent)v.getTag();
		if(null!=intent){
			startActivity(intent);
		}else {
			Toast.makeText(this, "�ĳ���������", 1).show();
		}
	}

	@Override
	public void onBackPressed() {
	
	}

	
}
