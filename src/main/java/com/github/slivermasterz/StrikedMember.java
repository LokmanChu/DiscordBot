package com.github.aqml15.discordbot;

public class StrikedMember extends Member {
	String reason;
	
	public StrikedMember(String name, Long id, String reason) {
		super(name, id);
		this.reason = reason;
	}
	
	@Override
	public Boolean isStriked() {
		return true;
	}

}
