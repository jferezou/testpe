package com.poleemploi;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = { "com.poleemploi.*" })
@PropertySource("classpath:com/poleemploi/config.properties")
@PropertySource("classpath:com/poleemploi/dictionary.properties")
public class AppConfig {
	
	
}
