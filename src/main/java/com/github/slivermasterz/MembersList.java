package com.github.slivermasterz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/*
 * MembersList implements a Tree Map to keep track of members and order alphabetically.
 */
public class MembersList {
	Map<Long, ArrayList<Member>> map;
	int size = 0;
	String print = "";
	
	public MembersList() {
		map = new HashMap<Long, ArrayList<Member>>();
	}
	
	public void add(Long key, Member value) {
		if (map.containsKey(key % 113)) {
			map.get(key % 133).add(value);
			size++;
		}
		
		else {
			ArrayList<Member> list = new ArrayList<>();
			list.add(value);
			map.put(key % 113, list);
			size++;
		}
	}
	
	public void remove(Member value) {
		if (map.containsKey(value.id % 113)) {
			map.get(value.id % 113).remove(value);
			size--;
		}
	}

	public int size() {
		return size;
	}
	
	public ArrayList<Member> get(Long id) {
		return map.get(id);
	}
	
	public Boolean contains(Member m) {
		if (map.get(m.id%113) != null) {
			if (map.get(m.id%113).isEmpty()) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	public String print() {
		Iterator<ArrayList<Member>> it = map.values().iterator();
		print = "";
		while (it.hasNext()) {
			ArrayList<Member> memList = (ArrayList<Member>) it.next();
			for (int i = 0; i < memList.size(); i++) {
				print = print + "Name: " + memList.get(i).name + " || ID: " + memList.get(i).id + "\n";
			}
		}
		return print;
	}
	
}