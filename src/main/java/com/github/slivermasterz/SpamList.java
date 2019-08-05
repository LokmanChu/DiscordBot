package com.github.slivermasterz;

import java.util.HashMap;

public class SpamList {

	HashMap<String, SpamMember> spamList;

	/**
	 * Init
	 */
	public SpamList() {
		spamList = new HashMap<String, SpamMember>();
	}

	/**
	 * Adds member to spam list
	 * @param m
	 */
	public void add(SpamMember m) {
		spamList.put(m.name, m);
	}

	/**
	 * Remove member from spam list
	 * @param m
	 */
	public void remove(SpamMember m) {
		spamList.remove(m.name);
	}

	/**
	 * Gets member from spam list
	 * @param name
	 * @return SpamMember
	 */
	public SpamMember get(String name) {
		return spamList.get(name);
	}

	/**
	 * Checks if member exists from name
	 * @param m
	 * @return Boolean
	 */
	public Boolean exist(String m) {
		if (spamList.get(m) != null)
			return true;
		return false;
	}

	/**
	 * Get size of list
	 * @return int
	 */
	public int size() {
		return spamList.size();
	}

	public void print() {
		System.out.println(spamList.toString());
		System.out.println(spamList.size());
	}
}
