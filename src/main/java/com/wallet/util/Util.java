package com.wallet.util;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.wallet.entity.User;
import com.wallet.service.UserService;

@Component //SPring manipula a classe para usarmos o UserService
public class Util {
	
	private static UserService staticService;
	
	public Util(UserService service) {
		Util.staticService = service;
	}
	
	public static Long getAuthenticatedUserId() {
		try {
			//pegamso o usuário autenticado no conexto com  SecurityContextHolder.getContext()
			Optional<User> user = staticService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
			if(user.isPresent()) {
				return user.get().getId();
			}else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
