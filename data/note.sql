CREATE TABLE android_metadata (locale TEXT);

CREATE TABLE data(
	_id INTEGER PRIMARY KEY,
	mime_type TEXT NOT NULL,
	note_id INTEGER NOT NULL DEFAULT 0,
	created_date INTEGER NOT NULL DEFAULT (strftime('%s','now') * 1000),
	modified_date INTEGER NOT NULL DEFAULT (strftime('%s','now') * 1000),
	content TEXT NOT NULL DEFAULT '',
	data1 INTEGER,
	data2 INTEGER,
	data3 TEXT NOT NULL DEFAULT '',
	data4 TEXT NOT NULL DEFAULT '',
	data5 TEXT NOT NULL DEFAULT ''
);

CREATE TABLE note(
	_id INTEGER PRIMARY KEY,
	parent_id INTEGER NOT NULL DEFAULT 0,
	alert_date INTEGER NOT NULL DEFAULT 0,
	bg_color_id INTEGER NOT NULL DEFAULT 0,
	created_date INTEGER NOT NULL DEFAULT (strftime('%s','now') * 1000),
	has_attachment INTEGER NOT NULL DEFAULT 0,
	modified_date INTEGER NOT NULL DEFAULT (strftime('%s','now') * 1000),
	notes_count INTEGER NOT NULL DEFAULT 0,
	snippet TEXT NOT NULL DEFAULT '',
	type INTEGER NOT NULL DEFAULT 0,
	widget_id INTEGER NOT NULL DEFAULT 0,
	widget_type INTEGER NOT NULL DEFAULT -1,
	sync_id INTEGER NOT NULL DEFAULT 0,
	local_modified INTEGER NOT NULL DEFAULT 0,
	origin_parent_id INTEGER NOT NULL DEFAULT 0,
	gtask_id TEXT NOT NULL DEFAULT '',
	version INTEGER NOT NULL DEFAULT 0
);

CREATE INDEX note_id_index ON data(note_id);

CREATE TRIGGER decrease_folder_count_on_delete  
	AFTER DELETE ON note 
	BEGIN   UPDATE note   SET notes_count=notes_count-1  WHERE _id=old.parent_id  AND notes_count>0; END;

CREATE TRIGGER decrease_folder_count_on_update  
	AFTER UPDATE OF parent_id ON note 
	BEGIN   UPDATE note   SET notes_count=notes_count-1  WHERE _id=old.parent_id  AND notes_count>0; END;

CREATE TRIGGER delete_data_on_delete  
	AFTER DELETE ON note 
	BEGIN  DELETE FROM data   WHERE note_id=old._id; END;

CREATE TRIGGER folder_delete_notes_on_delete  
	AFTER DELETE ON note 
	BEGIN  DELETE FROM note   WHERE parent_id=old._id; END;

CREATE TRIGGER folder_move_notes_on_trash  
	AFTER UPDATE ON note WHEN new.parent_id=-3 
	BEGIN  UPDATE note   SET parent_id=-3  WHERE parent_id=old._id; END;

CREATE TRIGGER increase_folder_count_on_insert  
	AFTER INSERT ON note 
	BEGIN   UPDATE note   SET notes_count=notes_count + 1  WHERE _id=new.parent_id; END;

CREATE TRIGGER increase_folder_count_on_update  
	AFTER UPDATE OF parent_id ON note 
	BEGIN   UPDATE note   SET notes_count=notes_count + 1  WHERE _id=new.parent_id; END;

CREATE TRIGGER update_note_content_on_delete  
	AFTER delete ON data WHEN old.mime_type='vnd.android.cursor.item/text_note' 
	BEGIN  UPDATE note   SET snippet=''  WHERE _id=old.note_id; END;

CREATE TRIGGER update_note_content_on_insert  
	AFTER INSERT ON data WHEN new.mime_type='vnd.android.cursor.item/text_note' 
	BEGIN  UPDATE note   SET snippet=new.content  WHERE _id=new.note_id; END;

CREATE TRIGGER update_note_content_on_update  
	AFTER UPDATE ON data WHEN old.mime_type='vnd.android.cursor.item/text_note' 
	BEGIN  UPDATE note   SET snippet=new.content  WHERE _id=new.note_id; END;

