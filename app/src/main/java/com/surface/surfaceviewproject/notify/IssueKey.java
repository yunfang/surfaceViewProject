package com.surface.surfaceviewproject.notify;

/**
 * Created by CG on 14-1-3.
 * 订阅的主题
 * @author ll
 * @version 3.4.0
 */

public enum IssueKey {

	/**
	 * 关闭ConfirmPayDetailActivity
	 */
	MESSAGE_PARSE_GIFT_NOTIFY,
	/**
	 * 面签刮起通知，
	 */
	CHAT_WINDOW_INTEGRATED_MESSAGE,

	/**
	 * 在打卡页面定位的回调
	 */
	OPEN_GIFT_ANIM,

	/**
	 * 没有开启定位（禁用）
	 */
	MESSAGE_VIEW_SHOW_SWITCH,

	CLOSE_GIFT_ANIM,


	UPDATE_GIFT_STATE;


}
