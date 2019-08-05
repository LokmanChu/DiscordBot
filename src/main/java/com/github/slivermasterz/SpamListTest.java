package com.github.slivermasterz;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SpamListTest {

    @Test
    public void addTest() {
        SpamList list = new SpamList();

        SpamMember bob = new SpamMember("bob", (long) 123456789);
        SpamMember fred = new SpamMember("fred", (long) 123456789);
        SpamMember jon = new SpamMember("jon", (long) 123456789);

        list.add(bob);
        list.add(fred);
        list.add(jon);

        assertEquals(3, list.size());
    }

    @Test
    public void removeTest() {
        SpamList list = new SpamList();

        SpamMember bob = new SpamMember("bob", (long) 123456789);
        SpamMember fred = new SpamMember("fred", (long) 123456789);
        SpamMember jon = new SpamMember("jon", (long) 123456789);

        list.add(bob);
        list.add(fred);
        list.add(jon);

        list.remove(jon);
        list.remove(fred);

        assertEquals(1, list.size());
    }

    @Test
    public void getTest() {
        SpamList list = new SpamList();

        SpamMember bob = new SpamMember("bob", (long) 123456789);
        SpamMember fred = new SpamMember("fred", (long) 123456789);
        SpamMember jon = new SpamMember("jon", (long) 123456789);

        list.add(bob);
        list.add(fred);
        list.add(jon);

        assertEquals(bob, list.get("bob"));
    }

    @Test
    public void existTest() {
        SpamList list = new SpamList();

        SpamMember bob = new SpamMember("bob", (long) 123456789);
        SpamMember fred = new SpamMember("fred", (long) 123456789);
        SpamMember jon = new SpamMember("jon", (long) 123456789);

        list.add(bob);
        list.add(fred);
        list.add(jon);

        assertEquals(true, list.exist("bob"));
    }

}
