package com.mfd.vidauth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles({"test"})
@SpringBootTest
public class EndpointTest {
	@Autowired
	private WebApplicationContext wac;
	private MockMvc               mock;

	@Autowired
	private DataSource   dataSource;
	private JdbcTemplate tmplt;

	@Autowired
	UserRepository userRepository;


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
	}


	@Test
	void testCreateUser() throws Exception {

		mock.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"goof\", \"password\":\"pass1\"} ")).andExpect(status().is2xxSuccessful());

		// check is existence in database
		Assertions.assertTrue(userRepository.checkIfUserExists("goof"));

		// check for reuse of username
		mock.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"goof\", \"password\":\"pass1\"} ")).andExpect(status().is4xxClientError());


	}

	@Test
	void testLogin() throws Exception {
		// expect a login token
		mock.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"user1\", \"password\":\"pass1\"} "))
			.andExpect(status().is2xxSuccessful())
			.andDo(result -> System.out.println(result.getResponse().getContentAsString()))
			.andExpect(jsonPath("$.authorization", notNullValue()));
	}
}
