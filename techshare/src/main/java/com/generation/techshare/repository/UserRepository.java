package com.generation.techshare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.techshare.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
