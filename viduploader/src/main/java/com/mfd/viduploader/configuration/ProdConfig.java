package com.mfd.viduploader.configuration;


import com.mfd.viduploader.GcsStorage;
import com.mfd.viduploader.VideoStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
@Profile("prod")
public class ProdConfig {
	static Logger logger = Logger.getLogger(ProdConfig.class.getName());

	@Bean
	DataSource googleSql() {
		String host = System.getenv("DB_HOST");
		String user = System.getenv("DB_USER");
		String password = System.getenv("DB_PASSWORD");

		if (host == null || host.isEmpty())
			throw new RuntimeException("DB_HOST environment variable not set");
		if (user == null || user.isEmpty())
			throw new RuntimeException("DB_USER environment variable not set");
		if (password == null || password.isEmpty())
			throw new RuntimeException("DB_PASSWORD environment variable not set");

		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource("jdbc:mysql://" + host + "/viddb", user, password);
		try {
			Connection connection = driverManagerDataSource.getConnection();
			logger.log(Level.INFO, "Connected to database successfully :\n" + connection.getMetaData());
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return driverManagerDataSource;


	}

	@Bean(initMethod = "init")
	VideoStorage gcsStorage() {
		String PROJECT_ID = System.getenv("PROJECT_ID");//"cloud-test-254814";
		String JSON_KEY = System.getenv("GCS_KEY");//"/home/mfd/Downloads/cloud-storage-key.json";
		String BUCKET_NAME = System.getenv("GCS_BUCKET_NAME");//"user-uploaded-videos";

		return new GcsStorage(PROJECT_ID, JSON_KEY, BUCKET_NAME);
	}


}
