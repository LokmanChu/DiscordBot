package com.github.aqml15.discordbot;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class Spam extends SpamList implements MessageCreateListener {
	final static int minTime = 2000;
	
	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		long oldTime = 0;
		long newTime = 100000;
		SpamMember sm;
		
		if (event.getMessageAuthor().isBotUser())
			return;
		
		if (exist(event.getMessageAuthor().getName())) {
			sm = get(event.getMessageAuthor().getName());
			oldTime = get(sm.name).time;
			remove(sm);
			add(sm);
			newTime = getTime(sm);
		} else {
			sm = new SpamMember(event.getMessageAuthor().getName(), event.getMessageAuthor().getId());
			add(sm);
		}
		
		if (newTime - oldTime <= minTime) {
			sm.strikes++;
		} else {
			sm.strikes = 0;
		}
		
		if (sm.strikes >= 3) {
			//TODO: Ban
			event.getChannel().sendMessage("Spam detected, you are now temporarily muted...");
			event.getMessageAuthor().asUser().get().addRole(event.getServer().get().getRolesByName("Chat Restrict").get(0));
			sm.strikes = 0;
		}
		else if (sm.strikes == 2) {
			event.getChannel().sendMessage("Spam warning, please wait between messages...");
		}
		
	}
}
