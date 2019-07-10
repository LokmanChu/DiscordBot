package com.github.slivermasterz;

import org.javacord.api.DiscordApi;

import java.util.Arrays;

public class MessageListener {

    DiscordApi api;
    BlockedWords blockedWords;

    public MessageListener(DiscordApi api) {
        this.api = api;
        blockedWords = new BlockedWords();

        api.addMessageCreateListener(event -> {
            String msg = event.getMessage().getContent();
            //! CMD key
            System.out.println(msg.substring(0, 1).equals("!"));
            if (msg.substring(0, 1).equals("!")) {
                String args[] = msg.substring(1,msg.length()).split(" ");
                String cmd = args[0];
                args = Arrays.copyOfRange(args, 1, args.length);

                switch (cmd) {
                    case "list":
                        //TODO: list method
                        break;
                    case "add":
                        blockedWords.tree.insert(args[0]);
                        break;
                    case "remove":
                        blockedWords.tree.delete(args[0]);
                        break;
                    default:
                        event.getChannel().sendMessage("Command not found");
                        break;
                }
            }
        });
    }
}


