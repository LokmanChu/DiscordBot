package com.github.slivermasterz;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EventListener {
    DiscordApi api;
    BlockedWords blockedWords;
    ReportCommand reportCommand;
    MembersListCommands listCommands;
    Spam spam;

    public EventListener(DiscordApi api) {
        this.api = api;
        reportCommand = new ReportCommand(api);
        listCommands = new MembersListCommands(api);
        spam = new Spam(api);
        blockedWords = new BlockedWords(listCommands,reportCommand);
        setupMessageCreateListener();
        onServerMemberJoin();
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
                    case "listWords":
                         blockedWords.listWords(event.getChannel());
                        break;
                    case "addWord":
                        if (args.length == 1) blockedWords.addWords(args[0],event.getChannel());
                        else blockedWords.addWords(args[0],args[1],event.getChannel());
                        break;
                    case "removeWord":
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
                    case "strike":
                        if (args.length < 2) {
                            event.getChannel().sendMessage("Command too short, please add reason");
                            return;
                        }
                        String name = args[0];
                        User user = api.getCachedUsersByName(name).iterator().next();
                        String reason = "";
                        for (int i = 1; i < args.length; i++) {
                            reason = reason + args[i] + " ";
                        }
                        listCommands.strike(user, reason);
                        break;
                    case "unmute":
                        String member = args[0];
                        User us = api.getCachedUsersByName(member).iterator().next();
                        listCommands.unMute(us);
                        break;
                    case "striked?":
                        if (args.length < 1) {
                            event.getChannel().sendMessage("Command too short, please add name");
                            return;
                        }
                        User check = api.getCachedUsersByName(args[0]).iterator().next();
                        if (listCommands.list.contains(check.getId())) {
                            Member mem = listCommands.get(check);
                            final StringBuilder builder = new StringBuilder("Number of Strikes: ");
                            if (!mem.isStriked()) {
                                builder.append(0);
                            }
                            else {
                                builder.append(((StrikedMember)mem).reasons.size() + "\n");
                                ((StrikedMember) mem).reasons.stream().forEach(s->builder.append(s+"\n"));
                            }
                            event.getChannel().sendMessage(builder.toString());

                        }
                        break;
                    case "changespamtime":
                        spam.setBanTime(Integer.parseInt(args[0]));
                        event.getChannel().sendMessage("Spam mute time changed!");
                        break;
                    case "addMember":
                        System.out.println(args[0]);
                        listCommands.add(api.getCachedUsersByName(args[0]).iterator().next());
                        listCommands.write();
                        break;
                    case "removeMember":
                        System.out.println(args[0]);
                        listCommands.remove((api.getCachedUsersByName(args[0]).iterator().next()));
                        listCommands.write();
                        break;
                    case "save":
                        listCommands.write();
                        event.getChannel().sendMessage("Saved...");
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
            }

            if (listCommands.get(author)==null) {
                listCommands.add(author);
            }
            if (listCommands.get(author).creationDate == -1) {
                listCommands.get(author).setCreationDate(author.getCreationTimestamp().getEpochSecond());
            }
            listCommands.get(author).increase();
            spam.spamCheck(event);
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
            User author = event.getUser();
            if (author.isBot()) {
                System.out.println("bot");
                return;
            }
            if (event.getChannel() == event.getApi().getChannelsByName("reports").iterator().next()) {
                if (event.getEmoji().equalsEmoji("👍")) {
                    api.getTextChannelsByName("reports").iterator().next().sendMessage("Striked!");
                    ReportCommand.Report report = reportCommand.activeReports.stream().filter(report1 -> report1.messageId == event.getMessage().get().getId()).iterator().next();
                    listCommands.strike(report.offender, report.reportMessage);
                } else if (event.getEmoji().equalsEmoji("👎")) {
                    api.getTextChannelsByName("reports").iterator().next().sendMessage("Safe!");
                }
            }
        });
    }
}


