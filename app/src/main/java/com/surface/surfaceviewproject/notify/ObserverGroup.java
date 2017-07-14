package com.surface.surfaceviewproject.notify;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by CG on 14-4-1.
 * 一个组对象对应一个Activity对象
 * @author ll
 * @version 3.7.0
 */
public final class ObserverGroup {

    private ObserverGroupType mGroupType;

    private ObserverGroup(ObserverGroupType type) {
        mGroupType = type;
    }

    private ObserverGroupType getType() {
        return mGroupType;
    }

    // 检查使用
    /** 设置某一组类型的对象的最大数量 */
    private static Map<ObserverGroupType, Integer> mGroupDefMaxCount = new HashMap<ObserverGroupType, Integer>();
    /** 设置某一组类型的对象的set */
    private static Map<ObserverGroupType, Set<ObserverGroup>> mGroupMap = new HashMap<ObserverGroupType, Set<ObserverGroup>>();

    static void setGroupDefMaxCount(ObserverGroupType type, int count) {
        mGroupDefMaxCount.put(type, count);
    }

    private static void addGroup(ObserverGroup group) {
        int maxCount = 2;
        if (mGroupDefMaxCount.get(group.getType()) != null) {
            maxCount = mGroupDefMaxCount.get(group.getType());
        }

        Set<ObserverGroup> observerGroups = mGroupMap.get(group.getType());
        if (observerGroups == null) {
            observerGroups = new HashSet<ObserverGroup>(2);
            mGroupMap.put(group.getType(), observerGroups);
        }
        if (observerGroups.size() < maxCount) {
            observerGroups.add(group);
        } else {
            try {
				throw new IllegalArgumentException("repeat register, group = " + group.getType());
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }

    static void removeGroup(ObserverGroup group) {
        Set<ObserverGroup> observerGroups = mGroupMap.get(group.getType());
        if (observerGroups != null) {
            observerGroups.remove(group);
        }
    }

    private static ObserverGroup mConfirmPayDetailGroup;
    private static ObserverGroup mApplyDetailsGroup;
    private static ObserverGroup mClockGroup;
    private static ObserverGroup mCouponHomeGroup;
    private static ObserverGroup mCheckWorkGroup;
    private static ObserverGroup mHomeTabGroup;
    private static ObserverGroup mCouponOffGroup;
    private static ObserverGroup mCouNewGroup;
    private static ObserverGroup mLoginGroup;
    private static ObserverGroup mPayGroup;
    private static ObserverGroup mChatGroup;



    /**
     * createDrawLayoutGroup
     * @return ObserverGroup
     */
    public static ObserverGroup createConfirmPayDetailGroup() {
        mConfirmPayDetailGroup = new ObserverGroup(ObserverGroupType.MAIN_DRAWLAYOUT);
        addGroup(mConfirmPayDetailGroup);
        return mConfirmPayDetailGroup;
    }

    /**
     * getDrawLayoutGroup
     * @return ObserverGroup
     */
    public static ObserverGroup getConfirmPayDetailGroup() {
        return mConfirmPayDetailGroup;
    }


}
