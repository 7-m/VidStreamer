package com.mfd.viduploader.configuration;

import com.mfd.viduploader.GcsStorage;
import com.mfd.viduploader.VideoStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


@Profile(value = "dev")
@Configuration
public class DevConfig {
	@Bean
	DataSource embedded() {
		return new DriverManagerDataSource("jdbc:mysql://localhost/viddb?user=vid&password=password");
	}


	@Bean(initMethod = "init")
	VideoStorage gcsStorage() {
		String PROJECT_ID = "cloud-test-254814";
		String JSON_KEY = "/home/mfd/Downloads/cloud-storage-key.json";
		String BUCKET_NAME = "user-uploaded-videos";

		return new GcsStorage(PROJECT_ID, JSON_KEY, BUCKET_NAME);
	}


}


