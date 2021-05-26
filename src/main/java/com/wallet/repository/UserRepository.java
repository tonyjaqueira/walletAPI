package com.wallet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wallet.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	public Optional<User> findByEmailEquals(String email);

}
