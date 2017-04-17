package com.poleemploi;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@ComponentScan(basePackages = { "com.poleemploi.*" })
@PropertySources({
	@PropertySource("classpath:com/poleemploi/config.properties"),
	@PropertySource("classpath:com/poleemploi/dictionary.properties")
})
public class AppConfig {
	
	
}
