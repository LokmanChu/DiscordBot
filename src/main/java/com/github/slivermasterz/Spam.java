package com.github.slivermasterz;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.Timer;
import java.util.TimerTask;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

public class Spam {

	DiscordApi api;
	int warnBuffer = 3;
	int banBuffer = 5;
	long interval = 1800;
	String warningMessage = "stop spamming!";
	String banMessage = "has been banned for spamming for 5 minutes.";
	SpamList list;
	static Timer timer = new Timer();
	static int seconds = 0;
	static int MAX_SECONDS = 60;


	public Spam(DiscordApi api) {
		this.api = api;
		list = new SpamList();
	}

	public void spamCheck(MessageCreateEvent event) {
		User user = event.getMessageAuthor().asUser().get();
		SpamMember spamMember;

		if (!exist(user)) add(user);

		spamMember = list.get(user.getName());
		spamMember.increaseMessage();
		spamMember.addTime(event.getMessage().getCreationTimestamp().toEpochMilli());

		if (spamMember.getStrike() == banBuffer) {
			event.getChannel().sendMessage(sendBan(user));
			banHammer(spamMember);
		}
		if (spamMember.getStrike() >= banBuffer) {
			event.deleteMessage();
			return;
		}

		long time = spamMember.calculateTime();
		int count = spamMember.getNoMessage();

		if (time <= interval && count >= warnBuffer) {
			spamMember.strike();
			spamMember.clearTime();
			spamMember.setNoMessage(0);
			System.out.println("Spam detected");
			event.getChannel().sendMessage(sendWarning(user));
		}

		else if (time > interval) {
			spamMember.clearTime();
			spamMember.setNoMessage(0);
		}
	}

	public static void banHammer(SpamMember offender) {
		TimerTask task;

		task = new TimerTask() {

			@Override
			public void run() {
				if (seconds < MAX_SECONDS) {
					seconds++;
				} else {
					offender.setStrike(0);
					offender.clearTime();
					offender.setNoMessage(0);
					cancel();
				}
			}
		};
		offender.strike();
		timer.schedule(task,  0, 1000);
	}

	public void setBanTime(int seconds) {
		MAX_SECONDS = seconds;
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
