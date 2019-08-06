package com.github.aqml15.discordbot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
 * MembersList implements a Tree Map to keep track of members and order alphabetically.
 */
public class MembersList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7545869819593866005L;
	Map<Long, ArrayList<Member>> map;
	int size = 0;
	String print = "";
	File f = new File("/Users/Albert/eclipse-workspace/discordbotmain/data/MembersList.txt");
	
	/**
	 * Init
	 */
	public MembersList() {
		map = new HashMap<Long, ArrayList<Member>>();
		read();
	}
	
	public void write() {	
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(map);
			oos.close();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void read() {
		FileInputStream fis;
		try {
			fis = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fis);
			map = (HashMap) ois.readObject();
			ois.close();
			fis.close();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * Adds Member
	 * @param value Member
	 */
	public void add(Member value) {
		if (map.get(value.id % 113) != null) {
			for (Member i:map.get(value.id % 113)) {
				if (i == value) {
					return;
				}
			}
			map.get(value.id % 113).add(value);
			size++;
		}
		
		else {
			ArrayList<Member> list = new ArrayList<Member>();
			list.add(value);
			map.put(value.id % 113, list);
			size++;
		}
	}
	
	/**
	 * Removes member
	 * @param value Member
	 */
	public void remove(Long id) {
		if (map.containsKey(id % 113)) {
			ArrayList<Member> list = map.get(id % 113);
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).id == id) {
						list.remove(i);
						size--;
					}
				}
			}
		}
	}

	/**
	 * Returns size of list
	 * @return int size
	 */
	public int size() {
		return size;
	}
	
	/**
	 * Gets member
	 * @param id
	 * @return Member
	 */
	public Member getMember(long id) {	
		ArrayList<Member> members = map.get(id % 113);
		for (Member i: members) {
			if (i.id == id) {
				return i;
			}
		}
		return null;
	}
	
	/**
	 * Checks if ID is in list
	 * @param id
	 * @return Boolean
	 */
	public Boolean contains(long id) {
		if (map.containsKey(id % 113)) {
			if (map.get(id % 113).size() > 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Clear list
	 */
	public void clear() {
		map.clear();
		size = 0;
		try {
			PrintWriter pw = new PrintWriter(f);
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Prints list
	 * @return String
	 */
	public String print() {
		Iterator<ArrayList<Member>> it = map.values().iterator();
		String print = "";
		while (it.hasNext()) {
			ArrayList<Member> memList = (ArrayList<Member>) it.next();
			for (int i = 0; i < memList.size(); i++) {
				print = print + "Name: " + memList.get(i).name + ", ID: " + memList.get(i).id + "\n";
			}
		}
		return print;
	}

	/**
	 * Prints member's statistics
	 * @return String
	 */
	public String printStats() {
		Iterator<ArrayList<Member>> it = map.values().iterator();
		String print = "";
		while (it.hasNext()) {
			ArrayList<Member> memList = (ArrayList<Member>) it.next();
			for (int i = 0; i < memList.size(); i++) {
				print = print + "Name: " + memList.get(i).name + ", ID: " + memList.get(i).id + ", # Messages: " + memList.get(i).count + ", Age: " + memList.get(i).age/84000 + " days" + "\n";
			}
		}
		return print;
	}

}