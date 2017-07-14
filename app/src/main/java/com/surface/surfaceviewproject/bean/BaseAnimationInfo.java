package com.surface.surfaceviewproject.bean;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public abstract class BaseAnimationInfo {


	public static final int COMMON_GIFT = 0; 		// 普通礼物
	public static final int WINNING_GIFT = 1; 		// 反奖礼物
	public static final int NOBILITY_MOUNT = 2; 	// 贵族座驾
	public static final int GENERAL_MOUNT = 3; 		// 普通座驾
	public static final int GUARD_ANIM = 4; 		// 开通守护
	public static final int MARQUEE_ANIM = 5; 		// 开通守护
	public static final int STEPDISTANCE = 10;		// 每次上下移动距离
	
	protected int animationType = -1; 				// 礼物类型，普通礼物、反奖礼物

	protected int mX = 0;
	protected int mY = 0;
	protected int startX = 0;
	protected int startY = 0;
	protected Bitmap mGiftBitmap = null;
	protected Context mContext;

	protected float mTime = 0.0F;

	protected int mMobileWidth = 0;
	protected int mMobileHeight = 0;

	protected float scale = 1.0f;
	protected int alpha = 1;

	protected Paint mPaint;

	protected int mSequence = 0;
	protected boolean isFinish = false;

	protected View animView = null;
	
	protected boolean hasInitBitmap = false;	// 是否已经初始化动画图片：true是，fase否
	
	protected boolean isAnimRunning = false;	// 当前动画是否正在执行中：true是，false否
	
	public BaseAnimationInfo(Context context, int startX, int startY, int type) {
		mContext = context;

		mX = startX;
		mY = startY;
		this.startX = startX;
		this.startY = startY;
		
		animationType = type;

		mPaint = new Paint();

		mPaint.setStyle(Style.FILL_AND_STROKE);
		mPaint.setColor(0xffffffff);
		mPaint.setAntiAlias(true);
	}
	
	public abstract void initBitmap(int animationType);

	// {
	// if(hasInitBitmap){
	// return;
	// }
	//
	// int height = 0;
	// int width = 0;
	// switch (animationType) {
	// case NOBILITY_MOUNT:
	// height = (int) mContext.getResources().getDimension(R.dimen.animation_nobility_mount_height);
	// width = (int) mContext.getResources().getDimension(R.dimen.animation_nobility_mount_width);
	// scale = 0.0F;
	// break;
	// case GENERAL_MOUNT:
	// width = (int) mContext.getResources().getDimension(R.dimen.animation_general_mount_width);
	// height = (int) mContext.getResources().getDimension(R.dimen.animation_general_mount_height);
	// scale = 1.0F;
	// break;
	// case GUARD_ANIM:
	// width = (int) (mContext.getResources().getDimension(R.dimen.animation_winning_gift_width) * 0.85F);
	// height = (int) (mContext.getResources().getDimension(R.dimen.animation_winning_gift_height) * 0.85F);
	//
	// scale = 0.0F;
	// break;
	// case COMMON_GIFT:
	// width = (int) mContext.getResources().getDimension(R.dimen.animation_common_gift_width);
	// height = (int) mContext.getResources().getDimension(R.dimen.animation_common_gift_height);
	// BitmapFactory.Options options = new BitmapFactory.Options();
	// options.inJustDecodeBounds = true;
	// Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.nobility_173_anim_bg);
	// // width = bmp.getWidth();
	// scale = 0.0F;
	// break;
	// case WINNING_GIFT:
	// height = (int) mContext.getResources().getDimension(R.dimen.animation_winning_gift_height);
	// width = (int) mContext.getResources().getDimension(R.dimen.animation_winning_gift_width);
	//
	// scale = 0.0F;
	// break;
	// }
	// mMobileWidth = width;
	// mMobileHeight = height;
	//
	// mGiftBitmap = getViewBitmap(animView, width, height);
	// System.out.println("=====initBitmap null is" + mGiftBitmap == null);
	// if(mGiftBitmap == null){
	// return;
	// }
	//
	// hasInitBitmap = true;
	// }
	
	public boolean hasInitBitmap(){
		return hasInitBitmap;
	}
	
	public Bitmap getViewBitmap(View comBitmap, int width, int height) {
		Bitmap bitmap = null;
		if (comBitmap != null) {
			try {
				comBitmap.clearFocus();
				comBitmap.setPressed(false);

				boolean willNotCache = comBitmap.willNotCacheDrawing();
				comBitmap.setWillNotCacheDrawing(false);

				int color = comBitmap.getDrawingCacheBackgroundColor();
				comBitmap.setDrawingCacheBackgroundColor(0);
				float alpha = comBitmap.getAlpha();
				comBitmap.setAlpha(1.0f);

				if (color != 0) {
					comBitmap.destroyDrawingCache();
				}

				int widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
				int heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
				comBitmap.measure(widthSpec, heightSpec);
				comBitmap.layout(0, 0, width, height);

				comBitmap.buildDrawingCache();
				Bitmap cacheBitmap = comBitmap.getDrawingCache();
				if (cacheBitmap == null) {
					Log.e("view.ProcessImageToBlur", "failed getViewBitmap(" + comBitmap + ")", new RuntimeException());
					return null;
				}
				bitmap = Bitmap.createBitmap(cacheBitmap);
				// Restore the view
				comBitmap.setAlpha(alpha);
				comBitmap.destroyDrawingCache();
				comBitmap.setWillNotCacheDrawing(willNotCache);
				comBitmap.setDrawingCacheBackgroundColor(color);
			} catch (Exception e) {
				bitmap = null;
			}
		}
		return bitmap;
	}

	public void setX(int x) {
		mX = x;
	}

	public int getX() {
		return mX;
	}

	public void setY(int y) {
		if (Math.abs(y - startY) >= mMobileHeight * (2 - mSequence)) {
			mY = startY - mMobileHeight * (2 - mSequence);
			return;
		}
		mY = y;
	}

	public int getY() {
		return mY;
	}

	public void setScale(float scale) {
		if (scale >= 1.0F) {
			this.scale = 1.0F;
		} else {
			this.scale = scale;
		}
	}

	public float getScale() {
		return this.scale;
	}

	public void setTime(float time) {
		mTime = time;
	}

	public float getTime() {

		return mTime;
	}

	public Bitmap getBimap() {
		return mGiftBitmap;
	}

	public int getAnimationType() {
		return animationType;
	}
	
	public View getAnimView(){
		return animView;
	}

	public boolean isFinish() {
		return isFinish;
	}

	public void recycleBitmap() {
		if (mGiftBitmap != null) {
			mGiftBitmap.recycle();
			mGiftBitmap = null;
		}
	}

	public abstract void doneDraw(Canvas canvas, int sequence);

	public abstract boolean isAnimRunning();
}
