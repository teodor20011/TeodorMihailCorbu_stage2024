package com.teo.springbootrest.controller;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import com.teo.springbootrest.model.User;
import com.teo.springbootrest.repo.UserRepo;
import com.teo.springbootrest.service.UserService;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserRestController {
	
	@Autowired
	private UserService service;

	@Autowired		//per usare un metodo definito nel repo
	private UserRepo userRepository;


	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("users")
	public List<User> getAllUsers() {
		return service.getAllUsers();
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/loggedInUsers")
	public List<User> getAllLoggedInUsers() {
		return service.getAllLoggedInUsers();
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/user/{nickname}")
	public User getUser(@PathVariable String nickname) {
		return service.getUser(nickname);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("users/keyword/{keyword}")
	public List<User> searchByKeyword(@PathVariable("keyword") String keyword){
		return service.search(keyword);
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("user")
	@MessageMapping("/user.addUser")
	@SendTo("/user/public")
	public User addUser(@RequestBody @Payload User user) {
		service.addUser(user);
		return user;
	}

	@GetMapping("/getUsers")
	public ResponseEntity<List<User>> findConnectedUsers() {
		return ResponseEntity.ok(service.getAllLoggedInUsers());
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("saveLoggedUser")
	@MessageMapping("/user.saveUser")
	@SendTo("/user/topic")
	public User addLoggedUser(@RequestBody @Payload User user) {
		service.saveUser(user);
		return user;
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@PutMapping("disconnect")
	@MessageMapping("/user.disconnectUser")
	@SendTo("/user/public")
	public User disconnect(@RequestBody @Payload User user) {
		service.disconnectUser(user);
		return user;
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@PutMapping("user")
	public User updateUser(@RequestBody User user) {
		service.updateUser(user);
		return user;
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@DeleteMapping("user/{userId}")
	public String deleteUser(@PathVariable String nickname)
	{
		service.deleteUser(nickname);
		return "Deleted";
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/user/login")
	public ResponseEntity<?> loginUser(@RequestBody User user) {
		// Verifica l'autenticazione
		User foundUser = authenticate(user);
		if (foundUser != null) {
			// Genera il token
			String token = generateToken(foundUser.getNickname());

			// Costruisci la risposta JSON con successo, token e userID
			LoginResponse response = new LoginResponse(true, "Login success", token, foundUser.getNickname());
			return ResponseEntity.ok().body(response);
		} else {
			 //Autenticazione fallita
			ErrorResponse errorResponse = new ErrorResponse(false, "Credenziali non valide");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}

    }

	// Metodo di verifica dell'autenticazione, controlla se le credenziali sono valide
	private User authenticate(User user) {
		// Cerca l'utente nel database per nickaname
		User foundUser = userRepository.findByNickname(user.getNickname());

		// Verifica se l'utente è stato trovato e se la password corrisponde
		if (foundUser != null && foundUser.getEmail().equals(user.getEmail()) && foundUser.getPassword().equals(user.getPassword())) {
			return foundUser; // Ritorna l'utente trovato
		} else {
			return null; // Ritorna null se l'autenticazione fallisce
		}
	}

	// Metodo per la generazione del token
	private String generateToken(String nickname) {
		// In questo esempio, utilizzeremo solo l'email per generare il token
		// Puoi personalizzare questo metodo in base alle tue esigenze
		return Jwts.builder()
				.setSubject(nickname)
				.compact();
	}

	// Classe per la risposta di login
	private static class LoginResponse {
		private boolean success;
		private String message;
		private String token;
		private String nickname;

		public LoginResponse(boolean success, String message, String token, String nickname) {
			this.success = success;
			this.message = message;
			this.token = token;
			this.nickname = nickname;
		}

		public boolean isSuccess() {
			return success;
		}

		public String getMessage() {
			return message;
		}

		public String getToken() {
			return token;
		}

		public String getNickname() {
			return nickname;
		}
	}

	// Classe per la risposta di errore
	private static class ErrorResponse {
		private boolean success;
		private String message;

		public ErrorResponse(boolean success, String message) {
			this.success = success;
			this.message = message;
		}

		public boolean isSuccess() {
			return success;
		}

		public String getMessage() {
			return message;
		}
	}

	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("load")
	public String loadData() {
		service.load();
		return "success";
	}
}
