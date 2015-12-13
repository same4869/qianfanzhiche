package com.xun.qianfanzhiche.manager;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.xun.qianfanzhiche.common.Constant;
import com.xun.qianfanzhiche.utils.LogUtil;
import com.xun.qianfanzhiche.utils.ZhiCheSPUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

/**
 * 管理一些后台下载并保存在SD卡的任务
 * 
 * @author wangxun
 * 
 */
public class DownLoadManager {
	public final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/qianfanzhiche/";
	public final static String mFileName = "splash.jpg";

	private static DownLoadManager instance = null;

	private DownLoadManager() {

	}

	public static DownLoadManager getInstance() {
		if (instance == null) {
			syncInit();
		}
		return instance;
	}

	private static synchronized void syncInit() {
		if (instance == null) {
			instance = new DownLoadManager();
		}
	}

	private class DownLoadAsyncTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... arg0) {
			try {
				Bitmap mBitmap = BitmapFactory.decodeStream(getImageStream(ZhiCheSPUtil.getSplashImgUrl()));
				saveFile(mBitmap, mFileName);
			} catch (Exception e) {
				LogUtil.d(LogUtil.TAG, "获得远程img或保存错误");
				e.printStackTrace();
			}

			return null;
		}

	}

	// 下载最新的闪屏图片保存本地，下次启动的时候会使用
	public void startDownloadSplashPic(String url) {
		DownLoadAsyncTask downLoadAsyncTask = new DownLoadAsyncTask();
		downLoadAsyncTask.execute(url);
	}

	private byte[] getImage(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		InputStream inStream = conn.getInputStream();
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return readStream(inStream);
		}
		return null;
	}

	private InputStream getImageStream(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return conn.getInputStream();
		}
		return null;
	}

	private static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	private void saveFile(Bitmap bm, String fileName) throws IOException {
		File dirFile = new File(ALBUM_PATH);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}
		File myCaptureFile = new File(ALBUM_PATH + fileName);
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
	}

}
