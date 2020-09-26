
DROP TABLE IF EXISTS VIDEO;
DROP TABLE IF EXISTS USER;

CREATE TABLE USER (  username varchar(30) NOT NULL,  passwd varchar(30) NOT NULL,  PRIMARY KEY (username)) ;

CREATE TABLE VIDEO (  vid_id varchar(30) NOT NULL, uploader varchar(30) NOT NULL,   link varchar(500) NOT NULL,  name varchar(200) NOT NULL,  tags varchar(500) DEFAULT NULL, upload_ts bigint,   FULLTEXT(name, tags), PRIMARY KEY (vid_id),   CONSTRAINT VIDEO_ibfk_1 FOREIGN KEY (uploader) REFERENCES USER (username) ON DELETE CASCADE ON UPDATE CASCADE) ;