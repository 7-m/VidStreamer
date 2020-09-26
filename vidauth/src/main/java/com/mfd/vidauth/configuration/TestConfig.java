package com.mfd.vidauth.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


@Profile(value = "test")
@Configuration
public class TestConfig {
	@Bean
	DataSource embedded() {
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource(
				"jdbc:mysql://localhost/vidtestdb?user=vid-test&password=password");
		return driverManagerDataSource;

	}


}


