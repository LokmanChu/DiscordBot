package com.github.slivermasterz;

import java.io.PipedOutputStream;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;

/**
 * WordTree uses a Trie structure in order to get an O(n) contains method.
 * Where n is the length of the String word.
 */
public class WordTree {

    Node root;
    boolean writeLock;

    public WordTree() {
        root = new Node("");
        writeLock = false;
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

    public ArrayList<String> messageContains(String msg) {
        ArrayList<String> list = new ArrayList<String>(1);

        int i = 0;
        Node node = root;
        while (i < msg.length()) {
            int index = convertToIndex(msg.charAt(i));
            if (node.contains(index)) {
                node = node.getChild(index);
                if (!node.value.equals("") && msg.charAt(i+1) == ' ') {
                    list.add(node.value);
                }
            }
            else {
                while (msg.charAt(i)!= ' ' || msg.charAt(i)!='\n') i++;
                i++;
                node = root;
            }

            i++;
        }

        return list;
    }

    /**
     * Insert String into tree
     * @param value String to be inserted
     */
    public boolean insert(String value) {
        if (writeLock) {
            return false;
        }
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
        return true;
    }

    public boolean insertSorted(String value) {
        if (writeLock) {
            return false;
        }
        int index;
        Node node = root;
        for (int i = 0; i < value.length(); i++) {
            index = convertToIndex(value.charAt(i));
            if (node.childIndex[index] == -1) {
                node.addSorted(index);
            }
            node = node.getChild(index);
            node.numWords += 1;
        }
        node.value = value;
        root.numWords += 1;
        return true;
    }


    /**
     * Delete String from tree
     * @param value String to be deleted
     * @return true if String has been deleted, false if the String could not be found
     */
    public boolean delete(String value) {
        if (!contains(value)) {
            return false;
        }
        if (writeLock) {
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

    public void traverse(Node node, PipedOutputStream pos) {
        if (!node.value.equals("")) {
            try {
                byte[] value = node.value.getBytes("US-ASCII");
                pos.write(value,0,value.length);
                pos.write(10);
                pos.flush();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (!node.sorted) {
            node.sort();
        }
        for (Node child : node.children) {
            traverse(child, pos);
        }
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
    boolean sorted;

    public Node(String value) {
        this.value = value;
        children = new java.util.ArrayList<Node>(2);
        childIndex = new byte[(96-32) + 4]; //includes SPACE to ` on ASCII plus {|}~
        java.util.Arrays.fill(childIndex,(byte)-1);
        numWords = 0;
        sorted = false;
    }

    public void add(int index) {
        Node node = new Node("");
        children.add(node);
        childIndex[index] = (byte) (children.size()-1);
        node.index = (byte) index;
        sorted = false;
    }

    public void addSorted(int index) {
        Node node = new Node("");
        int i;
        for (i = 0; i < children.size(); i++) {
            if (index < children.get(i).index) {
                children.add(i,node);
                break;
            }
        }
        reindex(i+1);
        node.index = (byte) index;
        sorted = true;
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

    public boolean isInOrder() {
        int left = -1;
        int right;
        for (Node n : children) {
            right = n.index;
            if (right < left) {
                sorted = false;
                return false;
            }
        }
        sorted = true;
        return sorted;
    }

    public void sort() {
        for (int i = 1; i < children.size(); i++) {
            Node node = children.get(i);
            int j = i - 1;

            while (j >= 0 && children.get(j).index > node.index) {
                children.set(j+1,children.get(j));
                j--;
            }
            children.set(j+1,node);
        }

        reindex();
        sorted = true;
    }

    public void reindex() {
        reindex(0);
    }

    public void reindex(int n) {
        for (int i = n; i < children.size(); i++) {
            childIndex[children.get(i).index] = (byte) i;
        }
    }

}
