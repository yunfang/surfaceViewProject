package com.surface.surfaceviewproject.bean;

import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.surface.surfaceviewproject.R;

public class GiftAnimationInfo extends BaseAnimationInfo {

	private final int ANIMATION_FIRST_PHASE = 1;
	private final int ANIMATION_SECOND_PHASE = 2;
	private final int ANIMATION_THIRD_PHASE = 3;

	private Bitmap iconBitmap = null;

	private long startTime = 0;

//	private Message.CaromSendGiftModel giftModel;
//
//	private Message.SendGiftModel winningGiftModel;

	public GiftAnimationInfo(Context context, int startX, int startY, int type) {
		super(context, startX, startY, type);

//		giftModel = model;
	}

//	public GiftAnimationInfo(Context context, int startX, int startY, int type) {
//		super(context, startX, startY, type);
//
////		winningGiftModel = model;
//	}

	public GiftAnimationInfo(Context context, Bitmap icon, int startX, int startY, int type) {
		super(context, startX, startY, type);

		if (icon == null) {
			throw new NullPointerException("Gift icon is null");
		}

		iconBitmap = icon;
	}

	@Override
	public void initBitmap(int type) {
		if (hasInitBitmap) {
			return;
		}

		int height = 0;
		int width = 0;
		switch (type) {
		case COMMON_GIFT:
			width = (int) mContext.getResources().getDimension(R.dimen.animation_common_gift_width);
			height = (int) mContext.getResources().getDimension(R.dimen.animation_common_gift_height);
			// BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inJustDecodeBounds = true;
			// Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.nobility_173_anim_bg);
			// width = bmp.getWidth();
			scale = 0.0F;

			if (animView == null) {
				if(type == 1){
//					int nobilityLevel = giftModel.getData().getNobilityLevel();
//					String fromName = giftModel.getData().getByName();
//					long count = giftModel.getData().getCount();
//					long groupCount = giftModel.getData().getCarom();
//					String toName = null;
//					// if (giftModel.getData().getPriv() != 2) {
//					// }
//					toName = giftModel.getData().getToName();
//
////					GiftListResult.Gift gift = GiftUtils.getGift(giftModel.getData().getGiftId());
//					Bitmap giftIconBitmap = ImageCache.loadImageSync(gift.getPicUrl());
					animView = createGiftAnimView();
				}else if(type ==2){
//					List<String> fromRoleInfo = winningGiftModel.getData().getmFromRole();
//
//					int nobilityLevel = -1;
//					for(String roleInfo:fromRoleInfo){
//						if(roleInfo.contains("noble")){
//							nobilityLevel = Integer.parseInt(roleInfo.substring(5));
//							break;
//						}
//					}
//
//					String fromName = winningGiftModel.getData().getFrom().getNickName();
//					long count = winningGiftModel.getData().getGift().getCount();
//					GiftListResult.Gift gift = GiftUtils.getGift(winningGiftModel.getData().getGift().getId());
//					Bitmap giftIconBitmap = ImageCache.loadImageSync(gift.getPicUrl());
//					long groupCount = winningGiftModel.getData().getFrom().getCarom();
//					String toName = winningGiftModel.getData().getTo().getNickName();
					animView = createGiftAnimView();
				}
			}
			break;
		case WINNING_GIFT:
			height = (int) mContext.getResources().getDimension(R.dimen.animation_winning_gift_height);
			width = (int) mContext.getResources().getDimension(R.dimen.animation_winning_gift_width);

			scale = 0.0F;
			if (animView == null) {
				animView = createWinningAnimView();
			}
			break;
		}
		mMobileWidth = width;
		mMobileHeight = height;

		mGiftBitmap = getViewBitmap(animView, width, height);

		hasInitBitmap = true;
		if (mGiftBitmap == null) {
			isFinish = true;
		}
	}

	@Override
	public void recycleBitmap() {
		super.recycleBitmap();

		iconBitmap.recycle();
		iconBitmap = null;
	}

	@Override
	public void doneDraw(Canvas canvas, int sequence) {
		mSequence = sequence;
		canvas.save();

		switch (animationType) {
		case COMMON_GIFT:
			doneDrawCommonGift(canvas, sequence);
			break;
		case WINNING_GIFT:
			doneDrawWinningGift(canvas, sequence);
			break;
		}

		mTime += 0.1F;
		canvas.restore();
	}

	private void doneDrawCommonGift(Canvas canvas, int sequence) {
		isAnimRunning = true;
		if (mY <= startY - mMobileHeight * (2 - sequence) && startTime <= 0) {
			startTime = System.currentTimeMillis(); // 动画停靠起始时间
		}
		int r = (int) (mX + mMobileWidth);
		int b = mY + mMobileHeight;

		if (mPaint.getAlpha() >= 0 && mTime > 15) { // 停靠时间超过3秒
			int alpha = mPaint.getAlpha() - 5;
			if (alpha < 0.2) {
				alpha = 0;
				isFinish = true;
				return;
			}
			mPaint.setAlpha(alpha);
		}

		canvas.drawBitmap(this.mGiftBitmap, null, new RectF(mX, mY, r, b), mPaint);
	}

	private void doneDrawWinningGift(Canvas canvas, int sequence) {
		isAnimRunning = true;
		if (mTime < 2) {
			return;
		}

		if (scale >= 1.0F && mPaint.getAlpha() >= 0 && mTime > 15) {
			alpha = mPaint.getAlpha() - 5;
			if (alpha < 0) {
				alpha = 0;
				isFinish = true;
			}
			mPaint.setAlpha(alpha);
		}
		int tempX = (int) (mMobileWidth * scale / 2);
		int tempY = (int) (mMobileHeight * scale / 2);

		int r = (int) (mX + tempX);
		int b = (int) (mY + tempY);
		canvas.drawBitmap(this.mGiftBitmap, null, new RectF(mX - tempX, mY - tempY, r, b), mPaint);

	}

	private long getBetweenTime() {
		return System.currentTimeMillis() - startTime;
	}

	private View createGiftAnimView() {

		View animView = View.inflate(mContext, R.layout.layout_gift_anim_new, null);

		LinearLayout animLayout = (LinearLayout) animView.findViewById(R.id.iv_gift_bg);
		ImageView giftIcon = (ImageView) animView.findViewById(R.id.iv_gift_icon);
		TextView fromUserName = (TextView) animView.findViewById(R.id.tv_from_user_name);
		TextView toUserName = (TextView) animView.findViewById(R.id.tv_to_user_name);
		TextView giftCount = (TextView) animView.findViewById(R.id.tv_gift_count);
		TextView giftCountShadow = (TextView) animView.findViewById(R.id.tv_gift_count_shaow);
		TextView giftGroupCount = (TextView) animView.findViewById(R.id.tv_gift_group_count);
		TextView giftGroupCountShadow = (TextView) animView.findViewById(R.id.tv_gift_group_count_shaow);
		TextView evenSendLabel = (TextView) animView.findViewById(R.id.tv_even_send);
		TextView tvX = (TextView) animView.findViewById(R.id.tv_x);
		RelativeLayout evenSendLayout = (RelativeLayout) animView.findViewById(R.id.rl_even_send_layout);

		try {

			// 礼物图标
//			giftIcon.setImageBitmap(giftIconBitmap);
			giftIcon.setVisibility(View.VISIBLE);

			// 根据任务身份确定背景

			switch (0) {
			case -1:
//				animLayout.setBackgroundResource(R.drawable.nobility_poor_anim_bg);
//				fromUserName.setTextColor(Color.parseColor("#346883"));
//				toUserName.setTextColor(Color.parseColor("#346883"));
				break;
			case 0:
				animLayout.setBackgroundResource(R.drawable.nobility_176_anim_bg);
				fromUserName.setTextColor(Color.parseColor("#1a4c6f"));
				toUserName.setTextColor(Color.parseColor("#1a4c6f"));
				break;
			case 1:
				animLayout.setBackgroundResource(R.drawable.nobility_175_anim_bg);
				fromUserName.setTextColor(Color.parseColor("#4d1d6e"));
				toUserName.setTextColor(Color.parseColor("#4d1d6e"));

				break;
			case 2:
				animLayout.setBackgroundResource(R.drawable.nobility_174_anim_bg);
				fromUserName.setTextColor(Color.parseColor("#3b6f1f"));
				toUserName.setTextColor(Color.parseColor("#3b6f1f"));

				break;
			case 3:
				animLayout.setBackgroundResource(R.drawable.nobility_173_anim_bg);
				fromUserName.setTextColor(Color.parseColor("#004b6b"));
				toUserName.setTextColor(Color.parseColor("#004b6b"));

				break;
			case 4:
				animLayout.setBackgroundResource(R.drawable.nobility_172_anim_bg);
				fromUserName.setTextColor(Color.parseColor("#302883"));
				toUserName.setTextColor(Color.parseColor("#302883"));

				break;
			case 5:
				animLayout.setBackgroundResource(R.drawable.nobility_171_anim_bg);
				fromUserName.setTextColor(Color.parseColor("#8d1e47"));
				toUserName.setTextColor(Color.parseColor("#8d1e47"));

				break;
			case 6:
				animLayout.setBackgroundResource(R.drawable.nobility_170_anim_bg);
				fromUserName.setTextColor(Color.parseColor("#7d0c00"));
				toUserName.setTextColor(Color.parseColor("#7d0c00"));

				break;
			}

			// 设置礼物个数字体样式
			AssetManager assets = mContext.getAssets();
			Typeface typeface = Typeface.createFromAsset(assets, "fonts/GIFT-COUNT.TTF");
			giftCount.setTypeface(typeface);
			giftCount.setText(3 + "");
			giftCountShadow.setTypeface(typeface);
			giftCountShadow.setShadowLayer(5F, 1, 1, Color.parseColor("#0050bd"));
			giftCountShadow.setText(3 + "");

			// 设置连送组数字体样式
			if (3 > 1) {
				evenSendLayout.setVisibility(View.VISIBLE);
				giftGroupCount.setTypeface(typeface);
				giftGroupCountShadow.setTypeface(typeface);
				giftGroupCount.setText(3 + "");
				giftGroupCountShadow.setText(3 + "");
				evenSendLabel.setTypeface(typeface);
				tvX.setTypeface(typeface);
			} else {
				evenSendLayout.setVisibility(View.GONE);
			}

			// 设置送礼物人名称
			fromUserName.setText("牛人");

			// 设置收礼物人名称
			if ("sdf" != null) {
				toUserName.setText("sdf");
			} else {
				toUserName.setVisibility(View.GONE);
			}

			// setUserLevelIcon(userIconFirst, userIconSecond, giftModel.getData());

			return animView;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private View createWinningAnimView() {
		View animView = View.inflate(mContext, R.layout.layout_gift_winning_anim, null);

		ImageView giftMultipleImg = (ImageView) animView.findViewById(R.id.iv_gift_multiple_img);
		ImageView giftIcon = (ImageView) animView.findViewById(R.id.iv_gift_img);
		RelativeLayout giftDescripBg = (RelativeLayout) animView.findViewById(R.id.rl_send_gift_description);
		TextView fromUserName = (TextView) animView.findViewById(R.id.tv_from_user_name);
		ImageView userLeveIconFirst = (ImageView) animView.findViewById(R.id.iv_user_icon_first);
		ImageView userLeveIconSecond = (ImageView) animView.findViewById(R.id.iv_user_icon_second);
		ImageView giftMultiple = (ImageView) animView.findViewById(R.id.iv_winning_multiple);

		try {
//			int[] winCoin = giftModel.getData().getWinCoin();
//			Message.SendGiftModel.Data giftData = giftModel.getData();
//			if (winCoin != null && winCoin.length > 0) {
//				GiftListResult.Gift gift = GiftUtils.getGift(giftData.getGift().getId());
//
//				Bitmap giftBitmap = ImageCache.loadImageSync(gift.getPicUrl());
//				giftIcon.setImageBitmap(giftBitmap);
//				giftIcon.setVisibility(View.VISIBLE);

//				fromUserName.setText(giftData.getFrom().getNickName());

				// setUserLevelIcon(userLeveIconFirst, userLeveIconSecond, giftData);

//				int win = winCoin[0];
				switch (5000) {
				case 10:
					giftDescripBg.setBackgroundResource(R.drawable.winning_10_mulriple_orange);
					giftMultiple.setImageResource(R.drawable.winning_return_10_mulriple);
					giftMultipleImg.setImageResource(R.drawable.winning_10_multiple_coin);
					break;
				case 50:
					giftDescripBg.setBackgroundResource(R.drawable.winning_10_mulriple_orange);
					giftMultiple.setImageResource(R.drawable.winning_return_50_mulriple);
					giftMultipleImg.setImageResource(R.drawable.winning_10_multiple_coin);
					break;
				case 100:
					giftDescripBg.setBackgroundResource(R.drawable.winning_100_mulriple_blue);
					giftMultiple.setImageResource(R.drawable.winning_return_100_mulriple);
					giftMultipleImg.setImageResource(R.drawable.winning_100_multiple_coin);
					break;
				case 500:
					giftDescripBg.setBackgroundResource(R.drawable.winning_100_mulriple_blue);
					giftMultiple.setImageResource(R.drawable.winning_return_500_mulriple);
					giftMultipleImg.setImageResource(R.drawable.winning_100_multiple_coin);
					break;
				case 1000:
					giftDescripBg.setBackgroundResource(R.drawable.winning_1000_mulriple_red);
					giftMultiple.setImageResource(R.drawable.winning_return_1000_mulriple);
					giftMultipleImg.setImageResource(R.drawable.winning_1000_multiple_coin);
					break;
				case 5000:
					giftDescripBg.setBackgroundResource(R.drawable.winning_1000_mulriple_red);
					giftMultiple.setImageResource(R.drawable.winning_return_5000_mulriple);
					giftMultipleImg.setImageResource(R.drawable.winning_1000_multiple_coin);
					break;
				}

				return animView;
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean isAnimRunning() {
		return isAnimRunning;
	}
}
