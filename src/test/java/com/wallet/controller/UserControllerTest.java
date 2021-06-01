package com.wallet.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
	
	private static final Long ID = 1L;
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
			mvc.perform(MockMvcRequestBuilders.post(URL).content(this.getJsonPayLoad(ID, EMAIL, USER, PASSWORD)).contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated()) //status 201 isCreated
					.andExpect(jsonPath("$.data.id").value(ID)) //fanedo as vlaidações qu esta sendo retornado da Classe Response que o que estamos setando aqui
					.andExpect(jsonPath("$.data.email").value(EMAIL))
					.andExpect(jsonPath("$.data.nome").value(USER))
					.andExpect(jsonPath("$.data.password").value(PASSWORD));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testSaveInvalidUser() throws JsonProcessingException, Exception {
		mvc.perform(MockMvcRequestBuilders.post(URL).content(this.getJsonPayLoad(ID, "email", USER, PASSWORD)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest()) 
				.andExpect(jsonPath("$.errors[0]").value("Email Inválido"));
	}
	
	public User getMockUser() {
		User u = new User();
		u.setId(ID);
		u.setName(USER);
		u.setPassword(PASSWORD);
		u.setEmail(EMAIL);
		return u;
	}
	
	public String getJsonPayLoad(Long id, String email, String nome, String password) throws JsonProcessingException {
		UserDTO dto = new UserDTO();
		dto.setId(id);
		dto.setEmail(email);
		dto.setNome(nome);
		dto.setPassword(password);
		
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dto);
	}
	
	
}
