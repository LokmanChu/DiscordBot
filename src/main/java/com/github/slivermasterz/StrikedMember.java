package com.github.aqml15.discordbot;

import java.util.ArrayList;

public class StrikedMember extends Member {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8970691621767163897L;
	String reason;
	ArrayList<String> reasons;

	public StrikedMember(String name, long id, String reason) {
		super(name, id);
		reasons = new ArrayList<String>();
		reasons.add(reason);
	}

	public boolean isStriked() {
		return reasons.size()!=0;
	}

}
