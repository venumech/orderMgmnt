package org.venu.develop.config;


import org.apache.commons.dbcp2.BasicDataSource;
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
import org.venu.develop.dao.OrderDBInfc;
import org.venu.develop.dao.OrderDao;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@EnableWebMvc //<mvc:annotation-driven />
@Configuration
@PropertySource("classpath:application.properties")
@ComponentScan(basePackages = { "org.venu.develop.*" })
public class SpringWebConfig extends WebMvcConfigurerAdapter {
 

	//ora driver
	@Value("${oracle.db.driver}")
	private String oraDriver;

	//ora url
	@Value("${oracle.db.url}")
	private String oraUrl;
	//ora user
	@Value("${oracle.user}")
	private String dbUser;
 
		//Ora pwd
		@Value("${oracle.password}")
		private String dbPwd;
		
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**") 
                        .addResourceLocations("/resources/");
	}
 
	/* this(spring's) is for testing purposes only. not good for multiple request scenarios in production systems
	 * 
	 */
	/* 
	<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
		<property name="driverClassName" value="${jdbc.driver}"></property>
		<property name="url" value="${jdbc.url}"></property>
		<property name="username" value="${jdbc.username}"></property>
		<property name="password" value="${jdbc.password}"></property>
	</bean>
	*/
	
	@Bean
	public DriverManagerDataSource dataSource (){
		DriverManagerDataSource dataSource
		                             = new DriverManagerDataSource();
		dataSource.setDriverClassName(oraDriver);
		dataSource.setUrl(oraUrl);
		dataSource.setUsername(dbUser);
		dataSource.setPassword(dbPwd);
		return dataSource;
		
	};
	
	/*
	 * for real time deployments
	 */
	/*	
    <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
    <property name="driverClassName" value="${jdbc.driverClassName}"/>
    <property name="url" value="${jdbc.url}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
    </bean>
	 */
	@Bean
	public BasicDataSource apacheDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(oraDriver);
		dataSource.setUrl(oraUrl);
		dataSource.setUsername(dbUser);
		dataSource.setPassword(dbPwd);
		return dataSource;		
	};
	
	@Bean
	public JdbcTemplate jdbcOperations() {
	    return new JdbcTemplate(apacheDataSource());
	}

	
	@Bean
    public OrderDBInfc getOrderDao() {
		//OrderDao od = new OrderDao();
		//od.setJdbcTemplate((JdbcTemplate) jdbcOperations());
        return new OrderDao(apacheDataSource());
    }
	
	
	
	
	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver 
                         = new InternalResourceViewResolver();
		
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