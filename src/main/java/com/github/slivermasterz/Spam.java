package com.github.aqml15.discordbot;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class Spam extends SpamList implements MessageCreateListener {
	final static int minTime = 1500;
	
	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		long oldTime = 0;
		long newTime = 100000;
		SpamMember sm;
		
		if (event.getMessageAuthor().isBotUser())
			return;
		
		if (exist(event.getMessageAuthor().getName())) {
			newTime = System.currentTimeMillis();
			sm = getSpam(event.getMessageAuthor().getName());
			oldTime = getSpam(sm.name).time;
			sm.time = newTime;
			
		} else {
			sm = new SpamMember(event.getMessageAuthor().getName(), event.getMessageAuthor().getId());
			sm.time = System.currentTimeMillis();
			addSpam(sm);
		}
		
		if (newTime - oldTime <= minTime) {
			sm.strikes++;
			event.getChannel().sendMessage("Spam warning, please wait between messages...");
			event.deleteMessage();
		}
		
		if (sm.strikes < 0) {
			event.deleteMessage();
			sm.strikes++;
		}
		else if (sm.strikes == 5) {
			//TODO: Ban
			event.getChannel().sendMessage("Spam detected, you are now temporarily muted...");
			event.getMessageAuthor().asUser().get().addRole(event.getServer().get().getRolesByName("Chat Restrict").get(0));
			addSpam(sm);
			sm.strikes = -10;
		}
		
		
	}
}
