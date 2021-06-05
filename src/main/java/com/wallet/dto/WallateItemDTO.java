package com.wallet.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;

public class WallateItemDTO {
	
	private Long id;
	@NotNull
	private Long wallet;
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-br", timezone = "Brazil/East")
	private Date date;
	@NotNull
	@Pattern(regexp = "^(ENTRADA|SAIDA)$", message = "Para o tipo somente serão aceitos os valores ENTRADA ou SAÍDA")
	private String type;
	@NotNull
	private String description;
	@NotNull
	private BigDecimal value;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getWallet() {
		return wallet;
	}
	public void setWallet(Long wallet) {
		this.wallet = wallet;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
}
