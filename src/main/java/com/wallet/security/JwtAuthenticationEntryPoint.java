package com.wallet.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/*
QUando o usuário  não tiber acesso a algum end point essa classe é acionada para mandar uma mensagem personalzada aoi usuário.
*/

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
				"Acesso negado. Você deve estar autenticado no sistema para acessar a URL solicitada.");
	}
}
