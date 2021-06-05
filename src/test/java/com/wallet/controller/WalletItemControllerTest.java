package com.wallet.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.dto.WallateItemDTO;
import com.wallet.entity.Wallet;
import com.wallet.entity.WalletItem;
import com.wallet.service.WalletItemService;
import com.wallet.util.enums.TypeEnum;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("application-test.properties")
public class WalletItemControllerTest {

	@MockBean
	WalletItemService service;
	
	@Autowired
	MockMvc mvc;
	
	private static final Long ID = 1L;
	private static final Date DATE = new Date();
	private static final LocalDate TODAY = LocalDate.now();
	private static final TypeEnum TYPE = TypeEnum.EN;
	private static final String DESCRIPTION = "Conta de Luz";
	private static final BigDecimal VALUE = BigDecimal.valueOf(65); 
	private static final String URL = "/wallet-item";
	
	@Test
	public void testeSave() {
		try {
			BDDMockito.given(service.save(Mockito.any(WalletItem.class))).willReturn(getMockWalletItem());
			mvc.perform(MockMvcRequestBuilders.post(URL).content(this.getJsonPayLoad())
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isCreated()) //status 201 isCreated
					.andExpect(jsonPath("$.data.id").value(ID)) //fanedo as vlaidações qu esta sendo retornado da Classe Response que o que estamos setando aqui
					.andExpect(jsonPath("$.data.date").value(TODAY.format(getDateFormater())))
					.andExpect(jsonPath("$.data.description").value(DESCRIPTION))
					.andExpect(jsonPath("$.data.type").value(TYPE.getValue()))
					.andExpect(jsonPath("$.data.value").value(VALUE))
					.andExpect(jsonPath("$.data.wallet").value(ID)); //para que a senha não seja retonada na resposta
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFindBetweenDates() {
		List<WalletItem> list = new ArrayList<>();
		list.add(getMockWalletItem());
		Page<WalletItem> page = new PageImpl(list);
		
		String starDate = TODAY.format(getDateFormater());
		String endDate = TODAY.plusDays(5).format(getDateFormater());
		
		try {
			BDDMockito.given(service.findBetweenDates(Mockito.anyLong(), Mockito.any(Date.class), Mockito.any(Date.class), Mockito.anyInt())).willReturn(page);
			mvc.perform(MockMvcRequestBuilders.get(URL + "/1?starDate=" + starDate + "&endDate=" + endDate)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()) // 200 ok
					.andExpect(jsonPath("$.data.content[0].id").value(ID)) //fanedo as vlaidações qu esta sendo retornado da Classe Response que o que estamos setando aqui
					.andExpect(jsonPath("$.data.content[0].date").value(TODAY.format(getDateFormater())))
					.andExpect(jsonPath("$.data.content[0].description").value(DESCRIPTION))
					.andExpect(jsonPath("$.data.content[0].type").value(TYPE.getValue()))
					.andExpect(jsonPath("$.data.content[0].value").value(VALUE))
					.andExpect(jsonPath("$.data.content[0].wallet").value(ID)); //para que a senha não seja retonada na resposta
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testFindByType() {
		List<WalletItem> list = new ArrayList<>();
		list.add(getMockWalletItem());
		
		try {
			BDDMockito.given(service.findByWalletAndType(Mockito.anyLong(), Mockito.any(TypeEnum.class))).willReturn(list);
			mvc.perform(MockMvcRequestBuilders.get(URL + "/type/138?type=ENTRADA") //1? pq a consulta é path variable
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()) // 200 ok
					.andExpect(jsonPath("$.data.[0].id").value(ID)) //fanedo as vlaidações qu esta sendo retornado da Classe Response que o que estamos setando aqui
					.andExpect(jsonPath("$.data.[0].date").value(TODAY.format(getDateFormater())))
					.andExpect(jsonPath("$.data.[0].description").value(DESCRIPTION))
					.andExpect(jsonPath("$.data.[0].type").value(TYPE.getValue()))
					.andExpect(jsonPath("$.data.[0].value").value(VALUE))
					.andExpect(jsonPath("$.data.[0].wallet").value(ID)); //para que a senha não seja retonada na resposta
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testSumByWallet() {
		BigDecimal value = BigDecimal.valueOf(536.90);		
		try {
			BDDMockito.given(service.sumByWalletId(Mockito.anyLong())).willReturn(value);
			mvc.perform(MockMvcRequestBuilders.get(URL + "/total/1")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()) // 200 ok
					.andExpect(jsonPath("$.data").value("536.9"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testeUpdate() {
		String description = "Conta de Luz";
		Wallet w = new Wallet();
		w.setId(ID);
		try {
			BDDMockito.given(service.findById(Mockito.anyLong())).willReturn(Optional.of(getMockWalletItem()));
			BDDMockito.given(service.save(Mockito.any(WalletItem.class))).willReturn(new WalletItem(1L, w, DATE, TypeEnum.SD, description, VALUE));
			
			mvc.perform(MockMvcRequestBuilders.put(URL).content(this.getJsonPayLoad())
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()) //status 201 isCreated
					.andExpect(jsonPath("$.data.id").value(ID)) //fanedo as vlaidações qu esta sendo retornado da Classe Response que o que estamos setando aqui
					.andExpect(jsonPath("$.data.date").value(TODAY.format(getDateFormater())))
					.andExpect(jsonPath("$.data.description").value(DESCRIPTION))
					.andExpect(jsonPath("$.data.type").value(TypeEnum.SD.getValue()))
					.andExpect(jsonPath("$.data.value").value(VALUE))
					.andExpect(jsonPath("$.data.wallet").value(ID)); //para que a senha não seja retonada na resposta
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testUpdateWalletChange() { //não permitir alterar uma carteira na carteira item
		
		Wallet w = new Wallet();
		w.setId(99L);
		
		WalletItem wi = new WalletItem(1L, w, DATE, TypeEnum.SD, DESCRIPTION, VALUE);
		
		try {
			BDDMockito.given(service.findById(Mockito.anyLong())).willReturn(Optional.of(wi));
			
			mvc.perform(MockMvcRequestBuilders.put(URL).content(this.getJsonPayLoad())
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest()) 
					.andExpect(jsonPath("$.data").doesNotExist())
					.andExpect(jsonPath("$.errors[0]").value("Você não pode alterar a carteira"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUpdateInvalidId() {
		
		try {
			BDDMockito.given(service.findById(Mockito.anyLong())).willReturn(Optional.empty());
			
			mvc.perform(MockMvcRequestBuilders.put(URL).content(this.getJsonPayLoad())
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest()) 
					.andExpect(jsonPath("$.data").doesNotExist())
					.andExpect(jsonPath("$.errors[0]").value("WalletItem não encontrado"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testeDelete() {
		try {
			BDDMockito.given(service.findById(Mockito.anyLong())).willReturn(Optional.of(new WalletItem()));
			
			mvc.perform(MockMvcRequestBuilders.delete(URL + "/delete/1")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.data").value("Carteira de id "+ID+" apagada com sucesso"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testeDeleteInvalid() {
		try {
			BDDMockito.given(service.findById(Mockito.anyLong())).willReturn(Optional.empty());
			
			mvc.perform(MockMvcRequestBuilders.delete(URL + "/delete/99")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound())
					.andExpect(jsonPath("$.data").doesNotExist())
					.andExpect(jsonPath("$.errors[0]").value("Carteira de id "+ 99 + " não encontrada"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private WalletItem getMockWalletItem() {
		Wallet w = new Wallet();
		w.setId(1L);
		
		WalletItem wi = new WalletItem(1L, w, DATE, TYPE, DESCRIPTION, VALUE);
		return wi;
	}
	
	public String getJsonPayLoad() throws JsonProcessingException {
		WallateItemDTO dto = new WallateItemDTO();
		dto.setId(ID);
		dto.setDate(DATE);
		dto.setDescription(DESCRIPTION);
		dto.setType(TYPE.getValue());
		dto.setValue(VALUE);
		dto.setWallet(ID);
		
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dto);
		
	}
	
	private DateTimeFormatter getDateFormater() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		return formatter;
	}
	
}
