package com.rd.bnote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

	private DBHelper mDbHelper;
	private SQLiteDatabase mdb;
	
	public DBManager(Context context) {
		mDbHelper = new DBHelper(context);
		mdb = mDbHelper.getWritableDatabase();
	}
	public void add() {
	}
	public void update() {
	}
	public void delete() {
		
	}
	public void query() {
		
	}
}
