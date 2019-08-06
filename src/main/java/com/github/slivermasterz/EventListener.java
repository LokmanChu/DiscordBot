package com.github.aqml15.discordbot;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;

import java.util.Arrays;

public class EventListener {
    DiscordApi api;
    BlockedWords blockedWords = new BlockedWords();
    ReportCommand reportCommand;
    MembersListCommands listCommands;
    Spam spam;
    Setup setup;

    public EventListener(DiscordApi api) {
        this.api = api;
        reportCommand = new ReportCommand(api);
        listCommands = new MembersListCommands(api);
        spam = new Spam(api);
        setup = new Setup(api);
        setupMessageCreateListener();
        onReaction();
        onServerMemberJoin();
        onServerMemberLeave();
    }

    public void setupMessageCreateListener()
    {
        api.addMessageCreateListener(event -> {
            String msg = event.getMessage().getContent();
            User author = event.getMessageAuthor().asUser().get();
            if (event.getChannel() == api.getChannelsByName("reports").iterator().next()) {
            	if (event.getMessageContent().toString().length() > 10) {
            		System.out.println("reports");
                	event.getMessage().addReaction("👍");
                	event.getMessage().addReaction("👎");
            	}
            }
            if (author.isBot()) {
            	return;
            }
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
                    	if (args.length < 2) {
                    		event.getChannel().sendMessage("Command too short, please add reason");
                    		return;
                    	}
                    	String name = args[0];
                    	User user = api.getCachedUsersByName(name).iterator().next();
                    	String reason = "";
                    	for (int i = 2; i < args.length; i++) {
                    		reason = reason + args[i] + " ";
                    	}
                    	listCommands.ban(user, reason);
                    	break;
                    case "unban":
                    	String member = args[0];
                    	User us = api.getCachedUsersByName(member).iterator().next();
                    	listCommands.unBan(us);
                    	break;
                    case "ban?":
                        if (args.length < 1) {
                        	event.getChannel().sendMessage("Command too short, please add name");
                        	return;
                        }
                        User check = api.getCachedUsersByName(args[0]).iterator().next();
                    	if (listCommands.list.contains(check.getId())) {
                    		event.getChannel().sendMessage("Ban Status: " + listCommands.get(check).isStriked());
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
            	System.out.println("report");
                reportCommand.addPMReport(author,msg);
            }
            else {
            	listCommands.countUp(author);
                blockedWords.checkMessage(event);
                Long time = event.getMessage().getCreationTimestamp().getEpochSecond() - author.getCreationTimestamp().getEpochSecond();
                listCommands.get(author).setAge(time);
                // spam
                spam.spamCheck(event);
            }
        });
    }
    
    public void onReaction()
    {
    	api.addReactionAddListener(listener -> {
    		User author = listener.getUser();
    		if (author.isBot()) {
    			System.out.println("bot");
    			return;
    		}
    		else {
    			if (listener.getEmoji().equalsEmoji("👍")) {
        			reportCommand.strikeOffender();
        			listCommands.ban(api.getCachedUsersByName(reportCommand.offender).iterator().next(), reportCommand.reportMessage);
        		}
        		else if (listener.getEmoji().equalsEmoji("👎")) {
        			reportCommand.disregardReport();
        		}
    		}	
    	});
		
	}

    public void onServerMemberJoin()
    {
    	api.addServerMemberJoinListener(event -> {
    		listCommands.memberJoin(event.getUser());
    		listCommands.write();
    	});
		
	}

    public void onServerMemberLeave()
    {
    	api.addServerMemberLeaveListener(event -> {
    		listCommands.memberLeave(event.getUser());
    		listCommands.write();
    	});
		
	}
	
}


