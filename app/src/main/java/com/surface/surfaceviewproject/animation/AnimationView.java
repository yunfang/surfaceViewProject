package com.surface.surfaceviewproject.animation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

import com.surface.surfaceviewproject.DisplayUtils;
import com.surface.surfaceviewproject.bean.BaseAnimationInfo;
import com.surface.surfaceviewproject.bean.GiftAnimationInfo;
import com.surface.surfaceviewproject.bean.RoomAnimationInfo;
import com.surface.surfaceviewproject.notify.DataChangeNotification;
import com.surface.surfaceviewproject.notify.IssueKey;
import com.surface.surfaceviewproject.notify.ObserverGroup;
import com.surface.surfaceviewproject.notify.OnDataChangeObserver;


public class AnimationView extends AnimationBaseSurfaceView implements SurfaceHolder.Callback, OnDataChangeObserver {

	private DrawLiveAnimThread mThread = null;

	private boolean isCreated = false;

	private Context mContext;

	private HashMap<Long, String> carNameMap = new HashMap<Long, String>();

	private ObserverGroup mObserverGroup;

	private List<BaseAnimationInfo> mCommonGiftList = new ArrayList<BaseAnimationInfo>(2);
	private List<BaseAnimationInfo> mWinningGiftList = new ArrayList<BaseAnimationInfo>(2);
	private List<BaseAnimationInfo> mNobilityMountList = new ArrayList<BaseAnimationInfo>(2);
	private List<BaseAnimationInfo> mGeneralMountList = new ArrayList<BaseAnimationInfo>(2);
	private List<BaseAnimationInfo> mBuyGuardList = new ArrayList<BaseAnimationInfo>(2);

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (isCreated) {
				BaseAnimationInfo animInfo = (BaseAnimationInfo) msg.obj;

				if (animInfo != null) {
					animInfo.initBitmap(1);
				}
			}

		};
	};

	public AnimationView(Context context, AttributeSet attrs) {
		super(context, attrs);


		DataChangeNotification.getInstance().addObserver(IssueKey.MESSAGE_PARSE_GIFT_NOTIFY, this, ObserverGroup.getConfirmPayDetailGroup());
		DataChangeNotification.getInstance().addObserver(IssueKey.CHAT_WINDOW_INTEGRATED_MESSAGE, this, ObserverGroup.getConfirmPayDetailGroup());
		DataChangeNotification.getInstance().addObserver(IssueKey.OPEN_GIFT_ANIM, this, ObserverGroup.getConfirmPayDetailGroup());
		DataChangeNotification.getInstance().addObserver(IssueKey.MESSAGE_VIEW_SHOW_SWITCH, this, ObserverGroup.getConfirmPayDetailGroup());
		DataChangeNotification.getInstance().addObserver(IssueKey.CLOSE_GIFT_ANIM, this, ObserverGroup.getConfirmPayDetailGroup());

		mContext = context;

		/**
		 * 将assets中配置座驾名的文件读到内存中
		 */
		try {
			InputStream is = context.getResources().getAssets().open("carname");

			BufferedReader dr = new BufferedReader(new InputStreamReader(is));
			String lineStr = "";
			carNameMap.clear();
			while ((lineStr = dr.readLine()) != null) {
				String[] lineResult = lineStr.split("=");

				carNameMap.put(Long.parseLong(lineResult[0].trim()), lineResult[1].trim());
			}
			dr.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
//							String sendGiftMessage = "{\"action\":\"gift.notify\",\"data_d\":{\"from\":{\"_id\":102957,\"qd\":\"1000001\",\"nick_name\":\"微笑阳光abc\",\"priv\":5,\"finance\":{\"coin_spend_total\":5810677},\"vip\":1,\"memberLevel\":26,\"memberLevelPic\":\"http://static.51weibo.com/styles/images/level/rich/star-26.gif\"},\"from_role\":[\"noble3\",\"vip\"],\"to\":{\"_id\":51000049,\"finance\":{\"bean_count_total\":202427660,\"coin_spend_total\":120344},\"nick_name\":\"权重值100\",\"priv\":2},\"to_role\":[\"anchor\"],\"gift\":{\"_id\":418,\"name\":\"干杯\",\"count\":99,\"coin_price\":10},\"room_id\":51000049,\"win_coin\":[500],\"topsend\":{\"action\":\"gift.topsend\",\"data_d\":{\"rank_0\":{\"to_user\":51000049,\"from_user_name\":\"test11180\",\"gift_id\":408,\"to_user_name\":\"权重值100\",\"gift_name\":\"游艇\",\"gift_count\":1314,\"gift_pic\":\"/swf/gift/yacht.png\",\"from_user\":127670,\"timestamp\":1447990959271}}}}}";
//							Message.SendGiftModel sendGiftModel = JSONUtils.fromJsonString(sendGiftMessage, Message.SendGiftModel.class);
//							DataChangeNotification.getInstance().notifyDataChanged(IssueKey.MESSAGE_PARSE_GIFT_NOTIFY, sendGiftModel);
//
//							String caromModel = "{\"action\":\"gift.carom\",\"data_d\":{\"date\":\"20151206\",\"cost\":198000,\"to_name\":\"denglu3\",\"gift_url\":\"/swf/gift/light.png\",\"gift_id\":420,\"count\":1314,\"to_id\":101507,\"room\":51000049,\"carom\":1314,\"category_id\":5,\"by_name\":\"阿斯拉加德阿斯利\",\"gift_name\":\"荧光棒\",\"_id\":\"51010914_101507_420_66_1448962150886\",\"priv\":\"2\",\"by_id\":51010914,\"timestamp\":1448962150886,\"noble\":5}}";
//							Message.CaromSendGiftModel caromSendGiftModel = JSONUtils.fromJsonString(caromModel, Message.CaromSendGiftModel.class);
//							DataChangeNotification.getInstance().notifyDataChanged(IssueKey.MESSAGE_PARSE_GIFT_NOTIFY, caromSendGiftModel);
//							
//							String roomModel = "{\"action\":\"room.change\",\"data_d\":{\"nobilityInfo\":{\"level\":\"3\",\"type\":\"伯爵\"},\"badges\":{},\"s\":\"2\",\"car\":173,\"spend\":5810677.0,\"nick_name\":\"微笑阳光abc\",\"ip\":\"172.16.66.131\",\"_id\":\"102957\",\"priv\":\"5\",\"pic\":\"http://img.51weibo.com/45/5/102957_0_112112.jpg?v=112_112_1435137788509\",\"vip\":1,\"enterTime\":1448962379373}}";
//							Message.EnterRoomModel enterRoomModel = JSONUtils.fromJsonString(roomModel, Message.EnterRoomModel.class);
//							DataChangeNotification.getInstance().notifyDataChanged(IssueKey.CHAT_WINDOW_INTEGRATED_MESSAGE, enterRoomModel);
//							
//							String message = "{\"action\":\"room.buy_guard\",\"data_d\":{\"user_id\":101256,\"room_id\":101133,\"total_value\":0.02,\"guard_value\":0.02,\"nobility_value\":0.0,\"nobilityInfo\":{\"level\":3,\"type\":\"伯爵\"},\"nick_name\":\"禁止刷礼物\",\"memberLevel\":50,\"timestamp\":1448510263956}}";
//							Message.RoomBuyGuard roomBuyGuard = JSONUtils.fromJsonString(message, Message.RoomBuyGuard.class);
//							DataChangeNotification.getInstance().notifyDataChanged(IssueKey.CHAT_WINDOW_INTEGRATED_MESSAGE, roomBuyGuard);
						}

					}, 1000);
				}
			}
		}, 5000);

	}

	public void startAnimation(BaseAnimationInfo info) {
		if (isCreated) {
			if (!hasData()) {
				mThread = new DrawLiveAnimThread(mSurfaceHolder, mHandler, mCommonGiftList, mWinningGiftList, mNobilityMountList, mGeneralMountList, mBuyGuardList);
				if (!isMessageViewShow) {
					mThread.start();
				}
			}

			mThread.addData(info);
		}
	}

	private void clearDataList() {
		mCommonGiftList.clear();
		mWinningGiftList.clear();
		mNobilityMountList.clear();
		mGeneralMountList.clear();
		mBuyGuardList.clear();
	}

	private boolean hasData() {
		return false;
//		return mCommonGiftList.size() > 0 || mWinningGiftList.size() > 0 || mNobilityMountList.size() > 0 || mGeneralMountList.size() > 0 || mBuyGuardList.size() > 0;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		isCreated = true;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isCreated = false;

		if (mThread != null) {
			mThread.stopThread();
			clearDataList();
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		if (mThread != null) {
			mThread.setDestoryed(true);
			mThread = null;
			clearDataList();
		}
		super.onDetachedFromWindow();

	}

	private boolean isOpen = true;
	private boolean isMessageViewShow = false;

	@Override
	public void onDataChanged(IssueKey issue, Object o) {
		if (IssueKey.OPEN_GIFT_ANIM.equals(issue)) {
			isOpen = true;
			DataChangeNotification.getInstance().notifyDataChanged(IssueKey.UPDATE_GIFT_STATE, true);
		} else if (IssueKey.CLOSE_GIFT_ANIM.equals(issue)) {
			isOpen = false;
			DataChangeNotification.getInstance().notifyDataChanged(IssueKey.UPDATE_GIFT_STATE, false);
			mCommonGiftList.clear();
			mWinningGiftList.clear();
		} else if (IssueKey.MESSAGE_VIEW_SHOW_SWITCH.equals(issue)) {	// 聊天浮窗弹出时停止所有动画特效
			boolean isShow = (Boolean) o;

//			if (this.isMessageViewShow == isShow) {
//				return;
//			}

			if (isShow) {
				if (mThread != null) {
					mThread.stopThread();
					mThread = null;
				}
			} else {
				if (hasData()) {
					if (mThread != null && mThread.isStarted()) {
						mThread.stopThread();
						mThread = null;
					}

					getHandler().postDelayed(new Runnable() {

						@Override
						public void run() {
							mThread = new DrawLiveAnimThread(mSurfaceHolder, mHandler, mCommonGiftList, mWinningGiftList, mNobilityMountList, mGeneralMountList, mBuyGuardList);
							mThread.start();
						}

					}, 1000);
				}
			}

//			this.isMessageViewShow = isShow;
		} else if (IssueKey.MESSAGE_PARSE_GIFT_NOTIFY.equals(issue)) {
//			if (o == null) {
//				return;
//			}
//
//			if (!isOpen) {
//				return;
//			}

			int startY = 0;
			int startX = 0;

			int type = BaseAnimationInfo.COMMON_GIFT;
			if ((Boolean)o) { // 非连送
//				SendGiftModel giftModel = (SendGiftModel) o;
//				int[] winCoin = giftModel.getData().getWinCoin();
//
//				if (winCoin != null && winCoin.length > 0) { // 反奖信息，执行反奖动画
					startX = getWidth() / 2;
					startY = DisplayUtils.getHeightPixels() / 3;
					type = BaseAnimationInfo.WINNING_GIFT;

					startAnimation(new GiftAnimationInfo(mContext, startX, startY, type));
//				}
//
//				if(giftModel.getData().getGift().getCount() >= 66){
					type = BaseAnimationInfo.COMMON_GIFT;//    COMMON_GIFT
					startY = (int) (DisplayUtils.getHeightPixels() * 0.64F);
					startX = DisplayUtils.dp2px(15);
					startAnimation(new GiftAnimationInfo(mContext, startX, startY, type));
//				}

			} else{ // 连送
//				Message.CaromSendGiftModel giftModel = (CaromSendGiftModel) o;
//				Message.CaromSendGiftModel.Data giftModelData = giftModel.getData();
//				if (giftModelData == null) {
//					return;
//				}

//				if(giftModel.getData().getCost() >= 10000){		// 当连送礼物总金额超过10000是所有房间显示跑道消息
//					DataChangeNotification.getInstance().notifyDataChanged(IssueKey.MESSAGE_PARSE_MARQUEE_NOTIFY, giftModel);
//				}
				// 非当前房间连送礼物消息
//				if (LiveCommonDataCopy.getRoomId() != giftModel.getData().getRoom()) {
//					return;
//				}
				
			}
		} else if (IssueKey.CHAT_WINDOW_INTEGRATED_MESSAGE.equals(issue)) {
//			try {
//				if (o instanceof com.xiuba.lib.model.Message.EnterRoomModel) { // 进场消息
//					com.xiuba.lib.model.Message.EnterRoomModel message = (com.xiuba.lib.model.Message.EnterRoomModel) o;
//					if (message != null) {
//						com.xiuba.lib.model.Message.EnterRoomModel.Data data = message.getData();
//						if (data != null) {
//							long mountId = data.getMountId();
//							if (mountId > 0) {
//								String carName = carNameMap.get(mountId);
//								if(mountId >= 170 && mountId <= 176){		 // 贵族乘着贵族特有座驾
									int startX = getWidth() / 2;
									int startY = (int) (DisplayUtils.getHeightPixels() / 2);
//
									startAnimation(new RoomAnimationInfo(mContext, startX, startY, BaseAnimationInfo.NOBILITY_MOUNT));
//								}else {		// 普通座驾
//									int startX = 0;
//									int startY = (int) (DisplayUtils.getHeightPixels() * 0.64F);
//									startAnimation(new RoomAnimationInfo(mContext, startX, startY, BaseAnimationInfo.GENERAL_MOUNT, data, carName));
//								}
//							}
//						}
//					}
//				} else if (o instanceof Message.RoomBuyGuard) { // 开通守护消息
//					if (o != null) {
//						Message.RoomBuyGuard guardMsg = (RoomBuyGuard) o;
//
//						Message.RoomBuyGuard.Data data = guardMsg.getmData();
//						if (data != null) {
//							if (data != null) {
//								int startX = getWidth() / 2;
//								int startY = (int) (DisplayUtils.getHeightPixels() * 0.3F);
//
//								startAnimation(new GuardAnimationInfo(mContext, startX, startY, BaseAnimationInfo.GUARD_ANIM, guardMsg));
//							}
//						}
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
		}
	}

	public void destroyActivity() {
		if(mThread != null){
			mThread.stopThread();
			mThread = null;
		}
	}

//	public void setObserverGroup(ObserverGroup observerGroup) {
//		mObserverGroup = observerGroup;
//
//		if (mObserverGroup != null) {
//			DataChangeNotification.getInstance().addObserver(IssueKey.MESSAGE_PARSE_GIFT_NOTIFY, this, mObserverGroup);
//			DataChangeNotification.getInstance().addObserver(IssueKey.CHAT_WINDOW_INTEGRATED_MESSAGE, this, mObserverGroup);
//			DataChangeNotification.getInstance().addObserver(IssueKey.OPEN_GIFT_ANIM, this, ObserverGroup.getLiveGroup());
//			DataChangeNotification.getInstance().addObserver(IssueKey.MESSAGE_VIEW_SHOW_SWITCH, this, ObserverGroup.getLiveGroup());
//			DataChangeNotification.getInstance().addObserver(IssueKey.CLOSE_GIFT_ANIM, this, ObserverGroup.getLiveGroup());
//		}
//	}

}
