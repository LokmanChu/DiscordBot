package com.github.aqml15.discordbot;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageDecoration;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.listener.message.reaction.ReactionAddListener;

public class ReportCommand implements MessageCreateListener, ReactionAddListener {
	
	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		// TODO Auto-generated method stub
		// Upon receiving '!report' command, send DM for further instructions
        if (event.getMessageContent().equalsIgnoreCase("!report")) {
        	User author = event.getMessageAuthor().asUser().get();
        	try {
        		event.getChannel().sendMessage("Report Found!");
				author.openPrivateChannel().get().sendMessage(
						"To Report a User for breaking the rules, please reply to this DM with the following format:\n"
						+ "[!!! <userid to be reported> : <'Message breaking the rule'>]\n"
						+ "[Example: !!! JohnDoe : 'What the ****!']"
						);
			}
        	catch(Exception e) {
        		System.out.println("Report Failed!");
        	}
        }
        
     // Check if receiving message is DM, if so paste message into a private channel for review later
        if (event.isPrivateMessage()) {
        	Channel channel = event.getApi().getChannelsByName("reports").iterator().next();
        	System.out.println("channel: " + channel + " ... " + channel.toString());
        	User author = event.getMessageAuthor().asUser().get();
        	String message = event.getMessageContent();	
        	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        	Date date = new Date();
        	String fullDate = dateFormat.format(date);
        	String offender = message.split("\\s+")[1];
        	String reportMessage = "";
        	String[] collection = message.split("\\s+");
        	for (int i = 3; i < collection.length; i++) {
        		reportMessage = reportMessage + " " + collection[i];
        	}
        	offender = offender.substring(0, offender.length()-1);
  
    		if (message.startsWith("!!!", 0)) {
    			System.out.println("NAME: " + offender);
    			System.out.println(date);
    			if (event.getApi().getCachedUsersByName(offender).size() < 1) {
    				System.out.println("Error! This USER does not exist!");
    			}
    			else {
    				new MessageBuilder()
    			    .append("Report!", MessageDecoration.BOLD, MessageDecoration.UNDERLINE)
    			    .setEmbed(new EmbedBuilder()
    			            .setTitle(fullDate)
    			            .setDescription("Report!\n" + "Reporter: " + author + "\nOffender: " + event.getApi().getCachedUsersByName(offender).iterator().next() + "\nMessage: " + reportMessage)
    			            .setColor(Color.ORANGE))
    			    .send((TextChannel)channel);
    			}
    		}
    		else {
    			System.out.println("Incorrect Formatting, Please Try Again!");
    		}
    		System.out.println(author.getName() + " -> PM CONTENT -> " + message);
        } 
	}

	@Override
	public void onReactionAdd(ReactionAddEvent event) {
		// TODO Auto-generated method stub
		if (event.getChannel() == event.getApi().getChannelsByName("reports").iterator().next()) {
    		if (event.getEmoji().equalsEmoji("👍")) {
    			//TODO: Strike Offender
    			event.getApi().getTextChannelsByName("reports").iterator().next().sendMessage("Striked Boi!");
    			System.out.println("*****");
    		}
    		else if (event.getEmoji().equalsEmoji("👎")) {
    			//TODO: Disregard Report
    			event.getApi().getTextChannelsByName("reports").iterator().next().sendMessage("Disregarded Boi!");
    			System.out.println("*****");
    		}
    	}
		
	}
	
}
