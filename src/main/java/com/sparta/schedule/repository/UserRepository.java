package com.sparta.schedule.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.schedule.entity.User;

public interface UserRepository  extends JpaRepository<User,Long> {
	Optional<User> findByUsername(String username);

	Optional<User> findByNickname(String nickname);
}
