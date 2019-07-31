package com.github.aqml15.discordbot;

public class Member {
	String name;
	long id;
	int count = 0;
	
	/**
	 * Init
	 * @param name
	 * @param id
	 */
	public Member(String name, Long id) {
		this.name = name;
		this.id = id;
	}
	
	/**
	 * Checks if striked
	 * @return Boolean
	 */
	public Boolean isStriked() {
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
	public Long getId() {
		return id;
	}
	
	/**
	 * Set id
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}
}