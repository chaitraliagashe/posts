package com.shown.posts.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class ApiKeyAuthentication extends AbstractAuthenticationToken {
	private static final long serialVersionUID = 1L;
	private final String apiKey;
	
	public ApiKeyAuthentication(Collection<? extends GrantedAuthority> authorities, String apiKey) {
		super(authorities);
		this.apiKey = apiKey;	
		setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPrincipal() {
		return apiKey;
	}

}
