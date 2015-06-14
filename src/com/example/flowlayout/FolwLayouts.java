package com.example.flowlayout;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


public class FolwLayouts extends ViewGroup {

	private static int DEFAULT_HORNIZED_SPACEING = 20;

	private static int DEFAULT_VERTICAL_SPACEING = 20;

	private Line mLine;

	private int useWidth = 0;

	private List<Line> lines = new ArrayList<Line>();

	private boolean mLayout = true;
	
	private int MAX_LINE=Integer.MAX_VALUE;

	public FolwLayouts(Context context) {
		super(context);
	}

	public FolwLayouts(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FolwLayouts(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//得到本布局宽高
		int width_size = MeasureSpec.getSize(widthMeasureSpec)
				- getPaddingLeft() - getPaddingRight();
		int height_size = MeasureSpec.getSize(heightMeasureSpec)
				- getPaddingTop() - getPaddingBottom();

		//得到模式
		int width_mode = MeasureSpec.getMode(widthMeasureSpec);
		int height_mode = MeasureSpec.getMode(heightMeasureSpec);

		restoreLine();//还原数据
		
		// 得到孩子的数量，对每个孩子进行测量
		int childrenCount = getChildCount();
		for (int i = 0; i < childrenCount; i++) {
			View view = getChildAt(i);

		
			if (view.getVisibility() ==GONE) {
				continue;
			}
			final float scale = getContext().getResources().getDisplayMetrics().density;
			//测量每个孩子，宽度必须是精确的不能是包裹内容的
			int childrenwidth_size = MeasureSpec.makeMeasureSpec((int) (scale*80+0.5),MeasureSpec.EXACTLY);
			//高度也要设置为 
			int childrenheight_size = MeasureSpec.makeMeasureSpec(height_size,
					height_mode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST
							: height_mode);
			view.measure(childrenwidth_size, childrenheight_size);
			//new一行
			if (mLine == null) {
				mLine = new Line();
			}
		int	childrenWidth = view.getMeasuredWidth();
			useWidth += childrenWidth;
			if (useWidth <= width_size) {
				mLine.addView(view);
				useWidth += DEFAULT_HORNIZED_SPACEING;
				if (useWidth >=width_size) {
					if (!newLine()) {
						break;
					}
				}

			} else {
				if (mLine.getViewCount() == 0) {
					mLine.addView(view);
					if (!newLine()) {
						break;
					}
				} else {
					if (!newLine()) {
						break;
					}
					mLine.addView(view);
					useWidth += childrenWidth+DEFAULT_HORNIZED_SPACEING;
				}

			}

		}

		if (mLine != null && mLine.getViewCount() > 0 && !lines.contains(mLine)) {
			lines.add(mLine);
		}
	int 	totalwidth_size = MeasureSpec.getSize(widthMeasureSpec);

	int totalheight_size = 0;

	for (int i = 0; i < lines.size(); i++) {
		totalheight_size += height;
	}
	totalheight_size += DEFAULT_VERTICAL_SPACEING * (lines.size() - 1);
	totalheight_size += getPaddingTop() + getPaddingBottom();
		
		/*super.onMeasure(
				MeasureSpec.makeMeasureSpec(width_size, MeasureSpec.EXACTLY),
				MeasureSpec.makeMeasureSpec(height_size, MeasureSpec.EXACTLY));
		*/
		setMeasuredDimension(totalwidth_size,
				resolveSize(totalheight_size, heightMeasureSpec));
	}

	private void restoreLine() {
		lines.clear();
		mLine = new Line();
		useWidth = 0;
	}

	private boolean newLine() {
			if(lines.size()<=MAX_LINE){
				lines.add(mLine);
				mLine = new Line();
				useWidth = 0;
				return true;
			}
			return false;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		if (!mLayout || changed) {
			int left = getPaddingLeft();
			int top = getPaddingTop();
			mLayout = false;
			for (int i = 0; i < lines.size(); i++) {
				Line line = lines.get(i);
				line.layout(left, top);
				top +=height + DEFAULT_VERTICAL_SPACEING;
			}
		}

		for(int i=0 ; i<lines.size() ; i++ ){
			System.out.println("line numbers is"+lines.get(i).getViewCount());
		}
	}
	protected int height =(int) (110*getResources().getDisplayMetrics().density+0.5);

	class Line {
		protected int width = 0;
		protected List<View> views = new ArrayList<View>();
		private int splitSpacing;

		public void addView(View view) {
			views.add(view);
			width += view.getMeasuredWidth();
			//int tempHeight = view.getMeasuredHeight();
		//	height = height< tempHeight ? tempHeight : height;
			
		}

		public void layout(int left, int top) {// 布局
			int l= left;
			int t = top;
			int count = getViewCount();
			// 总宽度
			int layoutWidth = getMeasuredWidth() - getPaddingLeft()
					- getPaddingRight();
			// 剩余的宽度，是除了View和间隙的剩余空间
			int surplusWidth = layoutWidth - width - DEFAULT_HORNIZED_SPACEING
					* (count - 1);
			if (surplusWidth >= 0) {
				if (count == 1) {
					View view = views.get(0);
					view.layout(left, top, left +width+splitSpacing, top
							+ height);
				} else {
					splitSpacing = (int) (surplusWidth / count + 0.5);
					for (int i = 0; i < count; i++) {
						final View view = views.get(i);
						int childWidth = view.getMeasuredWidth();
						int childHeight = view.getMeasuredHeight();
						// 计算出每个View的顶点，是由最高的View和该View高度的差值除以2
//						int topOffset = (int) ((height - childHeight) / 2.0 + 0.5);
					//	int topOffset =height;
					
					/*	if (topOffset < 0) {
							topOffset = 0;
						}*/
						// 把剩余空间平均到每个View上
						childWidth = childWidth + splitSpacing;
						view.getLayoutParams().width = childWidth;
						if (splitSpacing > 0) {// View的长度改变了，需要重新measure
							int widthMeasureSpec = MeasureSpec.makeMeasureSpec(
									childWidth, MeasureSpec.EXACTLY);
							int heightMeasureSpec = MeasureSpec.makeMeasureSpec(
									childHeight, MeasureSpec.EXACTLY);
							view.measure(widthMeasureSpec, heightMeasureSpec);
						}
						// 布局View
						view.layout(left, top, left + childWidth, top
								+ height);
						left += childWidth + DEFAULT_HORNIZED_SPACEING; // 为下一个View的left赋值
					}
				
				}
			} else {
				if (count == 1) {
					View view = views.get(0);
					view.layout(left, top, left + view.getMeasuredWidth(), top
							+ view.getMeasuredHeight());
				} else {
					// 走到这里来，应该是代码出问题了，目前按照逻辑来看，是不可能走到这一步
				}
			}
		}

		public int getViewCount() {
			return views.size();
		}
	}
}
