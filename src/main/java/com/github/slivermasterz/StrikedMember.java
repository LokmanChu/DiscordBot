package com.github.aqml15.discordbot;

import java.util.ArrayList;

public class StrikedMember extends Member {
	ArrayList<String> array;
	
	public StrikedMember(String name, Long id) {
		super(name, id);
	}
	
	public void addReason(String reason) {
		array.add(reason);
	}
	
	@Override
	public Boolean isStriked() {
		return true;
	}
	
	public int numStrikes() {
		return array.size();
	}
	
	public ArrayList<String> getReasons() {
		return array;
	}
}
