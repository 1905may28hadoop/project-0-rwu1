package com.revature;

import com.revature.exception.Anothercustom;
import com.revature.exception.Custom;
import com.revature.model.BankUser;
import com.revature.repository.BankUserDAO;
import com.revature.repository.BankUserImplOJDBC;
import com.revature.utilities.ConnectionUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/** 
 * Create an instance of your controller and launch your application.
 * 
 * Try not to have any logic at all on this class.
 */
public class Main {

	public static void main(String[] args) throws Custom, Anothercustom {
		// create data access object
		BankUserDAO bankUserDAO = new BankUserImplOJDBC();
		
		System.out.println("_____________________________");
		System.out.println("|$|=== Krusty Krab Bank ===|$|");
		System.out.println("|$|________________________|$|");
		System.out.println("|$|--||---||-------||--||--|$|");
		System.out.println("|$|--||---||-------||--||--|$|");
		System.out.println("|$|--||---||--___--||--||--|$|");
		System.out.println("|$|__||___||__|_|__||__||__|$|");
		bankUserDAO.home();
	
	}
	
}
	
	
