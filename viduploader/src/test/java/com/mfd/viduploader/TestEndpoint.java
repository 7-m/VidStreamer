package com.mfd.viduploader;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles({"test"})
public class TestEndpoint {

	/*// uncomment to use GCS as video storage
	@TestConfiguration
	public static class MyTestConfig {
		@Bean(initMethod = "init")
		@Primary
		VideoStorage gcsStorage() throws IOException {
			String PROJECT_ID = "cloud-test-254814";
			String JSON_KEY = Files.lines(Path.of("/home/mfd/Downloads/cloud-storage-key.json")).collect(Collectors.joining());
			String BUCKET_NAME = "user-uploaded-videos";

			return new GcsStorage(PROJECT_ID, JSON_KEY, BUCKET_NAME);
		}

	}
*/

	// setup mock
	@Autowired
	private WebApplicationContext wac;
	private MockMvc               mock;

	@Autowired VideoRepository rep;

	@BeforeEach
	void setup() throws IOException, URISyntaxException {
		mock = MockMvcBuilders.webAppContextSetup(wac).build();

		setAndClearDb();
		initDb();

	}

	// set up database
	@Autowired DataSource dataSource;
	JdbcTemplate tmplt;


	@Test
	void upload() throws Exception {
		mock.perform(get("/uploader/insert")).andExpect(status().is(200));

		String autok = JWT.create()
						  .withIssuer("vidauth")
						  .withSubject("unowned/no-owner")
						  .withExpiresAt(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
						  .sign(Algorithm.HMAC256("passwordpasswordpassword"));

		String content = "mp4 file data contectn blaa blaa";
		MockMultipartFile file = new MockMultipartFile("file", "a video file", "video/mp4", content.getBytes());

		var result = mock.perform(multipart("/uploader/insert").file(file)
															   .header("Authorization", autok)
															   .param("name", "cats n dofs")
															   .param("tags", "tag1 tag2 tag3")
															   .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
						 .andExpect(status().is2xxSuccessful()).andReturn();

		// check all values persisted or not
		// from API response
		UploadResponseDto resp = new Gson().fromJson(result.getResponse().getContentAsString(), UploadResponseDto.class);
		assertTrue(!resp.getVidLink().isEmpty());
		// from database
		String[] vidname = resp.getVidLink().split("/"); // gives nnnnn.mp4
		String vidId = vidname[vidname.length - 1].split("\\.")[0]; // gives nnnnn

		Video actual = rep.retrieve(vidId);
		assertNotNull(actual);
		assertEquals(resp.getVidLink(), actual.getLink());
		// from storage
		assertEquals(Files.readString(Paths.get(new URI(resp.getVidLink()))), content);

		// 4xx client error tests
		//test for  empty fields
		mock.perform(multipart("/uploader/insert").file(file)
												  .header("Authorization", autok)
												  .param("name", "")
												  .param("tags", "tag1 tag2 tag3")
												  .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
			.andExpect(status().is4xxClientError()).andReturn();
		// test for wrong content type
		file = new MockMultipartFile("file", "a video file", "wrong/content-type", content.getBytes());

		mock.perform(multipart("/uploader/insert").file(file)
												  .header("Authorization", autok)
												  .param("name", "cats n dofs")
												  .param("tags", "tag1 tag2 tag3")
												  .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
			.andExpect(status().is4xxClientError()).andReturn();

		// expired token test
		autok = JWT.create()
				   .withIssuer("vidauth")
				   .withSubject("unowned/no-owner")
				   .withExpiresAt(Date.from(Instant.now().minus(1, ChronoUnit.DAYS)))
				   .sign(Algorithm.HMAC256("passwordpasswordpassword"));

		mock.perform(multipart("/uploader/insert").file(file)
												  .header("Authorization", autok)
												  .param("name", "cats n dofs")
												  .param("tags", "tag1 tag2 tag3")
												  .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
			.andExpect(status().isUnauthorized()).andReturn();


	}

	void setAndClearDb() throws URISyntaxException {
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
