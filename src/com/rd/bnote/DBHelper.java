package com.rd.bnote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "note.db";
	private static final String DATABASE_TABLE = "note";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_TABLE_DROP = "DROP TABLE IF EXISTS " + DATABASE_TABLE;
	private static final String DATABASE_TABLE_CREATE = "CREATE TABLE " + DATABASE_TABLE + "("
	    + "_id INTEGER PRIMARY KEY,"
	    + "pub_date INTEGER NOT NULL DEFAULT (strftime('%s','now') * 1000),"
	    + "snap TEXT NOT NULL DEFAULT '',"
	    + "content TEXT NOT NULL DEFAULT '',"
	    + ");";
	
	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_TABLE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DATABASE_TABLE_DROP);
		onCreate(db);
	}
}
