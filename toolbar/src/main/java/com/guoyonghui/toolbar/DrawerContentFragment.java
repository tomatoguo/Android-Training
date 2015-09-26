package com.guoyonghui.toolbar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guoyonghui.resourcelibrary.LogHelper;

public class DrawerContentFragment extends Fragment {

    public static final String EXTRA_CONTENT = "com.guoyonghui.toolbar.EXTRA_CONTENT";

    private String mContent;

    /**
     * 创建DrawerContentFragment实例，并附加content参数
     *
     * @param content fragment标题内容
     * @return DrawerContentFragment实例
     */
    public static DrawerContentFragment newInstance(String content) {
        LogHelper.trace();

        Bundle args = new Bundle();
        args.putString(EXTRA_CONTENT, content);

        DrawerContentFragment fragment = new DrawerContentFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LogHelper.trace();

        mContent = getArguments().getString(EXTRA_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogHelper.trace();

        View view = inflater.inflate(R.layout.fragment_drawer_content, container, false);

        if (mContent != null) {
            TextView contentTextView = (TextView) view.findViewById(R.id.drawer_content);
            contentTextView.setText(mContent);
        }

        return view;
    }
}
