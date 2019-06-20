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
        node.children[52] = new Node("");
        node = node.children[52];
        node.children[40] = new Node( "");
        node = node.children[40];
        node.children[37] = new Node( "");
        node = node.children[37];
        node.children[50] = new Node( "");
        node = node.children[50];
        node.children[37] = new Node( "there");
        node = node.children[37];

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
    public void sizeTest() {
        WordTree tree = new WordTree();
        tree.insert("there");
        assertEquals(1,tree.size(),"size == 1");
        tree.insert("the");
        assertEquals(2,tree.size(),"size == 2");
    }
}
