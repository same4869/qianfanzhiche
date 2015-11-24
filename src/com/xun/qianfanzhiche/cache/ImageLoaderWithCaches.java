package com.xun.qianfanzhiche.cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.widget.AbsListView;
import android.widget.ImageView;

public class ImageLoaderWithCaches {
	private AbsListView listView;
	private LruCache<String, Bitmap> mMemoryCaches;
	private Set<ASyncDownloadImage> mTask;
	private DiskLruCache mDiskCaches;
	private List<String> imgUrls;

	public ImageLoaderWithCaches(Context context, AbsListView listView, List<String> imgUrls) {
		this.listView = listView;
		this.imgUrls = imgUrls;
		mTask = new HashSet<ImageLoaderWithCaches.ASyncDownloadImage>();
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;
		mMemoryCaches = new LruCache<String, Bitmap>(cacheSize) {

			@SuppressLint("NewApi")
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getByteCount();
			}

		};

		File cacheDir = getFileCache(context, "disk_caches");
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		try {
			mDiskCaches = DiskLruCache.open(cacheDir, 1, 1, 10 * 1024 * 1024);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Bitmap getBitmapFromMemoryCaches(String url) {
		return mMemoryCaches.get(url);
	}

	public void addBitmapToMemoryCaches(String url, Bitmap bitmap) {
		if (getBitmapFromMemoryCaches(url) == null) {
			mMemoryCaches.put(url, bitmap);
		}
	}

	public void setImgUrls(List<String> imgUrls) {
		this.imgUrls = imgUrls;
	}

	public void showImage(String url, ImageView imageView, int defaultImg) {
		if (url == null) {
			imageView.setImageResource(defaultImg);
			return;
		}
		Bitmap bitmap = getBitmapFromMemoryCaches(url);
		if (bitmap == null) {
			imageView.setImageResource(defaultImg);
		} else {
			imageView.setImageBitmap(bitmap);
		}
	}

	@SuppressLint("NewApi")
	private File getFileCache(Context context, String cacheFileName) {
		String cachePath;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
			cachePath = context.getExternalCacheDir().getPath();
		} else {
			cachePath = context.getCacheDir().getPath();
		}
		return new File(cachePath + File.separator + cacheFileName);
	}

	private static Bitmap getBitmapFromUrl(String urlString) {
		Bitmap bitmap;
		InputStream is = null;
		try {
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			is = new BufferedInputStream(conn.getInputStream());
			bitmap = BitmapFactory.decodeStream(is);
			conn.disconnect();
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (IOException e) {
			}
		}
		return null;
	}

	private static boolean getBitmapUrlToStream(String urlString, OutputStream outputStream) {
		HttpURLConnection urlConnection = null;
		BufferedOutputStream out = null;
		BufferedInputStream in = null;
		try {
			final URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
			out = new BufferedOutputStream(outputStream, 8 * 1024);
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			return true;
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void cancelAllTask() {
		if (mTask != null) {
			for (ASyncDownloadImage task2 : mTask) {
				task2.cancel(false);
			}
		}
	}

	public String toMD5String(String key) {
		String cacheKey;
		try {
			final MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(key.getBytes());
			cacheKey = bytesToHexString(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			cacheKey = String.valueOf(key.hashCode());
		}
		return cacheKey;
	}

	private String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	public void flushCache() {
		if (mDiskCaches != null) {
			try {
				mDiskCaches.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void loadImagesWithUrl(ImageView imageView, String url) {
		Bitmap bitmap = getBitmapFromMemoryCaches(url);
		if (bitmap == null) {
			ASyncDownloadImage task = new ASyncDownloadImage(url, imageView);
			mTask.add(task);
			task.execute(url);
		} else {
			imageView.setImageBitmap(bitmap);
		}
	}

	public void loadImages(int start, int end) {
		for (int i = start; i < end; i++) {
			String url = imgUrls.get(i);
			if (url.equals("null")) {
				continue;
			}
			Bitmap bitmap = getBitmapFromMemoryCaches(url);
			if (bitmap == null) {
				ASyncDownloadImage task = new ASyncDownloadImage(url, null);
				mTask.add(task);
				task.execute(url);
			} else {
				ImageView imageView = (ImageView) listView.findViewWithTag(url);
				imageView.setImageBitmap(bitmap);
			}
		}
	}

	private class ASyncDownloadImage extends AsyncTask<String, Void, Bitmap> {
		private String urlString;
		private ImageView imageView;

		public ASyncDownloadImage(String url, ImageView imageView) {
			this.urlString = url;
			this.imageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(String... arg0) {
			urlString = arg0[0];
			FileDescriptor fileDescriptor = null;
			FileInputStream fileInputStream = null;
			DiskLruCache.Snapshot snapShot = null;
			String key = toMD5String(urlString);

			try {
				snapShot = mDiskCaches.get(key);
				if (snapShot == null) {
					DiskLruCache.Editor editor = mDiskCaches.edit(key);
					if (editor != null) {
						OutputStream outputStream = editor.newOutputStream(0);
						if (getBitmapUrlToStream(urlString, outputStream)) {
							editor.commit();
						} else {
							editor.abort();
						}
					}
					snapShot = mDiskCaches.get(key);
				}
				if (snapShot != null) {
					fileInputStream = (FileInputStream) snapShot.getInputStream(0);
					fileDescriptor = fileInputStream.getFD();
				}
				Bitmap bitmap = null;
				if (fileDescriptor != null) {
					bitmap = decodeSuitableBitmap(fileDescriptor, null, 800, 800);
				}
				if (bitmap != null) {
					addBitmapToMemoryCaches(arg0[0], bitmap);
				}
				return bitmap;
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fileDescriptor == null && fileInputStream != null) {
					try {
						fileInputStream.close();
					} catch (IOException e) {
					}
				}
			}
			return null;

		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (imageView == null) {
				imageView = (ImageView) listView.findViewWithTag(urlString);
			}
			if (imageView != null && result != null) {
				imageView.setImageBitmap(result);
			}
			mTask.remove(this);
		}

	}

	public static int getInSampleSize(BitmapFactory.Options options, int targetWidth, int targetHeight) {
		// 原始图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > targetHeight || width > targetWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRate = Math.round((float) height / (float) targetHeight);
			final int widthRate = Math.round((float) width / (float) targetWidth);
			inSampleSize = heightRate < widthRate ? heightRate : widthRate;
		}
		return inSampleSize;
	}

	public static Bitmap decodeSuitableBitmap(FileDescriptor fd, Rect rect, int targetWidth, int targetHeight) {
		// 空手套白狼
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFileDescriptor(fd, rect, options);
		// 计算合适的inSampleSize
		options.inSampleSize = getInSampleSize(options, targetWidth, targetHeight);
		// 加载到内存
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFileDescriptor(fd, rect, options);
	}

}
