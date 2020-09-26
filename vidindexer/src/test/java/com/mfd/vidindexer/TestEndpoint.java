package com.mfd.vidindexer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
public class TestEndpoint {

	@Autowired
	private WebApplicationContext wac;
	private MockMvc               mock;

	@Autowired
	private DataSource   dataSource;
	private JdbcTemplate tmplt;

	@Autowired
	VideoRepository vidrepo;


	@BeforeEach
	void setup() {
		mock = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@BeforeEach
	void init() {
		resetDb();
		populateDb();
	}

	void resetDb() {
		tmplt = new JdbcTemplate(dataSource);
		// the foreign key check disabling to circumvent truncates restriction
		tmplt.batchUpdate("SET FOREIGN_KEY_CHECKS = 0;",
						  "truncate  `VIDEO`;",
						  "truncate  `USER`;",
						  "SET FOREIGN_KEY_CHECKS = 1;");
	}


	void populateDb() {
		tmplt.update("INSERT INTO `USER` VALUES('user1','pass1');");
		tmplt.update("INSERT INTO `USER` VALUES('user2','pass2');");

		Video v = new Video("user1", "vid1", "l1", "funny cute sweet", "cat plays with wool ball", System.currentTimeMillis());
		Video v1 = new Video("user1", "vid2", "l2", "funny cute sweet", "dog  plays with bone", System.currentTimeMillis()+1);
		Video v2 = new Video("user2", "vid3", "l3", "confused what dilemma", "crazy magician makes table dsappear", System.currentTimeMillis()+2);
		vidrepo.insertVideo(v1);
		vidrepo.insertVideo(v);
		vidrepo.insertVideo(v2);
	}


	@Test
	void retreiveLinksForSearch() throws Exception {
		mock.perform(get("/indexer/search/cat"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.ids", hasSize(3)))
			.andExpect(jsonPath(("$.ids"), contains("vid1", "vid2", "vid3")));

	}
	@Test
	void retrieveRecents() throws Exception {
		mock.perform(get("/indexer/new?count=10"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.ids", hasSize(3)))
			.andExpect(jsonPath(("$.ids"), contains("vid3", "vid2", "vid1")));

	}

	@Test
	void retrieveVideoById() throws Exception {
		mock.perform(get("/indexer/fetch/vid2"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$.uploader", is("user1")))
			.andExpect(jsonPath("$.vid", is("vid2")))
			.andExpect(jsonPath("$.name", is("dog  plays with bone")))
			.andExpect(jsonPath("$.tags", is("funny cute sweet")))
			.andExpect(jsonPath("$.link", is("l2")));

		// non exisitng video

		mock.perform(get("/indexer/fetch/non-existent")).andExpect(status().is4xxClientError()).andDo(result -> System.out.println(result.getResponse().getContentAsString()));
	}
}
