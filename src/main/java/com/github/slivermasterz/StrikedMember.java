package com.github.slivermasterz;

public class StrikedMember extends Member {
	String reason;
	
	public StrikedMember(String name, Long id, String reason) {
		super(name, id);
		this.reason = reason;
	}

}
