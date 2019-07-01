package com.github.aqml15.discordbot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ReportsListTest {

	@Test
	public void sizeTest() {
		MembersList list = new MembersList();
		list.add("Obama");
		list.add("Kobe");
		list.add("Test Man");
		list.add("Test Woman");
		int size = list.size();
		assertEquals(4, size);
	}
	
	@Test
	public void addTest() {
		MembersList list = new MembersList();
		list.add("Obama");
		list.add("Kobe");
		list.add("Test Man");
		list.add("Test Woman");
		assertEquals(true, list.searchByName("Obama") & list.searchById(0));
		assertEquals(true, list.searchByName("Kobe") & list.searchById(1));
		assertEquals(true, list.searchByName("Test Man") & list.searchById(2));
		assertEquals(true, list.searchByName("Test Woman") & list.searchById(3));
	}
	
	@Test
	public void removeTest() {
		MembersList list = new MembersList();
		list.add("Obama");
		list.add("Kobe");
		list.add("Test Man");
		list.add("Test Woman");
		list.remove("Obama");
		list.remove("Kobe");
		assertEquals(false, list.searchByName("Obama"));
		assertEquals(false, list.searchByName("Kobe"));
		assertEquals(true, list.searchByName("Test Man"));
		assertEquals(true, list.searchByName("Test Woman"));
	}
	
	@Test
	public void searchTest() {
		MembersList list = new MembersList();
		list.add("Obama");
		list.add("Kobe");
		list.add("Test Man");
		list.add("Test Woman");
		list.remove("Obama");
		list.remove("Test Man");
		assertEquals(false, list.searchByName("Obama") & list.searchById(0));
		assertEquals(true, list.searchByName("Kobe") & list.searchById(1));
		assertEquals(false, list.searchByName("Test Man") & list.searchById(2));
		assertEquals(true, list.searchByName("Test Woman") & list.searchById(3));
	}

}
