package com.teo.springbootrest.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.teo.springbootrest.model.User;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, String> {

	List<User> findByEmailContainingOrPasswordContaining(String email, String password);

	@Query("SELECT u FROM User u WHERE u.email = ?1")	//query per controllare se le credenziali sono valide (se le credenziali inserite corrispondono ad una tupla del database)
	User findByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.isLoggedIn=true")
	List<User> findByLoggedIn();

	@Query("SELECT u FROM User u WHERE u.nickname = ?1")
	User findByNickname(String nickname);
}
