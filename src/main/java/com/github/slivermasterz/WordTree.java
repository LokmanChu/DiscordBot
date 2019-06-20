package com.github.slivermasterz;

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
     *
     * @param value
     * @return
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
            char c = value.charAt(i);
            index = convertToIndex(c);
            if (node.childIndex[index] == -1) {
                node.add(index);
            }
            node.numWords += 1;
            node = node.getChild(index);
        }
        node.value = value;
    }

    public boolean delete(String value) {
        return false;
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

    public static void main(String[] args) {
        WordTree tree = new WordTree();
        tree.insert("there");
        System.out.println(tree.contains("there"));
    }
}

class Node {
    java.util.ArrayList<Node> children;
    int childIndex[];
    String value;
    int numWords; //number of words traversing this node

    public Node(String value) {
        this.value = value;
        children = new java.util.ArrayList<Node>(2);
        childIndex = new int[(96-32) + 4]; //includes SPACE to ` on ASCII plus {|}~
        java.util.Arrays.fill(childIndex,-1);
        numWords = 0;
    }


    public void add(int index) {
        children.add(new Node(""));
        childIndex[index] = children.size()-1;
    }

    public Node getChild(int index) {
        return children.get(childIndex[index]);
    }

    public boolean contains(int index) {
        return childIndex[index] >= 0;
    }
}
