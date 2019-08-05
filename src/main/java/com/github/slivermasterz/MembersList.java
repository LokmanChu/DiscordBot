package com.github.aqml15.discordbot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/*
 * MembersList implements a Tree Map to keep track of members and order alphabetically.
 */
public class MembersList {
	Map<Long, ArrayList<Member>> map;
	int size = 0;
	String print = "";
	String file = "/Users/Albert/eclipse-workspace/discordbot/data/data.properties";
	Properties properties;
	
	/**
	 * Init
	 */
	public MembersList() {
		map = new HashMap<Long, ArrayList<Member>>();
		properties = new Properties();
		read();
	}
	
	public void write() {		
		for (Map.Entry<Long, ArrayList<Member>> entry : map.entrySet()){
            properties.put(entry.getKey().toString(), entry.getValue().toString());
        }

		try {
			properties.store(new FileOutputStream(file), null);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void read() {
		try {
			properties.load(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		map = (Map)properties;
		
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
	public void remove(Member value) {
		if (map.containsKey(value.id % 113)) {
			if (map.get(value.id % 113).size() > 0) {
				map.get(value.id % 113).remove(value);
				size--;
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
				print = print + "Name: " + memList.get(i).name + " || ID: " + memList.get(i).id + "\n";
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
				print = print + "Name: " + memList.get(i).name + ", ID: " + memList.get(i).id + ", # Messages: " + memList.get(i).count + "\n";
			}
		}
		return print;
	}
	
	public static void main(String a[]) {
		MembersList list = new MembersList();
		
		Member bob = new Member("bob", (long) 123456789);
		Member jon = new Member("jon", (long) 123456777);
		
		list.add(bob);
		list.add(jon);
		
		System.out.println("list: " + "\n" + list.print());
		
		list.write();
		MembersList list2 = new MembersList();
		list2.read();
		
		System.out.println("list2: " + "\n" + list.print());
		
	}

}