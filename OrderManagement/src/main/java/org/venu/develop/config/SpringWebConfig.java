package org.venu.develop.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
 
@EnableWebMvc //<mvc:annotation-driven />
@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = { "org.venu.develop.*" })
public class SpringWebConfig extends WebMvcConfigurerAdapter {
 
	 
		//user
		@Value("${oracle.user}")
		private String dbUser;
	 
		//1.2.3.4
		@Value("${oracle.password}")
		private String dbPwd;
	 
		
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**") 
                        .addResourceLocations("/resources/");
	}
 
	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver 
                         = new InternalResourceViewResolver();
		System.out.println("oracle.user =====================================================" +dbUser);

		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/jsp/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver viewResolver 
                         = new CommonsMultipartResolver();
		viewResolver.setMaxUploadSize(1000000l);
		viewResolver.setMaxInMemorySize(1000000);
		return viewResolver;
	}
 
	 
	//To resolve ${} in @Value
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}
		
	
}