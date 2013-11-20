package com.rd.bnote;

public class Note {
	public int _id;
	public int pub_date;
	public String snap;
	public String content;
	
	public Note(String content) {
		this.content = content;
		int snap_len = content.length();
		snap_len = (snap_len > 20) ? 20 : snap_len;
		this.snap = content.substring(0, snap_len);
	}
}
