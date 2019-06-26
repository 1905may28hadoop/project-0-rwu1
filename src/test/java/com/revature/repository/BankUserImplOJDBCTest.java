package com.revature.repository;

import static org.junit.Assert.*;
import org.junit.*;

import org.junit.Test;

public class BankUserImplOJDBCTest {

	@Test
	public void testCheckLogin() {
		final String inputusername = "SBOB";
		final String inputpassword = "YELLOW11";
		final String expectedusername = "SBOB";
		final String expectedpassword = "YELLOW11";
		assertEquals(expectedusername, inputusername);
		assertEquals(expectedpassword, inputpassword);
	}

	@Test
	public void testCheckLogin2() {
		final String inputusername = "SBOB";
		final String inputpassword = "YELLOW22";
		final String expectedusername = "SBOB";
		final String expectedpassword = "YELLOW11";
		assertEquals(expectedusername, inputusername);
		assertEquals(expectedpassword, inputpassword);
	}
}
