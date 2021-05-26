package com.wallet.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.wallet.entity.User;

@SpringBootTest
public class UserRepositoryTest {
	private static final String EMAIL = "email@teste.com";
	@Autowired
	UserRepository repository;
	
	@BeforeEach //Os métodos anotados com a  anotação @BeforeEach são executados antes de cada teste.
	public void setUp() {
		//salvo um usuário antres de exexutar os testes
		User u = new User();
		u.setName("Set Up user");
		u.setPassword("senha123");
		u.setEmail(EMAIL);
		repository.save(u);
	}
	
	@AfterEach //tudo que estiver dentor do apter é executado depois dos testes das classes
	public void tearDownn() {
		//removo o usuário depois q executar os testes
		repository.deleteAll();
	}

	@Test
	public void testSave() {
		User u = new User();
		u.setName("Teste");
		u.setPassword("123456");
		u.setEmail("teste@teste.com");
		User response = repository.save(u);
		assertNotNull(response);
	}
	
	public void testeFindByEmail() {
		Optional<User> response = repository.findByEmailEquals(EMAIL);
		assertTrue(response.isPresent());
		assertEquals(response.get().getEmail(), EMAIL);
	}
	
	
}
