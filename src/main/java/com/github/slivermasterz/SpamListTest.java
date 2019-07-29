package com.github.aqml15;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.aqml15.discordbot.SpamList;
import com.github.aqml15.discordbot.SpamMember;

public class SpamListTest {

	@Test
	public void addTest() {
		SpamList list = new SpamList();
		
		SpamMember bob = new SpamMember("bob", (long) 123456789);
		SpamMember fred = new SpamMember("fred", (long) 123456789);
		SpamMember jon = new SpamMember("jon", (long) 123456789);
		
		list.addSpam(bob);
		list.addSpam(fred);
		list.addSpam(jon);
		
		assertEquals(3, list.size());
	}
	
	@Test
	public void removeTest() {
		SpamList list = new SpamList();
		
		SpamMember bob = new SpamMember("bob", (long) 123456789);
		SpamMember fred = new SpamMember("fred", (long) 123456789);
		SpamMember jon = new SpamMember("jon", (long) 123456789);
		
		list.addSpam(bob);
		list.addSpam(fred);
		list.addSpam(jon);
		
		list.removeSpam(jon);
		list.removeSpam(fred);
		
		assertEquals(1, list.size());
	}
	
	@Test
	public void getTest() {
		SpamList list = new SpamList();
		
		SpamMember bob = new SpamMember("bob", (long) 123456789);
		SpamMember fred = new SpamMember("fred", (long) 123456789);
		SpamMember jon = new SpamMember("jon", (long) 123456789);
		
		list.addSpam(bob);
		list.addSpam(fred);
		list.addSpam(jon);
		
		assertEquals(bob, list.getSpam("bob"));
	}
	
	@Test
	public void existTest() {
		SpamList list = new SpamList();
		
		SpamMember bob = new SpamMember("bob", (long) 123456789);
		SpamMember fred = new SpamMember("fred", (long) 123456789);
		SpamMember jon = new SpamMember("jon", (long) 123456789);
		
		list.addSpam(bob);
		list.addSpam(fred);
		list.addSpam(jon);
		
		assertEquals(true, list.exist("bob"));
	}
	
}
