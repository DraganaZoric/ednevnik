package com.iktpreobuka.ednevnik.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.ednevnik.security.Viewes;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Table(name = "users")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(name = "id")
	@JsonView(Viewes.AdminView.class)
	private Integer id;

	@Column(name = "username", nullable = false, unique = true)
	@NotBlank(message = "Username must be provided.")
	@Size(min = 5, max = 15, message = "Username must be between {min} and {max} characters long.")
	@JsonView(Viewes.StudentView.class)
	private String username;

	@JsonIgnore
	@Column(name = "password", nullable = false)
	@NotBlank(message = "Password must be provided.")
	@Size(min = 3, max = 90, message = "Password must be between {min} and {max} characters long.")
	private String password;

	@Column(nullable = false, unique = true)
	@NotNull(message = "Email must be provided.")
	@Email(message = "Email is not valid.")
	@JsonView(Viewes.StudentView.class)
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	// @JsonBackReference
	@JoinColumn(name = "role")
	@JsonView(Viewes.AdminView.class)
	private RoleEntity role;

	@Version
	private Integer version;

	public UserEntity(Integer id,
			@NotBlank(message = "Username must be provided.") @Size(min = 5, max = 15, message = "Username must be between {min} and {max} characters long.") String username,
			@NotBlank(message = "Password must be provided.") @Size(min = 3, max = 90, message = "Password must be between {min} and {max} characters long.") String password,
			@NotNull(message = "Email must be provided.") @Email(message = "Email is not valid.") String email,
			RoleEntity role, Integer version) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
		this.version = version;
	}

	public UserEntity() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public RoleEntity getRole() {
		return role;
	}

	public void setRole(RoleEntity role) {
		this.role = role;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
