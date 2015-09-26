package com.guoyonghui.listview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;

import com.guoyonghui.resourcelibrary.LogHelper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SceneDownloader extends HandlerThread implements Handler.Callback {

    private static final int MSG_DOWNLOAD_SCENE = 0;

    private Handler mHandler;

    private Handler mResponseHandler;

    private Map<ImageView, String> mRequestMap;

    private ExecutorService mExecutorService;

    private LruCache<String, Bitmap> mLruCache;

    private Callback mCallback;

    public interface Callback {
        void onSceneDownload(ImageView scene, Bitmap bitmap);
    }

    public SceneDownloader(Handler responseHandler, Callback callback) {
        super("SceneDownloader");

        mResponseHandler = responseHandler;
        mCallback = callback;

        mRequestMap = new HashMap<>();

        mExecutorService = Executors.newFixedThreadPool(10);

        int maxMemory = (int) (Runtime.getRuntime().maxMemory());
        int cacheSize = maxMemory / 4;
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
        LogHelper.trace("generate cache with size: " + cacheSize / 1024 + "KB");
    }

    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler(this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_DOWNLOAD_SCENE:
                mExecutorService.submit(new DownloadSceneRunnable((ImageView) msg.obj));
                break;

            default:
                break;
        }
        return false;
    }

    public void queueDownloadSceneRequest(ImageView scene, String sceneUrl) {
        mRequestMap.put(scene, sceneUrl);

        mHandler.obtainMessage(MSG_DOWNLOAD_SCENE, scene).sendToTarget();
        }

        public void clearQueue() {
            mHandler.removeMessages(MSG_DOWNLOAD_SCENE);
        }

        private class DownloadSceneRunnable implements Runnable {

            private ImageView mScene;

            public DownloadSceneRunnable(ImageView scene) {
                mScene = scene;
            }

            @Override
            public void run() {
            final String sceneUrl = mRequestMap.get(mScene);
            if (sceneUrl == null) {
                return;
            }

            final Bitmap bitmap;
            if (mLruCache.get(sceneUrl) == null) {
                bitmap = download(sceneUrl);
                mLruCache.put(sceneUrl, bitmap);

                LogHelper.trace("from network");
            } else {
                bitmap = mLruCache.get(sceneUrl);

                LogHelper.trace("from cache");
            }

            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (!sceneUrl.equals(mRequestMap.get(mScene)) || bitmap == null) {
                        return;
                    }

                    mRequestMap.remove(mScene);
                    mCallback.onSceneDownload(mScene, bitmap);
                }
            });
        }

        private Bitmap download(String sceneUrl) {
            Bitmap bitmap = null;
            HttpURLConnection conn = null;
            try {
                URL url = new URL(sceneUrl);
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(20 * 1000);
                conn.setReadTimeout(20 * 1000);
                conn.setDoInput(true);
                conn.connect();

                bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            return bitmap;
        }

    }

}
