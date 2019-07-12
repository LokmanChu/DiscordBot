package com.github.aqml15.discordbot;

import java.util.ArrayList;

public class StrikedMember extends Member {

	ArrayList<String> list = new ArrayList<>();
	
	public StrikedMember(String name, Long id) {
		super(name, id);
		// TODO Auto-generated constructor stub
	}
	
	public void addStrike(String content) {
		list.add(content);
	}
	
	public int getNumStrikes() {
		return list.size();
	}
	
}
