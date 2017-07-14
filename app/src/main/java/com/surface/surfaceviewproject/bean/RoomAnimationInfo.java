package com.surface.surfaceviewproject.bean;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.surface.surfaceviewproject.DisplayUtils;
import com.surface.surfaceviewproject.R;


public class RoomAnimationInfo extends BaseAnimationInfo {

//	private Message.EnterRoomModel.Data enterRoomModel = null;

	private String carName;

	public RoomAnimationInfo(Context context, int startX, int startY, int type) {
		super(context, startX, startY, type);

//		enterRoomModel = model;
//		this.carName = carName;
	}

//	public Message.EnterRoomModel.Data getEnterRoomModel() {
//		return enterRoomModel;
//	}

	@Override
	public void initBitmap(int type) {
		if (hasInitBitmap) {
			return;
		}

		int height = 0;
		int width = 0;
		switch (animationType) {
		case NOBILITY_MOUNT:
			height = (int) mContext.getResources().getDimension(R.dimen.animation_nobility_mount_height);
			width = (int) mContext.getResources().getDimension(R.dimen.animation_nobility_mount_width);
			scale = 0.0F;

			if (animView == null) {
				animView = createNobilityView(type);
			}
			break;
		case GENERAL_MOUNT:
			width = (int) mContext.getResources().getDimension(R.dimen.animation_general_mount_width);
			height = (int) mContext.getResources().getDimension(R.dimen.animation_general_mount_height);
			scale = 1.0F;

//			String carId = "car" + enterRoomModel.getMountId();
//			String nickName = enterRoomModel.getNickName();
			if (animView == null) {
				animView = createGeneralRoomAnim("11", "car11", "妞妞");
			}

			startX = (DisplayUtils.getWidthPixels() - width) / 2;
			mX = startX;

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
	public void doneDraw(Canvas canvas, int sequence) {
		isAnimRunning = true;
		mSequence = sequence;
		canvas.save();

		switch (animationType) {
		case NOBILITY_MOUNT:
			doneDrawNobility(canvas, sequence);
			break;
		case GENERAL_MOUNT:
			doneDrawGeneral(canvas, sequence);
			break;

		default:
			break;
		}
		mTime += 0.1F;

		canvas.restore();
	}

	private void doneDrawNobility(Canvas canvas, int sequence) {

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

	private void doneDrawGeneral(Canvas canvas, int sequence) {

		int r = (int) (mX + mMobileWidth);
		int b = mY + mMobileHeight;

		if (mPaint.getAlpha() >= 0 && mTime > 15) {
			int alpha = mPaint.getAlpha() - 5;
			if (alpha < 0) {
				alpha = 0;
				isFinish = true;
			}
			mPaint.setAlpha(alpha);
		}

		canvas.drawBitmap(this.mGiftBitmap, null, new RectF(mX, mY, r, b), mPaint);

	}

	@Override
	public void setY(int y) {
		if (Math.abs(y - startY) >= mMobileHeight * (2 - mSequence) * 0.6F) {
			mY = (int) (startY - mMobileHeight * (2 - mSequence) * 0.6F);
			return;
		}
		mY = y;
	}

	private View createGeneralRoomAnim(final String carId, String carName, String nickName) {
		try {
			// 座驾图+欢迎语（欢迎+这里是用户名+乘着+这里是座驾名+进场）
			int drawableID = mContext.getResources().getIdentifier(carId, "drawable", mContext.getPackageName());
			View animView = View.inflate(mContext, R.layout.layout_general_entran_anim, null);

			Drawable drawable = mContext.getResources().getDrawable(drawableID);

			TextView tvNickName = (TextView) animView.findViewById(R.id.tv_hint);
			ImageView ivIcon = (ImageView) animView.findViewById(R.id.iv_mount_icon);

			if (carName == null) {
				carName = "";
			}

			String source = "欢迎" + nickName + "<br/>乘着&nbsp;" + "<font color='#37f6e6'>" + carName + "</font>" + "&nbsp;进场";
			tvNickName.setText(Html.fromHtml(source));

			ivIcon.setImageDrawable(drawable);

			return animView;
		} catch (Exception e) {

		}

		return null;
	}

	private View createNobilityView(int type) {
//		NobilityInfo nobilityInfo = data.getNobilityInfo();
//		if (nobilityInfo != null) {
//			int level = nobilityInfo.getLevel();
//			String nobilityName = nobilityInfo.getType();
//			String nickName = data.getNickName();
			try {
				View animView = View.inflate(mContext, R.layout.layout_nobility_entran_anim, null);

				TextView tvNobilityName = (TextView) animView.findViewById(R.id.tv_nobility_level);
				TextView tvNickName = (TextView) animView.findViewById(R.id.tv_hint_bg);
				ImageView ivIcon = (ImageView) animView.findViewById(R.id.iv_nobility_icon);

//				if (nobilityName == null) {
//					nobilityName = "";
//				}
//
//				if (nickName == null) {
//					nickName = "";
//				}

				tvNobilityName.setText("牛牛");
				// 欢迎+贵族名称（见附表）+这里是用户名+进场
				tvNickName.setText("欢迎" +"牛牛" + "进场");

				switch (type) {
				case 0:
					ivIcon.setImageResource(R.drawable.nobility176);
					break;
				case 1:
					ivIcon.setImageResource(R.drawable.nobility175);
					break;
				case 2:
					ivIcon.setImageResource(R.drawable.nobility174);
					break;
				case 3:
					ivIcon.setImageResource(R.drawable.nobility173);
					break;
				case 4:
					ivIcon.setImageResource(R.drawable.nobility172);
					break;
				case 5:
					ivIcon.setImageResource(R.drawable.nobility171);
					break;
				case 6:
					ivIcon.setImageResource(R.drawable.nobility170);
					break;
				}

				return animView;
			} catch (Exception e) {
			}
//		}
		return null;
	}

	@Override
	public boolean isAnimRunning() {
		return isAnimRunning;
	}

}
