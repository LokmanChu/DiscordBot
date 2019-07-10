package com.github.slivermasterz;

import org.javacord.api.event.Event;
import org.javacord.api.event.message.MessageCreateEvent;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

public class BlockedWords {
    Scanner in;
    FileWriter out;
    PipedInputStream pis;
    PipedOutputStream pos;
    File file;

    ArrayBlockingQueue<MessageCreateEvent> messageQueue = new ArrayBlockingQueue<MessageCreateEvent>(100);

    WordTree tree;

    public BlockedWords() {
        tree = new WordTree();
        file = new File("BannedWords.txt");
    }

    public void writeWords() {
        try{
            out = new FileWriter(file);
            pis = new PipedInputStream();
            pos = new PipedOutputStream(pis);
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
        Runnable producer = () -> {
            tree.writeLock = true;
            tree.traverse(tree.root,pos);
            tree.writeLock = false;
            try {
                pos.close();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        };

        Runnable consumer = () -> {
            try {
                int val = -1;
                int strlen = 0;
                String string = "";
                while ((val = pis.read()) != -1) {
                    string += (char) val;
                    strlen += 1;
                    if (strlen >= 1000) {
                        out.write(string);
                        string = "";
                        strlen = 0;
                    }
                }
                out.write(string);
                out.close();
                pis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        new Thread(producer).start();
        new Thread(consumer).start();

    }

    public void readWords() {
        try {
            in = new Scanner(file);
            pis = new PipedInputStream();
            pos = new PipedOutputStream(pis);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        Runnable producer = () -> {
            try {
                while (in.hasNextLine()) {
                    byte[] value = in.nextLine().getBytes("US-ASCII");
                    pos.write(value,0,value.length);
                    pos.write(10);
                    pos.flush();
                }
                pos.close();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        };

        Runnable consumer = () -> {
            try {
                int val = -1;
                String temp = "";
                while ((val = pis.read()) != -1) {
                    if (val == 10) {
                        tree.insert(temp);
                        temp = "";
                    }
                    else {
                        temp += (char) val;
                    }
                }
                pis.close();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        };

        new Thread(producer).start();
        Thread b = new Thread(consumer);
        b.start();
        try {
            b.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addEvent(MessageCreateEvent event) {
        messageQueue.offer(event);
    }

    public String test() {
        return "hello";
    }


    public static void main(String[] args) throws java.io.IOException {
        BlockedWords b = new BlockedWords();
        b.readWords();
        PipedOutputStream pos = new PipedOutputStream();
        PipedInputStream pis = new PipedInputStream();
        pis.connect(pos);
        System.out.println(b.tree.size());
        try {
            b.tree.traverse(b.tree.root,pos);
            pos.close();
        }
        catch (Exception ex) {

        }
        int val = -1;
        String temp = "";
        while ((val = pis.read())!=-1) {
            if (val == 10) {
                System.out.println(temp);
                temp = "";
            }
            else {
                temp += (char) val;
            }
        }
        pis.close();
    }

}
