package com.teo.springbootrest.service;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
//import lombok.var;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teo.springbootrest.model.User;
import com.teo.springbootrest.repo.UserRepo;
import org.springframework.web.bind.annotation.RequestBody;


@Service
@RequiredArgsConstructor
public class UserService {

	@Autowired
	public UserRepo repo;

		//method to return all Users
		public List<User> getAllUsers() {
			return repo.findAll();
		}



		// method to add a user
		public void addUser(User user) {
			 repo.save(user);
		}

		//serve per websocket tutorial
		public List<User> getAllLoggedInUsers() {
		return repo.findByLoggedIn();
	}

		//websocket tutorial
		public void saveUser(User user) {
			user.setLoggedIn(true);
			repo.save(user);
		}

		//websocket tutorial
		public void disconnectUser( User user) {
			User storedUser=repo.findById(user.getNickname()).orElse(null);
			if(storedUser != null)
			{
				storedUser.setLoggedIn(false);
				repo.save(storedUser);
			}
		}

		//method to get user by nickname
		public User getUser(String nickname) {
			return repo.findById(nickname).orElse(new User());
		}

		//method to update user with user object
		public void updateUser(User user) {
		repo.save(user);
		}

		//method to delete user by id
		public void deleteUser(String nickname) {
			repo.deleteById(nickname);
		}
//@PostConstruct
		public void load() {
			// arrayList to store User objects
			List<User> users =
					new ArrayList<>(List.of(
							new User("a", "a@gmail.com", "a", false, new ArrayList<>(), new ArrayList<>()),
							new User("b", "b@gmail.com", "b", true,new ArrayList<>(), new ArrayList<>()),
							new User("c", "c@gmail.com", "c", false,new ArrayList<>(), new ArrayList<>()),
							new User("d", "d@gmail.com", "d", true,new ArrayList<>(), new ArrayList<>()),
							new User("e", "e@gmail.com", "e", false,new ArrayList<>(), new ArrayList<>())
					));

			repo.saveAll(users);
		}

	public List<User> search(String keyword) {
			return repo.findByEmailContainingOrPasswordContaining(keyword, keyword);
	}


}
