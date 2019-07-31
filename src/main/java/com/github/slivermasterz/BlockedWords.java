package com.github.slivermasterz;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.Event;
import org.javacord.api.event.channel.ChannelEvent;
import org.javacord.api.event.message.MessageCreateEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Scanner;
import java.util.concurrent.*;

public class BlockedWords {
    Scanner in;
    FileWriter out;
    PipedInputStream pis;
    PipedOutputStream pos;
    File file;

    WordTree tree;

    public BlockedWords() {
        tree = new WordTree();
        file = new File("BannedWords.txt");
        readWords();
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
                String value = "";
                int n = -1;
                boolean first = false;
                while ((val = pis.read()) != -1) {
                    if (val == 10) {
                        tree.insert(value,n,temp);
                        temp = "";
                        value = "";
                        n = -1;
                    }
                    else if ((char)val == ']' && first) {
                        value = temp.substring(2,temp.length()-1);
                        temp = "";
                    }
                    else if (!value.equals("") && n == -1 && (char)val == '|') {
                        n = Integer.parseInt(temp);
                        temp = "";
                    }
                    else {
                        temp += (char) val;
                        first = (char)val == '"';
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

    public void listWords(TextChannel channel){
        Runnable runnable = () -> {
            ArrayList<String> list = new ArrayList<String>();
            StringBuffer sb = new StringBuffer(2000);
            String temp = "";
            int tempLen = 0;
            tree.traverse(tree.root,(string)->{
                if (sb.length()+string.length()+1 > 2000){
                    channel.sendMessage(sb.toString());
                    sb.delete(0,sb.length());
                }
                sb.append(string + "\n");
            });
            channel.sendMessage(sb.toString());
        };
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(runnable);
        es.shutdown();
    }

    public void addWords(String word, TextChannel channel) {
        tree.insertSorted(word);
        channel.sendMessage("\"" + word + "\"" + " has been added successfully");
    }

    public void checkMessage(MessageCreateEvent event) {
        String msg = event.getMessageContent();
        ArrayList<String> list = tree.messageContains(msg);
        if (!list.isEmpty()) {
            event.getMessage().delete("Bad Language");
        }
    }


    public static void main(String[] args) throws java.io.IOException {
        BlockedWords b = new BlockedWords();
        //b.tree.insert("fuck",8,"****");
        //b.tree.insert("bitch", 8,"");
        b.writeWords();
        System.out.println(b.tree.size());
        b.tree.traverse(b.tree.root,System.out::println);

    }

}
