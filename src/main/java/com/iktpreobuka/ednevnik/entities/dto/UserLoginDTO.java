package com.iktpreobuka.ednevnik.entities.dto;


public class UserLoginDTO {

	// Klasa koja se koristi od strane klijenata za logovanje na server.

	private String username;
	private String token;

	public UserLoginDTO() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
