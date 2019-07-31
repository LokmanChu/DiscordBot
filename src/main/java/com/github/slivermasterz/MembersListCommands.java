package com.github.aqml15.discordbot;

import java.awt.Color;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.MessageDecoration;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.api.event.server.member.ServerMemberLeaveEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;
import org.javacord.api.listener.server.member.ServerMemberLeaveListener;

public class MembersListCommands extends MembersList implements MessageCreateListener, ServerMemberJoinListener, ServerMemberLeaveListener {
	
	public void onMessageCreate(MessageCreateEvent event) {

		Member mem = getMember(event.getMessageAuthor().getId());
		if (event.getMessageAuthor().isBotUser())
			return;
		if (contains(mem.id)) {
			getMember(event.getMessageAuthor().getId()).increase();
			System.out.println("count up");
		}
		
		/**
		 * !viewMembers command
		 * !viewStats command
		 */
        if (event.getMessageContent().equalsIgnoreCase("!viewMembers")) {
        	event.getChannel().sendMessage("Members List Requested...");

        	new MessageBuilder()
			.append("Report!", MessageDecoration.BOLD,
					MessageDecoration.UNDERLINE)
			.setEmbed(new EmbedBuilder().setTitle("Members List")
					.setDescription(print())
					.setColor(Color.BLUE))
			.send((TextChannel) event.getChannel());
        }	
        
        if (event.getMessageContent().equalsIgnoreCase("!viewStats")) {
        	event.getChannel().sendMessage("Stats Requested...");
        	event.getChannel().sendMessage(printStats());
        }
	}

	/**
	 * Adds member to list on join
	 */
	public void onServerMemberJoin(ServerMemberJoinEvent event) {
		Member member = new Member(event.getUser().getName(), event.getUser().getId());
		add(member);
	}

	/**
	 * Removes member from list on leave
	 */
	public void onServerMemberLeave(ServerMemberLeaveEvent event) {
		Member member = new Member(event.getUser().getName(), event.getUser().getId());
		remove(member);		
	}
	
}
