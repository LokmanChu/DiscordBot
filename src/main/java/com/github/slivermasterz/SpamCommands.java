package com.github.aqml15.discordbot;

import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class SpamCommands extends SpamList implements MessageCreateListener {

	@Override
	public void onMessageCreate(MessageCreateEvent event) {
		User culprit = event.getMessageAuthor().asUser().get();
		System.out.println(culprit);
		System.out.println(culprit.getName());
		System.out.println(exist(culprit.getName()));
		
		if (exist(culprit.getName())) {
			if (getSpam(culprit.getName()).strikes < 0) {
				event.deleteMessage();
				getSpam(culprit.getName()).strikes++;
				System.out.println("deleted");
			}
		}
		
		if (event.getMessageContent().equalsIgnoreCase("!printspamlist")) {
			print();
		}

	}
	
}
