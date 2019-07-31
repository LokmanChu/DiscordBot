package com.github.slivermasterz;

public class SpamMember extends Member {
	int strikes;
	long time;
	
	public SpamMember(String name, Long id) {
		super(name, id);
		strikes = 0;
		time = 0;
	}

}
