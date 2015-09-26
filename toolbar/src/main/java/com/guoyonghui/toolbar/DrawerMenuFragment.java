package com.guoyonghui.toolbar;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.guoyonghui.resourcelibrary.LogHelper;

public class DrawerMenuFragment extends Fragment implements AdapterView.OnItemClickListener {

    /**
     * KEY - 当前被选中菜单项的位置
     */
    private static final String KEY_SELECT_POSITION = "com.guoyonghui.toolbar.KEY_SELECT_POSITION";

    /**
     * 菜单项列表
     */
    private ListView mDrawerMenu;

    /**
     * 回调接口实例
     */
    private OnDrawerMenuItemClickListener mCallback;

    /**
     * 回调接口，用于处理用户点击菜单项时的事件
     */
    public interface OnDrawerMenuItemClickListener {
        void onItemClick(int position, String description);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogHelper.trace();

        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogHelper.trace();

        View view = inflater.inflate(R.layout.fragment_drawer_menu, container, false);

        mDrawerMenu = (ListView) view.findViewById(R.id.drawer_menu);
        mDrawerMenu.setAdapter(new DrawerMenuAdapter(getActivity(), R.layout.drawer_menu_item, DrawerMenuItemLab.getInstance(getActivity().getApplicationContext()).getMenuItems()));
        mDrawerMenu.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        LogHelper.trace();

        try {
            mCallback = (OnDrawerMenuItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnDrawerMenuItemClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        LogHelper.trace();

        mCallback = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        LogHelper.trace();

        outState.putInt(KEY_SELECT_POSITION, ((DrawerMenuAdapter) mDrawerMenu.getAdapter()).getSelectedPosition());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        LogHelper.trace();

        if (savedInstanceState != null) {
            ((DrawerMenuAdapter) mDrawerMenu.getAdapter()).setSelectedPosition(savedInstanceState.getInt(KEY_SELECT_POSITION, 0));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogHelper.trace();

        if (position == ((DrawerMenuAdapter) mDrawerMenu.getAdapter()).getSelectedPosition()) {
            return;
        }

        ((DrawerMenuAdapter) mDrawerMenu.getAdapter()).setSelectedPosition(position);

        mCallback.onItemClick(position, DrawerMenuItemLab.getInstance(getActivity().getApplicationContext()).getMenuItems().get(position).getContent());
    }
}
