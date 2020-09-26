package com.mfd.vidauth.configuration;

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


}


