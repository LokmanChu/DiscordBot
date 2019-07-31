package com.github.aqml15.discordbot;

public class SpamMember extends Member {
	int strikes;
	long time;
	
	public SpamMember(String name, Long id) {
		super(name, id);
		strikes = 0;
		time = 0;
	}
	
	public void setStrike(int i) {
		strikes = i;
	}

}
