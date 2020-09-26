package com.mfd.vidauth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.Writer;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@RestController
public class Api {

	UserRepository userRepository;

	@Autowired
	public Api(UserRepository userRepository) {
		this.userRepository = userRepository;
	}



	@PostMapping(path = "/auth/register", consumes = MediaType.APPLICATION_JSON_VALUE)
	void register(@RequestBody UserDto register) throws InvalidDataException {
		if (register.username.isEmpty() || register.password.isEmpty())
			throw new InvalidDataException("One or more required fields empty.");

		if (userRepository.checkIfUserExists(register.username))
			throw new InvalidDataException("User Already exists");

		userRepository.createUser(register.username, register.password);

	}


	@PostMapping(path = "/auth/login")
	AuthToken login(@RequestBody UserDto login) throws InvalidDataException {
		if (login.username.isEmpty() || login.password.isEmpty())
			throw new InvalidDataException("One or more required fields empty.");

		if (!userRepository.validate(login.username, login.password))
			throw new InvalidDataException("Invalid username or password");

		return authorise(login.getUsername());

	}
	@GetMapping(path = "/auth/")
	void health(Writer writer) throws IOException {
		writer.write("hello from auth!");
	}



	private AuthToken authorise(String username) {
		Algorithm alg = Algorithm.HMAC256("passwordpasswordpassword");
		return new AuthToken(JWT.create().withIssuer("vidauth")
								.withSubject(username)
								.withExpiresAt(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
								.sign(alg));
	}
}
