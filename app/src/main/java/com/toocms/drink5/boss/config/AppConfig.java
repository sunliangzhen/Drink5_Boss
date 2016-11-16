package com.toocms.drink5.boss.config;

import org.xutils.x;

import cn.zero.android.common.util.PreferencesUtils;

/**
 * APP配置
 *
 * @date 2016/1/22 10:34
 */
public class AppConfig {

    /**
     * 主URL
     */
//    public static final String BASE_URL = "http://weika.toocms.com/index.php/Api/";
    public static final String BASE_URL = "http://drink-bossapi.toocms.com/index.php/";
    public static final String BASE_URL2 = "http://drink-workerapi.toocms.com/index.php/";
    public static final String BASE_URL4 = "http://drink-api.toocms.com/index.php/";

    public static final String UPDATE_URL = "http://www.wecomeshow.com/index.php/Api/Base/appVersion";

    // 当前聊天的用户名和头像
    public static final String CURRENT_USER = "current_user";
    public static final String CURRENT_NAME = "current_name";
    public static final String CURRENT_HEAD = "current_head";

    // 设置当前聊天的环信用户id
    public static void setCurrentChatUser(String username) {
        PreferencesUtils.putString(x.app(), CURRENT_USER, username);
    }

    // 设置当前聊天的环信用户昵称
    public static void setCurrentChatName(String name) {
        PreferencesUtils.putString(x.app(), CURRENT_NAME, name);
    }

    // 设置当前聊天的环信用户头像
    public static void setCurrentChatHead(String head) {
        PreferencesUtils.putString(x.app(), CURRENT_HEAD, head);
    }

    // 获取当前聊天的环信用户id
    public static String getCurrentChatUser() {
        return PreferencesUtils.getString(x.app(), CURRENT_USER, "");
    }

    // 获取当前聊天的环信用户昵称
    public static String getCurrentChatName() {
        return PreferencesUtils.getString(x.app(), CURRENT_NAME, "");
    }

    // 获取当前聊天的环信用户头像
    public static String getCurrentChatHead() {
        return PreferencesUtils.getString(x.app(), CURRENT_HEAD, "");
    }
}
