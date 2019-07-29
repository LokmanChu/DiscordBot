package com.github.aqml15.discordbot;

import java.util.HashMap;

public class SpamList {
	HashMap<String, SpamMember> spamList;
	
	public SpamList() {
		spamList = new HashMap<>();
	}
	
	public void addSpam(SpamMember m) {
		m.time = System.currentTimeMillis();
		spamList.put(m.name, m);
	}
	
	public void removeSpam(SpamMember m) {
		spamList.remove(m.name);
	}
	
	public SpamMember getSpam(String name) {
		return spamList.get(name);
	}
	
	
	public Boolean exist(String m) {
		if (spamList.get(m) != null)
			return true;
		return false;
	}
	
	public int size() {
		return spamList.size();
	}
	
	public void print() {
		System.out.println(spamList.toString());
		System.out.println(spamList.size());
	}
}
