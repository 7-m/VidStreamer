package com.mfd.vidindexer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public class VideoRepository {

	JdbcOperations jops;

	@Autowired
	public VideoRepository(JdbcTemplate template) {
		this.jops = template;
	}

	public static final String INSERT_VIDEO   = "insert into VIDEO(uploader, vid_id, link, name, tags, upload_ts) values(?, ?, ?, ?, ?, ?);";
	public static final String RETRIEVE_VIDEO = "select * from VIDEO where vid_id=?;";
	//public static final String RETRIVE_LIKE_NAME=

	void insertVideo(Video v) {
		jops.update(INSERT_VIDEO, v.getUploader(), v.getVid(), v.getLink(), v.getName(), v.getTags(), v.getUploadTimestamp());
	}

	public Video retrieve(String vidId) throws VideoNotFoundException {
		var res = jops.query(RETRIEVE_VIDEO, new Object[]{vidId},
							 (rs, rowNum) -> new Video(rs.getString("uploader"), rs.getString("vid_id"), rs.getString("link"),
													   rs.getString("tags"), rs.getString("name"),
													   Long.parseLong(rs.getString("upload_ts"))));
		if (res.isEmpty()) {
			throw new VideoNotFoundException(vidId);
		}
		return res.get(0);
	}

	public List<String> retrieveByText(String searchQuery) {
		return jops.query("select vid_id,  match(name, tags) against (? IN NATURAL LANGUAGE MODE) AS score from VIDEO " +
								  "ORDER BY score desc" +
								  " limit 10;", new Object[]{searchQuery},
						  (rs, rowNum) -> rs.getString("vid_id"));
	}

	public List<String> retrieveRecent(int maxIds) {
		return jops.query("select vid_id from VIDEO ORDER BY upload_ts desc LIMIT " + maxIds + ";", (rs, rowNum) -> rs.getString("vid_id"));
	}
}
