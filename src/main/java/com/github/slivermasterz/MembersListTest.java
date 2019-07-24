package com.github.aqml15;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.github.aqml15.discordbot.Member;
import com.github.aqml15.discordbot.MembersList;

public class MembersListTest {

	@Test
	public void sizeTest() {
		MembersList list = new MembersList();
		Member m1 = new Member("Obama", (long) 123456789);
		Member m2 = new Member("Kobe", (long) 123456789);
		Member m3 = new Member("Jackie Chan", (long) 123456789);
		list.add(m1);
		list.add(m2);
		list.add(m3);
		int size = list.size();
		assertEquals(3, size);
	}
	
	@Test
	public void addTest() {
		MembersList list = new MembersList();
		Member m1 = new Member("Obama", (long) 123456789);
		Member m2 = new Member("Kobe", (long) 123456789);
		Member m3 = new Member("Jackie Chan", (long) 123456789);
		list.add(m1);
		list.add(m2);
		list.add(m3);
		assertEquals(true, list.contains(m1));
		assertEquals(true, list.contains(m2));
		assertEquals(true, list.contains(m3));
	}
	
	@Test
	public void removeTest() {
		MembersList list = new MembersList();
		Member m1 = new Member("Obama", (long) 123456789);
		Member m2 = new Member("Kobe", (long) 123456789);
		Member m3 = new Member("Jackie Chan", (long) 123456789);
		list.add(m1);
		list.add(m2);
		list.add(m3);
		list.remove(m1);
		list.remove(m2);
		assertEquals(false, list.contains(m1));
		assertEquals(false, list.contains(m2));
		assertEquals(true, list.contains(m3));
	}
	
	@Test
	public void clearTest() {
		MembersList list = new MembersList();
		Member m1 = new Member("Obama", (long) 123456789);
		Member m2 = new Member("Kobe", (long) 123456789);
		Member m3 = new Member("Jackie Chan", (long) 123456789);
		list.add(m1);
		list.add(m2);
		list.add(m3);
		list.clear();
		assertEquals(false, list.contains(m1));
		assertEquals(false, list.contains(m2));
		assertEquals(false, list.contains(m3));
		assertEquals(0, list.size());
	}
}
