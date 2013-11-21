
DROP TABLE IF EXISTS note;
CREATE TABLE note (
    _id INTEGER PRIMARY KEY AUTOINCREMENT,
    pub_date INTEGER NOT NULL DEFAULT (strftime('%s', 'now') * 1000),
    snap TEXT NOT NULL DEFAULT '',
    content TEXT NOT NULL DEFAULT ''
);

