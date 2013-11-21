package com.rd.bnote;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;

public class EditActivity extends Activity {
	
	private DBManager mDbManager;
	private EditText mEditText;
	private Note mNote;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note_edit);
		findViews();
		mDbManager = new DBManager(this);
		mNote = null;
		fillEdittext(savedInstanceState);
	}
	
	public void findViews() {
		mEditText = (EditText) findViewById(R.id.text_note);
	}
	
	public void fillEdittext(Bundle bundle) {
		int _id = 0;
		if (null == bundle) {
			Bundle extras = getIntent().getExtras();
			if (null != extras) {
				_id = extras.getInt(Note.FIELD__ID);
			}
		} else {
			_id = bundle.getInt(Note.FIELD__ID);
		}
		if (0 != _id) {
			mNote = new Note(_id);
			Cursor cursor = mDbManager.query(mNote);
			if (cursor.moveToNext()) {
				String content = cursor.getString(cursor.getColumnIndex(Note.FIELD_CONTENT));
				mEditText.setText(content);
				mEditText.setSelection(content.length());
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit, menu);
		return true;
	}
	
	@Override
	protected void onPause() {
		String content = mEditText.getText().toString();
		if (null == mNote) {
			if (null != content && !("".equals(content))) {
				mNote = new Note(content);
				mDbManager.add(mNote);
			}
		} else {
			mNote.setContent(content);
			mDbManager.update(mNote);
		}
		setResult(RESULT_OK);
		super.onPause();
	}

}
