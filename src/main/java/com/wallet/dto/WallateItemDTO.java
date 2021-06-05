package com.wallet.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class WallateItemDTO {
	
	private Long id;
	@NotNull
	private Long wallet;
	@NotNull
	private Date date;
	@NotNull
	@Pattern(regexp = "^(ENTRADA|SAIDA)$", message = "Para o tipo somente serão aceitos os valores ENTRADA ou SAÍDA")
	private String type;
	@NotNull
	private String descriotion;
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
	public String getDescriotion() {
		return descriotion;
	}
	public void setDescriotion(String descriotion) {
		this.descriotion = descriotion;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
}
