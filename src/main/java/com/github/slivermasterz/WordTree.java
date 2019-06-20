package com.github.slivermasterz;

import java.util.Collections;

/**
 * WordTree uses a Trie structure in order to get an O(n) contains method.
 * Where n is the length of the String word.
 */
public class WordTree {

    Node root;

    public WordTree() {
        root = new Node("");
    }

    /**
     * Returns number of elements in tree
     * @return int number of elements
     */
    public int size() {
        return root.numWords;
    }

    /**
     * true if tree is empty
     * false if tree is not empty
     * @return boolean
     */
    public boolean isEmpty() {
        return root.numWords > 0;
    }

    /**
     * Checks if a String is inside the tree
     * @param value String to be checked
     * @return true if in tree, false otherwise
     */
    public boolean contains(String value) {
        int index;
        Node node = root;
        for (int i = 0; i < value.length(); i++) {
            index = convertToIndex(value.charAt(i));
            if (node.contains(index)) {
                node = node.getChild(index);
            }
            else {
                return false;
            }
        }
        return !node.value.equals("");
    }

    public void insert(String value) {
        int index;
        Node node = root;
        for (int i = 0; i < value.length(); i++) {
            index = convertToIndex(value.charAt(i));
            if (node.childIndex[index] == -1) {
                node.add(index);
            }
            node = node.getChild(index);
            node.numWords += 1;
        }
        node.value = value;
        root.numWords += 1;
    }

    public boolean delete(String value) {
        if (!contains(value)) {
            return false;
        }

        int index;
        Node node = root;
        Node child;
        for (int i = 0; i < value.length(); i++) {
            index = convertToIndex(value.charAt(i));
            child = node.getChild(index);
            child.numWords -= 1;
            if (child.numWords == 0) {
                node.remove(index);
            }
            node = child;
        }
        node.value = "";
        return true;
    }

    public int convertToIndex(char c) {
        if (c >= 32)
        {
            c = Character.toUpperCase(c);
            if (c >= 123) {
                c -= 26;
            }
            c-=32;
        }
        return c;
    }
}

class Node {
    java.util.ArrayList<Node> children;
    byte childIndex[];
    String value;
    byte index;
    int numWords; //number of words traversing this node

    public Node(String value) {
        this.value = value;
        children = new java.util.ArrayList<Node>(2);
        childIndex = new byte[(96-32) + 4]; //includes SPACE to ` on ASCII plus {|}~
        java.util.Arrays.fill(childIndex,(byte)-1);
        numWords = 0;
    }

    public void add(int index) {
        children.add(new Node(""));
        childIndex[index] = (byte) (children.size()-1);
        this.index = (byte) index;
    }

    public Node remove(int index) {
        if (childIndex[index] != children.size() - 1) {
            Collections.swap(children, children.size() - 1, childIndex[index]);
            childIndex[children.get(index).index] = (byte) childIndex[index];
        }
        childIndex[index] = -1;
        return children.remove(children.size() - 1);
    }

    public Node getChild(int index) {
        return children.get(childIndex[index]);
    }

    public boolean contains(int index) {
        return childIndex[index] >= 0;
    }

}
