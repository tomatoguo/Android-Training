package com.guoyonghui.listview;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

public class SceneActivity extends AppCompatActivity implements SceneDownloader.Callback {

    private static final String[] SCENE_IMAGE_URLS = new String[]{
            "https://ooo.0o0.ooo/2015/09/26/5606aa07c8aaf.jpg",
            "https://ooo.0o0.ooo/2015/09/26/5606aa090fb67.jpg",
            "https://ooo.0o0.ooo/2015/09/26/5606aa0927061.jpg",
            "https://ooo.0o0.ooo/2015/09/26/5606aa09b7ca3.jpg",
            "https://ooo.0o0.ooo/2015/09/26/5606aa09bb889.jpg",
            "https://ooo.0o0.ooo/2015/09/26/5606aa0a80f84.jpg",
            "https://ooo.0o0.ooo/2015/09/26/5606aa0a963ba.jpg",
            "https://ooo.0o0.ooo/2015/09/26/5606aa0b5e067.jpg",
            "https://ooo.0o0.ooo/2015/09/26/5606aa0b60559.jpg",
            "https://ooo.0o0.ooo/2015/09/26/5606aa0c28395.jpg",
            "https://ooo.0o0.ooo/2015/09/26/5606aa1b2a466.jpg",
            "https://ooo.0o0.ooo/2015/09/26/5606aa1c4a6cb.jpg",
            "https://ooo.0o0.ooo/2015/09/26/5606aa1c4c22e.jpg",
            "https://ooo.0o0.ooo/2015/09/26/5606aa1cc238e.jpg",
            "https://ooo.0o0.ooo/2015/09/26/5606aa1cf04e7.jpg",
            "https://ooo.0o0.ooo/2015/09/26/5606aa1d3bc9c.jpg",
            "https://ooo.0o0.ooo/2015/09/26/5606aa1d8ce80.jpg",
            "https://ooo.0o0.ooo/2015/09/26/5606aa1df3542.jpg",
            "https://ooo.0o0.ooo/2015/09/26/5606aa1e3d80e.jpg",
            " https://ooo.0o0.ooo/2015/09/26/5606aa1e696f2.jpg"
    };

    private ListView mSceneList;

    private SceneDownloader mSceneDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene);

        mSceneDownloader = new SceneDownloader(new Handler(), this);
        mSceneDownloader.start();

        mSceneList = (ListView) findViewById(R.id.scene_list);
        mSceneList.setAdapter(new SceneListAdapter(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mSceneDownloader.clearQueue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scene, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSceneDownload(ImageView scene, Bitmap bitmap) {
        scene.setImageBitmap(bitmap);
    }

    private class SceneListAdapter extends ArrayAdapter<String> {

        public SceneListAdapter(Context context) {
            super(context, 0, SCENE_IMAGE_URLS);
        }

        @Override
        public int getCount() {
            return SCENE_IMAGE_URLS.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder VH;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.scene_item, parent, false);

                VH = new ViewHolder();
                VH.scene = (ImageView) convertView.findViewById(R.id.scene);

                convertView.setTag(VH);
            } else {
                VH = (ViewHolder) convertView.getTag();
            }

            String sceneUrl = SCENE_IMAGE_URLS[position];

            mSceneDownloader.queueDownloadSceneRequest(VH.scene, sceneUrl);

            return convertView;
        }

        class ViewHolder {
            ImageView scene;
        }
    }

}
