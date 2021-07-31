package com.iktpreobuka.ednevnik.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



import com.iktpreobuka.ednevnik.entities.UserEntity;
import com.iktpreobuka.ednevnik.entities.dto.UserLoginDTO;

import com.iktpreobuka.ednevnik.repository.UserRepository;

import com.iktpreobuka.ednevnik.services.FileHandler;
import com.iktpreobuka.ednevnik.utils.Encryption;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping(path = "/api/v1/project")
public class UserLoginController {


	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FileHandler fileHandler;

	@Value("${spring.security.secret-key}")
	private String secretKey;

	@Value("${spring.security.token-duration}")
	private Integer duration;

	// login – medota koju ce klijenti koristiti da posalju svoje kredencijale i kao
	// odgovor dobijati UserLoginDTO u kome se nalazi
	// njihovo korisnicko ime i token koje ce korisnici koristiti
	@RequestMapping(method = RequestMethod.POST, path = "/login")
	public ResponseEntity<?> login(@RequestParam String username, @RequestParam String pwd) {

		List<UserEntity> users = (List<UserEntity>) userRepository.findAll();

		for (UserEntity userEntity : users) {
			if (userEntity.getUsername().equals(username)
					&& Encryption.validatePassword(pwd, userEntity.getPassword())) {

				String token = getJWTToken(userEntity);

				// napraviti token i vratiti ga
				UserLoginDTO userLoginDTO = new UserLoginDTO();
				userLoginDTO.setUsername(username);
				userLoginDTO.setToken(token);

				logger.info(userLoginDTO.getUsername() + " " + userEntity.getRole().getName() + " : logged in.");

				return new ResponseEntity<>(userLoginDTO, HttpStatus.OK);
			}
		}

		return new ResponseEntity<>("Username and password do not match", HttpStatus.UNAUTHORIZED);
	}

	// getJWTToken– metoda za kreiranje JWT tokena koji ce biti vracen u login
	// metodi
	private String getJWTToken(UserEntity user) {

		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(user.getRole().getName());

		String token = Jwts.builder().setId("softtekJWT").setSubject(user.getUsername())
				.claim("authorities",
						grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 6000000))
				.signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();

		logger.info(user.toString() + " : JWTToken granted.");

		return "Bearer " + token;
	}

	// vrati sve korisnike
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/users", method = RequestMethod.GET)
	public ResponseEntity<?> listUsers() {
		return new ResponseEntity<List<UserEntity>>((List<UserEntity>) userRepository.findAll(), HttpStatus.OK);
	}

	// vraća korisnika po vrednosti prosleđenog ID-a
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public UserEntity user(@PathVariable Integer id) {
		if (!userRepository.existsById(id))
			return null;
		UserEntity user = userRepository.findById(id).get();
		return userRepository.save(user);
	}

	// download log fajla
	@Secured("ROLE_ADMIN")
	@PostMapping(value = "/download")
	public ResponseEntity<Resource> downloadLogs() {
		try {
			File file = fileHandler.downloadLog();

			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.add("content-disposition", "attachment; filename=" + "logs.txt");

			return ResponseEntity.ok().headers(responseHeaders).contentLength(file.length())
					.contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
		} catch (IOException e) {
			e.getStackTrace();
		}
		return null;
	}

}
