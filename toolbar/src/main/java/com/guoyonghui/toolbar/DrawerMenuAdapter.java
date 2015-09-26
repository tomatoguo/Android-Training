package com.guoyonghui.toolbar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DrawerMenuAdapter extends ArrayAdapter<DrawerMenuItemLab.DrawerMenuItem> {

    private int mSelectedPosition = 0;

    private int mMenuItemBgResource;

    private ArrayList<DrawerMenuItemLab.DrawerMenuItem> mMenuItems;

    public DrawerMenuAdapter(Context context, int resource, ArrayList<DrawerMenuItemLab.DrawerMenuItem> objects) {
        super(context, resource, objects);

        mMenuItemBgResource = resource;
        mMenuItems = objects;
    }

    @Override
    public int getCount() {
        return mMenuItems.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder VH;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mMenuItemBgResource, parent, false);

            VH = new ViewHolder();
            VH.contentTextView = (TextView) convertView.findViewById(R.id.menu_item_content);

            convertView.setTag(VH);
        } else {
            VH = (ViewHolder) convertView.getTag();
        }

        DrawerMenuItemLab.DrawerMenuItem menuItem = mMenuItems.get(position);

        VH.contentTextView.setText(menuItem.getContent());
        VH.contentTextView.setTextColor(getContext().getResources().getColor(position == mSelectedPosition ? android.R.color.white : android.R.color.darker_gray));
        VH.contentTextView.setBackgroundResource(position == mSelectedPosition ? android.R.color.darker_gray : android.R.color.white);

        return convertView;
    }

    public void setSelectedPosition(int selectedPosition) {
        mSelectedPosition = selectedPosition;

        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return mSelectedPosition;
    }

    /**
     * 使用ViewHolder模式优化ListView加载效率
     */
    private class ViewHolder {
        private TextView contentTextView;
    }

}
