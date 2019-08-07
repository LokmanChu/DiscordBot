package com.github.slivermasterz;

import java.util.ArrayList;

public class StrikedMember extends Member {
	ArrayList<String> reasons;

	public StrikedMember(String name, long id, String reason) {
		super(name, id);
		reasons = new ArrayList<String>();
		reasons.add(reason);
		System.out.println(reason);
	}

	public boolean isStriked() {
		return reasons.size()!=0;
	}

}
