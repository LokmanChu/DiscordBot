package com.github.aqml15.discordbot;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

public class Spam {
	
	DiscordApi api;
	int warnBuffer = 3;
	int banBuffer = 5;
	long interval = 2000;
	String warningMessage = "stop spamming!";
	String banMessage = "has been banned for spamming.";
	SpamList list;
	
	
	public Spam(DiscordApi api) {
		this.api = api;
		list = new SpamList();
	}
	
	public void spamCheck(MessageCreateEvent event) {
		User user = event.getMessageAuthor().asUser().get();
		System.out.println(event.getMessage().getCreationTimestamp().toEpochMilli());
		
		if (exist(user)) {
			list.get(user.getName()).increaseMessage();
			list.get(user.getName()).addTime(event.getMessage().getCreationTimestamp().toEpochMilli());
		}
		
		else {
			add(user);
			list.get(user.getName()).increaseMessage();
			list.get(user.getName()).addTime(event.getMessage().getCreationTimestamp().toEpochMilli());
		}
		
		if (list.get(user.getName()).getStrike() == banBuffer + 1) {
			return;
		}
		
		long time = list.get(user.getName()).calculateTime();
		int count = list.get(user.getName()).getNoMessage();
		
		if (time <= interval && count >= warnBuffer) {
			list.get(user.getName()).strike();
			list.get(user.getName()).clearTime();
			list.get(user.getName()).setNoMessage(0);
			System.out.println("Spam detected");
			event.getChannel().sendMessage(sendWarning(user));
		}
		
		else if (time > interval) {
			list.get(user.getName()).clearTime();
			list.get(user.getName()).setNoMessage(0);
		}
	}
	
	public void spamStrikeTest(MessageCreateEvent event) {
		User user = event.getMessageAuthor().asUser().get();
		if (list.get(user.getName()).getStrike() == banBuffer) {
			System.out.println("Ban");
			event.getChannel().sendMessage(sendBan(user));
			list.get(user.getName()).strike();
			event.deleteMessage();
		}
		
		else if (list.get(user.getName()).getStrike() == banBuffer + 1) {
			event.deleteMessage();
		}
	}
	
	public void add(User user) {
		list.add(new SpamMember(user.getName(), user.getId()));
	}
	
	public void remove(User user) {
		list.remove(new SpamMember(user.getName(), user.getId()));
	}
	
	public Boolean exist(User user) {
		return list.exist(user.getName());
	}
	
	public void addStrike(SpamMember member) {
		member.strike();
	}
	
	public int getStrikes(SpamMember member) {
		return member.getStrike();
	}
	
	public String sendWarning(User user) {
		return user.getName() + " " + warningMessage;
	}
	
	public String sendBan(User user) {
		return user.getName() + " " + banMessage;
	}
}
