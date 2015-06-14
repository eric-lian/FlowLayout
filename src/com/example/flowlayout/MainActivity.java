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

		//防止在小屏手机上显示不全，需要通过scrollView包裹主界面
		ScrollView mScrollView = new ScrollView(context);
		mScrollView.setFillViewport(true);//设置可以填充父窗体
		//初始化布局，该布局可以自动分配子View位置，保持每一行都能对齐
		FolwLayouts mLayout = new FolwLayouts(context);
		//设置间距
		int layoutPadding = (int) (scale*13);
		//设置左上右下的间距
		mLayout.setPadding(layoutPadding, layoutPadding, layoutPadding, layoutPadding);
		//设置横着的间距
		/*mLayout.setHorizontalSpacing(layoutPadding);
		//设置纵着的间距
		mLayout.setVerticalSpacing(layoutPadding);*/

		int textPaddingV =(int)(scale*4);
		int textPaddingH =(int)(scale*7);
		int backColor = 0xffcecece;
		int radius = (int) (scale*5);
		//代码动态创建一个图片
		/**
		 * 第一个参数的填充的颜色，第二个参数是描边的颜色，第三个参数是圆角
		 */
		GradientDrawable pressDrawable = DrawableUtils.createDrawable(backColor, backColor, radius);
	
		for (int i = 0; i < bean.size(); i++) {
			TextView tv = new TextView(context);
			// 随机颜色的范围0x202020~0xefefef
			int color =DrawableUtils.getRandomColor();
			//设置圆角
			GradientDrawable normalDrawable = DrawableUtils.createDrawable(color, color, radius);
			//创建背景图片选择器
			StateListDrawable selector = DrawableUtils.createSelector(normalDrawable, pressDrawable);
			//要是用这个丢弃的，因为使用setBackground()这个方法是在4.0后开始支持的
			tv.setBackgroundDrawable(selector);
			
			ApplicationBean app= bean.get(i);
			tv.setText(app.name);
			tv.setTextColor(Color.WHITE);
			//把字体设置成dip，只根据当前的屏幕显示字体的大小，系统设置字体大小不会改变字体的大小
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
			Toast.makeText(this, "改程序不能启动", 1).show();
		}
	}

	@Override
	public void onBackPressed() {
	
	}

	
}
