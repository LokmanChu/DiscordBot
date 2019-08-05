package com.github.slivermasterz;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;

import java.util.Arrays;

public class EventListener {
    DiscordApi api;
    BlockedWords blockedWords = new BlockedWords();
    ReportCommand reportCommand;
    MembersListCommands listCommands;
    Spam spam;

    public EventListener(DiscordApi api) {
        this.api = api;
        reportCommand = new ReportCommand(api);
        listCommands = new MembersListCommands(api);
        spam = new Spam(api);
        setupMessageCreateListener();
        setupReactionAddListener();
    }

    public void setupMessageCreateListener()
    {
        api.addMessageCreateListener(event -> {
            String msg = event.getMessage().getContent();
            User author = event.getMessageAuthor().asUser().get();
            if (author.isBot()) return;
            //! CMD k
            System.out.println(msg);
            if (msg.substring(0, 1).equals("!")) {
                String args[] = msg.substring(1, msg.length()).split(" ");
                String cmd = args[0];
                args = Arrays.copyOfRange(args, 1, args.length);

                switch (cmd) {
                    case "list":
                         blockedWords.listWords(event.getChannel());
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
                    case "members":
                        listCommands.viewMembers(event.getChannel());
                        break;
                    case "stats":
                        listCommands.viewStats(event.getChannel());
                        break;
                    case "ban":
                        if (args.length < 2) return;
                        String name = args[0];
                        User user = api.getCachedUsersByName(name).iterator().next();
                        String reason = "";
                        for (int i = 1; i < args.length; i++) {
                            reason = reason + args[i] + " ";
                        }
                        listCommands.ban(user, reason);
                        break;
                    case "striked?":
                        if (listCommands.list.contains(author.getId())) {
                            System.out.println(listCommands.list.getMember(author.getId()).isStriked());
                        }
                        break;
                    default:
                        event.getChannel().sendMessage("Command not found");
                        break;
                }
            }
            else if (event.isPrivateMessage() && reportCommand.pendingReporters.contains(author)) {
                reportCommand.addPMReport(author,msg);
            }
            else {
                blockedWords.checkMessage(event);
                spam.spamCheck(event);
            }
        });
    }

    public void onServerMemberJoin()
    {
        api.addServerMemberJoinListener(event -> {
            listCommands.memberJoin(event.getUser());
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


