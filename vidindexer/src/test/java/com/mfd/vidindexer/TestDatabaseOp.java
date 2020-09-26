package com.mfd.vidindexer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URISyntaxException;


@SpringBootTest
@ActiveProfiles(profiles = {"test"})
public class TestDatabaseOp {

	@Autowired DataSource dataSource;
	JdbcTemplate tmplt;
	@Autowired VideoRepository vidrepo;

	@BeforeEach
	void initDb() throws IOException, URISyntaxException {
		tmplt = new JdbcTemplate(dataSource);
		// the foreign key check disabling to circumvent truncates restriction
		tmplt.batchUpdate("SET FOREIGN_KEY_CHECKS = 0;",
						  "truncate  `VIDEO`;",
						  "truncate  `USER`;",
						  "SET FOREIGN_KEY_CHECKS = 1;");
	}

	@BeforeEach
	void resetDb() {
		tmplt.update("INSERT INTO `USER` VALUES('user1','pass1');");
		tmplt.update("INSERT INTO `USER` VALUES('user2','pass2');");
	}

	@Test
	void retrieveVideosBasedOnSearchTerms() {


		Video v = new Video("user1", "vid1", "l1", "funny cute sweet", "cat plays with wool ball", System.currentTimeMillis());
		Video v1 = new Video("user1", "vid2", "l2", "funny cute sweet", "dog  plays with bone", System.currentTimeMillis());
		Video v2 = new Video("user2", "vid3", "l3", "confused what dilemma", "crazy magician makes table dsappear",
							 System.currentTimeMillis());
		vidrepo.insertVideo(v1);
		vidrepo.insertVideo(v);
		vidrepo.insertVideo(v2);

		// expect cat vid first
		var links = vidrepo.retrieveByText("cat funny");
		Assertions.assertEquals(v.getLink(), links.get(0));

		// expect dog vid first
		links = vidrepo.retrieveByText("dog funny");
		Assertions.assertEquals(v1.getLink(), links.get(0));


	}

}
