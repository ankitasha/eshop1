package com.cybage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import com.cybage.authentication.MyDBAuthenticationService;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter 
{   
	@Autowired	 
	MyDBAuthenticationService myDBAauthenticationService;
	
	
	  @Autowired
	   public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	 
	       // For User in database.
	       auth.userDetailsService(myDBAauthenticationService);
	 
	   }
	 protected void configure(HttpSecurity http) throws Exception {
	 
	         http.csrf().disable();
	 
	       //  requires login as USER or ADMIN.
	       // If no login, it will redirect to /login page.
	      
	       http.authorizeRequests().antMatchers("/orderList","/order", "/accountInfo")//
	               .access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')");
	 
	       // For Admin only.
	       http.authorizeRequests().antMatchers("/product").access("hasRole('ROLE_ADMIN')");
	 
	       // When the user has logged in as XX.
	       // But access a page that requires role YY,
	       // AccessDeniedException will throw.
	       http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
	 
	       // Config for Login Form
	       http.authorizeRequests().and().formLogin()//
	               // Submit URL of login page.
	               //.loginProcessingUrl("/login") // Submit URL
	               .loginPage("/login")//
	               .defaultSuccessUrl("/accountInfo")//
	               .failureUrl("/login?error=true")//
	               .usernameParameter("userName")//
	               .passwordParameter("password")
	               // Config for Logout Page
	               // (Go to home page).
	               .and().logout().logoutUrl("/logout").logoutSuccessUrl("/");
	 
	   }
	
	
    } 
	 
	 
	
	
	
