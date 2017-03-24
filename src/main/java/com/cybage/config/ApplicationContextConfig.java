package com.cybage.config;

import javax.sql.DataSource;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
/*import org.springframework.context.annotation.Import;
*/import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


import com.cybage.dao.AccountDao;
import com.cybage.dao.OrderDao;
import com.cybage.dao.ProductDao;
import com.cybage.dao.impl.AccountDaoImpl;
import com.cybage.dao.impl.OrderDaoImpl;
import com.cybage.dao.impl.ProductDaoImpl;
import com.cybage.validator.CustomerInfoValidator;
import com.cybage.validator.ProductInfoValidator;

@Configuration // indicates it contains one or more beans 
@EnableWebMvc // eq to mvc:annotation-driven
@ComponentScan("com.cybage.*")
@EnableTransactionManagement
@PropertySource("classpath:ds-hibernate-cfg.properties")
/*@Import({WebSecurityConfig.class})*/
public class ApplicationContextConfig 
{

@Autowired
private Environment env;


@Bean(name="messageSource") //producing bean manageable by spring container
public ResourceBundleMessageSource messageSource()
{ 
	ResourceBundleMessageSource rb= new ResourceBundleMessageSource();
	rb.setBasenames(new String[] {"validator"});
	return rb;
}

@Bean(name="viewResolver")
public InternalResourceViewResolver getViewResolver()
{
InternalResourceViewResolver viewResolver= new InternalResourceViewResolver();
viewResolver.setPrefix("/WEB-INF/pages/");
viewResolver.setSuffix(".jsp");	
	return viewResolver;
}

@Bean(name="multiPartResolver")
public CommonsMultipartResolver multipartResolver()
{
	CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
    commonsMultipartResolver.setMaxInMemorySize(10240);
	commonsMultipartResolver.setMaxUploadSize(10240);
	return commonsMultipartResolver;
}

@Bean(name="dataSource")//(name="getDataSource")
public DataSource getDataSource()
{
 DriverManagerDataSource dataSource= new DriverManagerDataSource();
 dataSource.setDriverClassName(env.getProperty("ds.database-driver"));
 dataSource.setUrl(env.getProperty("ds.url"));
 dataSource.setUsername(env.getProperty("ds.username"));
 dataSource.setPassword(env.getProperty("ds.password"));
  
 System.out.println("## getDataSource: " + dataSource);
  
 return dataSource;	
}

@Autowired
@Bean(name = "sessionFactory")
public SessionFactory getSessionFactory(DataSource dataSource) throws Exception {
    Properties properties = new Properties();

    // See: ds-hibernate-cfg.properties
    properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
    properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
    properties.put("current_session_context_class", env.getProperty("current_session_context_class"));
     
  LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
     
     factoryBean.setPackagesToScan(new String[]{"com.cybage.entity"});
    // Package contain entity classes
/*    factoryBean.setAnnotatedPackages("com.cybage.entity");
*/      factoryBean.setDataSource(dataSource);
    factoryBean.setHibernateProperties(properties);
    factoryBean.afterPropertiesSet();
    //
    SessionFactory sf = factoryBean.getObject();
    System.out.println("## getSessionFactory: " + sf);
    return sf;
} 
  
 @Autowired
  @Bean(name="transactionManager")
  public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory)
  {
	 HibernateTransactionManager transactionManager = new HibernateTransactionManager();
	 transactionManager.setSessionFactory(sessionFactory);
	 return transactionManager;
	  }
  
/*  @Autowired
*/  @Bean(name="accountDao")
  public AccountDao getApplicantDao()
  {
	return new AccountDaoImpl();
	}
/* @Autowired
*/  @Bean(name="productDao")
 public ProductDao getProductDao()
 {
	 return new ProductDaoImpl();
 }
 
 @Bean(name="orderDao")
 public OrderDao getOrderDao()
 {
	 return new OrderDaoImpl();
 }
 
 @Bean(name="accountDao")
 public AccountDao getAccountDao()
 {
	 return new AccountDaoImpl();
 }
 
 @Bean(name = "productInfoValidator")
 public ProductInfoValidator getProductInfoValidator() {
     return new ProductInfoValidator();
 }


 @Bean(name = "customerInfoValidator")
 public CustomerInfoValidator getCustomerInfoValidator() {
     return new CustomerInfoValidator();
 }

}