package com.rd.bnote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

	private DBHelper mDbHelper;
	private SQLiteDatabase mdb;
	
	public DBManager(Context context) {
		mDbHelper = new DBHelper(context);
		mdb = mDbHelper.getWritableDatabase();
	}
	public void closeDBManager() {
		mdb.close();
	}
	public void add(Note note) {
		ContentValues values = new ContentValues();
		values.put(Note.FIELD_SNAP, note.snap);
		values.put(Note.FIELD_CONTENT, note.content);
		mdb.insert(DBHelper.DATABASE_TABLE, null, values);
	}
	public void update(Note note) {
		ContentValues values = new ContentValues();
		values.put(Note.FIELD_SNAP, note.snap);
		values.put(Note.FIELD_CONTENT, note.content);
		mdb.update(DBHelper.DATABASE_TABLE, values, Note.FIELD__ID + "=?", new String[]{(String.valueOf(note._id))});
	}
	public void delete(Note note) {
		mdb.delete(DBHelper.DATABASE_TABLE, Note.FIELD__ID + "=?", new String[]{(String.valueOf(note._id))});
	}
	public Cursor query() {
		String orderBy = Note.FIELD_PUB_DATE + " DESC";
		String[] columns = new String[]{Note.FIELD__ID, Note.FIELD_PUB_DATE, Note.FIELD_SNAP};
		Cursor cursor = mdb.query(DBHelper.DATABASE_TABLE, columns , null, null, null, null, orderBy);
		return cursor;
	}
	public Cursor query(Note note) {
		String[] columns = new String[]{Note.FIELD__ID, Note.FIELD_PUB_DATE, Note.FIELD_CONTENT};
		String selection = Note.FIELD__ID + "=?";
		String[] selectionArgs = new String[]{ String.valueOf(note._id) };
		Cursor cursor = mdb.query(DBHelper.DATABASE_TABLE, columns, selection, selectionArgs, null, null, null);
		return cursor;
	}
	public Cursor query(String s) {
		String orderBy = Note.FIELD_PUB_DATE + " DESC";
		String selection = Note.FIELD_CONTENT + " LIKE ?";
		String[] selectionArgs = new String[]{ "%" + s + "%" };
		String[] columns = new String[]{Note.FIELD__ID, Note.FIELD_PUB_DATE, Note.FIELD_SNAP};
		Cursor cursor = mdb.query(DBHelper.DATABASE_TABLE, columns , selection, selectionArgs, null, null, orderBy);
		return cursor;
	}
}
