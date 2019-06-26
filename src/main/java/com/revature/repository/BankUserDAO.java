package com.revature.repository;

import java.util.List;

import com.revature.exception.Anothercustom;
import com.revature.exception.Custom;
import com.revature.model.BankUser;

// abstraction
public interface BankUserDAO {
	// abstract methods that don't contain method body
	
	BankUser getBankUser(long id);
	
	BankUser home() throws Custom, Anothercustom;
	
	boolean checkLogin(String username, String password) throws Custom, Anothercustom;
	
	BankUser action(String inputusername) throws Custom, Anothercustom;
	
	boolean createBankUser(BankUser bankUser);
	
	boolean updateBankUser(BankUser bankUser);
	
	List<BankUser> getBankUsers();
	
}
