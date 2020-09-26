package com.mfd.viduploader.configuration;

import com.mfd.viduploader.Utils;
import com.mfd.viduploader.VideoStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Profile(value = "test")
@Configuration
public class TestConfig {
	@Bean
	DataSource embedded() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource(
				"jdbc:mysql://localhost/vidtestdb?user=vid-test&password=password");
		return driverManagerDataSource;

	}


	@Bean(destroyMethod = "destroy", initMethod = "init")
	VideoStorage inMemoryStorage() {
		 return new VideoStorage() {


			Path tmpDir;


			@Override
			public void init() throws IOException {
				// todo make configurable
				tmpDir = Files.createTempDirectory("viddb-tmp");
			}

			/**
			 *
			 * @param vId the id of the video
			 * @param bytes
			 * @return
			 * @throws IOException
			 */
			@Override
			public String put(String vId, byte[] bytes) throws IOException {


				Path filePath = Files.createFile(tmpDir.resolve(vId));

				// write the contents to file
				Files.write(filePath, bytes);


				return filePath.toUri().toString();
			}

			@Override
			public byte[] get(String vId) {
				try {
					return Files.readAllBytes(Paths.get(vId));
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void destroy() throws IOException {
				Utils.deleteDir(tmpDir);
			}

		};

	}


}


