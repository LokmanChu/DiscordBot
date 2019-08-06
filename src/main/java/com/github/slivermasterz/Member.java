package com.github.aqml15.discordbot;

import java.io.Serializable;

public class Member implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3850255598684225612L;
	String name;
	long id;
	int count = 0;
	long age = 0;
	
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
	
	public void setAge(Long age) {
		this.age = age.intValue();
	}
}