package com.surface.surfaceviewproject.animation;

import android.content.Context;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.View;

public abstract class AnimationThread extends Thread{

	protected boolean isStart = false;
	
	protected Context mContext;
	protected SurfaceHolder mSurfaceHolder;
	protected Paint paint;
	protected View mView;
	
	public AnimationThread(){
		super();
	}
	
	public AnimationThread(Context context, SurfaceHolder surfaceHolder, View view){
		this.mContext = context;
		mSurfaceHolder = surfaceHolder;
		mView = view;
		paint = new Paint();
	}
	
	public void startThread(){
		setStartFlag(true);
		this.start();
	}
	
	public void stopThread(){
		isStart = false;
	}
	
	public void setStartFlag(boolean isStart){
		this.isStart = isStart;
	}
	
	public boolean isStart(){
		return isStart;
	}
	
	public abstract void addData(Object obj, String type);
	
	public abstract void cleanData();
}
