package com.algo;

import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = { "com.algo.*" })
@PropertySource("classpath:com/algo/config.properties")
@PropertySource("classpath:com/algo/dictionary.properties")
@EnableTransactionManagement
public class AppConfig {


	@Autowired
	public DataSource dataSource;
	
	 @Bean
	 public DataSource dataSource(Environment environment) {
	
		String pw = environment.getProperty("jdbc.password");
		String user = environment.getProperty("jdbc.username");
		String url = environment.getProperty("jdbc.url");
		String driver = environment.getProperty("jdbc.driverClassName");
	
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setPassword(pw);
		dataSource.setUrl(url);
		dataSource.setUsername(user);
		dataSource.setDriverClassName(driver);
	
		return dataSource;
	 }
	
	 @Bean
	 public PlatformTransactionManager txManager() {
	     return new DataSourceTransactionManager(dataSource);
	 }
	
}
