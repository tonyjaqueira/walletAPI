package com.wallet.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.wallet.entity.WalletItem;
import com.wallet.repository.WalletItemRepository;
import com.wallet.service.WalletItemService;
import com.wallet.util.enums.TypeEnum;

@Service
public class WalletItemServiceImpl implements WalletItemService{
	
	@Autowired
	WalletItemRepository repository;
	
	@Value("${pagination.items_per_page}")
	private int itemsPage;

	@Override
	public WalletItem save(WalletItem i) {
		return repository.save(i);
	}

	@Override
	public Page<WalletItem> findBetweenDates(Long wallet, Date start, Date end, int page) {
		Pageable pg = PageRequest.of(page, itemsPage);
		return repository.findByWalletIdAndDateGreaterThanEqualAndDateLessThanEqual(wallet, start, end, pg);
	}

	@Override
	public List<WalletItem> findByWalletAndType(Long w, TypeEnum type) {
		return repository.findByWallet_idAndType(w, type);
	}

	@Override
	public BigDecimal sumByWalletId(Long wallet) {
		return repository.sumByWalletId(wallet);
	}

	@Override
	public Optional<WalletItem> findById(Long wi) {
		return repository.findById(wi);
	}

	@Override
	public void deleteById(Long id) {
		repository.deleteById(id);
	}

}
