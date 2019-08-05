package com.github.slivermasterz;

public class Member {
	String name;
	long id;
	int count = 0;

	/**
	 * Init
	 * @param name
	 * @param id
	 */
	public Member(String name, long id) {
		this.name = name;
		this.id = id;
	}

	/**
	 * Checks if striked
	 * @return Boolean
	 */
	public boolean isStriked() {
		return false;
	}

	/**
	 *
	 * @return int count
	 */
	public int count() {
		return count;
	}

	/**
	 * Increase count
	 */
	public void increase() {
		count++;
	}

	/**
	 * Get id
	 * @return Long id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Set id
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}
}