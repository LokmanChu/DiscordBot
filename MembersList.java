package com.github.aqml15.discordbot;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;


/*
 * MembersList implements a Tree Map to keep track of members and order alphabetically.
 */
public class MembersList implements Map<Long, Member>{

	// ADD JSON LATER
	ArrayList<Member> list[] = new ArrayList[113];
	int size;
	
	public MembersList() {	
		list = new ArrayList[113];
		size = 0;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return size == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return key == list[(int) Math.floorMod((long) key, 113)].stream()
				.filter(member -> (long)key == member.id).findAny().orElse(null);
		}

	@Override
	public boolean containsValue(Object value) {
		Member member = (Member)value;
		return value.equals(list[(int) Math.floorMod(member.id, 113)].stream()
				.filter(mem -> member == mem).findAny().orElse(null));
	}

	@Override
	public Member get(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Member put(Long key, Member value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Member remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putAll(Map<? extends Long, ? extends Member> m) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Long> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Member> values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Entry<Long, Member>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

}