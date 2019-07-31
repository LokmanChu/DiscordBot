package com.github.aqml15.discordbot;

import java.util.HashMap;

public class SpamList {
	HashMap<String, SpamMember> spamList;
	
	public SpamList() {
		spamList = new HashMap<>();
	}
	
	public void add(SpamMember m) {
		m.time = System.currentTimeMillis();
		spamList.put(m.name, m);
	}
	
	public void remove(SpamMember m) {
		spamList.remove(m.name);
	}
	
	public SpamMember get(String name) {
		return spamList.get(name);
	}
	
	public Long getTime(SpamMember m) {
		return m.time;
	}
	
	public Boolean exist(String m) {
		if (spamList.get(m) != null)
			return true;
		return false;
	}
}
