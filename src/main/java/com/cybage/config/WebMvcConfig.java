package com.cybage.config;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan("com.cybage.controller")
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter
{
	 private static final Charset UTF8 = Charset.forName("UTF-8");
	   
		
	   
	   @Bean
	 		public InternalResourceViewResolver viewResolver() {
	 			InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
	 			viewResolver.setViewClass(JstlView.class);
	 			viewResolver.setPrefix("webapp/WEB-INF/pages/jsp/");
	 			viewResolver.setSuffix(".jsp");
	 			return viewResolver;
	 		}	
	   
	   @Override
	   public void addViewControllers(ViewControllerRegistry registry)
	   {
		 registry.addViewController("/").setViewName("webapp/WEB_INF/index");  
		   
		   
	   }
	   
	 
	    // Config UTF-8 Encoding.
	    @Override
	    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
	        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
	        stringConverter.setSupportedMediaTypes(Arrays.asList(new MediaType("text", "plain", UTF8)));
	        converters.add(stringConverter);
	       	 
	    }
	 
	    // Static Resource Config
	    // equivalents for <mvc:resources/> tags
	    @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	         registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(31556926);
	        registry.addResourceHandler("/img/**").addResourceLocations("/img/").setCachePeriod(31556926);
	        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(31556926);
	    }
	    
	  
	    
	    // equivalent for <mvc:default-servlet-handler/> tag
	    @Override
	    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
	        configurer.enable();
	    }
	   
	 

}
