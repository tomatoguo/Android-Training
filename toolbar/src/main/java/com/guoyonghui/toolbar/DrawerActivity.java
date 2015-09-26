package com.guoyonghui.toolbar;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.guoyonghui.resourcelibrary.LogHelper;

public class DrawerActivity extends AppCompatActivity implements DrawerMenuFragment.OnDrawerMenuItemClickListener {

    private static final long DRAWER_CLOSE_DELAY_INTERVAL = 400;

    private static final String KEY_TOOLBAR_TITLE = "com.guoyonghui.toolbar.KEY_TOOLBAR_TITLE";

    private static final String KEY_TOOLBAR_BG_COLOR = "com.guoyonghui.toolbar.KEY_TOOLBAR_BG_COLOR";

    private static final String KEY_FAVORITE = "com.guoyonghui.toolbar.KEY_FAVORITE";

    private Toolbar mToolbar;

    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    private String mToolbarTitle;

    private int mToolbarBgColor;

    private boolean mIsFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogHelper.trace();

        if (savedInstanceState != null) {
            mToolbarTitle = savedInstanceState.getString(KEY_TOOLBAR_TITLE);
            mToolbarBgColor = savedInstanceState.getInt(KEY_TOOLBAR_BG_COLOR);
            mIsFavorite = savedInstanceState.getBoolean(KEY_FAVORITE, false);
        } else {
            mToolbarTitle = getResources().getStringArray(R.array.drawer_menu_contents)[0];
            mToolbarBgColor = R.color.material_Blue_800;
            mIsFavorite = false;
        }

        initViews();

        initToolbar();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        LogHelper.trace();

        outState.putString(KEY_TOOLBAR_TITLE, mToolbarTitle);
        outState.putInt(KEY_TOOLBAR_BG_COLOR, mToolbarBgColor);
        outState.putBoolean(KEY_FAVORITE, mIsFavorite);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        LogHelper.trace();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem favoriteMenuItem = menu.findItem(R.id.action_favorite);

        favoriteMenuItem.setIcon(mIsFavorite ? R.drawable.ic_favorite_white_24dp : R.drawable.ic_favorite_border_white_24dp);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        LogHelper.trace();

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LogHelper.trace();

        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                break;
            case R.id.action_favorite:
                mIsFavorite = !mIsFavorite;
                invalidateOptionsMenu();
                break;
            case R.id.action_settings:
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int position, String description) {
        LogHelper.trace();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tc = fm.beginTransaction();

        DrawerContentFragment fragment = DrawerContentFragment.newInstance(description);
        tc.replace(R.id.drawer_content_container, fragment);

        tc.commitAllowingStateLoss();

        mToolbarTitle = description;
        switch (position) {
            case 0:
                mToolbarBgColor = R.color.material_Blue_800;
                break;
            case 1:
                mToolbarBgColor = R.color.material_Red_700;
                break;
            case 2:
                mToolbarBgColor = R.color.material_Green_800;
                break;
            case 3:
                mToolbarBgColor = R.color.material_Orange_700;
                break;

            default:
                break;
        }

        configToolbar();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        }, DRAWER_CLOSE_DELAY_INTERVAL);
    }

    /**
     * 初始化视图，添加抽屉导航菜单fragment以及相应菜单项所对应的内容fragment
     */
    private void initViews() {
        LogHelper.trace();

        mToolbar = (Toolbar) findViewById(R.id.toolbar_layout);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tc = fm.beginTransaction();

        DrawerMenuFragment drawerMenuFragment = (DrawerMenuFragment) fm.findFragmentById(R.id.drawer_menu_container);
        if (drawerMenuFragment == null) {
            drawerMenuFragment = new DrawerMenuFragment();
            tc.add(R.id.drawer_menu_container, drawerMenuFragment);
        }

        DrawerContentFragment drawerContentFragment = (DrawerContentFragment) fm.findFragmentById(R.id.drawer_content_container);
        if (drawerContentFragment == null) {
            drawerContentFragment = DrawerContentFragment.newInstance(getResources().getStringArray(R.array.drawer_menu_contents)[0]);
            tc.add(R.id.drawer_content_container, drawerContentFragment);
        }

        tc.commitAllowingStateLoss();

    }

    /**
     * 初始化Toolbar
     */
    private void initToolbar() {
        LogHelper.trace();

        configToolbar();

        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    /**
     * 配置Toolbar的标题和背景颜色
     */
    private void configToolbar() {
        LogHelper.trace();

        mToolbar.setTitle(mToolbarTitle);
        mToolbar.setBackgroundResource(mToolbarBgColor);
    }

}
