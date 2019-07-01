package com.github.aqml15.discordbot;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/*
 * MembersList implements a Tree Map to keep track of members and order alphabetically.
 */
public class MembersList implements Serializable {

	private static final long serialVersionUID = -921260427607624568L;
	Map<String, Integer> treeMap;
	int id;
	
	class StringComparator implements Comparator<String>, Serializable {
		
		private static final long serialVersionUID = -921260427607624568L;
	 
	    public int compare(String x, String y) {
			return x.toLowerCase().compareTo(y.toLowerCase());
		}
	}
	
	public MembersList() {	
		id = 0;
		treeMap = new TreeMap<>(new StringComparator());
	}
	
	/*
	 * Returns number of elements
	 */
	public int size() {
		return treeMap.size();
	}
	
	/*
	 * Adds a member
	 * @param member String
	 */
	public void add(String member) {
		if (!(treeMap.containsKey(member)))
			treeMap.put(member, id);
			id++;
	}	
	
	/*
	 * Removes a member
	 * @param member String
	 */
	public void remove(String member) {
		if (treeMap.containsKey(member))
			treeMap.remove(member);
	}	
	
	/*
	 * Checks if a member exists by name
	 * @param member String
	 * @return boolean
	 */
	public boolean searchByName(String member) {
		return treeMap.containsKey(member);
	}
	
	/*
	 * Checks if a member exists by id
	 * @param member String
	 * @return boolean
	 */
	public boolean searchById(int id) {
		return treeMap.containsValue(id);
	}
	
	/*
	 * Prints the Reports List
	 * @return String
	 */
	public String print() {
		String print = "";
		for (Entry<String, Integer> entry : treeMap.entrySet()) {
			System.out.println("ID: " + entry.getValue() + ". Member: " + entry.getKey());
			print = print + "ID: " + entry.getValue() + ", Member: " + entry.getKey() + "\n";
		}
		return print;
	}
	
}