package com.github.slivermasterz;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;

import java.util.Arrays;

public class EventListener {
    DiscordApi api;
    BlockedWords blockedWords = new BlockedWords();
    ReportCommand reportCommand = new ReportCommand(api);

    public EventListener(DiscordApi api) {
        this.api = api;
        setupMessageCreateListener();
    }

    public void setupMessageCreateListener()
    {
        api.addMessageCreateListener(event -> {
            String msg = event.getMessage().getContent();
            User author = event.getMessageAuthor().asUser().get();
            //! CMD key
            System.out.println(msg.substring(0, 1).equals("!"));
            if (msg.substring(0, 1).equals("!")) {
                String args[] = msg.substring(1, msg.length()).split(" ");
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
                    case "report":
                        reportCommand.sendReportPM(event.getMessageAuthor().asUser().get());
                        break;
                    default:
                        event.getChannel().sendMessage("Command not found");
                        break;
                }
            }

            if (event.isPrivateMessage() && reportCommand.pendingReporters.contains(author)) {
                reportCommand.addPMReport(author,msg);
            }
        });
    }

    public void setupReactionAddListener() {
        api.addReactionAddListener(event -> {
            if (event.getChannel() == event.getApi().getChannelsByName("reports").iterator().next()) {
                if (event.getEmoji().equalsEmoji("👍")) {
                    reportCommand.strikeOffender();

                } else if (event.getEmoji().equalsEmoji("👎")) {
                    reportCommand.disregardReport();
                }
            }
        });
    }
}


