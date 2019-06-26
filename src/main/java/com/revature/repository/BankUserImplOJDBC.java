package com.revature.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.revature.exception.Anothercustom;
import com.revature.exception.Custom;
import com.revature.model.BankUser;
import com.revature.utilities.CloseStreams;
import com.revature.utilities.ConnectionUtil;

public class BankUserImplOJDBC implements BankUserDAO {
	
	private int balance;
	public int amount;
	public int newBalance;
	public int countloginfails = 5;
	
	// Declare our logger
	final static Logger logger = Logger.getLogger(BankUserImplOJDBC.class);
	
	@Override
	public BankUser getBankUser(long id) {
		return null;
	}
	
	@Override
	public BankUser home() throws Custom, Anothercustom {
		
		Scanner sel = new Scanner(System.in);
		System.out.println("\n=====HOME=====\nPress 1 to Login \nPress 2 to Signup");
		String selection = sel.nextLine();
		System.out.println("You selected: " + selection);
		
		int option = Integer.parseInt(selection);
		switch(option) {
		case 1:
			// LOGIN
			Scanner user = new Scanner(System.in);
			System.out.println("\n=====Login=====\nEnter Username: ");
			String inputusername = user.nextLine();
			
			Scanner pass = new Scanner(System.in);
			System.out.println("Enter Password: ");
			String inputpassword = pass.nextLine();
			
			// This method checks login, displays name, then asks withdraw/deposit/check balance
			checkLogin(inputusername,inputpassword);
			
			break;
		case 2:
			// Put your signup method here
			// SIGNUP
			Scanner suun = new Scanner(System.in);
			System.out.println("Create a username: ");
			String signupusername = suun.nextLine();
			
			Scanner supw = new Scanner(System.in);
			System.out.println("Create a password: ");
			String signuppassword = supw.nextLine();
			
			Scanner sufn = new Scanner(System.in);
			System.out.println("Enter your first name: ");
			String signupfirstname = sufn.nextLine();
			
			Scanner suln = new Scanner(System.in);
			System.out.println("Enter your last name: ");
			String signuplastname = suln.nextLine();
			
			// increment the id number everytime a new bankuser is created
			int initialbalance = 0;
			long signupid = 8;
			signupid++;
			// one way to do this is to get the id from the DB, find the maximum id number, then increment it. so that
			// the id is always unique to each user. 
			// I will do this later though
			
			BankUser newbankUser = new BankUser(signupid, signupusername, signuppassword, signupfirstname, signuplastname, initialbalance);
			createBankUser(newbankUser);
			home();
			break;
		}
		return null;
	}
	
	@Override
	public BankUser action(String inputusername) throws Custom, Anothercustom{
		
		Scanner choose = new Scanner(System.in);
		System.out.println("\nPress 3 to Withdraw \nPress 4 to Deposit \nPress 5 to Check Balance \nPress 6 to Logout");
		String pick = choose.nextLine();
		
		System.out.println("You selected: " + pick);
		
		int moOptions = Integer.parseInt(pick);
		switch(moOptions) {
		case 3:
			// WITHDRAW
			Scanner withdraw = new Scanner(System.in);
			System.out.println("\n=====WITHDRAW=====\nHow much do you want to withdraw?");
			String take = withdraw.nextLine();
			amount = Integer.parseInt(take);
			newBalance = balance - amount;
//			System.out.println("New Balance: $" + newBalance);
			// if the amount entered is greater than the balance available, throw an exception
			if (amount > balance) {
//					System.out.println("Not enough funds, please withdraw a smaller amount");
//					action(inputusername);
					System.err.print("Not enough funds, withdraw a smaller amount. ");
//					Logger.error("Cannot withdraw funds that exceed current balance");
					throw new Custom();
			}
			
			// update the DB with the new balance
			PreparedStatement statement = null;	
			try (Connection conn = ConnectionUtil.getConnection()){
				statement = conn.prepareStatement("UPDATE bank_users SET balance = ? WHERE balance = ?");
				statement.setInt(1, newBalance);
				statement.setInt(2, balance);
				statement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
//				Logger.error("SQL Exception: ", e);
			} finally {
				CloseStreams.close(statement);
			}
			action(inputusername);
			break;
		case 4:
			// DEPOSIT
			Scanner deposit = new Scanner(System.in);
			System.out.println("\n=====DEPOSIT=====\nHow much do you want to deposit?");
			String give = deposit.nextLine();
			amount = Integer.parseInt(give);
			newBalance = balance + amount;
//			System.out.println("New Balance: $" + newBalance);
			if (amount < 0) {
				// cannot deposit a negative number
				System.err.print("Cannot deposit a negative number");
				throw new Custom();
			}
			
			// update the DB with the new balance
			PreparedStatement depositstatement = null;
			try (Connection conn = ConnectionUtil.getConnection()){
				depositstatement = conn.prepareStatement("UPDATE bank_users SET balance = ? WHERE balance = ?");
				depositstatement.setInt(1, newBalance);
				depositstatement.setInt(2, balance);
				depositstatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
//				Logger.error("SQL Exception: ", e);
			} finally {
				CloseStreams.close(depositstatement);
			}
			action(inputusername);
			break;
		case 5:
			// CHECK BALANCE
			PreparedStatement cbstatement = null;
			ResultSet resultSet = null;
			
			try (Connection conn = ConnectionUtil.getConnection()){
				cbstatement = conn.prepareStatement("SELECT balance FROM bank_users WHERE username = ?");
				cbstatement.setString(1, inputusername);
				cbstatement.execute();
				resultSet = cbstatement.getResultSet();
				while (resultSet.next()) {
					balance = resultSet.getInt("balance");
					System.out.println("=====VIEW BALANCE=====\nBalance: $" + balance);
				}
			} catch (SQLException e) {
				e.printStackTrace();
//				Logger.error("SQL Exception: ", e);
			} finally {
				CloseStreams.close(cbstatement);
				CloseStreams.close(resultSet);
			}
			action(inputusername);
			break;
		case 6:
			// LOGOUT
			// Bring back to home
			home();
			break;
		}
		
		return null;
	}
	
	public boolean checkLogin(String inputusername, String inputpassword) throws Custom, Anothercustom {
		
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		
		// pull username and password from DB
		try (Connection conn = ConnectionUtil.getConnection()) {
			statement = conn.prepareStatement("SELECT * FROM bank_users WHERE username = ? AND password = ?");
			statement.setString(1, inputusername);
			statement.setString(2, inputpassword);
			statement.execute();
			resultSet = statement.getResultSet();
			// Extract data from result set
			while (resultSet.next()) {
				String un = resultSet.getString("username");
				String pw = resultSet.getString("password");
				balance = resultSet.getInt("balance");
				String firstname = resultSet.getString("firstname");
				String lastname = resultSet.getString("lastname");
				
				// if your user entry matches the credentials pulled from the DB then...print the name and balance
				while ((un.equals(inputusername)) && (pw.equals(inputpassword))) {
					System.out.println("Welcome to your account!");
					System.out.println(firstname);
					System.out.println(lastname);
					System.out.println("Current Balance: $" + balance);
					
					// Now that you're logged in, put your action method here
					action(inputusername);
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
//			Logger.error("SQL Exception: ", e);
		} finally {
			CloseStreams.close(statement);
			CloseStreams.close(resultSet);
		}
			String fail = "Login Failed.";
			countloginfails--;
			System.out.println(fail + " Number of attempts remaining: " + countloginfails);
			// if you fail to login 5 times throw an exception
			if (countloginfails == 0) {
				System.out.println("Maximum number of attempts exceeded");
				System.err.print("Maximum number of attempts exceeded ");
//				Logger.error("Maximum number of attempts exceeded");
				throw new Anothercustom();
			}
			home();
//		}
		return false;
	}
	
	@Override
	public boolean createBankUser(BankUser bankUser) {

		PreparedStatement statement = null;

		try (Connection conn = ConnectionUtil.getConnection()){
			statement = conn.prepareStatement("INSERT INTO bank_users VALUES(?,?,?,?,?,?)");

			statement.setLong(1, bankUser.getId());
			statement.setString(2, bankUser.getUsername());
			statement.setString(3, bankUser.getPassword());
			statement.setString(4, bankUser.getFirstname());
			statement.setString(5, bankUser.getLastname());
			statement.setInt(6, bankUser.getBalance());
			statement.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
//			Logger.error("SQL Exception: ", e);
			return false;
		} finally {
			CloseStreams.close(statement);
		}
		return true;
	}
	
	@Override
	public boolean updateBankUser(BankUser bankUser) {

		PreparedStatement statement = null;

		try (Connection conn = ConnectionUtil.getConnection()){
			statement = conn.prepareStatement("UPDATE bank_users SET balance= ? WHERE balance = ?");

//			statement.setLong(1, bankUser.getId());
//			statement.setString(2, bankUser.getUsername());
			// The user may change their password
			statement.setString(3, bankUser.getPassword());
//			statement.setString(4, bankUser.getFirstname());
//			statement.setString(5, bankUser.getLastname());
			
			statement.setInt(6, bankUser.getBalance());
			statement.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
//			Logger.error("SQL Exception: ", e);
			return false;
		} finally {
			CloseStreams.close(statement);
		}
		return true;
	}
	
	@Override 
	public List<BankUser> getBankUsers() {
		Statement statement = null;
		ResultSet resultSet = null;
		List<BankUser> bankUser = new ArrayList<>();
		
		try (Connection conn = ConnectionUtil.getConnection()){
			statement = conn.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM bank_users");
			while (resultSet.next()) {
				bankUser.add(new BankUser(resultSet));
			}
		} catch (SQLException e) {
			e.printStackTrace();
//			Logger.error("SQL Exception: ", e);
		} finally {
			CloseStreams.close(statement);
			CloseStreams.close(resultSet);
		}
		return bankUser;
	}

}
