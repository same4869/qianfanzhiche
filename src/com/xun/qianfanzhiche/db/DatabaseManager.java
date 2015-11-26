package com.xun.qianfanzhiche.db;

import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.xun.qianfanzhiche.bean.CommunityItem;
import com.xun.qianfanzhiche.db.MyDBHelper.FavTable;
import com.xun.qianfanzhiche.utils.BmobUtil;

public class DatabaseManager {

	private static DatabaseManager instance;
	private MyDBHelper myDBHelper;
	private Context context;

	public synchronized static DatabaseManager getInstance(Context context) {
		if (instance == null) {
			instance = new DatabaseManager(context);
		}
		return instance;
	}

	private DatabaseManager(Context context) {
		myDBHelper = new MyDBHelper(context);
		this.context = context;
	}

	public static void destory() {
		if (instance != null) {
			instance.onDestory();
		}
	}

	public void onDestory() {
		instance = null;
		if (myDBHelper != null) {
			myDBHelper.close();
			myDBHelper = null;
		}
	}

	public void deleteFav(CommunityItem communityItem) {
		Cursor cursor = null;
		String where = FavTable.USER_ID + " = '" + BmobUtil.getCurrentUser(context).getObjectId() + "' AND " + FavTable.OBJECT_ID + " = '"
				+ communityItem.getObjectId() + "'";
		cursor = myDBHelper.query(MyDBHelper.TABLE_NAME, null, where, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			int isLove = cursor.getInt(cursor.getColumnIndex(FavTable.IS_LOVE));
			if (isLove == 0) {
				myDBHelper.delete(MyDBHelper.TABLE_NAME, where, null);
			} else {
				ContentValues cv = new ContentValues();
				cv.put(FavTable.IS_FAV, 0);
				myDBHelper.update(MyDBHelper.TABLE_NAME, cv, where, null);
			}
		}
		if (cursor != null) {
			cursor.close();
			myDBHelper.close();
		}
	}

	public boolean isLoved(CommunityItem communityItem) {
		Cursor cursor = null;
		String where = FavTable.USER_ID + " = '" + BmobUtil.getCurrentUser(context).getObjectId() + "' AND " + FavTable.OBJECT_ID + " = '"
				+ communityItem.getObjectId() + "'";
		cursor = myDBHelper.query(MyDBHelper.TABLE_NAME, null, where, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			if (cursor.getInt(cursor.getColumnIndex(FavTable.IS_LOVE)) == 1) {
				return true;
			}
		}
		return false;
	}

	public long insertFav(CommunityItem communityItem) {
		long uri = 0;
		Cursor cursor = null;
		String where = FavTable.USER_ID + " = '" + BmobUtil.getCurrentUser(context).getObjectId() + "' AND " + FavTable.OBJECT_ID + " = '"
				+ communityItem.getObjectId() + "'";
		cursor = myDBHelper.query(MyDBHelper.TABLE_NAME, null, where, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			ContentValues conv = new ContentValues();
			conv.put(FavTable.IS_FAV, 1);
			conv.put(FavTable.IS_LOVE, 1);
			myDBHelper.update(MyDBHelper.TABLE_NAME, conv, where, null);
		} else {
			ContentValues cv = new ContentValues();
			cv.put(FavTable.USER_ID, BmobUtil.getCurrentUser(context).getObjectId());
			cv.put(FavTable.OBJECT_ID, communityItem.getObjectId());
			cv.put(FavTable.IS_LOVE, communityItem.isMyLove() == true ? 1 : 0);
			cv.put(FavTable.IS_FAV, communityItem.isMyFav() == true ? 1 : 0);
			uri = myDBHelper.insert(MyDBHelper.TABLE_NAME, null, cv);
		}
		if (cursor != null) {
			cursor.close();
			myDBHelper.close();
		}
		return uri;
	}

	public List<CommunityItem> setFav(List<CommunityItem> lists) {
		Cursor cursor = null;
		if (lists != null && lists.size() > 0) {
			for (Iterator<CommunityItem> iterator = lists.iterator(); iterator.hasNext();) {
				CommunityItem content = (CommunityItem) iterator.next();
				String where = FavTable.USER_ID + " = '" + BmobUtil.getCurrentUser(context).getObjectId() + "' AND " + FavTable.OBJECT_ID + " = '"
						+ content.getObjectId() + "'";
				cursor = myDBHelper.query(MyDBHelper.TABLE_NAME, null, where, null, null, null, null);
				if (cursor != null && cursor.getCount() > 0) {
					cursor.moveToFirst();
					if (cursor.getInt(cursor.getColumnIndex(FavTable.IS_FAV)) == 1) {
						content.setMyFav(true);
					} else {
						content.setMyFav(false);
					}
					if (cursor.getInt(cursor.getColumnIndex(FavTable.IS_LOVE)) == 1) {
						content.setMyLove(true);
					} else {
						content.setMyLove(false);
					}
				}
			}
		}
		if (cursor != null) {
			cursor.close();
			myDBHelper.close();
		}
		return lists;
	}
}
