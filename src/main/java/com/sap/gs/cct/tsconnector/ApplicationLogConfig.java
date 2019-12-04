package com.sap.gs.cct.tsconnector;

import com.sap.hcp.cf.logging.servlet.filter.RequestLoggingFilter;
import javax.servlet.DispatcherType;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for defining application logging behaviour.
 */
@Configuration
public class ApplicationLogConfig {

	/**
	 * Enable Servlet instrumentation for application logging by registering
	 * {@link RequestLoggingFilter} as a Servlet filter.
	 * <p>This filter adds information like
	 * 
	 * <pre>correlation_id</pre>
	 * 
	 * to the {@link org.slf4j.MDC} for each incoming request.
	 *
	 * @return the enhanced filter registration bean
	 */
	@Bean
	public FilterRegistrationBean<RequestLoggingFilter> loggingCorrelationFilter() {
		FilterRegistrationBean<RequestLoggingFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(new RequestLoggingFilter());
		filterRegistrationBean.setName("request-correlation-logging");
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST);
		return filterRegistrationBean;
	}

	/**
	 * Configures and returns a Spring Boot FilterRegistration bean. The
	 * FilterRegistration bean holds a SAP CF RequestLoggingFilter
	 * {@link RequestLoggingFilter}. Required for automatic generation and
	 * propagation of correlation ID.
	 *
	 * @return {@link FilterRegistrationBean}
	 */
	@Bean
	public FilterRegistrationBean<TenantFilter> loggingTenantFilter() {
		FilterRegistrationBean<TenantFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(new TenantFilter());
		filterRegistrationBean.setName("request-tenant-logging");
		filterRegistrationBean.setMatchAfter(true);
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST);
		return filterRegistrationBean;
	}

}
