package com.kns.shipshop.persistence.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name= "users")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true, nullable = true)
	@Size(min = 4, max = 20)
	private String username;
	
	private String firstName;
	
	private String lastName;
	
	@Column(unique = true)
	private String email;
	
	@Column(name = "encoded_pwd", nullable = false)
	@NotNull
	private String password;
	
	@Column(columnDefinition = "varchar(20) default 'ROLE_USER'")
	private String role;
	
	@Column(name= "insert_timestamp")
	private Date insertTimestamp;
	
	@Column(name = "update_timestamp")
	private Date updateTimestamp;
	
	@Column(name = "enabled")
	private Boolean enabled;
	
	@PrePersist
	protected void onCreate() {
		if(insertTimestamp == null)
			insertTimestamp = new Date();
		if(role == null)
			this.role = "ROLE_USER";
		if(enabled == null)
			this.enabled = false;
	}
	
	@PreUpdate
	protected void onUpdate() {
		updateTimestamp = new Date();
	}
	
	public User() {	}
	
	public User(@Size(min = 4, max = 20) String username, String firstName, String lastName, String email,
			@NotNull String password, String role, Date insertTimestamp, Date updateTimestamp,
			boolean enabled) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.role = role;
		this.insertTimestamp = insertTimestamp;
		this.updateTimestamp = updateTimestamp;
		this.enabled = enabled;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Date getInsertTimestamp() {
		return insertTimestamp;
	}

	public void setInsertTimestamp(Date insertTimestamp) {
		this.insertTimestamp = insertTimestamp;
	}

	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

}
	