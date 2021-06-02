package com.wallet.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

public class WalletDTO {
	
	private Long id;
	
	@NotNull(message = "O Nome não pode ser Nulo")
	private String name;
	
	@NotNull(message = "O valor não pode ser Nulo")
	private BigDecimal value;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	

}
