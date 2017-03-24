package com.cybage.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SpringSecurityInitializer extends  AbstractSecurityWebApplicationInitializer 
{

	public SpringSecurityInitializer()
	{
		super(WebSecurityConfig.class);
	}


}
