package com.revature.utilities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
	private static Connection conn = null;
	
	// This static block will run before anything -- when the class is loading
	// it's not strictly necessary, but it will tell us if we're missing
	// our driver
	
	static {
		try {
			// this line check for the Class of our Driver and loads it
			// probably not necessary, but nice!
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch ( ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		// in here we need our url, username, and password for our DB
		// but we don't want to hard code. We'll use properties
		
		try {
			Properties properties = new Properties();
			// we want to load properties from file. BUT the path might change
			// depending on how we build our project.
			// instead of hardcoding, we'll look on the classpath. It involves
			// quite a few method calls, but don't worry, it's just making sure
			// java can always find our properties file.
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			properties.load(loader.getResourceAsStream("connection.properties"));
			
			String url = properties.getProperty("url");
			String username = properties.getProperty("username");
			String password = properties.getProperty("password");
			
			// now we actually make the connection:
			conn = DriverManager.getConnection(url, username, password);
			System.out.println("====CONNECTED=====");
			
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
		
		
		return conn;
	}
}
