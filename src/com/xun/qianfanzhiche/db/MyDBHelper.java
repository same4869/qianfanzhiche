package com.xun.qianfanzhiche.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
	public static final String DATA_BASE_NAME = "qianfanzhiche_db";
	public static final int DATA_BASE_VERSION = 1;
	public static final String TABLE_NAME = "fav";
	
	private SQLiteDatabase mDb;

	public MyDBHelper(Context context) {
		super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
