package com.wallet.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.dto.UserDTO;
import com.wallet.entity.User;
import com.wallet.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("application-test.properties")
public class UserControllerTest {
	
	private static final String EMAIL = "email@teste.com";
	private static final String USER = "userteste";
	private static final String PASSWORD = "123456";
	private static final String URL = "/user";

	@MockBean
	UserService userService;
	
	@Autowired
	MockMvc mvc; //utilizado para testes de controoler arquiterura MVC
	
	@Test
	public void testeSave() {
		
		try {
			BDDMockito.given(userService.save(Mockito.any(User.class))).willReturn(getMockUser());
			mvc.perform(MockMvcRequestBuilders.post(URL).content(this.getJsonPayLoad()).contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated()); //status 201 isCreated
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public User getMockUser() {
		User u = new User();
		u.setName(USER);
		u.setPassword(PASSWORD);
		u.setEmail(EMAIL);
		return u;
	}
	
	public String getJsonPayLoad() throws JsonProcessingException {
		UserDTO dto = new UserDTO();
		dto.setEmail(EMAIL);
		dto.setNome(USER);
		dto.setPassword(PASSWORD);
		
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dto);
	}
	
	
}
