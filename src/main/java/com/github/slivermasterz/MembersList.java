package com.github.aqml15.discordbot;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	static Map<String, Integer> treeMap;
	int id;
	static String reportsPath = "src/data/reportsList.ser";
	
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
	
	@SuppressWarnings("unchecked")
	public Boolean lastSave() {
		Boolean success = true;
		try {
        	FileInputStream fileIn = new FileInputStream(reportsPath);
        	ObjectInputStream in = new ObjectInputStream(fileIn);
        	treeMap = (Map<String, Integer>) in.readObject();
        	in.close();
        	fileIn.close();
        	System.out.println("Members List found!");
        } catch (ClassNotFoundException c) {
        	System.out.println("Members List not found");
        	success = false;
        	//c.printStackTrace();
        } catch (IOException i) {
        	System.out.println("Members List not found");
        	success = false;
        	//i.printStackTrace();
        }
        
        if (!success) {
        	try {
        		FileOutputStream fileOut = new FileOutputStream(reportsPath);
        		ObjectOutputStream out = new ObjectOutputStream(fileOut);
        		out.writeObject(treeMap);
        		out.close();
        		fileOut.close();
        		System.out.println("Members List was saved into " + reportsPath);
        	} catch (IOException i) {
        		System.out.println("Save failed...");
        		i.printStackTrace();
        	}
        }
        
        return success;
	}
}