package com.revature.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BankUser {

	// keeps sensitive data hidden from users
	private long id;
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private int balance;
	
	// Create a constructor
	public BankUser(long id, String username, String password, String firstname, String lastname, int balance) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.balance = balance;
	}
	

	
	public BankUser(ResultSet resultSet) throws SQLException {
		this(resultSet.getLong("ID"),
				resultSet.getString("USERNAME"),
				resultSet.getString("PASSWORD"),
				resultSet.getString("FIRSTNAME"),
				resultSet.getString("LASTNAME"),
				resultSet.getInt("BALANCE"));
	}

	// Create our getters and setters for the private variables above.
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	@Override
	public String toString() {
		return "BankUser [id=" + id +", username=" + username +", password=" +password + ", firstname=" +firstname + ", lastname=" +
				lastname + ", balance=" + balance + "]";
	}
	
}
