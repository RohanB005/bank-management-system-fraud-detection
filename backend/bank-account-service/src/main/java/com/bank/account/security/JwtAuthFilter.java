package com.bank.account.security;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bank.account.controller.AccountController;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter{

    private final AccountController accountController;
	
	private final JwtUtil jwtUtil;
	
	public JwtAuthFilter(JwtUtil jwtUtil, AccountController accountController) {
		this.jwtUtil = jwtUtil;
		this.accountController = accountController;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String path = request.getRequestURI();
		
		if(path.contains("/api/accounts/customer/") && request.getMethod().equals("GET")) {
			
		}
		
		String authHeader = request.getHeader("Authorization");
		
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			String token = authHeader.substring(7);
			if(!jwtUtil.isToeknValid(token)) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("{\"success\":false,\"message\":\"Invalid or expired token\"}");
				response.setContentType("applicatipn/json");
				return ;
			}
			request.setAttribute("customerId",jwtUtil.extraxctCustomerId(token));
		}else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("{\"success\":false,\"message\":\"Missing Authorization header\"}");
            response.setContentType("application/json");
            return;
		}
		filterChain.doFilter(request, response);
	}

}
