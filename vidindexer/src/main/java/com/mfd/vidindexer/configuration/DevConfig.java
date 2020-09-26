package com.mfd.vidindexer.configuration;

import com.mfd.vidindexer.VideoStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Profile(value = "dev")
@Configuration
public class DevConfig {
	@Bean
	DataSource embedded() {
		return new DriverManagerDataSource("jdbc:mysql://localhost/viddb?user=vid&password=password");
	}


	@Bean(initMethod = "init")
	VideoStorage inMemoryStorage() {
		return new VideoStorage() {


			Path tmpDir;


			@Override
			public void init() {
				// todo make configurable
				tmpDir = Path.of("/home/mfd/development/viddb");
			}

			/**
			 *
			 * @param vid the id of the video
			 * @param bytes
			 * @return
			 * @throws IOException
			 */
			@Override
			public String put(String vid, byte[] bytes) throws IOException {

				String filename = "vid-" + vid + ".mp4";
				Path filePath = Files.createFile(tmpDir.resolve(filename));

				// write the contents to file
				Files.write(filePath, bytes);


				return filePath.toUri().toString();
			}

			@Override
			public byte[] get(String link) {
				try {
					return Files.readAllBytes(Paths.get(link));
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			public void destroy() {

			}

		};

	}


}


