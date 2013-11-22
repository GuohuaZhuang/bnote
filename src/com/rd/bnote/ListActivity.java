package com.rd.bnote;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
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
	protected static final String TAG = "BNOTE";
	protected static final int ID = 0x0103;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Remove title bar
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Remove notification bar
		// this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
		//     WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.note_list);
		findViews();
		mListView.setEmptyView(findViewById(R.id.empty));
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			openOptionsDialog();
			return true;
		case R.id.action_search:
			onSearchRequested();
			return true;
		case R.id.action_sync:
			dataSynchronous();
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@SuppressLint("NewApi")
	public void dataSynchronous() {
		final NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setTicker("Bnote同步笔记数据")
	        .setSmallIcon(R.drawable.ic_launcher)
	        .setContentTitle("Bnote同步笔记数据")
	        .setContentText("正在同步中……");
		Intent intent = new Intent(this, ListActivity.class);
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(contentIntent);
		mNotifyManager.notify(ID, mBuilder.build());
		
		new Thread(
		    new Runnable() {
		        @Override
		        public void run() {
		            int incr;
		            for (incr = 0; incr <= 100; incr+=20) {
	            		mBuilder.setContentText("已经同步了 " + (int)(((double)incr / 100.0) * 100) + "% 数据").setProgress(100, incr, false);
	                    mNotifyManager.notify(ID, mBuilder.build());
                        try {
                            Thread.sleep(2*1000);
                        } catch (InterruptedException e) {
                            Log.d(TAG, "sleep failure");
                        }
		            }
		        	jsonDataSynchronous();
		            mBuilder.setContentText("同步完成").setProgress(0,0,false);
		            mNotifyManager.notify(ID, mBuilder.build());
		        }
		    }
		).start();
	}
	
	public void jsonDataSynchronous() {
//		JSONParser jParser = new JSONParser();
	}
	
	public void openOptionsDialog() {
		new AlertDialog.Builder(ListActivity.this)
			.setTitle(R.string.about_title)
			.setMessage(R.string.about_msg)
			.setPositiveButton(R.string.about_ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			})
			.show();
	}
	
	public void showList() {
		Cursor cursor = mDbManager.query();
		DateSimpleCursorAdapter adapter = (DateSimpleCursorAdapter) mListView.getAdapter();
		if (null != adapter) {
			adapter.changeCursor(cursor);
			adapter.notifyDataSetChanged();
		} else {
			String[] from = new String[]{ Note.FIELD_SNAP, Note.FIELD_PUB_DATE };
			int[] to = new int[]{ R.id.note_list_item, R.id.note_list_item_date };
			adapter = new DateSimpleCursorAdapter(this, R.layout.note_list_item, cursor, from, to);
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
