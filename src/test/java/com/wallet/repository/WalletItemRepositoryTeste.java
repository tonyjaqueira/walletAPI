package com.wallet.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.wallet.entity.Wallet;
import com.wallet.entity.WalletItem;
import com.wallet.util.enums.TypeEnum;

@SpringBootTest
@ActiveProfiles("application-test.properties")
public class WalletItemRepositoryTeste {

	private static final Date DATE = new Date();
	private static final TypeEnum TYPE = TypeEnum.EN;
	private static final String DESCRIPTION = "Conta de Luz";
	private static final BigDecimal VALUE = BigDecimal.valueOf(65);
	private Long savedWalletItemId = null;
	private Long savedWalletId = null;
	
	@Autowired
	WalletItemRepository respository;
	
	@Autowired
	WalletRepository walletRepository;
	
	@BeforeEach
	public void setUp() { // antes de inicar os testes salve primeiro as entidades
		Wallet w = new Wallet();
		w.setName("Carteira Teste");
		w.setValue(BigDecimal.valueOf(500));
		walletRepository.save(w);
		
		WalletItem wi = new WalletItem(null, w, DATE, TYPE, DESCRIPTION, VALUE);
		respository.save(wi);
		
		savedWalletItemId = wi.getId();
		savedWalletId = w.getId();
	}
	
	@AfterEach
	public void tearDown() { //depois que executar os testes, apague tudo
		respository.deleteAll();
		walletRepository.deleteAll();
	}

	@Test
	public void testeSave() { //teste salvando normalmente um Wallet e eum  Wallet Item
		
		Wallet w = new Wallet();
		w.setName("Carteira 1 teste");
		w.setValue(BigDecimal.valueOf(500));
		walletRepository.save(w);
		
		WalletItem wi = new WalletItem(1L, w, DATE, TYPE, DESCRIPTION, VALUE);
		WalletItem response = respository.save(wi);
		
		assertNotNull(response);
		assertEquals(response.getDescription(), DESCRIPTION);
		assertEquals(response.getType(), TYPE);
		assertEquals(response.getValue(), VALUE);
		assertEquals(response.getWallet().getId(), w.getId());
	}
	
	@Test
	@ExceptionHandler(ConstraintViolationException.class)
	public void testeSaveInvalidWalletItem() { //falxendo um teste inválido de salvamento de wallteitem
		WalletItem wi = new WalletItem(null, null, DATE, null, DESCRIPTION, null);
		//essa é uma exeção para valores nulos passados em campos chave obrigatórios 
		respository.save(wi);
	}
	
	@Test
	public void testeUpdate() {
		Optional<WalletItem> wi = respository.findById(this.savedWalletItemId);
		String description = "Descrição alterada";
		WalletItem changed = wi.get();
		changed.setDescription(description);
		respository.save(changed);
		Optional<WalletItem> newWalletItem = respository.findById(savedWalletItemId);
		assertEquals(description, newWalletItem.get().getDescription());
	}
	
	@Test
	public void deleteWalletItem() {
		Optional<Wallet> w = walletRepository.findById(savedWalletId);
		WalletItem wi = new WalletItem(null, w.get(), DATE, TYPE, DESCRIPTION, VALUE);
		respository.save(wi);
		respository.deleteById(wi.getId());
		Optional<WalletItem> response = respository.findById(wi.getId());
		assertFalse(response.isPresent()); //asseteFalse pq eu quero realmente q seja false, se for false foi deletado
	}
	
	@Test
	public void testeFindBetweenDates() {
		Optional<Wallet> w = walletRepository.findById(savedWalletId);
		
		LocalDateTime localDateTime = DATE.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		
		Date currentDatePlusFiveDays = Date.from(localDateTime.plusDays(5).atZone(ZoneId.systemDefault()).toInstant());
		Date currentDatePlusSevenDays = Date.from(localDateTime.plusDays(7).atZone(ZoneId.systemDefault()).toInstant());
		
		respository.save(new WalletItem(null, w.get(), currentDatePlusFiveDays, TYPE, DESCRIPTION, VALUE));
		respository.save(new WalletItem(null, w.get(), currentDatePlusSevenDays, TYPE, DESCRIPTION, VALUE));
		
		//PageRequest pg = new PageRequest(0, 10, Sort.Direction.ASC); 
		Pageable pg = PageRequest.of(0, 10);//paginando os itens buiscados 10 por pagina
		Page<WalletItem> response = respository.findByWalletIdAndDateGreaterThanEqualAndDateLessThanEqual(savedWalletId, DATE, currentDatePlusFiveDays, pg);
		
		assertEquals(response.getContent().size(), 2);
		assertEquals(response.getTotalElements(), 2);
		assertEquals(response.getContent().get(0).getWallet().getId(), savedWalletId);
	}
	
	public void testFindByType() { //testandpo entradas de carteiras
		List<WalletItem> response = respository.findByWalletAndType(savedWalletId, TYPE);
		
		assertEquals(response.size(), 1);
		assertEquals(response.get(0).getType(), TYPE);
	}
	
	public void testeFindByTypeSd() { //testando saidas de carteiras
		Optional<Wallet> w = walletRepository.findById(savedWalletId);
		respository.save(new WalletItem(null, w.get(), DATE, TypeEnum.SD, DESCRIPTION, VALUE));
		List<WalletItem> response = respository.findByWalletAndType(savedWalletId, TypeEnum.SD);
		assertEquals(response.size(), 1);
		assertEquals(response.get(0).getType(), TypeEnum.SD);
	}
}

