package com.sap.gs.cct.tsconnector;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter;

import com.sap.xs2.security.commons.SAPOfflineTokenServicesCloud;

@EnableWebSecurity
@EnableResourceServer
@Configuration
public class WebSecurityConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.addFilterAfter(new TenantFilter(), SecurityContextHolderAwareRequestFilter.class)
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.NEVER)
				.and()
				.antMatcher("/**")
				.authorizeRequests()
				.anyRequest()
				.authenticated();
	}

	@Bean
	protected SAPOfflineTokenServicesCloud offlineTokenServices() {
		return new SAPOfflineTokenServicesCloud();
	}
}
