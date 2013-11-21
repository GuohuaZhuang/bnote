package com.rd.bnote;

public class Note {
	
	public static final String FIELD__ID = "_id";
	public static final String FIELD_PUB_DATE = "pub_date";
	public static final String FIELD_SNAP = "snap";
	public static final String FIELD_CONTENT = "content";
	public static final int MAX_SNAP_LEN = 50;
	
	public int _id;
	public int pub_date;
	public String snap;
	public String content;
	
	public Note(int _id) {
		this._id = _id;
	}
	
	public Note(String content) {
		setContent(content);
	}
	
	public void setContent(String content) {
		this.content = content;
		int snap_len = content.length();
		snap_len = (snap_len > MAX_SNAP_LEN) ? MAX_SNAP_LEN : snap_len;
		this.snap = content.substring(0, snap_len);
	}
}
