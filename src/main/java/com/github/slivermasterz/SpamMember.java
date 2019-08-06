package com.github.aqml15.discordbot;

import java.util.ArrayList;

public class SpamMember extends Member {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5766164508151345840L;
	int strikes;
	int noMessage;
	ArrayList<Long> times;
	
	public SpamMember(String name, long id) {
		super(name, id);
		strikes = 0;
		times = new ArrayList<Long>();
	}
	
	public void setStrike(int i) {
		strikes = i;
	}
	
	public void strike() {
		strikes++;
	}
	
	public int getStrike() {
		return strikes;
	}
	
	public void addTime(long time) {
		times.add(time);
	}
	
	public void clearTime() {
		times.clear();
	}
	
	public int getNoMessage() {
		return noMessage;
	}
	
	public void increaseMessage() {
		noMessage++;
	}
	
	public void setNoMessage(int noMessage) {
		this.noMessage = noMessage;
	}
	
	public long calculateTime() {
		long result = 0;
		if (times.size() > 1) {
			for (int i = 1; i < times.size(); i++) {
				result = result + (times.get(i) - times.get(i-1));
			}
		}
		return result;
	}

}
