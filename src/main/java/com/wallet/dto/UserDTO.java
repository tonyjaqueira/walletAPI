package com.wallet.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class UserDTO {

	private Long id;
	
	@NotNull
	private String nome;
	
	@Email(message = "Email inválido!")
	private String email;
	
	@NotNull
	private String password;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
