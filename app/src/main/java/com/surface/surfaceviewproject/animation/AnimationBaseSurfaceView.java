package com.surface.surfaceviewproject.animation;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class AnimationBaseSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

	protected SurfaceHolder mSurfaceHolder;
	protected AnimationThread myThread;
	
	protected boolean isSurfaceViewCreated = false;
	
	public AnimationBaseSurfaceView(Context context) {
		super(context);
		init(context);
	}
	
	public AnimationBaseSurfaceView(Context context, AttributeSet attrs){
		super(context, attrs);
		init(context);
	}
	
	public AnimationBaseSurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context){
		mSurfaceHolder = this.getHolder();
		mSurfaceHolder.addCallback(this);
		setZOrderOnTop(true);
		mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
	}
	
	public void setThread(AnimationThread thread){
		this.myThread = thread;
	}

}
