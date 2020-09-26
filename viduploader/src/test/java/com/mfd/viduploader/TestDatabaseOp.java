package com.mfd.viduploader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@ActiveProfiles(profiles = {"test"})
public class TestDatabaseOp {

	@Autowired DataSource dataSource;
	JdbcTemplate tmplt;
	@Autowired VideoRepository vidrepo;

	@BeforeEach
	void setUp() throws IOException, URISyntaxException {
		setAndClearDb();
		initDb();

	}

	@Test
	void insertAndUpdateVideos() {
		Video v = new Video("colo", "xyz123", "file:///locl", "lol pop sol", "catarag", System.currentTimeMillis());
		vidrepo.insertVideo(v);
		Video actual = vidrepo.retrieve( "xyz123");

		assertEquals(v, actual);

	}


	void setAndClearDb() throws IOException, URISyntaxException {
		tmplt = new JdbcTemplate(dataSource);
		// the foreign key check disabling to circumvent truncates restriction
		tmplt.batchUpdate("SET FOREIGN_KEY_CHECKS = 0;",
						  "truncate  `VIDEO`;",
						  "truncate  `USER`;",
						  "SET FOREIGN_KEY_CHECKS = 1;");
	}

	void initDb() {
		tmplt.update("INSERT INTO USER VALUES('unowned/no-owner','slobo');");
	}

}
