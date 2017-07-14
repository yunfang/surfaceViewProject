package com.surface.surfaceviewproject.animation;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.view.SurfaceHolder;

import com.surface.surfaceviewproject.bean.BaseAnimationInfo;
import com.surface.surfaceviewproject.bean.RoomAnimationInfo;

public class DrawLiveAnimThread extends Thread {

	private SurfaceHolder mSurfaceHolder;
	private boolean mThreadFlag;

	private final long FPS = 30;

	private boolean isGeneralMountRunning = false;

	private boolean isDestoryed = false;

	private List<BaseAnimationInfo> mCommonGiftList;
	private List<BaseAnimationInfo> mWinningGiftList;
	private List<BaseAnimationInfo> mNobilityMountList;
	private List<BaseAnimationInfo> mGeneralMountList;
	private List<BaseAnimationInfo> mBuyGuardList;

	private Handler mHandler = null;

	public DrawLiveAnimThread(SurfaceHolder holder, Handler handler, List<BaseAnimationInfo>... list) {
		mSurfaceHolder = holder;
		mHandler = handler;

		List<BaseAnimationInfo>[] animInfoLists = list;
		
		mCommonGiftList = animInfoLists[0];
		mWinningGiftList= animInfoLists[1];
		mNobilityMountList= animInfoLists[2];
		mGeneralMountList= animInfoLists[3];
		mBuyGuardList= animInfoLists[4];
	}

	public void setDestoryed(boolean b) {
		isDestoryed = b;
	}

	public boolean isStarted() {
		return mThreadFlag;
	}

	public void setDataList(List<BaseAnimationInfo> commonGift, List<BaseAnimationInfo> winningGift, List<BaseAnimationInfo> nobilityMount, List<BaseAnimationInfo> generalMount, List<BaseAnimationInfo> buyGuard){
		mCommonGiftList = commonGift;
		mWinningGiftList = winningGift;
		mNobilityMountList = nobilityMount;
		mGeneralMountList = generalMount;
		mBuyGuardList = buyGuard;
	}

	public void addData(BaseAnimationInfo viewInfo) {
		int type = viewInfo.getAnimationType();

		switch (type) {
		case BaseAnimationInfo.COMMON_GIFT:
			mCommonGiftList.add(viewInfo);
			break;
		case BaseAnimationInfo.WINNING_GIFT:
			mWinningGiftList.add(viewInfo);
			break;
		case BaseAnimationInfo.GENERAL_MOUNT:
//			if(((RoomAnimationInfo)viewInfo).getEnterRoomModel().getNobilityInfo() != null){		// 贵族普通座驾
//				mNobilityMountList.add(viewInfo);
//			}else {		// 普通用户座驾
//				mGeneralMountList.add(viewInfo);
//			}

			mNobilityMountList.add(viewInfo);
			mGeneralMountList.add(viewInfo);

			break;
		case BaseAnimationInfo.NOBILITY_MOUNT:
			mNobilityMountList.add(viewInfo);
			break;
		case BaseAnimationInfo.GUARD_ANIM:
			mBuyGuardList.add(viewInfo);
			break;
		}

	}

	public void stopThread() {
		mThreadFlag = false;
		try {
			this.join(1000L);
			return;
		} catch (InterruptedException localInterruptedException) {
			localInterruptedException.printStackTrace();
		}
	}

	@Override
	public void run() {
		super.run();
		mThreadFlag = true;
		long lastTime = System.currentTimeMillis();

		while (mThreadFlag) {
			long intervalTime = System.currentTimeMillis() - lastTime;

			if (intervalTime > FPS) {
				Canvas canvas = mSurfaceHolder.lockCanvas();

				try {
					logic(canvas);
					if (isDestoryed)
						try {
							mSurfaceHolder.unlockCanvasAndPost(canvas);
						} catch (Exception localException5) {
						}

				} catch (Exception e) {
					try {
						mSurfaceHolder.unlockCanvasAndPost(canvas);
					} catch (Exception localException2) {
					}
				}

				try {
					doDraw(canvas);
					try {
						mSurfaceHolder.unlockCanvasAndPost(canvas);
					} catch (Exception localException4) {
					}
				} catch (Exception localException3) {
					localException3.printStackTrace();
				}

				if (mCommonGiftList.size() == 0 && mWinningGiftList.size() == 0 && mNobilityMountList.size() == 0 && mGeneralMountList.size() == 0 && mBuyGuardList.size() == 0) {
					mThreadFlag = false;
				}
			} else {
				try {
					Thread.sleep(Math.max(2, FPS - intervalTime));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		Canvas canvas = mSurfaceHolder.lockCanvas();
		try {
			if (canvas != null) {
				canvas.drawColor(0, PorterDuff.Mode.CLEAR); // 清楚屏幕痕迹
				mSurfaceHolder.unlockCanvasAndPost(canvas);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void logic(Canvas canvas) {
		
		int upLimit = Math.min(2, mCommonGiftList.size());
		for (int i = 0; i < upLimit; i++) {
			BaseAnimationInfo viewInfo = mCommonGiftList.get(i);

			if (viewInfo.hasInitBitmap()) {
				viewInfo.setY(viewInfo.getY() - BaseAnimationInfo.STEPDISTANCE);
				if (viewInfo.isFinish()) {
					mCommonGiftList.remove(i);
					viewInfo.recycleBitmap();
					viewInfo = null;
					i--;
				}
			} else {
				android.os.Message msg = mHandler.obtainMessage();
				msg.obj = mCommonGiftList.get(i);
				if (i == 1 && mCommonGiftList.get(0).getTime() > 10) {
					mHandler.sendMessage(msg);
				} else if (i == 0) {
					mHandler.sendMessage(msg);
				}
			}
		}

		if (mNobilityMountList.size() > 0 && !isGeneralMountRunning) {
			BaseAnimationInfo viewInfo = mNobilityMountList.get(0);

			if (viewInfo.hasInitBitmap()) {
				if(viewInfo.getAnimationType() == BaseAnimationInfo.GENERAL_MOUNT){
					viewInfo.setY(viewInfo.getY() - BaseAnimationInfo.STEPDISTANCE);
				}else {
					viewInfo.setScale(viewInfo.getScale() + 0.05F);
				}
				if (viewInfo.isFinish()) {
					mNobilityMountList.remove(viewInfo);
					viewInfo.recycleBitmap();
					viewInfo = null;
				}
			} else {
				android.os.Message msg = mHandler.obtainMessage();
				msg.obj = mNobilityMountList.get(0);

				mHandler.sendMessage(msg);
			}
		} else if (mGeneralMountList.size() > 0) {
			BaseAnimationInfo viewInfo = mGeneralMountList.get(0);

			if (viewInfo.hasInitBitmap()) {
				isGeneralMountRunning = true;
				viewInfo.setY(viewInfo.getY() - BaseAnimationInfo.STEPDISTANCE);
				if (viewInfo.isFinish()) {
					mGeneralMountList.remove(viewInfo);
					viewInfo.recycleBitmap();
					viewInfo = null;
					isGeneralMountRunning = false;
				}
			} else {
				android.os.Message msg = mHandler.obtainMessage();
				msg.obj = mGeneralMountList.get(0);

				mHandler.sendMessage(msg);
			}
		}

		if (mBuyGuardList.size() > 0) {
			BaseAnimationInfo viewInfo = mBuyGuardList.get(0);

			if (viewInfo.hasInitBitmap()) {
				viewInfo.setScale(viewInfo.getScale() + 0.05F);

				if (viewInfo.isFinish()) {
					mBuyGuardList.remove(viewInfo);
					viewInfo.recycleBitmap();
					viewInfo = null;
				}
			} else {
				android.os.Message msg = mHandler.obtainMessage();
				msg.obj = viewInfo;

				mHandler.sendMessage(msg);
			}
		}
		
		if (mWinningGiftList.size() > 0) {
			BaseAnimationInfo viewInfo = mWinningGiftList.get(0);
			if (viewInfo.getBimap() != null) {
				viewInfo.setScale(viewInfo.getScale() + 0.05F);
				if (viewInfo.isFinish()) {
					mWinningGiftList.remove(viewInfo);
					viewInfo.recycleBitmap();
					viewInfo = null;
				}
			} else {
				android.os.Message msg = mHandler.obtainMessage();
				msg.obj = mWinningGiftList.get(0);

				mHandler.sendMessage(msg);
			}
		}

	}

	private void doDraw(Canvas canvas) {
		if(canvas == null){
			return;
		}
		canvas.drawColor(0, PorterDuff.Mode.CLEAR);
		
		try {
			int upLimit = Math.min(2, mCommonGiftList.size());
			for (int i = 0; i < upLimit; i++) {
				BaseAnimationInfo viewInfo = mCommonGiftList.get(i);

				if (viewInfo.hasInitBitmap()) {
					viewInfo.doneDraw(canvas, i);
				}
			}

			if (mWinningGiftList.size() > 0) {
				BaseAnimationInfo viewInfo = mWinningGiftList.get(0);

				if(viewInfo.isAnimRunning() || (mBuyGuardList.size() == 0 && mGeneralMountList.size() == 0 && mNobilityMountList.size() == 0)){
					viewInfo.doneDraw(canvas, 0);
					return;
				}
			}
			
			if (mBuyGuardList.size() > 0) {
				BaseAnimationInfo viewInfo = mBuyGuardList.get(0);
				
				if (viewInfo.isAnimRunning() || (mGeneralMountList.size() == 0 && mNobilityMountList.size() == 0)) {
					viewInfo.doneDraw(canvas, 0);
					
					return;
				}
			} 

			if (mGeneralMountList.size() > 0) {
				BaseAnimationInfo viewInfo = mGeneralMountList.get(0);
				
				if (viewInfo.isAnimRunning() || (mNobilityMountList.size() == 0)) {
					viewInfo.doneDraw(canvas, 0);
					
					return;
				}
			}
			
			if (mNobilityMountList.size() > 0) {
						 
				BaseAnimationInfo viewInfo = mNobilityMountList.get(0);
				if (viewInfo.hasInitBitmap()) {
					viewInfo.doneDraw(canvas, 0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
