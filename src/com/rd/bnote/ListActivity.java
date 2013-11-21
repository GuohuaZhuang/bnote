package com.rd.bnote;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ListActivity extends Activity {

	private DBManager mDbManager;
	private ListView mListView;
	protected static final int MENU_DELETE = Menu.FIRST;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_list);
		findViews();
		mDbManager = new DBManager(this);
	}
	
	public void findViews() {
		mListView = (ListView) findViewById(R.id.list_note);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Intent intent = new Intent(ListActivity.this, EditActivity.class);
				intent.putExtra(Note.FIELD__ID, (int)id);
				startActivityForResult(intent, RESULT_FIRST_USER);
			}
		});
		mListView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				menu.add(0, MENU_DELETE, 0, "删除");
				menu.setHeaderTitle("确定要删除吗？");
			}
		});
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case MENU_DELETE:
			mDbManager.delete(new Note((int) info.id));
			showList();
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	@Override
	protected void onDestroy() {
		mDbManager.closeDBManager();
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		showList();
		super.onResume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public void showList() {
		Cursor cursor = mDbManager.query();
		SimpleCursorAdapter adapter = (SimpleCursorAdapter) mListView.getAdapter();
		if (null != adapter) {
			adapter.changeCursor(cursor);
			adapter.notifyDataSetChanged();
		} else {
			String[] from = new String[]{ Note.FIELD_SNAP, Note.FIELD_PUB_DATE };
			int[] to = new int[]{ R.id.note_list_item, R.id.note_list_item_date };
			adapter = new SimpleCursorAdapter(this, R.layout.note_list_item, cursor, from, to);
			mListView.setAdapter(adapter);
		}
	}
	
	public void addNote(View view) {
		Intent intent = new Intent(this, EditActivity.class);
		startActivityForResult(intent, RESULT_FIRST_USER);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
