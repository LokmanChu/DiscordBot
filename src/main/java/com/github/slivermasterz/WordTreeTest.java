package com.github.slivermasterz;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WordTreeTest {
    @Test
    public void convertToIndexTest() {
        WordTree tree = new WordTree();
        assertEquals(tree.convertToIndex(' '),0,"0");
        assertEquals(33,tree.convertToIndex('A'),"A");
        assertEquals(33,tree.convertToIndex('a'),"a");
        assertEquals(58,tree.convertToIndex('Z'),"Z");
        assertEquals(65,tree.convertToIndex('{'),"{");
        assertEquals(68,tree.convertToIndex('~'),"~");
    }

    @Test
    public void containsTest() {
        WordTree tree = new WordTree();
        //inserts word THERE
        Node node = tree.root;
        node.add(52);
        node = node.getChild(52);
        node.add(40);
        node = node.getChild(40);
        node.add(37);
        node = node.getChild(37);
        node.add(50);
        node = node.getChild(50);
        node.add(37);
        node = node.getChild(37);
        node.value = "there";

        assertTrue(tree.contains("there"),"there");
        assertFalse(tree.contains("the"),"the");
    }

    @Test
    public void insertTest() {
        WordTree tree = new WordTree();
        tree.insert("there");
        assertTrue(tree.root.numWords == 1,"numWords changed by insert");
        assertTrue(tree.contains("there"),"there inserted");
        assertFalse(tree.contains("the"),"the not in tree");
    }

    @Test
    public void removeTestCaseOne() {
        WordTree tree = new WordTree();
        tree.insert("there");
        tree.insert("the");
        tree.delete("there");
        assertEquals(2,tree.root.numWords, "words == 1");
        assertTrue(tree.contains("the"),"the still exists");
        assertFalse(tree.contains("there"), "there does not exist");
    }

    @Test
    public void removeTestCaseTwo() {
        WordTree tree = new WordTree();
        tree.insert("there");
        tree.insert("the");
        tree.delete("the");
        assertEquals(2,tree.root.numWords, "words == 1");
        assertTrue(tree.contains("there"),"there still exists");
        assertFalse(tree.contains("the"), "the does not exist");
    }

    @Test
    public void sizeTest() {
        WordTree tree = new WordTree();
        tree.insert("there");
        assertEquals(1,tree.size(),"size == 1");
        tree.insert("the");
        assertEquals(2,tree.size(),"size == 2");
    }

    @Test
    public void sortTest() {
        Node n = new Node("");
        n.add(60);
        n.add(23);
        n.add(13);
        n.add(62);
        n.sort();
        assertEquals(13,n.children.get(0).index);
        assertEquals(23,n.children.get(1).index);
        assertEquals(60,n.children.get(2).index);
        assertEquals(62,n.children.get(3).index);
        assertTrue(n.sorted);
    }

}
