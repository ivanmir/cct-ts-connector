package com.sap.gs.cct.tsconnector;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sap.hcp.cf.logging.common.LogContext;
import com.sap.xs2.security.container.SecurityContext;
import com.sap.xs2.security.container.UserInfoException;

public class TenantFilter extends OncePerRequestFilter {

	private static final String TENANT_ID = "tenant_id";
	private static final Logger log = LoggerFactory.getLogger(TenantFilter.class);

	private List<String> excludedUrls = Collections.singletonList("/health");

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		LogContext.remove(TENANT_ID);

		String path = ((HttpServletRequest) request).getServletPath();
		try {
			if (!excludedUrls.contains(path)) {
				// Context might be filled from a previous request processed by this thread
				LogContext.add(TENANT_ID, SecurityContext.getUserInfo().getIdentityZone());
			}
		} catch (UserInfoException e) {
			log.info("Cannot retrieving tenant information", e);
		}
		chain.doFilter(request, response);
	}

}
