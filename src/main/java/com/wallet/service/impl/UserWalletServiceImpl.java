package com.wallet.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wallet.entity.UserWallet;
import com.wallet.repository.UserWalletRepository;
import com.wallet.service.UserWalletService;

@Service
public class UserWalletServiceImpl implements UserWalletService{

	@Autowired
	private UserWalletRepository repository;
	
	@Override
	public UserWallet save(UserWallet uw) {
		return repository.save(uw);
	}
	
	@Override
	public Optional<UserWallet> findUsersIdAndWalletId(Long user, Long wallet){
		return repository.findByUsers_IdAndWallet_Id(user, wallet);
	}

}
