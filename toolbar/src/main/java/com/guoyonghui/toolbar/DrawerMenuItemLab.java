package com.guoyonghui.toolbar;

import android.content.Context;

import java.util.ArrayList;

/**
 * 抽屉导航菜单项工厂（单例模式）
 */
public class DrawerMenuItemLab {

    /**
     * 工厂单例
     */
    private static DrawerMenuItemLab sMenuItemLab;

    /**
     * 菜单项集合
     */
    private ArrayList<DrawerMenuItem> mMenuItems;

    /**
     * 上下文
     */
    private Context mContext;

    private DrawerMenuItemLab(Context context) {
        mContext = context;
        mMenuItems = new ArrayList<>();

        for (String content : mContext.getResources().getStringArray(R.array.drawer_menu_contents)) {
            mMenuItems.add(new DrawerMenuItem(content));
        }
    }

    /**
     * 获取抽屉导航菜单项工厂单例
     *
     * @param context 上下文
     * @return 抽屉导航菜单项工厂单例
     */
    public static DrawerMenuItemLab getInstance(Context context) {
        if (sMenuItemLab == null) {
            sMenuItemLab = new DrawerMenuItemLab(context);
        }

        return sMenuItemLab;
    }

    public ArrayList<DrawerMenuItem> getMenuItems() {
        return mMenuItems;
    }

    public void setMenuItems(ArrayList<DrawerMenuItem> menuItems) {
        mMenuItems = menuItems;
    }

    public class DrawerMenuItem {
        private String content;

        public DrawerMenuItem(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return this.content;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

}
