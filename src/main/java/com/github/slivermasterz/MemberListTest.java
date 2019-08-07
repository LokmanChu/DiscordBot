package com.github.slivermasterz;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;


public class MemberListTest {

    @Test
    public void sizeTest() throws IOException {
        MembersList list = new MembersList();
        Member m1 = new Member("Obama", (long) 123456789);
        Member m2 = new Member("Kobe", (long) 123456789);
        Member m3 = new Member("Jackie Chan", (long) 123456789);
        list.add(m1);
        list.add(m2);
        list.add(m3);
        int size = list.size();
        assertEquals(3, size);
    }

    @Test
    public void addTest() throws IOException {
        MembersList list = new MembersList();
        Member m1 = new Member("Obama", (long) 123456789);
        Member m2 = new Member("Kobe", (long) 123456789);
        Member m3 = new Member("Jackie Chan", (long) 123456789);
        list.add(m1);
        list.add(m2);
        list.add(m3);
        assertEquals(true, list.contains(m1.getId()));
        assertEquals(true, list.contains(m2.getId()));
        assertEquals(true, list.contains(m3.getId()));
    }

    @Test
    public void removeTest() throws IOException {
        MembersList list = new MembersList();
        Member m1 = new Member("Obama", (long) 123456789);
        Member m2 = new Member("Kobe", (long) 123456788);
        Member m3 = new Member("Jackie Chan", (long) 123456787);
        list.add(m1);
        list.add(m2);
        list.add(m3);
        list.remove(m1.id);
        list.remove(m2.id);
        assertEquals(false, list.contains(m1.getId()));
        assertEquals(false, list.contains(m2.getId()));
        assertEquals(true, list.contains(m3.getId()));
    }

    @Test
    public void clearTest() throws IOException {
        MembersList list = new MembersList();
        Member m1 = new Member("Obama", (long) 123456789);
        Member m2 = new Member("Kobe", (long) 123456789);
        Member m3 = new Member("Jackie Chan", (long) 123456789);
        list.add(m1);
        list.add(m2);
        list.add(m3);
        list.clear();
        assertEquals(false, list.contains(m1.getId()));
        assertEquals(false, list.contains(m2.getId()));
        assertEquals(false, list.contains(m3.getId()));
        assertEquals(0, list.size());
    }

    @Test
    public void getMemberTest() throws IOException {
        MembersList list = new MembersList();
        Member m1 = new Member("Obama", (long) 123456789);
        Member m2 = new Member("Kobe", (long) 987654321);
        Member m3 = new Member("Jackie Chan", (long) 555555555);
        list.add(m1);
        list.add(m2);
        list.add(m3);
        assertEquals(m3, list.getMember((long) 555555555));
        //assertEquals(null, list.getMember((long) 123456777));
    }
}

