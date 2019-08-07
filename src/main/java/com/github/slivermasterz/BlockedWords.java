package com.github.slivermasterz;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.Event;
import org.javacord.api.event.channel.ChannelEvent;
import org.javacord.api.event.message.MessageCreateEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.*;

public class BlockedWords {
    Scanner in;
    FileWriter out;
    PipedInputStream pis;
    PipedOutputStream pos;
    File file;
    MembersListCommands listCommands;
    ReportCommand reportCommands;

    WordTree tree;

    public BlockedWords(MembersListCommands listCommands,ReportCommand reportCommands) {
        tree = new WordTree();
        file = new File("BannedWords.txt");
        try {
            if (!file.exists()) file.createNewFile();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        this.listCommands = listCommands;
        this.reportCommands = reportCommands;
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
        addWords(word,"r",channel);
    }

    //R: Report, S: Strike, D: Delete
    public void addWords(String word, String args, TextChannel channel) {
        boolean r,s,d;
        args = args.toLowerCase();
        r = args.indexOf('r')!=-1;
        s = args.indexOf('s')!=-1;
        d = args.indexOf('d')!=-1;

        int result = 0;
        int base = 1;
        result += d? base:0;
        base*=2;
        result += r? base:0;
        base*=2;
        result += s? base:0;

        System.out.println(result);

        tree.insertSorted(word,result,"");
        channel.sendMessage("\"" + word + "\""
                + " has been added successfully to be " +
                (r ? "Reported ":"") + (s ? "Striked ":"") + (d ? "Deleted ":""));
        writeWords();
    }

    public void checkMessage(MessageCreateEvent event) {
        String msg = event.getMessageContent();
        ArrayList<NodeInfo> list = tree.messageContains(msg);

        if (!list.isEmpty()) {
            System.out.println(list.get(0).replace);
            if (list.stream().filter((nodeInfo -> nodeInfo.strike)).count()!=0) {
                listCommands.strike(event.getMessageAuthor().asUser().get(),msg);
            }
            if (list.stream().filter(nodeInfo -> nodeInfo.channel).count()!=0) {
                reportCommands.addReport(event.getApi().getYourself(),event.getMessageAuthor().asUser().get(),event.getMessageContent());
            }
            if (list.stream().filter((nodeInfo -> nodeInfo.delete == true)).count()!=0) {
                event.getMessage().delete();
            }
        }
    }
}
