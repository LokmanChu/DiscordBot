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

public class ReportCommand extends MembersList implements MessageCreateListener, ReactionAddListener {
	Member guilty;
	StrikedMember guiltyStriked;

	/**
	 * !report command
	 */
	public void onMessageCreate(MessageCreateEvent event) {
		Channel reportChannel = event.getApi().getChannelsByName("reports").iterator().next();

		// Upon receiving '!report' command, send DM for further instructions
		if (event.getMessageContent().equalsIgnoreCase("!report")) {
			User author = event.getMessageAuthor().asUser().get();
			try {
				event.getChannel().sendMessage("Report Found!");
				author.openPrivateChannel().get().sendMessage(
						"To Report a User for breaking the rules, please reply to this DM with the following format:\n"
								+ "[!!! <userid to be reported>: <'Message breaking the rule'>]\n"
								+ "[Example: !!! JohnDoe: 'What the ****!']");
			} catch (Exception e) {
				System.out.println("Report Failed!");
			}
		}

		// Check if receiving message is DM, if so paste message into a private channel
		// for review later
		if (event.isPrivateMessage()) {
			System.out.println("channel: " + reportChannel + " ... " + reportChannel.toString());
			User author = event.getMessageAuthor().asUser().get();
			String message = event.getMessageContent();
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			String fullDate = dateFormat.format(date);
			String offender = message.split("\\s+")[1];
			offender = offender.substring(0, offender.length() - 1);
			String reportMessage = "";
			String[] collection = message.split("\\s+");
			User offenderUser = event.getApi().getCachedUsersByName(offender).iterator().next();
			System.out.println(offenderUser);
			guilty = new Member(offenderUser.getName(), offenderUser.getId());
			guiltyStriked = new StrikedMember(offenderUser.getName(), offenderUser.getId());
			
			for (int i = 2; i < collection.length; i++) {
				reportMessage = reportMessage + " " + collection[i];
			}
			System.out.println(reportMessage);
			//guiltyStriked.addReason(reportMessage);

			if (message.startsWith("!!!", 0)) {
				System.out.println("NAME: " + offender);
				System.out.println(date);
				if (event.getApi().getCachedUsersByName(offender).size() < 1) {
					System.out.println("Error! This USER does not exist!");
				} else {
					new MessageBuilder()
							.append("Report!", MessageDecoration.BOLD,
									MessageDecoration.UNDERLINE)
							.setEmbed(new EmbedBuilder().setTitle(fullDate)
									.setDescription("Report!\n" + "Reporter: " + author + "\nOffender: "
											+ event.getApi().getCachedUsersByName(offender).iterator().next()
											+ "\nMessage: " + reportMessage)
									.setColor(Color.ORANGE))
							.send((TextChannel) reportChannel);
				}
			} else {
				System.out.println("Incorrect Formatting, Please Try Again!");
			}
			System.out.println(author.getName() + " -> PM CONTENT -> " + message);
		}

		// if the message is from the report channel, emote
		if (event.getChannel() == reportChannel && event.getMessageContent().equals("**__Report!__**")) {
			System.out.println("emote");
			event.addReactionsToMessage("👍" ); // thumbs up
			event.addReactionsToMessage("👎"); // thumbs down
			System.out.println(event.getMessageContent());
		}
	}

	/**
	 * Add reaction of report pm
	 */
	public void onReactionAdd(ReactionAddEvent event) {
		Boolean f = false;
		
		try {
			f = event.getReaction().get().getUsers().get().size() == 1;
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(f);
		if(f) {
			return;
		}
		else {
			System.out.println("else statement");
			if (event.getChannel() == event.getApi().getChannelsByName("reports").iterator().next()) {
				// Strike offender if thumbs up
				if (event.getEmoji().equalsEmoji("👍")) {
					// TODO: Strike Offender
					if (contains(guilty.id)) {
						remove(guilty);
						add(guiltyStriked);
					}
					add(guilty);
					event.getApi().getTextChannelsByName("reports").iterator().next().sendMessage("Striked Boi!");
					System.out.println("Report");
					event.removeAllReactionsFromMessage();
				}
				// Ignore report if thumbs down
				else if (event.getEmoji().equalsEmoji("👎")) {
					// TODO: Disregard Report
					event.getApi().getTextChannelsByName("reports").iterator().next().sendMessage("Disregarded Boi!");
					System.out.println("Safe");
					event.removeAllReactionsFromMessage();
				}
			}
		}

	}
}
