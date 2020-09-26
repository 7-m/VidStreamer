package com.mfd.vidindexer.configuration;


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
		if (user==null || user.isEmpty())
			throw new RuntimeException("DB_USER environment variable not set");
		if (password ==null || password.isEmpty())
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

}
