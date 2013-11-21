package com.rd.bnote;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

@SuppressLint("SimpleDateFormat")
public class DateSimpleCursorAdapter extends SimpleCursorAdapter {

	@SuppressWarnings("deprecation")
	public DateSimpleCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
	}
	@Override
    public void setViewText(TextView v, String text) {
        if (v.getId() == R.id.note_list_item_date) {
            text = GetDate(Long.parseLong(text), "MM月dd日");
        }
        v.setText(text);
    }
	public static String GetDate(Long milliSeconds, String dateFormat) {
	    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
	    return formatter.format(milliSeconds);
	}
}
