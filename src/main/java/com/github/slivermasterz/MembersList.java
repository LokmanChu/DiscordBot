package com.github.aqml15.discordbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/*
 * MembersList implements a Tree Map to keep track of members and order alphabetically.
 */
public class MembersList {
	static Map<Long, ArrayList<Member>> map;
	int size = 0;
	String print = "";
	
	public MembersList() {
		map = new HashMap<Long, ArrayList<Member>>();
	}
	
	public void add(Member value) {
		if (map.get(value.id % 113) != null) {
			map.get(value.id % 113).add(value);
			size++;
		}
		
		else {
			ArrayList<Member> list = new ArrayList<>();
			list.add(value);
			map.put(value.id % 113, list);
			size++;
		}
	}
	
	public void remove(Member value) {
		if (map.containsKey(value.id % 113)) {
			if (map.get(value.id % 113).size() > 0) {
				map.get(value.id % 113).remove(value);
				size--;
			}
			else {
				//map.remove(value.id % 113, map.get(value.id % 113));
				map.put(value.id % 113, null);
			}
		}
	}

	public int size() {
		return size;
	}
	
	public ArrayList<Member> get(Long id) {
		return map.get(id % 113);
	}
	
	public Boolean contains(Member m) {
		if (map.get(m.id%113) != null) {
			if (map.get(m.id%113).isEmpty()) {
				return false;
			}
			else {
				for (int i = 0; i < map.get(m.id % 113).size(); i++) {
					if (m == map.get(m.id % 113).get(i)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void clear() {
		map.clear();
		size = 0;
	}
	
	public static String print() {
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
	
	public static void main(String[] args) {
		MembersList list = new MembersList();
		Member m1 = new Member("Obama", (long) 123456789);
		Member m2 = new Member("Kobe", (long) 123456789);
		Member m3 = new Member("Jackie Chan", (long) 123456789);
		list.add(m1);
		list.add(m2);
		list.add(m3);
		System.out.println(print());
	}
}